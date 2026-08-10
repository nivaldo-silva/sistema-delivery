package io.github.nivaldosilva.ms_pagamentos.entity;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.CreationTimestamp;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.nivaldosilva.ms_pagamentos.enums.FormaPagamento;
import io.github.nivaldosilva.ms_pagamentos.enums.StatusPagamento;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "pagamentos")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_pagamento", nullable = false, updatable = false)
    private UUID idPagamento;

    @Column(name = "id_pedido", nullable = false, unique = true)
    private UUID idPedido;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;

    @Column(name = "nome_titular", nullable = false, length = 100)
    private String nomeTitular;

    @Column(name = "numero_cartao", nullable = false, length = 19)
    private String numeroCartao;

    @Column(name = "validade_cartao", nullable = false, length = 7)
    private String validadeCartao;

    @Column(name = "codigo_seguranca", nullable = false, length = 3)
    private String codigoSeguranca;

    @Enumerated(EnumType.STRING)
    @Column(name = "forma_pagamento", nullable = false)
    private FormaPagamento formaPagamento;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_pagamento", nullable = false, length = 20)
    private StatusPagamento statusPagamento;

    @CreationTimestamp
    @Column(name = "data_pagamento", nullable = false, updatable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Instant dataPagamento;

}