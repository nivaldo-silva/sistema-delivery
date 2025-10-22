package io.github.nivaldosilva.ms_pagamentos.exceptions;

import java.util.UUID;

public class PagamentoNotFoundException extends RuntimeException {

    public PagamentoNotFoundException(UUID id) {
        super("Pagamento com id " + id + " nao encontrado");
    }

   

}
