package com.ict.finalpj.domain.book.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import com.ict.finalpj.domain.book.service.BookService;
import com.ict.finalpj.domain.book.vo.BookVO;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

// 토스 서버(API)에 저장하는 컨트롤러인데 next.js에서 처리해서 필요없음(미사용 컨트롤러)
@Slf4j
@RestController
@RequestMapping("/api/payments")
public class PaymentsController {

    @Autowired
    private BookService bookService;

    @Value("${toss.secretKey}")
    private String tossSecretKey;

    @PostMapping("/success")
    public ResponseEntity<String> handlePaymentSuccess(@RequestBody Map<String, Object> paymentData) {
        String orderId = (String) paymentData.get("orderId");
        String paymentKey = (String) paymentData.get("paymentKey");
        String amount = (String) paymentData.get("amount");

        BookVO bvo = new BookVO();
        bvo.setBookTotalPrice(amount);

        // 결제 검증 API 호출
        String tossUrl = "https://api.tosspayments.com/v1/payments/confirm";

        // 요청 데이터 설정
        Map<String, Object> requestData = new HashMap<>();
        requestData.put("paymentKey", paymentKey);
        requestData.put("orderId", orderId);
        requestData.put("amount", amount);

        // HTTP 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBasicAuth(tossSecretKey, ""); // Basic 인증

        // HTTP 요청 생성
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestData, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> tossResponse = restTemplate.postForEntity(tossUrl, requestEntity, Map.class);

        if (tossResponse.getStatusCode() == HttpStatus.OK) {
            System.out.println("결제 검증 성공: " + tossResponse.getBody());

            // TODO: DB에 결제 성공 기록 저장
            int result = bookService.getBookWrite(bvo);
            return ResponseEntity.ok("결제 성공 처리 완료");
        } else {
            System.out.println("결제 검증 실패: " + tossResponse.getBody());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("결제 검증 실패");
        }
    }
    
}