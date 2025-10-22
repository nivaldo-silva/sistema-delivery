package io.github.nivaldosilva.ms_pedidos.entity;

import java.math.BigDecimal;
import java.util.UUID;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "itens_pedido")
@Data
@ToString(exclude = "pedido")
@EqualsAndHashCode(exclude = "pedido")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemPedido {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_item", updatable = false, nullable = false)
    private UUID idItem;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;

    @Column(nullable = false, length = 255)
    private String nome;

    @Column(nullable = false, length = 255)
    private String descricao;

    @Column(name = "preco_unitario", nullable = false, precision = 10, scale = 2)
    private BigDecimal precoUnitario;

    @Column(nullable = false)
    private Integer quantidade;

    @Column(length = 200)
    private String observacao;

    public BigDecimal getSubtotal() {
        if (this.precoUnitario == null || this.quantidade == null) {
            return BigDecimal.ZERO;
        }
        return this.precoUnitario.multiply(new BigDecimal(this.quantidade));
    }

}
