package com.ict.finalpj.common.vo;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileVO {
  private String fileIdx, fileTableType, fileTableIdx, fileName, fileUpdated, fileCreated, fileOrder, fileActive, isThumbnail;

  public MultipartFile getMultipartFile() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getMultipartFile'");
  }

  
  private List<MultipartFile> mpFiles;
  private List<FileData> fileData;

  @Data
  public static class FileData {
      private String fileOrder;
      private String isThumbnail;
      private String fileName;
  }
}
