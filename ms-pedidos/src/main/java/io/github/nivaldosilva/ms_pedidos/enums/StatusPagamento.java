package io.github.nivaldosilva.ms_pedidos.enums;

import lombok.Getter;

@Getter
public enum StatusPagamento {

    AGUARDANDO_CONFIRMACAO,
    CONFIRMADO,
    RECUSADO,
    CANCELADO
}
