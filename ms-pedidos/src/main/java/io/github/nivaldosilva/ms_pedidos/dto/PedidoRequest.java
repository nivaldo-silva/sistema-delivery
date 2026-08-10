package io.github.nivaldosilva.ms_pedidos.dto;

import java.math.BigDecimal;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PedidoRequest {

    @NotEmpty(message = "Adicione pelo menos um item ao seu pedido")
    @Valid
    @JsonProperty("itens")
    @Schema(description = "Lista de itens do pedido", required = true)
    private List<ItemRequest> itens;

    @Size(max = 300, message = "Observações não podem exceder 300 caracteres")
    @JsonProperty("observacoes")
    @Schema(description = "Observações sobre o pedido", example = "Sem cebola, por favor")
    private String observacoes;

    @Data
    @Builder
    @Schema(description = "Item do pedido")
    public static class ItemRequest {

        @NotBlank(message = "Informe o nome do item")
        @Size(max = 100, message = "Nome muito longo")
        @JsonProperty("nome")
        @Schema(description = "Nome do produto", example = "Pizza Margherita Grande", required = true)
        private String nome;

        @NotBlank(message = "Informe a descrição do item")
        @Size(max = 300, message = "Descrição muito longa")
        @JsonProperty("descricao")
        @Schema(description = "Descrição do item", example = "Molho de tomate, mussarela, manjericão", required = true)
        private String descricao;

        @NotNull(message = "Informe o preço do item")
        @DecimalMin(value = "0.01", message = "Preço deve ser maior que zero")
        @DecimalMax(value = "99999.99", message = "Preço inválido")
        @JsonProperty("preco")
        @Schema(description = "Preço unitário", example = "45.90", required = true)
        private BigDecimal precoUnitario;

        @NotNull(message = "Informe a quantidade")
        @Min(value = 1, message = "Quantidade mínima: 1")
        @Max(value = 50, message = "Quantidade máxima: 50")
        @JsonProperty("quantidade")
        @Schema(description = "Quantidade", example = "2", required = true)
        private Integer quantidade;

        @Size(max = 200, message = "Observação muito longa")
        @JsonProperty("observacao")
        @Schema(description = "Observação do item", example = "Massa fina")
        private String observacao;
    }

}