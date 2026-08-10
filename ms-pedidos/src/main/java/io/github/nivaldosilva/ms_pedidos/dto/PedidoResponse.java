package io.github.nivaldosilva.ms_pedidos.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Resposta completa do pedido")
public class PedidoResponse {

    @JsonProperty("id_pedido")
    @Schema(description = "ID único do pedido", example = "a1b2c3d4-e5f6-7890-abcd-ef1234567890")
    private UUID idPedido;

    @JsonProperty("numero")
    @Schema(description = "Número amigável", example = "#12345")
    private String numero;

    @JsonProperty("status")
    @Schema(description = "Informações do status atual")
    private StatusInfo status;

    @JsonProperty("itens")
    @Schema(description = "Itens do pedido")
    private List<ItemResponse> itens;

    @JsonProperty("resumo")
    @Schema(description = "Resumo financeiro")
    private ResumoFinanceiro resumo;

    @JsonProperty("data_pedido")
    @Schema(description = "Data/hora de criação", example = "2025-10-14T15:30:00Z")
    private Instant dataPedido;

    @JsonProperty("observacoes")
    @Schema(description = "Observações do cliente")
    private String observacoes;

    @Data
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ItemResponse {
        @JsonProperty("id_item")
        @Schema(example = "a1b2c3d4-e5f6-7890-abcd-ef1234567890")
        private UUID idItem;

        @JsonProperty("nome")
        @Schema(example = "Pizza Margherita Grande")
        private String nome;

        @JsonProperty("descricao")
        private String descricao;

        @JsonProperty("preco_unitario")
        @Schema(example = "45.90")
        private BigDecimal precoUnitario;

        @JsonProperty("quantidade")
        @Schema(example = "2")
        private Integer quantidade;

        @JsonProperty("subtotal")
        @Schema(example = "91.80")
        private BigDecimal subtotal;

        @JsonProperty("observacao")
        private String observacao;
    }

    @Data
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class StatusInfo {
        @JsonProperty("codigo")
        @Schema(example = "CONFIRMADO")
        private String codigo;

        @JsonProperty("titulo")
        @Schema(example = "Pedido Confirmado")
        private String titulo; 

        @JsonProperty("descricao")
        @Schema(example = "Seu pedido foi confirmado e está sendo preparado")
        private String descricao;
    }

    @Data
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ResumoFinanceiro {
        @JsonProperty("subtotal")
        @Schema(description = "Soma dos itens", example = "91.80")
        private BigDecimal subtotal;

        @JsonProperty("taxa_entrega")
        @Schema(description = "Taxa de entrega", example = "8.00")
        private BigDecimal taxaEntrega;

        @JsonProperty("desconto")
        @Schema(description = "Desconto aplicado", example = "0.00")
        private BigDecimal desconto;

        @JsonProperty("total")
        @Schema(description = "Valor total", example = "99.80")
        private BigDecimal total;

        @JsonProperty("quantidade_itens")
        @Schema(description = "Total de itens", example = "3")
        private Integer quantidadeItens;
    }
}
