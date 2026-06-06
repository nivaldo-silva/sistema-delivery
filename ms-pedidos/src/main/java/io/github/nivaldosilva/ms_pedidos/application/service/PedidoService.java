package io.github.nivaldosilva.ms_pedidos.application.service;

import io.github.nivaldosilva.ms_pedidos.application.dto.AtualizacaoStatus;
import io.github.nivaldosilva.ms_pedidos.application.dto.PedidoRequest;
import io.github.nivaldosilva.ms_pedidos.application.dto.PedidoResponse;
import io.github.nivaldosilva.ms_pedidos.application.dto.PedidoResumo;
import java.util.List;
import java.util.UUID;

public interface PedidoService {

    PedidoResponse criarPedido(PedidoRequest request);

    PedidoResponse obterPorId(UUID id);

    List<PedidoResumo> listarTodos();

    PedidoResponse atualizarPedido(UUID id, PedidoRequest request);

    void atualizarStatus(UUID id, AtualizacaoStatus statusDTO);

    void aprovarPagamento(UUID id);

    void cancelarPedido(UUID id);

   
}
