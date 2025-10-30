package io.github.nivaldosilva.ms_pagamentos.api.controller;

import java.net.URI;
import java.util.UUID;

import io.github.nivaldosilva.ms_pagamentos.rabbitmq.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import io.github.nivaldosilva.ms_pagamentos.api.openapi.PagamentoOpenApi;
import io.github.nivaldosilva.ms_pagamentos.dto.PagamentoRequest;
import io.github.nivaldosilva.ms_pagamentos.dto.PagamentoResponse;
import io.github.nivaldosilva.ms_pagamentos.service.PagamentoService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/pagamentos")
@RequiredArgsConstructor
public class PagamentoController implements PagamentoOpenApi {

    private final PagamentoService service;
    private final RabbitTemplate rabbitTemplate;

    @Override
    @PostMapping
    public ResponseEntity<PagamentoResponse> cadastrar(@RequestBody @Valid PagamentoRequest request) {
        log.info("Recebida requisição para criar pagamento: {}", request);
        PagamentoResponse pagamentoCriado = service.criarPagamento(request);
        log.info("Pagamento processado com sucesso no serviço: {}", pagamentoCriado.getIdPagamento());

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(pagamentoCriado.getIdPagamento())
                .toUri();

        rabbitTemplate.convertAndSend(RabbitMQConfig.QUEUE_NAME, pagamentoCriado);
        log.info("Mensagem enviada para fila: {}", RabbitMQConfig.QUEUE_NAME);

        return ResponseEntity.created(location).body(pagamentoCriado);
    }

    @Override
    @GetMapping
    public ResponseEntity<Page<PagamentoResponse>> listar(
            @PageableDefault(size = 10, sort = "dataPagamento") Pageable paginacao) {
        return ResponseEntity.ok(service.buscarPagamentos(paginacao));
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<PagamentoResponse> buscar(@PathVariable UUID id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<PagamentoResponse> atualizar(
            @PathVariable UUID id,
            @RequestBody PagamentoRequest request) {
        return ResponseEntity.ok(service.atualizarPagamento(id, request));
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable UUID id) {
        service.excluirPagamento(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PatchMapping("/{id}/confirmar")
    public ResponseEntity<Void> confirmarPagamento(@PathVariable @NotNull UUID id) {
        service.confirmarPagamento(id);
        return ResponseEntity.noContent().build();
    }

}
