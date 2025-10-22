package io.github.nivaldosilva.ms_pedidos.entity;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.hibernate.annotations.CreationTimestamp;
import io.github.nivaldosilva.ms_pedidos.enums.StatusPedido;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "pedidos")
@Data
@ToString(exclude = "itens")
@EqualsAndHashCode(exclude = "itens")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_pedido", updatable = false, nullable = false)
    private UUID idPedido;

    @Column(name = "numero", nullable = true, length = 10)
    private String numero;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_pedido", nullable = false, length = 20)
    @Builder.Default
    private StatusPedido statusPedido = StatusPedido.REALIZADO;

    @Column(name = "observacao", length = 300)
    private String observacao;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "pedido", orphanRemoval = true)
    @Builder.Default
    private List<ItemPedido> itens = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "data_hora", nullable = false, updatable = false)
    private Instant dataHora;

    @PostPersist
    public void gerarNumero() {
        if (this.idPedido != null && this.numero == null) {
            String idStr = this.idPedido.toString().replace("-", "");
            this.numero = "#" + idStr.substring(Math.max(0, idStr.length() - 5)).toUpperCase();
        }
    }

    public void adicionarItem(ItemPedido item) {
        if (this.statusPedido != StatusPedido.REALIZADO) {
            throw new IllegalStateException("Não é possível adicionar itens a um pedido que não está REALIZADO.");
        }
        this.itens.add(item);
        item.setPedido(this);
    }

    public void removerItem(ItemPedido item) {
        this.itens.remove(item);
        item.setPedido(null);
    }

    public BigDecimal calcularTotal() {
        return this.itens.stream()
                .map(ItemPedido::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}