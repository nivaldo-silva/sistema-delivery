package io.github.nivaldosilva.ms_pagamentos.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import io.github.nivaldosilva.ms_pagamentos.entity.Pagamento;

public interface PagamentoRepository extends JpaRepository<Pagamento, UUID> {

    boolean existsByIdPedido(UUID idPedido);

}
