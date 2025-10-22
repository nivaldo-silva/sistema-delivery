package io.github.nivaldosilva.ms_pagamentos.exceptions;

public class PagamentoDuplicadoException extends RuntimeException {

    public PagamentoDuplicadoException(String idPedido) {
        super("Ja existe um pagamento para o pedido: " + idPedido);
    }

}
