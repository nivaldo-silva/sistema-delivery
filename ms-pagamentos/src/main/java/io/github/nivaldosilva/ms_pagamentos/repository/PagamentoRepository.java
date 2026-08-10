package io.github.nivaldosilva.ms_pagamentos.repository;

import java.util.UUID;
import io.github.nivaldosilva.ms_pagamentos.entity.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PagamentoRepository extends JpaRepository<Pagamento, UUID> {

    boolean existsByIdPedido(UUID idPedido);

}
