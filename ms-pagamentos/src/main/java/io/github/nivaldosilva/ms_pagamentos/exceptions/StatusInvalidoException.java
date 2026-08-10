package io.github.nivaldosilva.ms_pagamentos.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class StatusInvalidoException extends RuntimeException {
    
    public StatusInvalidoException(String message) {
		super(message);
    }
}