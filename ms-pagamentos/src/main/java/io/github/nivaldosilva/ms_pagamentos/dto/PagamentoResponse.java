package io.github.nivaldosilva.ms_pagamentos.dto;

import java.math.BigDecimal;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.github.nivaldosilva.ms_pagamentos.enums.FormaPagamento;
import io.github.nivaldosilva.ms_pagamentos.enums.StatusPagamento;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "DTO para resposta de pagamento")
public class PagamentoResponse {

    @Schema(description = "ID do pagamento", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID idPagamento;

    @Schema(description = "ID do pedido", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID idPedido;

    @Schema(description = "Valor do pagamento", example = "100.50")
    private BigDecimal valor;

    @Schema(description = "Nome do titular do cartão", example = "Nivaldo Silva")
    private String nomeTitular;

    @Schema(description = "Número do cartão mascarado", example = "************3456")
    private String numeroMascarado;

    @Schema(description = "Data de validade do cartão", example = "12/2030")
    private String validadeCartao;

    @Schema(description = "Status do pagamento", example = "CONFIRMADO")
    private StatusPagamento statusPagamento;

    @Schema(description = "Forma de pagamento", example = "CREDITO")
    private FormaPagamento formaPagamento;
}
