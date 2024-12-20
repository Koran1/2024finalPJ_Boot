package com.ict.finalpj.common.vo;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileVo {
  private String fileIdx, fileTableType, fileTableIdx, fileName, fileUpdated, fileCreated, fileOrder, fileActive;
  private MultipartFile multipartFile; 
  private String dealIdx;
}
