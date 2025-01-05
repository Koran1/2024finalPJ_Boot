package com.ict.finalpj.common.vo;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class FileVO {
    private String fileIdx, fileTableType, fileTableIdx, fileOrder, isThumbnail, fileName;
    private List<MultipartFile> mpFiles;
    private List<FileData> fileData;

    @Data
    public static class FileData {
        private String fileOrder;
        private String isThumbnail;
        private String fileName;
    }

}
