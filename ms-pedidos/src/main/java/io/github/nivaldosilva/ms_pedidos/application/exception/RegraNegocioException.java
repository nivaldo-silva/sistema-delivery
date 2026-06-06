package io.github.nivaldosilva.ms_pedidos.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class RegraNegocioException extends RuntimeException {

    public RegraNegocioException(String message) {
		super(message);
    }
}
