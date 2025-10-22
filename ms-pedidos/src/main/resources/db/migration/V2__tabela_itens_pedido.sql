CREATE TABLE itens_pedido (
  
  id_item BINARY(16) NOT NULL,
  pedido_id BINARY(16) NOT NULL, 
  nome VARCHAR(255) NOT NULL,
  descricao VARCHAR(255) NOT NULL,
  preco_unitario DECIMAL(10,2) NOT NULL,
  quantidade INT NOT NULL,
  observacao VARCHAR(255),
  PRIMARY KEY (id_item),
  FOREIGN KEY (pedido_id) REFERENCES pedidos(id_pedido)
);