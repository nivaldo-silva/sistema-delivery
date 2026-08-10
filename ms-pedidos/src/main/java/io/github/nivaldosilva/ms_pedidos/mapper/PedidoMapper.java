package io.github.nivaldosilva.ms_pedidos.mapper;

import io.github.nivaldosilva.ms_pedidos.dto.PedidoRequest;
import io.github.nivaldosilva.ms_pedidos.dto.PedidoResponse;
import io.github.nivaldosilva.ms_pedidos.dto.PedidoResumo;
import io.github.nivaldosilva.ms_pedidos.entity.ItemPedido;
import io.github.nivaldosilva.ms_pedidos.entity.Pedido;
import io.github.nivaldosilva.ms_pedidos.enums.StatusPedido;
import lombok.experimental.UtilityClass;
import java.math.BigDecimal;
import java.util.UUID;
import java.util.stream.Collectors;

@UtilityClass
public class PedidoMapper {

    public static Pedido toEntity(PedidoRequest request) {
        Pedido pedido = Pedido.builder()
                .statusPedido(StatusPedido.REALIZADO) 
                .observacao(request.getObservacoes())
                .build();

        request.getItens().forEach(itemRequest -> {
            ItemPedido item = ItemPedido.builder()
                    .nome(itemRequest.getNome())
                    .descricao(itemRequest.getDescricao())
                    .precoUnitario(itemRequest.getPrecoUnitario())
                    .quantidade(itemRequest.getQuantidade())
                    .observacao(itemRequest.getObservacao())
                    .build();
            pedido.adicionarItem(item);
        });

        return pedido;
    }

    public static PedidoResponse toResponse(Pedido pedido) {
        BigDecimal subtotal = pedido.calcularTotal();
        BigDecimal taxaEntrega = BigDecimal.valueOf(8.00);
        BigDecimal desconto = BigDecimal.ZERO;
        BigDecimal total = subtotal.add(taxaEntrega).subtract(desconto);

        String numero = pedido.getNumero();
        if (numero == null && pedido.getIdPedido() != null) {
            numero = formatarNumeroPedido(pedido.getIdPedido());
        }

        return PedidoResponse.builder()
                .idPedido(pedido.getIdPedido())
                .numero(numero)
                .status(mapearStatus(pedido.getStatusPedido()))
                .itens(pedido.getItens().stream()
                        .map(PedidoMapper::toItemResponse)
                        .collect(Collectors.toList()))
                .resumo(PedidoResponse.ResumoFinanceiro.builder()
                        .subtotal(subtotal)
                        .taxaEntrega(taxaEntrega)
                        .desconto(desconto)
                        .total(total)
                        .quantidadeItens(pedido.getItens().stream().mapToInt(ItemPedido::getQuantidade).sum())
                        .build())
                .dataPedido(pedido.getDataHora())
                .observacoes(pedido.getObservacao())
                .build();
    }

    public static PedidoResumo toResumo(Pedido pedido) {
        BigDecimal total = pedido.calcularTotal().add(BigDecimal.valueOf(8.00));

        String numero = pedido.getNumero();
        if (numero == null && pedido.getIdPedido() != null) {
            numero = formatarNumeroPedido(pedido.getIdPedido());
        }

        return PedidoResumo.builder()
                .idPedido(pedido.getIdPedido())
                .numero(numero)
                .status(PedidoResumo.StatusSimples.builder()
                        .codigo(pedido.getStatusPedido().name())
                        .descricao(getDescricaoStatus(pedido.getStatusPedido()))
                        .build())
                .total(total)
                .quantidadeItens(pedido.getItens().stream().mapToInt(ItemPedido::getQuantidade).sum())
                .dataPedido(pedido.getDataHora())
                .build();
    }

    private static PedidoResponse.ItemResponse toItemResponse(ItemPedido item) {
        return PedidoResponse.ItemResponse.builder()
                .idItem(item.getIdItem())
                .nome(item.getNome())
                .descricao(item.getDescricao())
                .precoUnitario(item.getPrecoUnitario())
                .quantidade(item.getQuantidade())
                .subtotal(item.getSubtotal())
                .observacao(item.getObservacao())
                .build();
    }

    private static PedidoResponse.StatusInfo mapearStatus(StatusPedido status) {
        return PedidoResponse.StatusInfo.builder()
                .codigo(status.name())
                .titulo(getTituloStatus(status))
                .descricao(getDescricaoStatus(status))
                .build();
    }

    private static String getTituloStatus(StatusPedido status) {
        switch (status) {
            case REALIZADO:
                return "Pedido Recebido";
            case CANCELADO:
                return "Pedido Cancelado";
            case PAGO:
                return "Pedido Pago";
            case EM_PREPARO:
                return "Em Preparo";
            case PRONTO:
                return "Pedido Pronto";
            case SAIU_PARA_ENTREGA:
                return "Saiu para Entrega";
            case ENTREGUE:
                return "Pedido Entregue";
            default:
                return status.name();
        }
    }

    private static String getDescricaoStatus(StatusPedido status) {
        switch (status) {
            case REALIZADO:
                return "Aguardando confirmação do estabelecimento";
            case CANCELADO:
                return "Este pedido foi cancelado";
            case PAGO:
                return "Seu pedido foi pago com sucesso";
            case EM_PREPARO:
                return "Estamos preparando seu pedido com carinho";
            case PRONTO:
                return "Seu pedido está pronto e aguardando entrega";
            case SAIU_PARA_ENTREGA:
                return "O entregador está a caminho do seu endereço";
            case ENTREGUE:
                return "Pedido entregue com sucesso. Bom apetite!";
            default:
                return "";
        }
    }

    private static String formatarNumeroPedido(UUID id) {
        if (id == null)
            return "#00000";
        String idStr = id.toString().replace("-", "");
        String ultimos5 = idStr.substring(Math.max(0, idStr.length() - 5)).toUpperCase();
        return "#" + ultimos5;
    }

}