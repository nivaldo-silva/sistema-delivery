package io.github.nivaldosilva.ms_pedidos.repository;

import io.github.nivaldosilva.ms_pedidos.entity.Pedido;
import io.github.nivaldosilva.ms_pedidos.enums.StatusPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PedidoRepository extends JpaRepository<Pedido, UUID> {

    @Query("SELECT p FROM Pedido p LEFT JOIN FETCH p.itens WHERE p.idPedido = :id")
    Optional<Pedido> findByIdWithItens(UUID id);

    @Query("SELECT DISTINCT p FROM Pedido p LEFT JOIN FETCH p.itens WHERE p.statusPedido = :status AND p.dataHora > :data")
    List<Pedido> findByStatusAndDataHoraAfterWithItens(StatusPedido status, Instant data);

}
