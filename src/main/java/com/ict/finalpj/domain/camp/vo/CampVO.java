package com.ict.finalpj.domain.camp.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CampVO {
    private String 
    campIdx, // 캠핑장 고유번호
    contentId, // 콘텐츠 ID
    facltNm, // 야영장명
    lineIntro, // 한 줄 소개
    intro, // 소개
    insrncAt, // 영업배상책임보험 가입여부
    trsagntNo, // 관광사업자번호
    bizrno, // 사업자번호
    facltDivNm, // 사업주제.구분
    manageSttus, // 운영상태.관리상태
    hvofBgnde, // 휴장기간.휴무기간 시작일 
    hvofEnddle, // 휴장기간.휴무기간 종료일
    featureNm, // 특징
    induty, // 업종
    lctCl, // 입지구분
    doNm, // 도
    regionCode, // 지역코드
    sigunguNm, // 시군구구
    addr1, // 주소
    mapY, // 위도
    mapX, // 경도도
    tel, // 전화
    homepage, // 홈페이지
    resveUrl, // 예약 페이지
    resveCl, // 예약 구분
    gnrlSiteCo, // 주요시설 일반야영장
    autoSiteCo, // 주요시설 자동차야영장
    glampSiteCo, // 주요시설 글램핑
    caravSiteCo, // 주요시설 카라반
    indvdlCaravSiteCo, // 주요시설 개인 카라반
    sitedStnc, // 사이트 간 거리
    siteMg1Width, // 사이트 크기1 가로
    siteMg2Width, // 사이트 크기2 가로
    siteMg3Width, // 사이트 크기3 가로
    siteMg1Vrticl, // 사이트 크기1 세로
    siteMg2Vrticl, // 사이트 크기2 세로
    siteMg3Vrticl, // 사이트 크기3 세로로
    siteMg1Co, // 사이트 크기1 수량
    siteMg2Co, // 사이트 크기2 수량
    siteMg3Co, // 사이트 크기3 수량
    siteBottomCl1, // 잔디
    siteBottomCl2, // 파쇄석
    siteBottomCl3, // 테크
    siteBottomCl4, // 자갈
    siteBottomCl5, // 맨흙
    glampInnerFclty, // 글램핑-내부시설
    caravInnerFclty, // 카라반-내부시설
    prmisnDe, // 인허가일자
    operPdCl, // 운영기간
    operDeCl, // 운영일
    trlerAcmpnyAt, // 개인 트레일러 동반 여부
    caravAcmpnyAt, // 개인 카라반 동반 여부
    toiletCo, // 화장실 개수
    swrmCo, // 수영장 개수
    wtrplCo, // 개수대 개수
    brazierCl, // 화로대
    sbrsCl, // 부대시설
    sbrsEtc, // 부대시설 기타
    posblFcltyCl, // 주변이용가능시설
    posblFcltyEtc, // 주변이용가능시설 기타
    extshrCo, // 소화기 개수
    frprvtWrppCo, // 방화수 개수
    frprvtSandCo, // 방화사 개수
    fireSensorCo, // 화재감지기 개수
    themaEnvrnCl, // 테마환경
    eqpmnLendCl, // 캠핑장비대여
    animalCmgCl, // 애완동물출입
    tourEraCl, // 여행시기
    firstImageUrl, // 대표이미지
    campImg2, // 이미지1
    campImg3, // 이미지2
    campImg4, // 이미지3
    campImg5, // 이미지4
    campImg6, // 이미지5
    createdtime, // 등록일
    modifiedtime, // 수정일
    campView, // 캠핑장 조회순
    campActive // 활성화 여부
    ;
}
