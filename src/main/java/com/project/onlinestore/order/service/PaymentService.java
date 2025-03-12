package com.project.onlinestore.order.service;

import com.project.onlinestore.exception.ApplicationException;
import com.project.onlinestore.exception.ErrorCode;
import com.project.onlinestore.order.Entity.Order;
import com.project.onlinestore.order.Entity.Payment;
import com.project.onlinestore.order.dto.request.paymentVerifyRequestDto;
import com.project.onlinestore.order.repository.OrderRepository;
import com.project.onlinestore.order.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final WebClient webClient;
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    private final String PORTONE_API_URL = "https://api.portone.io";

    @Value("${portone.secret.api}")
    private String apiToken;

    /*public PaymentService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(PORTONE_API_URL).build();
    }*/

    public Map<String, Object> getPayment(String paymentId) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("https://api.portone.io/payments/{paymentId}")
                        .build(paymentId))
                .header(HttpHeaders.AUTHORIZATION, "PortOne " + apiToken)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
                })
                .block();
    }

    // 결제 단건 조회
    public String checkPayment(String paymentId) throws IOException, InterruptedException {
        /*return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("https://api.portone.io/payments/" + paymentId)
                        .build(paymentId))
                .header(HttpHeaders.AUTHORIZATION, "PortOne " + apiToken)
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> {
                    int paidAmount = (int) ((Map<String, Object>) response.get("response")).get("amount");
                    return paidAmount == amount;
                });*/

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.portone.io/payments/" + paymentId))
                .header(HttpHeaders.AUTHORIZATION, "PortOne " + apiToken)
                .method("GET", HttpRequest.BodyPublishers.ofString("{}"))
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        return response.body();
    }

    // 결제 검증
    public Mono<Boolean> verifyPayment(String paymentId, BigDecimal amount) {
        return
                webClient.get()
                        .uri("https://api.portone.io/payments/" + paymentId)
                        .header(HttpHeaders.AUTHORIZATION, "PortOne " + apiToken)
                        .retrieve()
                        .bodyToMono(Map.class)
                        .map(response -> {
                            JSONObject jsonObject = new JSONObject(response);
                            System.out.println("jsonObject: " + jsonObject);
                            BigDecimal paidAmount = jsonObject.getJSONObject("amount").getBigDecimal("total");
                            System.out.println("paidAmount = " + paidAmount);
                            System.out.println("amount = " + amount);
                            //BigDecimal paidAmount = (BigDecimal) ((Map<String, Object>) response.get("response")).get("amount");
                            return Objects.equals(paidAmount, amount);  // 결제 금액 검증
                        }
        );
    }

    @Transactional
    public Mono<String> paymentVerification(paymentVerifyRequestDto dto){
        System.out.println("service 들어옴");
        return verifyPayment(dto.paymentId(), dto.amount()).flatMap(isValid -> {
            if (!isValid) {
                return Mono.error(new RuntimeException("결제 검증 실패"));
            }
            System.out.println("1번 service");
            return Mono.fromCallable(() -> {
                // 결제 정보 저장
                Order order = orderRepository.findById(dto.orderId())
                        .orElseThrow(() -> new ApplicationException(ErrorCode.ORDER_NOT_FOUND, "주문을 찾을 수 없습니다."));

                Payment payment = Payment.builder()
                        .paymentId(dto.paymentId())
                        .amount(dto.amount())
                        .order(order)
                        .paymentStatus(true)
                        .build();
                paymentRepository.save(payment);
                System.out.println("2번 service");
                orderRepository.changeOrderStatus(dto.orderId());
                //orderRepository.save(order);

                return "결제 검증 완료";
            }).subscribeOn(Schedulers.boundedElastic());
        });
    }
}
