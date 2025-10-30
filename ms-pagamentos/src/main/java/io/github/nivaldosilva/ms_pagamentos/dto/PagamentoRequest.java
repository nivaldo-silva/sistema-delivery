package io.github.nivaldosilva.ms_pagamentos.dto;

import java.math.BigDecimal;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.nivaldosilva.ms_pagamentos.enums.FormaPagamento;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para requisição de pagamento")
public class PagamentoRequest {

    @NotNull(message = "ID do pedido é obrigatório")
    @JsonProperty("id_pedido")
    @Schema(description = "ID do pedido", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID idPedido;

    @NotNull(message = "Valor é obrigatório")
    @Positive(message = "Valor deve ser maior que zero")
    @JsonProperty("valor")
    @Schema(description = "Valor do pagamento", example = "100.50")
    private BigDecimal valor;

    @NotBlank(message = "Nome do titular é obrigatório")
    @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
    @JsonProperty("nome_titular")
    @Schema(description = "Nome do titular do cartão", example = "Nivaldo Silva")
    private String nomeTitular;

    @NotBlank(message = "Número do cartão é obrigatório")
    @Pattern(regexp = "\\d{13,19}", message = "Número do cartão inválido")
    @JsonProperty("numero_cartao")
    @Schema(description = "Número do cartão", example = "1234567890123456")
    private String numeroCartao;

    @NotBlank(message = "Data de expiração é obrigatória")
    @Pattern(regexp = "(0[1-9]|1[0-2])/\\d{4}", message = "Formato deve ser MM/YYYY")
    @JsonProperty("validade_cartao")
    @Schema(description = "Data de validade do cartão", example = "12/2030")
    private String validadeCartao;

    @NotBlank(message = "Código de segurança é obrigatório")
    @Pattern(regexp = "\\d{3}", message = "CVV deve ter 3 dígitos")
    @JsonProperty("codigo_seguranca")
    @Schema(description = "Código de segurança do cartão", example = "123")
    private String codigoSeguranca;

    @NotNull(message = "A forma de pagamento é obrigatório")
    @JsonProperty("forma_pagamento")
    @Schema(description = "Forma de pagamento", example = "CREDITO")
    private FormaPagamento formaPagamento;
}