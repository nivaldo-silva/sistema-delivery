package io.github.nivaldosilva.ms_pedidos.service;

import io.github.nivaldosilva.ms_pedidos.dto.AtualizacaoStatus;
import io.github.nivaldosilva.ms_pedidos.dto.PedidoRequest;
import io.github.nivaldosilva.ms_pedidos.dto.PedidoResponse;
import io.github.nivaldosilva.ms_pedidos.dto.PedidoResumo;
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
