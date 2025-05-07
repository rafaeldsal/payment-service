package com.rafaeldsal.paymentservice.service.impl;

import com.rafaeldsal.paymentservice.dto.PaymentDto;
import com.rafaeldsal.paymentservice.exception.BadRequestException;
import com.rafaeldsal.paymentservice.exception.NotFoundException;
import com.rafaeldsal.paymentservice.mapper.PaymentMapper;
import com.rafaeldsal.paymentservice.model.Payment;
import com.rafaeldsal.paymentservice.repository.PaymentRepository;
import com.rafaeldsal.paymentservice.service.PaymentIntentPublisher;
import com.rafaeldsal.paymentservice.service.PaymentService;
import com.rafaeldsal.paymentservice.service.PaymentStatusNotifier;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.PaymentIntent;
import com.stripe.model.StripeObject;
import com.stripe.net.Webhook;
import com.stripe.param.PaymentIntentCreateParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

  @Value("${webservices.minhaprata.stripe.webhook.secret}")
  private String webhookSecret;

  private final PaymentRepository paymentRepository;
  private final PaymentIntentPublisher paymentIntentPublisher;
  private final PaymentStatusNotifier paymentStatusNotifier;

  @Override
  public void process(PaymentDto dto) {
    if (paymentRepository.findByOrderIdAndTransactionId(dto.orderId(), dto.transactionId()).isPresent()) {
      throw new BadRequestException("Pagamento já processado");
    }

    if (!List.of("brl", "usd").contains(dto.currency().toLowerCase())) {
      throw new BadRequestException("Moeda não suportada");
    }

    String description = "Pagamento solicitado por cartão para o pedido " + dto.orderId();

    try {
      var paymentIntent = createCardPaymentIntent(dto.amount(), dto.currency(), description, dto.transactionId(), dto.orderId());
      var payment = PaymentMapper.toEntity(dto, paymentIntent);

      paymentRepository.save(payment);

      paymentIntentPublisher.sendMessage(PaymentMapper.toResponseDto(payment));

    } catch (StripeException e) {
      throw new BadRequestException("Erro ao processar pagamento: " + e.getMessage());
    }
  }

  @Override
  public void processWebhookEvent(String payload, String sigHeader) {
    Event event = verifyEvent(payload, sigHeader);

    if (event != null) {
      log.info("Processando evento Webhook");
      handlePaymentIntentStatus(event);
    } else {
      log.error("Evento nulo");
    }
  }

  private Event verifyEvent(String payload, String sigHeader) {
    try {
      return Webhook.constructEvent(payload, sigHeader, webhookSecret);
    } catch (SignatureVerificationException e) {
      log.error("Falha na verificação da assinatura do webhook", e);
      throw new SecurityException("Assinatura inválida");
    } catch (Exception e) {
      log.error("Erro ao processar evento do webhook", e);
      throw new RuntimeException("Falha ao processar webhook");
    }
  }

  private void handlePaymentIntentStatus(Event event) {
    EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
    StripeObject stripeObject = null;

    if (dataObjectDeserializer.getObject().isPresent()) {
      stripeObject = dataObjectDeserializer.getObject().get();
    } else {
      log.error("Objeto do evento não pôde ser desserializado. Tipo do evento: {}", event.getType());
      return;
    }

    if (!(stripeObject instanceof PaymentIntent)) {
      log.error("Objeto do evento não é um PaymentIntent. Tipo recebido: {}",
          dataObjectDeserializer.getObject().get().getClass());
      return;
    }

    PaymentIntent paymentIntent = (PaymentIntent) stripeObject;

    switch (event.getType()) {
      case "payment_intent.succeeded",
           "payment_intent.payment_failed",
           "payment_intent.processing",
           "payment_intent.canceled":
        handleStatusPayment(paymentIntent, event.getType());
        break;
      default:
        log.info("Evento '{}' não tratado", event.getType());
    }
  }

  private void handleStatusPayment(PaymentIntent paymentIntent, String eventType) {
    var payment = paymentRepository.findByPaymentIntentId(paymentIntent.getId())
        .orElseThrow(() -> new NotFoundException("Intenção de pagamento não encontrada"));

    if (payment.getStatus().equalsIgnoreCase(paymentIntent.getStatus())) {
      log.info("Pagamento já está marcado como '{}'. Ignorando", payment.getStatus().toUpperCase());
      paymentStatusNotifier.sendMessage(PaymentMapper.toWebhookResponse(paymentIntent, eventType));
    } else {
      payment.setStatus(paymentIntent.getStatus());
      paymentRepository.save(payment);
      paymentStatusNotifier.sendMessage(PaymentMapper.toWebhookResponse(paymentIntent, eventType));
    }
  }

  private PaymentIntent createCardPaymentIntent(Long amount, String currency, String description, String transactionId, String orderId) throws StripeException {
    PaymentIntentCreateParams.Builder paramsBuilder = PaymentIntentCreateParams.builder()
        .setAmount(amount)
        .setCurrency(currency.toLowerCase())
        .setConfirm(false) // Confirmação será feita no front
        .setCaptureMethod(PaymentIntentCreateParams.CaptureMethod.AUTOMATIC)
        .putMetadata("transactionId", transactionId)
        .putMetadata("orderId", orderId)
        .setAutomaticPaymentMethods(
            PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                .setEnabled(true)
                .setAllowRedirects(PaymentIntentCreateParams.AutomaticPaymentMethods.AllowRedirects.NEVER)
                .build())
        .setStatementDescriptorSuffix("MINHAPRATA")
        .setDescription(description);

    return PaymentIntent.create(paramsBuilder.build());
  }
}
