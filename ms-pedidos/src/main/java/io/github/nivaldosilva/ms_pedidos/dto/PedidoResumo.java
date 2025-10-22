package io.github.nivaldosilva.ms_pedidos.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "Resumo do pedido para listagens")
public class PedidoResumo {

    @JsonProperty("id_pedido")
    @Schema(example = "a1b2c3d4-e5f6-7890-abcd-ef1234567890")
    private UUID idPedido;

    @JsonProperty("numero")
    @Schema(example = "#12345")
    private String numero;

    @JsonProperty("status")
    private StatusSimples status;

    @Schema(description = "Valor total", example = "99.80")
    private BigDecimal total;

    @JsonProperty("quantidade_itens")
    @Schema(example = "3")
    private Integer quantidadeItens;

    @JsonProperty("data_pedido")
    private Instant dataPedido;

    @Data
    @Builder
    public static class StatusSimples {
        @JsonProperty("codigo")
        @Schema(example = "CONFIRMADO")
        private String codigo;

        @JsonProperty("descricao")
        @Schema(example = "Confirmado")
        private String descricao;
    }
}
