package com.ict.finalpj.domain.camplog.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ict.finalpj.common.vo.DataVO;
import com.ict.finalpj.common.vo.FileVO;
import com.ict.finalpj.common.vo.FileVO.FileData;
import com.ict.finalpj.common.vo.ReportVO;
import com.ict.finalpj.domain.camp.vo.CampVO;
import com.ict.finalpj.domain.camplog.service.CampLogService;
import com.ict.finalpj.domain.camplog.vo.CampLogCommentVO;
import com.ict.finalpj.domain.camplog.vo.CampLogContentVO;
import com.ict.finalpj.domain.camplog.vo.CampLogContentVO.ContentData;
import com.ict.finalpj.domain.camplog.vo.CampLogVO;
import com.ict.finalpj.domain.camplog.vo.DetailDTO;
import com.ict.finalpj.domain.camplog.vo.DetailDTO.DetailTagData;
import com.ict.finalpj.domain.camplog.vo.TagInfoVO;
import com.ict.finalpj.domain.camplog.vo.TagInfoVO.TagData;
import com.ict.finalpj.domain.camplog.vo.WriteDTO;
import com.ict.finalpj.domain.deal.vo.DealVO;
import com.ict.finalpj.domain.user.vo.UserVO;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@RestController
@RequestMapping("/api/camplog")
public class CampLogController {
    @Autowired
    CampLogService campLogService;

    // 로그(후기) 내용
    public DataVO tagMethod(TagInfoVO tvo, WriteDTO dto) {
        DataVO dataVO = new DataVO();
        tvo.setLogIdx(dto.getLvo().getLogIdx());
        tvo.setUserIdx(dto.getUvo().getUserIdx());

        List<TagData> tagList = new ArrayList<>();
        for (TagData k : dto.getTvo().getTagData()) {
            TagData tag = new TagData();
            tag.setFieldIdx(k.getFieldIdx());
            tag.setTagContent(k.getTagContent());
            tag.setTagX(k.getTagX());
            tag.setTagY(k.getTagY());
            tag.setDealIdx(k.getDealIdx());
            tag.setTagId(k.getTagId());
            tagList.add(tag);
        }
        tvo.setTagData(tagList);
        int resultTVO = campLogService.insertToPjtaginfo(tvo);
        log.info("resultTVO: " + resultTVO);
        if (resultTVO > 0) {
            dataVO.setSuccess(true);
            dataVO.setMessage("작성이 완료되었습니다");
            return dataVO;
        } else {
            dataVO.setSuccess(true);
            dataVO.setMessage("작성이 완료되었습니다");
            return dataVO;
        }
    }

