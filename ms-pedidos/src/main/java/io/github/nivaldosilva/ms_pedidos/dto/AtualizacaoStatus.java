package io.github.nivaldosilva.ms_pedidos.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.nivaldosilva.ms_pedidos.enums.StatusPedido;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "Atualização de status do pedido")
public class AtualizacaoStatus {

    @NotNull(message = "O status é obrigatório")
    @JsonProperty("status")
    @Schema(description = "Novo status do pedido", example = "CONFIRMADO", required = true)
    private StatusPedido status;

}
