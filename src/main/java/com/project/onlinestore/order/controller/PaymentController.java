package com.project.onlinestore.order.controller;

import com.project.onlinestore.order.dto.request.PaymentRequestDto;
import com.project.onlinestore.order.dto.request.paymentVerifyRequestDto;
import com.project.onlinestore.order.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/pay")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/complete")
    public ResponseEntity<String> completePayment(@RequestBody PaymentRequestDto dto) {
        try{
            //paymentService.verifyPayment(dto.paymentId(), dto.orderId());
            String payment = paymentService.checkPayment(dto.paymentId());
            System.out.println("payment: " + payment);
            return ResponseEntity.ok("Payment completed successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/verify")
    public Mono<ResponseEntity<Map<String, String>>> verifyPayment(@RequestBody paymentVerifyRequestDto dto){
        System.out.println("controller 들어옴");
        System.out.println("verifyPayment: " + dto.paymentId());
        return paymentService.paymentVerification(dto)
                .map(message -> ResponseEntity.ok(Map.of("message", message)))
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", e.getMessage()))));
    }

}
