package com.ict.finalpj.domain.deal.controller;

import java.io.File;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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

// @GetMapping("/detail/{dealIdx}")
// public DataVO getDealDetail(@PathVariable("dealIdx") String dealIdx) {
//   DataVO dataVO = new DataVO();
//   try {
//     DealVO dealVO = dealService.getDealDetail(dealIdx);
//     if (dealVO == null) {
//       dataVO.setSuccess(false);
//       dataVO.setMessage("캠핑마켓 상세페이지 null");
//       return dataVO;
//     }
//       dataVO.setSuccess(true);
//       dataVO.setMessage("캠핑마켓 상세페이지 조회 완료");
//       dataVO.setData(dealVO);
//   } catch (Exception e) {
//     dataVO.setSuccess(false);
//     dataVO.setMessage("캠핑마켓 상세페이지 조회 실패");
//   }
//   return dataVO;
// }

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
//     dataVO.setMessage("상품수정 중 오류 발생");
//   }
//   return dataVO;
// }

@PostMapping("/write")
public DataVO getDealWrite(
  @ModelAttribute("data") DealVO dealVO, 
  @RequestParam(value="file", required=false) MultipartFile[] files) {
  DataVO dataVO = new DataVO();
  try {
    log.info("dealVO : " + dealVO);
    if (dealVO.getDealTitle() == null || dealVO.getDealCategory() == null || 
        dealVO.getDealStatus() == null || dealVO.getDealDescription() == null ||
        dealVO.getDealPrice() == null || dealVO.getDealPackage() == null || 
        dealVO.getDealDirect() == null || dealVO.getDealCount() == null) {
      dataVO.setSuccess(false);
      dataVO.setMessage("모든 항목을 입력해주세요.");
      return dataVO;
    }

    if (files != null && files.length > 0) {
      for (int i = 0; i < files.length; i++) {
        MultipartFile file = files[i];
        FileVo fileVo = new FileVo();
        
        // 파일명 생성
        UUID uuid = UUID.randomUUID();
        String fileName = uuid.toString() + "_" + file.getOriginalFilename();
        
        // FileVo 설정
        fileVo.setFileTableType("2");  // 테이블 타입 설정
        fileVo.setFileTableIdx(dealVO.getDealIdx());  // 테이블 인덱스 설정
        fileVo.setFileName(fileName);
        fileVo.setFileOrder(String.valueOf(i));  // 파일 순서 설정
        fileVo.setFileActive("1");  // 활성화 상태
        
        // 파일 업로드 경로
        String path = "D:\\upload\\deal";
        File upLoadDir = new File(path);
        
        if (!upLoadDir.exists()) {
          upLoadDir.mkdirs();
        }

        // 파일 업로드
        files[i].transferTo(new File(upLoadDir, fileName));

        // DealVO에 파일 정보를 설정
        dealVO.setFileVO(fileVo);
      }
    }
    
    // DealVO와 파일 정보를 함께 저장
    int result = dealService.getDealWrite(dealVO);
    if (result == 0) {
      dataVO.setSuccess(false);
      dataVO.setMessage("상품등록 실패");
      return dataVO;
    }
    dataVO.setSuccess(true);
    dataVO.setMessage("상품등록 완료");
  } catch (Exception e) {
    log.error("상품등록 중 오류", e);
    dataVO.setSuccess(false);
    dataVO.setMessage("상품등록 중 오류 발생");
  }
  return dataVO;
}

// @GetMapping("/management")
// public List<DealVO> getDealManagementList() {
//   DataVO dataVO = new DataVO();
//   try {
//     List<DealVO> list = dealService.getDealManagementList();
//     dataVO.setSuccess(true);
//     dataVO.setMessage("캠핑마켓 메인페이지 조회 완료");
//     dataVO.setData(list);
//   } catch (Exception e) {
//     dataVO.setSuccess(false);
//     dataVO.setMessage("캠핑마켓 메인페이지 조회 실패");
//   }
//   return dataVO;
// }

// @PostMapping("/note")
// public DataVO getDealNoteWrite(@PathVariable("dealIdx") String dealIdx, @RequestBody DealVO dealVO, Authentication authentication) {
//   return dealService.getDealNoteWrite(dealIdx, dealVO, authentication);
// }

// @PostMapping("/report")
// public DataVO getDealReportWrite(@PathVariable("dealIdx") String dealIdx, @RequestBody DealVO dealVO, Authentication authentication) {
//   return dealService.getDealReportWrite(dealIdx, dealVO, authentication);
// }

// @PostMapping("/satis")
// public DataVO getDealSatisWrite(@PathVariable("dealIdx") String dealIdx, @RequestBody DealVO dealVO, Authentication authentication) {
//   return dealService.getDealSatisWrite(dealIdx, dealVO, authentication);
// }
}
