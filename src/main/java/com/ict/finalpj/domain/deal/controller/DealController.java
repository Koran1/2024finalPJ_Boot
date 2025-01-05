package com.ict.finalpj.domain.deal.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ict.finalpj.common.vo.DataVO;
import com.ict.finalpj.common.vo.FileVO;
import com.ict.finalpj.domain.deal.service.DealService;
import com.ict.finalpj.domain.deal.vo.DealVO;
// import com.ict.finalpj.domain.user.vo.UserVO;

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
  @RequestParam(value="file", required=false) MultipartFile multipartFile) {
  DataVO dataVO = new DataVO();
  try {
    if (dealVO.getDealTitle() == null || dealVO.getDealCategory() == null || 
        dealVO.getDealStatus() == null || dealVO.getDealDescription() == null ||
        dealVO.getDealPrice() == null || dealVO.getDealPackage() == null || 
        dealVO.getDealDirect() == null || dealVO.getDealCount() == null) {
      dataVO.setSuccess(false);
      dataVO.setMessage("모든 항목을 입력해주세요.");
      return dataVO;
    }

    dealVO.setDealSellerUserIdx("임시사용자IDX");
    dealVO.setDealSellerNick("임시닉네임");

    // DealVO에 파일 정보 추가
    FileVO fileVo = new FileVO();
    if (multipartFile != null && !multipartFile.isEmpty()) {
      String fileName = multipartFile.getOriginalFilename();
      UUID uuid = UUID.randomUUID();
      String file = uuid.toString() + "_" + fileName;
      fileVo.setFileName(file);
      String path = "D:\\upload\\deal";
      File upLoadDir = new File(path);
      if (!upLoadDir.exists()) {
        upLoadDir.mkdirs();
      }
      multipartFile.transferTo(new File(upLoadDir, file));
      fileVo.setFileName(file);
    }

     // DealVO를 데이터베이스에 저장
    int result = dealService.getDealWrite(dealVO);
    if (result == 0) {
      dataVO.setSuccess(false);
      dataVO.setMessage("상품등록 실패");
      return dataVO;
    }
    dataVO.setSuccess(true);
    dataVO.setMessage("상품등록 완료");
  } catch (Exception e) {
    dataVO.setSuccess(false);
    dataVO.setMessage("상품등록 중 오류 발생");
  }
  return dataVO;
}

@GetMapping("/download/{filename}")
public ResponseEntity<Resource> getDealDownload(@PathVariable("filename") String filename) {
  try {
    Path filePath = Paths.get("D:/upload/deal/").resolve(filename).normalize();
    Resource resource = new UrlResource(filePath.toUri());
    if (!resource.exists() || !resource.isReadable()) {
      throw new FileNotFoundException("File not found: " + filename);
    } 
    return ResponseEntity.ok()
      .contentType(MediaType.APPLICATION_OCTET_STREAM)
      .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
      .body(resource);
  } catch (Exception e) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
  }
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
