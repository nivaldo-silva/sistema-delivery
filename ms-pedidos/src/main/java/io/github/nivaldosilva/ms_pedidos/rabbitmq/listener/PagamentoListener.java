package io.github.nivaldosilva.ms_pedidos.rabbitmq.listener;

import io.github.nivaldosilva.ms_pedidos.dto.PagamentoResponse;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class PagamentoListener {

    public static final String QUEUE_NAME = "pagamento.concluido";

    @RabbitListener(queues = QUEUE_NAME)
    public void receberMensagem(PagamentoResponse pagamentoResponse) {
        try {
            log.info("═══════════════════════════════════════════════════════════");
            log.info("🔔 NOVA MENSAGEM CONSUMIDA DA FILA: {}", QUEUE_NAME);
            log.info("═══════════════════════════════════════════════════════════");

            log.info("📦 Dados do Pagamento:");
            log.info("   ├─ ID Pagamento: {}", pagamentoResponse.getIdPagamento());
            log.info("   ├─ ID Pedido: {}", pagamentoResponse.getIdPedido());
            log.info("   ├─ Titular: {}", pagamentoResponse.getNomeTitular());
            log.info("   ├─ Número Cartão: {}", pagamentoResponse.getNumeroMascarado());
            log.info("   ├─ Validade: {}", pagamentoResponse.getValidadeCartao());
            log.info("   ├─ Valor: R$ {}", pagamentoResponse.getValor());
            log.info("   ├─ Forma Pagamento: {}", pagamentoResponse.getFormaPagamento());
            log.info("   └─ Status: {}", pagamentoResponse.getStatusPagamento());

            log.info("✅ Mensagem processada com sucesso!");
            log.info("═══════════════════════════════════════════════════════════\n");

        } catch (Exception e) {
            log.error("═══════════════════════════════════════════════════════════");
            log.error("❌ ERRO ao processar pagamento da fila: {}", QUEUE_NAME);
            log.error("📦 Dados recebidos: {}", pagamentoResponse);
            log.error("🔥 Detalhes do erro:", e);
            log.error("═══════════════════════════════════════════════════════════\n");
            throw new RuntimeException("Falha ao processar pagamento", e);
        }
    }
}
