package io.github.nivaldosilva.ms_pagamentos.application.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class PedidoNaoPagoException extends RuntimeException {

    public PedidoNaoPagoException(String message) {
		super(message);
    }
}
