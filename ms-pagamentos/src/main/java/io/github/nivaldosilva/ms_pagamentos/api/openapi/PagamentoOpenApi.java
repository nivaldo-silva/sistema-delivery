package io.github.nivaldosilva.ms_pagamentos.api.openapi;

import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import io.github.nivaldosilva.ms_pagamentos.dto.PagamentoRequest;
import io.github.nivaldosilva.ms_pagamentos.dto.PagamentoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@Tag(name = "Pagamentos", description = "API de gerenciamento para pagamentos")
public interface PagamentoOpenApi {

        @Operation(summary = "Cadastrar pagamento", description = "Cadastra um novo pagamento no sistema. Retorna o pagamento criado com ID gerado.")
        @ApiResponses({
                        @ApiResponse(responseCode = "201", description = "Pagamento criado com sucesso", content = @Content(schema = @Schema(implementation = PagamentoResponse.class))),
                        @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos na requisição", content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                        @ApiResponse(responseCode = "422", description = "Erro de validação de negócio", content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
        })
        ResponseEntity<PagamentoResponse> cadastrar(@Valid PagamentoRequest request);

        @Operation(summary = "Listar pagamentos", description = "Lista todos os pagamentos cadastrados com suporte a paginação e ordenação")
        @ApiResponse(responseCode = "200", description = "Lista de pagamentos retornada com sucesso")
        ResponseEntity<Page<PagamentoResponse>> listar(
                        @Parameter(description = "Configuração de paginação e ordenação") Pageable paginacao);

        @Operation(summary = "Buscar pagamento por ID", description = "Busca um pagamento específico utilizando seu identificador único")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Pagamento encontrado", content = @Content(schema = @Schema(implementation = PagamentoResponse.class))),
                        @ApiResponse(responseCode = "404", description = "Pagamento não encontrado com o ID fornecido", content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
        })
        ResponseEntity<PagamentoResponse> buscar(
                        @Parameter(description = "ID único do pagamento", required = true) @NotNull UUID id);

        @Operation(summary = "Atualizar pagamento", description = "Atualiza os dados de um pagamento existente")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Pagamento atualizado com sucesso", content = @Content(schema = @Schema(implementation = PagamentoResponse.class))),
                        @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos na requisição", content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                        @ApiResponse(responseCode = "404", description = "Pagamento não encontrado", content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
        })
        ResponseEntity<PagamentoResponse> atualizar(
                        @Parameter(description = "ID único do pagamento", required = true) @NotNull UUID id,
                        @Valid PagamentoRequest request);



        @Operation(summary = "Confirmar pagamento", description = "Confirma um pagamento existente")
        @ApiResponses({
                        @ApiResponse(responseCode = "204", description = "Pagamento confirmado com sucesso"),
                        @ApiResponse(responseCode = "404", description = "Pagamento não encontrado", content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
        })
        ResponseEntity<Void> confirmarPagamento(
                        @Parameter(description = "ID único do pagamento", required = true) @NotNull UUID id);


                        

        @Operation(summary = "Remover pagamento", description = "Remove permanentemente um pagamento do sistema")
        @ApiResponses({
                        @ApiResponse(responseCode = "204", description = "Pagamento removido com sucesso"),
                        @ApiResponse(responseCode = "404", description = "Pagamento não encontrado", content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
        })
        ResponseEntity<Void> remover(
                        @Parameter(description = "ID único do pagamento", required = true) @NotNull UUID id);


}