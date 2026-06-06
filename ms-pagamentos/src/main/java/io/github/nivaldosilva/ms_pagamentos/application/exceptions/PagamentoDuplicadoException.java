package io.github.nivaldosilva.ms_pagamentos.application.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class PagamentoDuplicadoException extends RuntimeException {

    public PagamentoDuplicadoException(String idPedido) {
		super("Ja existe um pagamento para o pedido: " + idPedido);
    }

}
