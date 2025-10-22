package io.github.nivaldosilva.ms_pedidos.api.controller;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.web.util.UriComponentsBuilder;
import io.github.nivaldosilva.ms_pedidos.api.openapi.PedidoOpenApi;
import io.github.nivaldosilva.ms_pedidos.dto.AtualizacaoStatus;
import io.github.nivaldosilva.ms_pedidos.dto.PedidoRequest;
import io.github.nivaldosilva.ms_pedidos.dto.PedidoResponse;
import io.github.nivaldosilva.ms_pedidos.dto.PedidoResumo;
import io.github.nivaldosilva.ms_pedidos.service.PedidoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/pedidos")
@RequiredArgsConstructor
@Slf4j
public class PedidoController implements PedidoOpenApi {

    private final PedidoService service;

    @Override
    @PostMapping
    public ResponseEntity<PedidoResponse> criarPedido(@RequestBody @Valid PedidoRequest request,
            UriComponentsBuilder uriBuilder) {
        PedidoResponse pedidoCriado = service.criarPedido(request);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(pedidoCriado.getIdPedido()).toUri();

        return ResponseEntity.created(location).body(pedidoCriado);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponse> obterPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(service.obterPorId(id));
    }

    @Override
    @GetMapping
    public ResponseEntity<List<PedidoResumo>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<PedidoResponse> atualizarPedido(
            @PathVariable UUID id,
            @RequestBody @Valid PedidoRequest request) {
        return ResponseEntity.ok(service.atualizarPedido(id, request));
    }

    @Override
    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> atualizarStatus(
            @PathVariable UUID id,
            @RequestBody @Valid AtualizacaoStatus status) {
        service.atualizarStatus(id, status);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PutMapping("/{id}/pago")
    public ResponseEntity<Void> aprovarPagamento(@PathVariable UUID id) {
        log.info("=== RECEBIDA REQUISIÇÃO PARA APROVAR PAGAMENTO ===");
        log.info("ID do Pedido: {}", id);

        try {
            service.aprovarPagamento(id);
            log.info("Pagamento aprovado com sucesso para o pedido: {}", id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("ERRO ao aprovar pagamento para o pedido: {}. Erro: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelarPedido(@PathVariable UUID id) {
        service.cancelarPedido(id);
        return ResponseEntity.noContent().build();
    }

}