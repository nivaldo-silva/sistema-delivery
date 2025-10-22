CREATE TABLE pagamentos (

    id_pagamento BINARY(16) NOT NULL,
    id_pedido BINARY(16) NOT NULL UNIQUE,
    valor DECIMAL(19,2) NOT NULL,
    nome_titular VARCHAR(100) NOT NULL,
    numero_cartao VARCHAR(19) NOT NULL,
    validade_cartao VARCHAR(7) NOT NULL,
    codigo_seguranca VARCHAR(3) NOT NULL,
    forma_pagamento ENUM('DEBITO','CREDITO') NOT NULL,
    status_pagamento ENUM('AGUARDANDO_CONFIRMACAO','CONFIRMADO','RECUSADO','CANCELADO') NOT NULL,  
    data_pagamento DATETIME NOT NULL,   
    PRIMARY KEY (id_pagamento)
);
