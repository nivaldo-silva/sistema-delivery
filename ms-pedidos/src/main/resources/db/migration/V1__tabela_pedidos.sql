CREATE TABLE pedidos (
  
  id_pedido BINARY(16) NOT NULL,
  numero VARCHAR(10) NULL,
  observacao VARCHAR(255),
  data_hora DATETIME(6) NOT NULL,
  status_pedido ENUM('REALIZADO', 'CANCELADO', 'PAGO', 'EM_PREPARO', 'PRONTO', 'SAIU_PARA_ENTREGA', 'ENTREGUE') NOT NULL,
  PRIMARY KEY (id_pedido)
);

