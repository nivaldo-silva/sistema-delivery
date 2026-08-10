package io.github.nivaldosilva.ms_pedidos.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.nivaldosilva.ms_pedidos.enums.FormaPagamento;
import io.github.nivaldosilva.ms_pedidos.enums.StatusPagamento;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Resposta completa do pagamento")
public class PagamentoResponse {

    @JsonProperty("id_pagamento")
    @Schema(description = "ID do pagamento", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID idPagamento;

    @JsonProperty("id_pedido")
    @Schema(description = "ID do pedido", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID idPedido;

    @JsonProperty("valor")
    @Schema(description = "Valor do pagamento", example = "100.50")
    private BigDecimal valor;

    @JsonProperty("nome_titular")
    @Schema(description = "Nome do titular do cartão", example = "Nivaldo Silva")
    private String nomeTitular;

    @JsonProperty("numero_mascarado")
    @Schema(description = "Número do cartão mascarado", example = "************3456")
    private String numeroMascarado;

    @JsonProperty("validade_cartao")
    @Schema(description = "Data de validade do cartão", example = "12/2030")
    private String validadeCartao;

    @JsonProperty("status_pagamento")
    @Schema(description = "Status do pagamento", example = "CONFIRMADO")
    private StatusPagamento statusPagamento;

    @JsonProperty("forma_pagamento")
    @Schema(description = "Forma de pagamento", example = "CREDITO")
    private FormaPagamento formaPagamento;
}
