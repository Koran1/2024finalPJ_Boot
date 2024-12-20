package com.ict.finalpj.domain.deal.controller;

import java.io.File;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ict.finalpj.common.vo.DataVO;
import com.ict.finalpj.common.vo.FileVo;
import com.ict.finalpj.domain.deal.service.DealService;
import com.ict.finalpj.domain.deal.vo.DealVO;

import lombok.extern.slf4j.Slf4j;
// import com.ict.finalpj.domain.user.vo.UserVO;

@Slf4j
@RestController
@RequestMapping("/api/deal")
public class DealController {

  @Autowired
  private DealService dealService;

  // @Autowired
  // private UserService userService;

  // @Autowired
  // private PasswordEncoder passwordEncoder;

@GetMapping("/dealMain")
public DataVO getDealMainList() {
  DataVO dataVO = new DataVO();
  try {
    List<DealVO> list = dealService.getDealMainList();
    dataVO.setSuccess(true);
    dataVO.setMessage("캠핑마켓 메인페이지 조회 완료");
    dataVO.setData(list);
  } catch (Exception e) {
    dataVO.setSuccess(false);
    dataVO.setMessage("캠핑마켓 메인페이지 조회 실패");
  }
  return dataVO;
}

@GetMapping("/detail/{dealIdx}")
public DataVO getDealDetail(@PathVariable("dealIdx") String dealIdx) {
    DataVO dataVO = new DataVO();
    try {
        log.info("상세 페이지 조회 요청 - dealIdx: {}", dealIdx);
        
        // dealIdx 유효성 검사
        if (dealIdx == null || dealIdx.trim().isEmpty()) {
            log.warn("유효하지 않은 dealIdx");
            dataVO.setSuccess(false);
            dataVO.setMessage("유효하지 않은 상품 ID입니다.");
            return dataVO;
        }
        
        DealVO dealVO = dealService.getDealDetail(dealIdx);
        log.info("조회된 상품 정보: {}", dealVO);
        
        if (dealVO == null) {
            log.warn("상품 정보 없음 - dealIdx: {}", dealIdx);
            dataVO.setSuccess(false);
            dataVO.setMessage("존재하지 않는 상품입니다.");
            return dataVO;
        }
        
        dataVO.setSuccess(true);
        dataVO.setMessage("상품 정보 조회 성공");
        dataVO.setData(dealVO);
        
    } catch (Exception e) {
        log.error("상품 상세 조회 중 오류 발생 - dealIdx: {}", dealIdx, e);
        dataVO.setSuccess(false);
        dataVO.setMessage("상품 정보를 불러오는 중 오류가 발생했습니다.");
    }
    return dataVO;
}

// @PutMapping("/update/{dealIdx}")
// public DataVO getDealUpdate(@PathVariable("dealIdx") String dealIdx, @RequestBody DealVO dealVO, Authentication authentication) {
//   DataVO dataVO = new DataVO();
//   try {
//     if (authentication == null) {
//       dataVO.setSuccess(false);
//       dataVO.setMessage("로그인이 필요합니다.");
//       return dataVO;
//     }
//     dealVO.setDealIdx(dealIdx);
//     if (dealVO.getDealTitle() == null || dealVO.getDealCategory() == null || dealVO.getDealStatus() == null || dealVO.getDealDescription() == null
//     || dealVO.getDealPrice() == null || dealVO.getDealPackage() == null || dealVO.getDealDirect() == null || dealVO.getDealCount() == null) {
//       dataVO.setSuccess(false);
//       dataVO.setMessage("모든 항목을 입력해주세요.");
//       return dataVO;
//     }

//     int result = dealService.getDealUpdate(dealVO);
//     if (result == 0) {
//       dataVO.setSuccess(false);
//       dataVO.setMessage("상품수정 실패");
//       return dataVO;
//     }
//     dataVO.setSuccess(true);
//     dataVO.setMessage("상품수정 완료");
//   } catch (Exception e) {
//     dataVO.setSuccess(false);
//     dataVO.setMessage("상품수정 중 오�� 발생");
//   }
//   return dataVO;
// }

@PostMapping("/write")
public DataVO getDealWrite(
  @ModelAttribute("data") DealVO dealVO, 
  @RequestParam(value="file", required=false) MultipartFile[] files) {
  
  // UUID 생성
  String dealIdx = UUID.randomUUID().toString();
  dealVO.setDealIdx(dealIdx); // DealVO에 dealIdx 설정
  log.info("Generated dealIdx: " + dealIdx); // 로그 추가

  DataVO dataVO = dealService.getDealWrite(dealVO);
  
  // dealIdx가 서비스에서 설정된 후 로그 추가
  log.info("After service call, dealIdx: " + dealVO.getDealIdx()); // 로그 추가

  // 파일 업로드 및 DB 저장 처리
  if (files != null && files.length > 0) {
    log.info("첨부된 파일 개수: " + files.length);
    
    for (int i = 0; i < files.length; i++) {
      MultipartFile file = files[i];
      if (!file.isEmpty()) {
        FileVo fileVo = new FileVo();
        
        // 파일명 생성
        UUID uuid = UUID.randomUUID();
        String fileName = uuid.toString() + "_" + file.getOriginalFilename();
        log.info("생성된 파일명: " + fileName);
        
        // FileVo 설정
        fileVo.setFileTableType("2");
        fileVo.setFileTableIdx(dealIdx); // dealIdx를 fileTableIdx에 설정
        fileVo.setFileName(fileName);
        fileVo.setFileOrder(String.valueOf(i));
        
        // 파일 업로드 경로
        String path = "D:\\upload\\deal";
        // 업로드할 디렉토리 객체 생성
        File upLoadDir = new File(path);
        
        if (!upLoadDir.exists()) {
          upLoadDir.mkdirs();
        }

        // 파일 업로드
        try {
            file.transferTo(new File(upLoadDir, fileName));
        } catch (Exception e) {
            log.error("파일 업로드 중 오류 발생: " + e.getMessage());
        }
        log.info("파일 업로드 완료: " + fileName);
        
        // DB에 파일 정보 저장
        dealService.insertFileInfo(fileVo);
        log.info("파일 정보 DB 저장 완료 - 순서: " + i);
      }
    }
  } else {
    log.info("첨부된 파일 없음");
  }
  
  return dataVO;
}

}