    @PostMapping("/write")
    public DataVO getWriteCampLog(@RequestPart(value = "WriteData") WriteDTO dto,
            @RequestPart(value = "mpFiles", required = false) MultipartFile[] mpFiles) {
        DataVO dataVO = new DataVO();
        try {
            // pjlog insert 하기
            CampLogVO logVO = new CampLogVO();
            logVO.setUserIdx(dto.getUvo().getUserIdx());
            logVO.setCampIdx(dto.getCvo().getCampIdx());
            logVO.setLogTitle(dto.getLvo().getLogTitle());
            int resultCVO = campLogService.insertToPjcamplog(logVO);
            if (resultCVO > 0) {
                // pjlogcontent insert 하기
                CampLogContentVO contentVO = new CampLogContentVO();
                contentVO.setLogIdx(dto.getLvo().getLogIdx());
                List<ContentData> contentList = new ArrayList<>();
                for (ContentData k : dto.getLcvo().getContentData()) {
                    ContentData content = new ContentData();
                    content.setLogContent(k.getLogContent());
                    content.setLogContentOrder(k.getLogContentOrder());
                    contentList.add(content);
                }
                contentVO.setContentData(contentList);
                int resultCTVO = campLogService.insertToPjlogcontent(contentVO);
                if (resultCTVO > 0) {
                    if (dto.getFvo() != null) {
                        // pjfile테이블 inser하기
                        MultipartFile[] files = mpFiles;
                        String[] fileNames = new String[files.length];
                        String path = "D:\\upload";
                        File dir = new File(path);
                        if (!dir.exists()) {
                            dir.mkdirs();
                        }
                        for (int i = 0; i < fileNames.length; i++) {
                            UUID uuid = UUID.randomUUID();
                            fileNames[i] = uuid.toString() + "_" + files[i].getOriginalFilename();
                            files[i].transferTo(new File(dir, fileNames[i]));
                        }

                        FileVO fvo = new FileVO();
                        fvo.setFileTableType("1");
                        fvo.setFileTableIdx(dto.getLvo().getLogIdx());
                        List<FileData> dataList = new ArrayList<>();
                        int count = 0;
                        for (FileData k : dto.getFvo().getFileData()) {
                            FileData data = new FileData();
                            data.setFileOrder(k.getFileOrder());
                            data.setIsThumbnail(k.getIsThumbnail());
                            data.setFileName(fileNames[count]);
                            count++;
                            dataList.add(data);
                        }
                        fvo.setFileData(dataList);
                        int resultFVO = campLogService.insertToPjfile(fvo);
                        if (resultFVO > 0) {
                            if (dto.getTvo() != null) {
                                TagInfoVO tvo = new TagInfoVO();
                                DataVO dataVO2 = tagMethod(tvo, dto);
                                return dataVO2;
                            } else {
                                dataVO.setSuccess(true);
                                dataVO.setMessage("작성이 완료되었습니다");
                                return dataVO;
                            }
                        }
                    } else {
                        dataVO.setSuccess(true);
                        dataVO.setMessage("작성이 완료되었습니다");
                        return dataVO;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        dataVO.setSuccess(false);
        dataVO.setMessage("작성 중 오류 발생");
        return dataVO;
    }

    @GetMapping("/linkmodal/{userIdx}")
    public DataVO openLinkModal(@PathVariable("userIdx") String userIdx) {
        DataVO dataVO = new DataVO();
        try {
            List<DealVO> result = campLogService.getDealListByuserIdx(userIdx);
            if (result == null) {
                dataVO.setSuccess(false);
                dataVO.setMessage("거래 중인 상품이 없습니다.");
                return dataVO;

            }
            List<String> dealIdxList = new ArrayList<>();
            Map<String, String> map = new HashMap<>();
            for (DealVO k : result) {
                dealIdxList.add(k.getDealIdx());
                map.put(k.getDealIdx(), null);
            }
            String[] fileNames = campLogService.getFileNamesByDealIdxes(dealIdxList);
            if (fileNames == null) {
                dataVO.setSuccess(false);
                dataVO.setMessage("데이터를 불러오는 중에 문제가 발생했습니다.");
                return dataVO;
            }
            for (int i = 0; i < dealIdxList.size(); i++) {
                String dealIdx = dealIdxList.get(i);
                map.put(dealIdx, fileNames[i]);
            }
            // 컬렉션으로 만들어서 사진, 상품명, 가격 이렇게 만들어야함
            List<Map<String, String>> linkModalResult = new ArrayList<>();
            for (DealVO k : result) {
                Map<String, String> dealInfo = new HashMap<>();
                dealInfo.put("dealTitle", k.getDealTitle());
                dealInfo.put("dealPrice", k.getDealPrice());
                dealInfo.put("dealIdx", k.getDealIdx());
                dealInfo.put("fileName", map.get(k.getDealIdx()));
                linkModalResult.add(dealInfo);
            }
            dataVO.setSuccess(true);
            dataVO.setData(linkModalResult);
            return dataVO;
        } catch (Exception e) {
            dataVO.setSuccess(false);
            dataVO.setMessage("서버 오류 발생");
            return dataVO;
        }
    }

    @GetMapping("/campmodal")
    public DataVO openCampModal(@RequestParam("userIdx") String userIdx, @RequestParam("campIdx") String campIdx) {
        DataVO dataVO = new DataVO();
        try {
            List<CampVO> result = campLogService.getCampListAll();
            log.info("result" + result);
            if (result != null) {
                List<Map<String, String>> campModalResult = new ArrayList<>();
                for (CampVO k : result) {
                    Map<String, String> campInfo = new HashMap<>();
                    campInfo.put("campIdx", k.getCampIdx());
                    campInfo.put("firstImageUrl", k.getFirstImageUrl());
                    campInfo.put("campImg2", k.getCampImg2());
                    campInfo.put("campImg3", k.getCampImg3());
                    campInfo.put("facltNm", k.getFacltNm());
                    campInfo.put("sigunguNm", k.getSigunguNm());
                    campInfo.put("doNm2", k.getDoNm2());
                    if (k.getCampIdx().equals(campIdx)) {
                        campInfo.put("isLinked", "true");
                    } else {
                        campInfo.put("isLinked", "false");
                    }
                    campModalResult.add(campInfo);
                }
                log.info("campModalResult" + campModalResult);
                dataVO.setSuccess(true);
                dataVO.setData(campModalResult);
            } else {
                dataVO.setSuccess(false);
                dataVO.setMessage("데이터를 불러오는 중에 문제가 발생했습니다.");
            }
            return dataVO;
        } catch (Exception e) {
            dataVO.setSuccess(false);
            dataVO.setMessage("서버 오류 발생");
            return dataVO;
        }
    }

    @GetMapping("/detail")
    public DataVO getLogDetail(@RequestParam("logIdx") String logIdx, @RequestParam("userIdx") String userIdx) {
        DataVO dataVO = new DataVO();
        try {
            Map<String, Object> map = new HashMap<>();
            CampLogVO logVO = campLogService.getLogDetailByLogIdx(logIdx);
            List<CampLogContentVO> contentVO = campLogService.getLogContentByLogIdx(logIdx);
            int isUserRemommend = campLogService.isUserRemommend(logIdx, userIdx);
            List<FileVO> fileVO = campLogService.getLogFileByLogIdx(logIdx);
            List<TagInfoVO> tagVO = campLogService.getLogTagByLogIdx(logIdx);
            List<DealVO> dealVO = campLogService.getDealList();
            // 신고 추가
            List<ReportVO> rvo = campLogService.getLogReportCount(logIdx);
            log.info("data.data.rvo.reportCount" + rvo);
            map.put("rvo", rvo);
          
            if (isUserRemommend > 0) {
                map.put("doRecommend", true);
            } else {
                map.put("doRecommend", false);
            }
            
            if (logVO != null) {
                map.put("logVO", logVO);
                UserVO userVO = campLogService.getUserDataByUserIdx(logVO.getUserIdx());
                if (userVO != null) {
                    map.put("userVO", userVO);
                    
                }
            }
            if (dealVO != null) {
                map.put("dealVO", dealVO);
            }
            if (tagVO != null) {
                List<String> tempt1 = new ArrayList<>();
                for (TagInfoVO k : tagVO) {
                    if (k.getDealIdx() != null) {
                        tempt1.add(k.getDealIdx());
                    }
                }
                Set<String> tempt2 = new HashSet<>(tempt1);
                List<String> dealIdxes = new ArrayList<>(tempt2);
                String[] fileNames = campLogService.getFileNamesBydealIdxes(dealIdxes);

                Map<String, String> fNameBydealIdx = new HashMap<>();

                for (int i = 0; i < fileNames.length; i++) {
                    fNameBydealIdx.put(dealIdxes.get(i), fileNames[i]);
                }
                map.put("fNameByDealIdx", fNameBydealIdx);
            }

            List<DetailDTO> totalData = new ArrayList<>(); // 상세페이지에 뿌리기 위한 가공된 데이터
            if (contentVO != null && fileVO != null) {

                int maxSize = Math.max(contentVO.size(), fileVO.size());

                for (int i = -1; i < maxSize; i++) {
                    DetailDTO data = new DetailDTO();
                    data.setOrder(i + 1);

                    if (i + 1 < contentVO.size()) {
                        data.setLogContent(contentVO.get(i + 1).getLogContent());
                    }

                    if (i != -1) {
                        if (i < fileVO.size()) {
                            data.setFileName(fileVO.get(i).getFileName());
                        }

                        if (tagVO != null) {
                            List<DetailTagData> fieldTagData = new ArrayList<>();
                            for (int j = 0; j < tagVO.size(); j++) {
                                if (tagVO.get(j).getFieldIdx().equals(String.valueOf(data.getOrder()))) {
                                    DetailTagData tagData = new DetailTagData();
                                    tagData.setDealIdx(tagVO.get(j).getDealIdx());
                                    tagData.setTagX(tagVO.get(j).getTagX());
                                    tagData.setTagY(tagVO.get(j).getTagY());
                                    tagData.setTagContent(tagVO.get(j).getTagContent());
                                    tagData.setTagId(tagVO.get(j).getTagId());
                                    fieldTagData.add(tagData);
                                }
                            }
                            data.setTagData(fieldTagData);
                        }
                    }
                    totalData.add(data);
                }
            } else if (contentVO != null && fileVO == null) { // 로그에 파일이 없는 경우
                for (int i = 0; i < contentVO.size(); i++) {
                    DetailDTO data = new DetailDTO();
                    data.setOrder(i);
                    data.setLogContent(contentVO.get(i).getLogContent());
                    totalData.add(data);
                }
            }
            map.put("pData", totalData);
            if (map.size() > 0) {
                dataVO.setSuccess(true);
                dataVO.setData(map);
                return dataVO;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        dataVO.setSuccess(false);
        dataVO.setMessage("서버 오류 발생");
        return dataVO;
    }
    @GetMapping("/toggleRecommend")
    public DataVO getToggleRecommend(@RequestParam("logIdx") String logIdx, @RequestParam("userIdx") String userIdx, @RequestParam("doRecommend")String doRecommend) {
        DataVO dataVO = new DataVO();
        try {
            Map<String, String> map = new HashMap<>();
            map.put("logIdx", logIdx);
            map.put("userIdx", userIdx);
            if (doRecommend.equals("1")) {
                int result = campLogService.toogleOff(map);
                if (result > 0 ) {
                    dataVO.setSuccess(true);
                    dataVO.setData("0");
                }
            }else {
                int result = campLogService.toogleOn(map);
                if (result > 0 ) {
                    dataVO.setSuccess(true);
                    dataVO.setData("1");
                }
            }
            return dataVO;
        } catch (Exception e) {
            e.printStackTrace();
        }
        dataVO.setSuccess(false);
        dataVO.setMessage("서버 오류 발생");
        return dataVO;
    }

    @PostMapping("/logDelete")
    public DataVO deleteLogByLogIdx(@RequestParam("logIdx")String logIdx) {
        DataVO dataVO = new DataVO();
        try {
             int result = campLogService.getLogActiveZero(logIdx);
             if (result > 0 ) {
                dataVO.setMessage("삭제가 완료되었습니다.");
                dataVO.setSuccess(true);
             }
             return dataVO;
        } catch (Exception e) {
            e.printStackTrace();
        }
        dataVO.setSuccess(false);
        dataVO.setMessage("작성 중 오류 발생");
        return dataVO;
    }

    // 로그(후기)글 신고
    @PostMapping("logReport")
    public DataVO getLogReport(@ModelAttribute ReportVO rvo) {
        DataVO dataVO = new DataVO();
        try {
            log.info("rvo : " + rvo);
            int result = campLogService.getLogReport(rvo);

            if(result > 0){
                dataVO.setSuccess(true);
                dataVO.setMessage("로그(후기)글 신고 완료");
            }
        } catch (Exception e) {
            dataVO.setSuccess(false);
            dataVO.setMessage("로그(후기)글 신고 오류");
            e.printStackTrace();
        }
        return dataVO;
    }







    // *댓글*
    // 댓글 리스트 불러오기
    @GetMapping("commentList")
    public DataVO getCommentList(@RequestParam("logIdx") String logIdx) {
        log.info("logIdx : " + logIdx);
        DataVO dataVO = new DataVO();
        try {
            // 댓글 리스트 불러오기
            List<CampLogCommentVO> lcvo = campLogService.getCommentList(logIdx);
            // 댓글 리스트에서 userIdx 만 추출
            List<String> userIdxList = lcvo.stream()
                                 .map(CampLogCommentVO::getUserIdx)  // CampLogCommentVO에서 userIdx만 추출
                                 .distinct()                        // 중복 제거
                                 .collect(Collectors.toList());
            // userIdxList를 통해 UserVO 리스트 불러오기
            List<UserVO> uvo = campLogService.getUserInfoByIdx(userIdxList);

            // userIdxList를 통해 
            List<ReportVO> rvo = campLogService.getCommentReportCount(userIdxList);
            log.info("lcvo : " + lcvo);

            // userIdx와 userNickname을 매핑
            Map<String, String> userNicknameMap = uvo.stream()
                .collect(Collectors.toMap(UserVO::getUserIdx, UserVO::getUserNickname));

            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("lcvo", lcvo);
            resultMap.put("userNicknameMap", userNicknameMap);
            resultMap.put("rvo", rvo);
            dataVO.setSuccess(true);
            dataVO.setMessage("댓글 리스트를 불러옵니다.");
            dataVO.setData(resultMap);
            return dataVO;
        } catch (Exception e) {
            dataVO.setSuccess(false);
            dataVO.setMessage("댓글 리스트 불러오기 오류 발생");
            e.printStackTrace();
        }
        return dataVO;
    }

    // 댓글 등록(작성)
    @PostMapping("commentWrite")
    public DataVO getCommentWrite(@ModelAttribute CampLogCommentVO lcvo) {
        DataVO dataVO = new DataVO();
        try {
            int result = campLogService.getCommentWrite(lcvo);
            if(result > 0){
                dataVO.setSuccess(true);

                if(lcvo.getCommentIdx() != null){
                    dataVO.setMessage("댓글 저장 완료");
                }else{
                    dataVO.setMessage("답글 저장 완료");
                }
            }
        } catch (Exception e) {
            dataVO.setSuccess(false);
            dataVO.setMessage("댓/답글 저장 오류");
            e.printStackTrace();
        }
        return dataVO;   
    }

    // 댓글 삭제
    @PostMapping("commentDelete")
    public DataVO getCommentDelete(@ModelAttribute CampLogCommentVO lcvo) {
        DataVO dataVO = new DataVO();
        try {
            String logCommentIdx = lcvo.getLogCommentIdx();
            int result = campLogService.getCommentDelete(logCommentIdx);

            if(result > 0){
                dataVO.setSuccess(true);

                if(lcvo.getCommentIdx() != null){
                    dataVO.setMessage("댓글 삭제 완료");
                }else{
                    dataVO.setMessage("답글 삭제 완료");
                }
            }
        } catch (Exception e) {
            dataVO.setSuccess(false);
            dataVO.setMessage("댓/답글 삭제 오류");
            e.printStackTrace();
        }
        return dataVO;
    }

    // 댓글 신고
    @PostMapping("commentReport")
    public DataVO getCommentReport(@ModelAttribute ReportVO rvo) {
        DataVO dataVO = new DataVO();
        try {
            log.info("rvo : " + rvo);
            int result = campLogService.getCommentReport(rvo);

            if(result > 0){
                dataVO.setSuccess(true);
                dataVO.setMessage("댓글 신고 완료");
            }
        } catch (Exception e) {
            dataVO.setSuccess(false);
            dataVO.setMessage("댓/답글 신고 오류");
            e.printStackTrace();
        }
        return dataVO;
    }
}
