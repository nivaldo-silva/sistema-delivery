package io.github.nivaldosilva.ms_pagamentos.service;

import java.util.UUID;
import io.github.nivaldosilva.ms_pagamentos.dto.PagamentoRequest;
import io.github.nivaldosilva.ms_pagamentos.dto.PagamentoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PagamentoService {

    PagamentoResponse criarPagamento(PagamentoRequest request);

    Page<PagamentoResponse> buscarPagamentos(Pageable paginacao);

    PagamentoResponse buscarPorId(UUID id);

    void confirmarPagamento(UUID id);

    PagamentoResponse atualizarPagamento(UUID id, PagamentoRequest request);

    void excluirPagamento(UUID id);

}
