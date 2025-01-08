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

import com.ict.finalpj.common.vo.DataVO;
import com.ict.finalpj.domain.book.service.BookService;
import com.ict.finalpj.domain.book.vo.BookVO;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// 토스 서버(API)에 저장하는 컨트롤러인데 next.js에서 처리해서 필요없음(미사용 컨트롤러)
@Slf4j
@RestController
@RequestMapping("/api/payments")
public class PaymentsController {

    @Autowired
    private BookService bookService;

    @Value("${toss.secretKey}")
    private String tossSecretKey;

    // 토스 API successUrl
    @PostMapping("/success")
    public ResponseEntity<String> handlePaymentSuccess(@RequestBody Map<String, Object> paymentData, @RequestParam String orderId) {
        // String orderId = (String) paymentData.get("orderId");
        if(orderId != (String) paymentData.get("orderId")) {
            System.out.println("주문 번호 검증 실패: ");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("주문 번호 검증 실패");
        }
        String paymentKey = (String) paymentData.get("paymentKey");
        String amount = (String) paymentData.get("amount");

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

            // DB에 예약 정보 저장
            orderId = orderId.trim().replace("\"", "");
            BookVO bvo = tempStorage.remove(orderId);
            bvo.setPaymentKey(paymentKey);

            log.info("success 시작");
            log.info("받은 orderId : " + orderId);
            log.info("tempStorage : " + tempStorage);
            log.info("campIdx : " + bvo.getCampIdx());
            log.info("userIdx : " + bvo.getUserIdx());
            log.info("getBookCheckInDate : " + bvo.getBookCheckInDate());
            log.info("getBookCheckOutDate : " + bvo.getBookCheckOutDate());
            log.info("getBookAdultCount : " + bvo.getBookAdultCount());
            log.info("getBookYouthCount : " + bvo.getBookYouthCount());
            log.info("getBookChildCount : " + bvo.getBookChildCount());
            log.info("getBookCarCount : " + bvo.getBookCarCount());
            log.info("getBookTotalPrice : " + bvo.getBookTotalPrice());
            log.info("getBookUserName : " + bvo.getBookUserName());
            log.info("getBookUserPhone : " + bvo.getBookUserPhone());
            log.info("getBookCar1 : " + bvo.getBookCar1());
            log.info("getBookCar2 : " + bvo.getBookCar2());
            log.info("getBookRequest : " + bvo.getBookRequest());
            log.info("getOrderId : " + bvo.getOrderId());
            log.info("getPaymentKey : " + bvo.getPaymentKey());
            log.info("success 끝");

            // 토스 결제 완료 시 DB에 저장
            int result = bookService.getBookWrite(bvo);
            if(result == 0){
                System.out.println("예약 실패");
                return ResponseEntity.ok("예약 실패");
            }else{
                System.out.println("예약 성공");
                return ResponseEntity.ok("예약 성공");
            }
        } else {
            System.out.println("결제 검증 실패: " + tossResponse.getBody());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("결제 검증 실패");
        }
    }
    

    // 임시 데이터 저장소 (Thread-safe) 서버 종료될 때까지 살아있음
    private ConcurrentHashMap<String, BookVO> tempStorage = new ConcurrentHashMap<>();

    @PostMapping("saveData")
    public DataVO temporarySaveData(@ModelAttribute BookVO bvo) {
        DataVO dataVO = new DataVO();

        try {
            tempStorage.put(bvo.getOrderId(), bvo); // 데이터 임시 저장
            log.info("saveData 시작");
            log.info("tempStorage : " + tempStorage);
            log.info("saveData 끝");

            dataVO.setSuccess(true);
            dataVO.setMessage("임시 저장 성공");
        } catch (Exception e) {
            dataVO.setSuccess(false);
            dataVO.setMessage("임시 저장 실패");
        }
        return dataVO;
    }

}