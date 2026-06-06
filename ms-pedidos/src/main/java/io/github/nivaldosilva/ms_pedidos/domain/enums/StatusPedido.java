package io.github.nivaldosilva.ms_pedidos.domain.enums;

import lombok.Getter;

@Getter
public enum StatusPedido {

    REALIZADO,
    CANCELADO,
    PAGO,
    EM_PREPARO,
    PRONTO,
    SAIU_PARA_ENTREGA,
    ENTREGUE
  
}
