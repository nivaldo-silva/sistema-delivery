package io.github.nivaldosilva.ms_pagamentos.domain.enums;

import lombok.Getter;

@Getter
public enum StatusPagamento {

    AGUARDANDO_CONFIRMACAO,
    CONFIRMADO,
    RECUSADO,
    CANCELADO

}
