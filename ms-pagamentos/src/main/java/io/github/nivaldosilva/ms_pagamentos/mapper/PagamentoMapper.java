package io.github.nivaldosilva.ms_pagamentos.mapper;

import io.github.nivaldosilva.ms_pagamentos.entity.Pagamento;
import io.github.nivaldosilva.ms_pagamentos.dto.PagamentoRequest;
import io.github.nivaldosilva.ms_pagamentos.dto.PagamentoResponse;
import lombok.experimental.UtilityClass;

@UtilityClass
public class PagamentoMapper {

    public static Pagamento toEntity(PagamentoRequest request) {
        return Pagamento.builder()
                .idPedido(request.getIdPedido())
                .valor(request.getValor())
                .nomeTitular(request.getNomeTitular())
                .numeroCartao(request.getNumeroCartao())
                .validadeCartao(request.getValidadeCartao())
                .codigoSeguranca(request.getCodigoSeguranca())
                .formaPagamento(request.getFormaPagamento())
                .build();
    }

    public static PagamentoResponse toResponse(Pagamento pagamento) {
        return PagamentoResponse.builder()
                .idPagamento(pagamento.getIdPagamento())
                .idPedido(pagamento.getIdPedido())
                .valor(pagamento.getValor())
                .nomeTitular(pagamento.getNomeTitular())
                .numeroMascarado(mascaraNumero(pagamento.getNumeroCartao()))
                .validadeCartao(pagamento.getValidadeCartao())
                .statusPagamento(pagamento.getStatusPagamento())
                .formaPagamento(pagamento.getFormaPagamento())
                .build();
    }

    private static String mascaraNumero(String numero) {
        if (numero == null || numero.length() < 4) {
            return "****";
        }
        return "*".repeat(numero.length() - 4) + numero.substring(numero.length() - 4);
    }
}
