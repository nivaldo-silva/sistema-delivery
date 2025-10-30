package io.github.nivaldosilva.ms_pedidos.service.impl;

import io.github.nivaldosilva.ms_pedidos.dto.AtualizacaoStatus;
import io.github.nivaldosilva.ms_pedidos.dto.PedidoRequest;
import io.github.nivaldosilva.ms_pedidos.dto.PedidoResponse;
import io.github.nivaldosilva.ms_pedidos.dto.PedidoResumo;
import io.github.nivaldosilva.ms_pedidos.entity.ItemPedido;
import io.github.nivaldosilva.ms_pedidos.entity.Pedido;
import io.github.nivaldosilva.ms_pedidos.enums.StatusPedido;
import io.github.nivaldosilva.ms_pedidos.exception.BusinessException;
import io.github.nivaldosilva.ms_pedidos.exception.ResourceNotFoundException;
import io.github.nivaldosilva.ms_pedidos.mapper.PedidoMapper;
import io.github.nivaldosilva.ms_pedidos.repository.PedidoRepository;
import io.github.nivaldosilva.ms_pedidos.service.PedidoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepository repository;

    @Override
    @Transactional
    public PedidoResponse criarPedido(PedidoRequest request) {
        log.info("Iniciando criação de pedido");

        Pedido pedidoDuplicado = verificarPedidoDuplicado(request);
        if (pedidoDuplicado != null) {
            log.warn("Pedido duplicado detectado! Retornando pedido existente: {}",
                    pedidoDuplicado.getIdPedido());
            return PedidoMapper.toResponse(pedidoDuplicado);
        }

        Pedido pedido = PedidoMapper.toEntity(request);
        pedido.setStatusPedido(StatusPedido.REALIZADO);
        Pedido pedidoSalvo = repository.save(pedido);

        log.info("Gerando o número do pedido: {}", pedidoSalvo.getIdPedido());
        repository.flush();
        String idStr = pedidoSalvo.getIdPedido().toString().replace("-", "");
        pedidoSalvo.setNumero("#" + idStr.substring(Math.max(0, idStr.length() - 5)).toUpperCase());
        pedidoSalvo = repository.save(pedidoSalvo);

        log.info("Pedido criado com sucesso: {}", pedidoSalvo.getIdPedido());
        return PedidoMapper.toResponse(pedidoSalvo);
    }

    @Override
    @Transactional(readOnly = true)
    public PedidoResponse obterPorId(UUID id) {
        log.info("Buscando pedido: {}", id);

        Pedido pedido = repository.findByIdWithItens(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado: " + id));

        return PedidoMapper.toResponse(pedido);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PedidoResumo> listarTodos() {
        log.info("Listando todos os pedidos");

        return repository.findAll().stream()
                .map(PedidoMapper::toResumo)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PedidoResponse atualizarPedido(UUID id, PedidoRequest request) {
        log.info("Atualizando pedido: {}", id);

        Pedido pedido = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado: " + id));

        if (pedido.getStatusPedido() != StatusPedido.REALIZADO) {
            throw new BusinessException("Apenas pedidos com status 'REALIZADO' podem ser alterados");
        }

        pedido.getItens().clear();
        pedido.setObservacao(request.getObservacoes());

        request.getItens().forEach(itemRequest -> {
            ItemPedido item = ItemPedido.builder()
                    .nome(itemRequest.getNome())
                    .descricao(itemRequest.getDescricao())
                    .precoUnitario(itemRequest.getPrecoUnitario())
                    .quantidade(itemRequest.getQuantidade())
                    .observacao(itemRequest.getObservacao())
                    .build();
            pedido.adicionarItem(item);
        });

        Pedido pedidoAtualizado = repository.save(pedido);
        log.info("Pedido atualizado com sucesso: {}", id);

        return PedidoMapper.toResponse(pedidoAtualizado);
    }

    @Override
    @Transactional
    public void aprovarPagamento(UUID id) {
        log.info("==========================================");
        log.info("Iniciando aprovação de pagamento");
        log.info("ID do Pedido: {}", id);
        log.info("==========================================");

        Pedido pedido = repository.findById(id)
                .orElseThrow(() -> {
                    log.error("ERRO: Pedido não encontrado: {}", id);
                    return new ResourceNotFoundException("Pedido não encontrado: " + id);
                });

        log.info("Pedido encontrado!");
        log.info("Status atual: {}", pedido.getStatusPedido());
        log.info("ID: {}", pedido.getIdPedido());

        if (pedido.getStatusPedido() != StatusPedido.REALIZADO) {
            log.error("ERRO: Status inválido para aprovação de pagamento");
            log.error("Status esperado: REALIZADO");
            log.error("Status atual: {}", pedido.getStatusPedido());
            throw new BusinessException(
                    "Não é possível aprovar o pagamento de um pedido que não esteja com o status 'REALIZADO'. Status atual: "
                            + pedido.getStatusPedido());
        }

        log.info("Validação de status OK! Atualizando para PAGO...");

        pedido.setStatusPedido(StatusPedido.PAGO);
        Pedido pedidoSalvo = repository.save(pedido);

        log.info("Pedido salvo no banco de dados!");
        log.info("Novo status: {}", pedidoSalvo.getStatusPedido());
        log.info("==========================================");
        log.info("Pagamento do pedido {} aprovado com sucesso!", id);
        log.info("==========================================");
    }

    @Override
    @Transactional
    public void atualizarStatus(UUID id, AtualizacaoStatus status) {
        log.info("Atualizando status do pedido: {} para {}", id, status.getStatus());

        Pedido pedido = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado: " + id));

        if (pedido.getStatusPedido() == StatusPedido.CANCELADO ||
                pedido.getStatusPedido() == StatusPedido.ENTREGUE) {
            throw new BusinessException(
                    "Não é possível alterar o status de um pedido que já foi entregue ou cancelado");
        }

        pedido.setStatusPedido(status.getStatus());
        repository.save(pedido);

        log.info("Status atualizado com sucesso");
    }

    @Override
    @Transactional
    public void cancelarPedido(UUID id) {
        log.info("Cancelando pedido: {}", id);

        Pedido pedido = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado: " + id));

        if (pedido.getStatusPedido() == StatusPedido.ENTREGUE ||
                pedido.getStatusPedido() == StatusPedido.CANCELADO) {
            throw new BusinessException(
                    "Não é possível cancelar um pedido que já foi entregue ou previamente cancelado");
        }

        pedido.setStatusPedido(StatusPedido.CANCELADO);
        repository.save(pedido);

        log.info("Pedido cancelado com sucesso: {}", id);
    }

    private Pedido verificarPedidoDuplicado(PedidoRequest request) {
        Instant tresMinutosAtras = Instant.now().minus(3, ChronoUnit.MINUTES);

        log.debug("Verificando pedidos duplicados após: {}", tresMinutosAtras);

        List<Pedido> pedidosRecentes = repository.findByStatusAndDataHoraAfterWithItens(StatusPedido.REALIZADO,
                tresMinutosAtras);

        log.debug("Encontrados {} pedidos recentes", pedidosRecentes.size());

        BigDecimal totalNovo = calcularTotalRequest(request);
        int quantidadeItensNovo = request.getItens().size();

        log.debug("Novo pedido - Total: {}, Itens: {}", totalNovo, quantidadeItensNovo);

        for (Pedido pedidoExistente : pedidosRecentes) {
            BigDecimal totalExistente = pedidoExistente.calcularTotal();
            int quantidadeItensExistente = pedidoExistente.getItens().size();

            log.debug("Comparando com pedido {} - Total: {}, Itens: {}",
                    pedidoExistente.getIdPedido(), totalExistente, quantidadeItensExistente);

            if (quantidadeItensExistente == quantidadeItensNovo &&
                    totalExistente.compareTo(totalNovo) == 0) {

                if (compararItens(pedidoExistente, request)) {
                    log.warn("DUPLICADO ENCONTRADO! Pedido: {}", pedidoExistente.getIdPedido());
                    return pedidoExistente;
                }
            }
        }

        log.debug("Nenhum pedido duplicado encontrado");
        return null;
    }

    private BigDecimal calcularTotalRequest(PedidoRequest request) {
        return request.getItens().stream()
                .map(item -> item.getPrecoUnitario().multiply(new BigDecimal(item.getQuantidade())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private boolean compararItens(Pedido existente, PedidoRequest novo) {
        List<String> assinaturasExistentes = existente.getItens().stream()
                .map(item -> item.getNome() + "|" + item.getQuantidade() + "|" + item.getPrecoUnitario())
                .sorted()
                .collect(Collectors.toList());

        List<String> assinaturasNovas = novo.getItens().stream()
                .map(item -> item.getNome() + "|" + item.getQuantidade() + "|" + item.getPrecoUnitario())
                .sorted()
                .collect(Collectors.toList());

        boolean iguais = assinaturasExistentes.equals(assinaturasNovas);

        if (iguais) {
            log.debug("Itens são idênticos!");
        }

        return iguais;
    }
}