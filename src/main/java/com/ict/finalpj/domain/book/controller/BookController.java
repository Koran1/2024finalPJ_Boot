package com.ict.finalpj.domain.book.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ict.finalpj.common.vo.DataVO;
import com.ict.finalpj.domain.book.service.BookService;
import com.ict.finalpj.domain.book.vo.BookVO;
import com.ict.finalpj.domain.camp.vo.CampVO;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Slf4j
@RestController
@RequestMapping("/api/book")
public class BookController {
    @Autowired
    private BookService bookService;

    // 캠핑장 정보 페이지에서 예약하기 버튼 클릭 시 예약 페이지로 이동
    @GetMapping("/goBookPage")
    public DataVO goBookPage(@RequestParam("campIdx") String campIdx) {
        log.info("campIdx" + campIdx);
        DataVO dataVO = new DataVO();
        try {
            CampVO cvo = bookService.goBookPage(campIdx);

            log.info(cvo.getFacltNm());
            log.info(cvo.getAddr1());
            log.info(cvo.getTel());
            log.info(cvo.getInduty());

            dataVO.setSuccess(true);
            dataVO.setMessage("예약 페이지로 이동합니다.");
            dataVO.setData(cvo);
            return dataVO;
        } catch (Exception e) {
            dataVO.setSuccess(false);
            dataVO.setMessage("예약 페이지로 이동 실패");
            e.printStackTrace();
        }
        return dataVO;
    }

    // 예약 상세 보기
    @GetMapping("/detail")
    public DataVO getBookDetail(@RequestParam("bookIdx") String bookIdx) {
        log.info(bookIdx);
        DataVO dataVO = new DataVO();
        try {
            BookVO bvo = bookService.getBookInfo(bookIdx);
            CampVO cvo = bookService.goBookPage(bvo.getCampIdx());
            log.info(bvo.getBookIdx());
            log.info(bvo.getBookUserName());
            log.info(bvo.getBookUserPhone());
            log.info(bvo.getBookRequest());

            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("bvo", bvo);
            resultMap.put("cvo", cvo);
            dataVO.setSuccess(true);
            dataVO.setMessage("예약 상세 페이지로 이동합니다.");
            dataVO.setData(resultMap);
            return dataVO;
        } catch (Exception e) {
            dataVO.setSuccess(false);
            dataVO.setMessage("예약 상세 페이지로 이동 오류 발생");
            e.printStackTrace();
        }
        return dataVO;
    }

    // 예약 취소
    @GetMapping("/cancel")
    public DataVO getBookCancel(@RequestParam("bookIdx") String bookIdx) {
        log.info(bookIdx);
        DataVO dataVO = new DataVO();
        try {
            int result = bookService.getBookCancel(bookIdx);

            if(result > 0){
                dataVO.setSuccess(true);
                dataVO.setMessage("예약을 취소 합니다.");
                return dataVO;
            }else{
                dataVO.setSuccess(false);
                dataVO.setMessage("예약 취소 실패");
                return dataVO;
            }
        } catch (Exception e) {
            dataVO.setSuccess(false);
            dataVO.setMessage("예약 취소 오류 발생");
            e.printStackTrace();
        }
        return dataVO;
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

    @PostMapping("write")
    public DataVO getBookWrite(@ModelAttribute BookVO bvo) {
        DataVO dataVO = new DataVO();
        try {
            // orderId = orderId.trim().replace("\"", "");
            // BookVO bvo = tempStorage.remove(orderId);

            // 로그인 여부 확인
            // if(authentication == null){
            //     dataVO.setSuccess(false);
            //     dataVO.setMessage("로그인이 필요합니다.");
            //     return dataVO;
            // }

            // 로그인한 사람의 id 추출
            // log.info(authentication.getName());
            // bvo.setUserIdx(authentication.getName());

            log.info("write2 시작");
            // log.info("받은 orderId : " + orderId);
            log.info("tempStorage : " + tempStorage);
            log.info("campIdx : " + bvo.getCampIdx());
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
            log.info("write2 끝");

            // 토스 결제 완료 시 DB에 저장
            if(bvo.getBookTotalPrice() == null){
                log.info("is null");
                dataVO.setSuccess(false);
                dataVO.setMessage("결제가 완료되지 않았습니다.");
                return dataVO;
            }

            dataVO.setMessage("결제가 완료되었습니다.");
            // DB에 저장
            int result = bookService.getBookWrite(bvo);
            log.info("result" + result);
            if(result == 0){
                dataVO.setSuccess(false);
                dataVO.setMessage("예약 실패");
                return dataVO;
            }else{
                dataVO.setSuccess(true);
                dataVO.setMessage("예약 성공");
                return dataVO;
            }
        } catch (Exception e) {
            dataVO.setSuccess(false);
            dataVO.setMessage("예약 오류");
            e.printStackTrace();
        }
        return dataVO;
    }
}