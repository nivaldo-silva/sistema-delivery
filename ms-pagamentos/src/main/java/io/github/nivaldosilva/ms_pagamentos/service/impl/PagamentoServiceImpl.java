package io.github.nivaldosilva.ms_pagamentos.service.impl;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import io.github.nivaldosilva.ms_pagamentos.client.PedidoClient;
import io.github.nivaldosilva.ms_pagamentos.dto.PagamentoRequest;
import io.github.nivaldosilva.ms_pagamentos.dto.PagamentoResponse;
import io.github.nivaldosilva.ms_pagamentos.entity.Pagamento;
import io.github.nivaldosilva.ms_pagamentos.enums.StatusPagamento;
import io.github.nivaldosilva.ms_pagamentos.exceptions.PagamentoDuplicadoException;
import io.github.nivaldosilva.ms_pagamentos.exceptions.PagamentoNotFoundException;
import io.github.nivaldosilva.ms_pagamentos.mapper.PagamentoMapper;
import io.github.nivaldosilva.ms_pagamentos.repository.PagamentoRepository;
import io.github.nivaldosilva.ms_pagamentos.service.PagamentoService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import feign.FeignException;

@Slf4j
@Service
@RequiredArgsConstructor
public class PagamentoServiceImpl implements PagamentoService {

    private final PagamentoRepository repository;
    private final PedidoClient pedidoClient;

    @Override
    @Transactional
    public PagamentoResponse criarPagamento(PagamentoRequest request) {
        log.info("Iniciando processo de criação de pagamento para o pedido ID: {}", request.getIdPedido());
        log.debug("Detalhes da requisição de pagamento: {}", request);

        if (repository.existsByIdPedido(request.getIdPedido())) {
            log.warn("Tentativa de criar pagamento duplicado para o pedido ID: {}", request.getIdPedido());
            throw new PagamentoDuplicadoException(
                    "Já existe um pagamento processado para o pedido com ID: " + request.getIdPedido());
        }
        log.info("Verificação de duplicidade concluída. Nenhuma duplicata encontrada para o pedido ID: {}",
                request.getIdPedido());

        Pagamento pagamento = PagamentoMapper.toEntity(request);
        pagamento.setStatusPagamento(StatusPagamento.AGUARDANDO_CONFIRMACAO);
        log.debug("Entidade Pagamento mapeada e pronta para ser salva: {}", pagamento);

        try {
            pagamento = repository.save(pagamento);
            log.info("Pagamento salvo com sucesso no banco de dados. ID do Pagamento: {}", pagamento.getIdPagamento());
        } catch (Exception e) {
            log.error("Falha ao salvar o pagamento no banco de dados para o pedido ID: {}. Erro: {}",
                    request.getIdPedido(), e.getMessage(), e);
            throw e;
        }

        PagamentoResponse response = PagamentoMapper.toResponse(pagamento);
        log.debug("Resposta do pagamento mapeada: {}", response);
        log.info("Criação de pagamento finalizada com sucesso para o pedido ID: {}", request.getIdPedido());
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PagamentoResponse> buscarPagamentos(Pageable paginacao) {
        log.debug("Buscando pagamentos - página: {}, tamanho: {}",
                paginacao.getPageNumber(), paginacao.getPageSize());
        return repository.findAll(paginacao)
                .map(PagamentoMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public PagamentoResponse buscarPorId(UUID id) {
        log.debug("Buscando pagamento: {}", id);
        return repository.findById(id)
                .map(PagamentoMapper::toResponse)
                .orElseThrow(() -> {
                    log.warn("Pagamento não encontrado: {}", id);
                    return new PagamentoNotFoundException(id);
                });
    }

    @Override
    @Transactional
    public PagamentoResponse atualizarPagamento(UUID id, PagamentoRequest request) {
        log.info("Iniciando atualização do pagamento: {}", id);

        Pagamento pagamentoExistente = repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Tentativa de atualizar pagamento inexistente: {}", id);
                    return new PagamentoNotFoundException(id);
                });

        pagamentoExistente.setIdPedido(request.getIdPedido());
        pagamentoExistente.setValor(request.getValor());
        pagamentoExistente.setNomeTitular(request.getNomeTitular());
        pagamentoExistente.setNumeroCartao(request.getNumeroCartao());
        pagamentoExistente.setValidadeCartao(request.getValidadeCartao());
        pagamentoExistente.setCodigoSeguranca(request.getCodigoSeguranca());
        pagamentoExistente.setFormaPagamento(request.getFormaPagamento());

        Pagamento pagamentoAtualizado = repository.save(pagamentoExistente);

        log.info("Pagamento atualizado com sucesso: {}", id);
        return PagamentoMapper.toResponse(pagamentoAtualizado);
    }

    @Override
    @Transactional
    public void excluirPagamento(UUID id) {
        log.info("Excluindo pagamento: {}", id);

        if (!repository.existsById(id)) {
            log.warn("Tentativa de excluir pagamento inexistente: {}", id);
            throw new PagamentoNotFoundException(id);
        }

        repository.deleteById(id);
        log.info("Pagamento excluído com sucesso: {}", id);
    }

    @Override
    @Transactional
    public void confirmarPagamento(UUID id) {
        log.info("Confirmando pagamento ID: {}", id);

        Pagamento pagamento = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pagamento não encontrado: " + id));

        log.info("Pedido associado: {} | Status atual: {}",
                pagamento.getIdPedido(), pagamento.getStatusPagamento());

        pagamento.setStatusPagamento(StatusPagamento.CONFIRMADO);
        repository.save(pagamento);
        log.info("Pagamento confirmado. Notificando ms-pedidos...");

        try {
            pedidoClient.atualizaPagamento(pagamento.getIdPedido());
            log.info("Pedido {} atualizado com sucesso", pagamento.getIdPedido());
        } catch (FeignException e) {
            log.error("Erro ao notificar ms-pedidos. Status: {} | Response: {}",
                    e.status(), e.contentUTF8());

            pagamento.setStatusPagamento(StatusPagamento.AGUARDANDO_CONFIRMACAO);
            repository.save(pagamento);
            throw new RuntimeException("Falha ao atualizar pedido: " + e.contentUTF8(), e);
        }
    }

    public void alteraStatus(UUID id) {
        Optional<Pagamento> pagamento = repository.findById(id);

        if (!pagamento.isPresent()) {
            throw new EntityNotFoundException();
        }

        pagamento.get().setStatusPagamento(StatusPagamento.AGUARDANDO_CONFIRMACAO);
        repository.save(pagamento.get());

    }

}
