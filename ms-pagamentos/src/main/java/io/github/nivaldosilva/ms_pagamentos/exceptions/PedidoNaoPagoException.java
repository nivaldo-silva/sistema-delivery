package io.github.nivaldosilva.ms_pagamentos.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PedidoNaoPagoException extends RuntimeException {

    public PedidoNaoPagoException(String message) {
        super(message);
    }
}
