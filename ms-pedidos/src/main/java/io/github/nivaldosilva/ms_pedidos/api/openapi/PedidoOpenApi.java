package io.github.nivaldosilva.ms_pedidos.api.openapi;

import io.github.nivaldosilva.ms_pedidos.dto.AtualizacaoStatus;
import io.github.nivaldosilva.ms_pedidos.dto.PedidoRequest;
import io.github.nivaldosilva.ms_pedidos.dto.PedidoResponse;
import io.github.nivaldosilva.ms_pedidos.dto.PedidoResumo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import java.util.List;
import java.util.UUID;

@Tag(name = "Pedidos", description = "API de gerenciamento de pedidos delivery")
public interface PedidoOpenApi {

        @Operation(summary = "Criar pedido", description = "Registra um novo pedido no sistema de delivery com itens, endereço de entrega e observações. Retorna o pedido criado com status REALIZADO e ID único gerado.")
        @ApiResponses({
                        @ApiResponse(responseCode = "201", description = "Pedido criado com sucesso e pronto para processamento", content = @Content(schema = @Schema(implementation = PedidoResponse.class))),
                        @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos na requisição (validação de campos)", content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                        @ApiResponse(responseCode = "422", description = "Erro de validação de negócio (ex: produto indisponível, endereço fora da área de entrega)", content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
        })
        @PostMapping
        ResponseEntity<PedidoResponse> criarPedido(
                        @RequestBody @Valid PedidoRequest request,
                        UriComponentsBuilder uriBuilder);

        @Operation(summary = "Buscar pedido por ID", description = "Retorna os detalhes completos de um pedido específico incluindo itens, status, valor total e informações de entrega.")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Pedido encontrado e retornado com sucesso", content = @Content(schema = @Schema(implementation = PedidoResponse.class))),
                        @ApiResponse(responseCode = "404", description = "Pedido não encontrado com o ID fornecido", content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
        })
        @GetMapping("/{id}")
        ResponseEntity<PedidoResponse> obterPorId(
                        @Parameter(description = "ID único do pedido no formato UUID", required = true, example = "123e4567-e89b-12d3-a456-426614174000") @PathVariable @NotNull UUID id);

        @Operation(summary = "Listar todos os pedidos", description = "Retorna um resumo de todos os pedidos cadastrados no sistema com informações básicas como ID, status, valor total e data.")
        @ApiResponse(responseCode = "200", description = "Lista de pedidos retornada com sucesso (pode estar vazia se não houver pedidos)")
        @GetMapping
        ResponseEntity<List<PedidoResumo>> listarTodos();

        @Operation(summary = "Atualizar pedido", description = "Atualiza os dados de um pedido existente. Apenas pedidos com status REALIZADO podem ser alterados. Permite modificar itens, endereço e observações.")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Pedido atualizado com sucesso", content = @Content(schema = @Schema(implementation = PedidoResponse.class))),
                        @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos na requisição", content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                        @ApiResponse(responseCode = "404", description = "Pedido não encontrado com o ID fornecido", content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                        @ApiResponse(responseCode = "422", description = "Status do pedido não permite alteração (pedido já em processamento, entregue ou cancelado)", content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
        })
        @PutMapping("/{id}")
        ResponseEntity<PedidoResponse> atualizarPedido(
                        @Parameter(description = "ID único do pedido no formato UUID", required = true, example = "123e4567-e89b-12d3-a456-426614174000") @PathVariable @NotNull UUID id,
                        @RequestBody @Valid PedidoRequest request);

        @Operation(summary = "Atualizar status do pedido", description = "Altera o status do pedido seguindo o fluxo válido: REALIZADO → EM_PREPARACAO → PRONTO → SAIU_PARA_ENTREGA → ENTREGUE. Pedidos com status CANCELADO não podem ter o status alterado.")
        @ApiResponses({
                        @ApiResponse(responseCode = "204", description = "Status do pedido atualizado com sucesso (sem conteúdo no corpo da resposta)"),
                        @ApiResponse(responseCode = "404", description = "Pedido não encontrado com o ID fornecido", content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                        @ApiResponse(responseCode = "422", description = "Transição de status inválida (ex: não é possível voltar de ENTREGUE para PRONTO)", content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
        })
        @PatchMapping("/{id}/status")
        ResponseEntity<Void> atualizarStatus(
                        @Parameter(description = "ID de um pedido", example = "a1b2c3d4-e5f6-7890-1234-567890abcdef") @PathVariable UUID id,
                        @RequestBody @Valid AtualizacaoStatus status);

        @Operation(summary = "Aprova o pagamento de um pedido", responses = {
                        @ApiResponse(responseCode = "204", description = "Pagamento aprovado com sucesso"),
                        @ApiResponse(responseCode = "400", description = "Status do pedido não permite o pagamento", content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                        @ApiResponse(responseCode = "404", description = "Pedido não encontrado", content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
        })
        ResponseEntity<Void> aprovarPagamento(
                        @Parameter(description = "ID de um pedido", example = "a1b2c3d4-e5f6-7890-1234-567890abcdef") @PathVariable UUID id);

        @Operation(summary = "Cancela um pedido", responses = {
                        @ApiResponse(responseCode = "204", description = "Pedido cancelado com sucesso"),
                        @ApiResponse(responseCode = "404", description = "Pedido não encontrado com o ID fornecido", content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                        @ApiResponse(responseCode = "422", description = "Pedido não pode ser cancelado devido ao status atual (já está pronto, saiu para entrega ou foi entregue)", content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
        })
        @DeleteMapping("/{id}")
        ResponseEntity<Void> cancelarPedido(
                        @Parameter(description = "ID único do pedido no formato UUID", required = true, example = "123e4567-e89b-12d3-a456-426614174000") @PathVariable @NotNull UUID id);

}