package com.ict.finalpj.domain.weather.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ict.finalpj.common.vo.DataVO;
import com.ict.finalpj.domain.weather.service.WthrService;
import com.ict.finalpj.domain.weather.vo.RegionVO;
import com.ict.finalpj.domain.weather.vo.WthrVO;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
@RequestMapping("/api/weather")
public class WthrController {
    @Autowired
    private WthrService wthrService;

    @GetMapping("/getWthrInfo/{region}")
    public DataVO requestMethodName(@PathVariable("region") String region) {
        DataVO dvo = new DataVO();
		List<WthrVO> list = wthrService.getWthrInfo(region);
		if(list == null){
			dvo.setSuccess(false);
			dvo.setMessage("Failed to retrieve weather data");
			return dvo;
		}
		dvo.setData(list);
		dvo.setSuccess(true);
		dvo.setMessage("Successfully got weather data");
        return dvo;
    }

    @GetMapping("/getWthrDatas")
	public DataVO getWthrDatas() {
		DataVO dvo = new DataVO();
		try {
			wthrService.deleteWthrInfo();
			for (int i = 1; i < 17; i++) {
				getWthrDataRegion(i);
				getLunAge(i);
				getSunMoon(i);
				log.info(i+"번째 지역 최신화 완료");
			}
			log.info("날씨 최신화 완료!");


			dvo.setSuccess(true);
			dvo.setMessage("Wthr 최신화 성공!");
			
		} catch (Exception e) {
			e.printStackTrace();
			dvo.setSuccess(false);
			dvo.setMessage("Wthr 정보 가져오기 실패");
			
		}
		return dvo;

	}


	// @GetMapping("/getSunMoon")
	public void getSunMoon(int regionNum) {
		String region = String.valueOf(regionNum);
		RegionVO wvo = wthrService.getRegInfo(region);
		String reg_repname = wvo.getReg_repname();

		int i = 0;

		while(i<11){
			// 오늘 날짜
			LocalDate date = LocalDate.now().plusDays(i);
			
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd");
			String days = date.format(dtf);
			
			try {
				
				StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/B090041/openapi/service/RiseSetInfoService/getAreaRiseSetInfo"); /*URL*/
				urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=Phsud7RN2nkw6wPmg2Fa7q%2BQZbDH%2Bnpp3W8%2F4SPFFTtGDYQFO89qKkyqQwLKFEV9cBRvLktzL3E0TueDq3%2B2bw%3D%3D"); /*Service Key*/
				urlBuilder.append("&" + URLEncoder.encode("locdate","UTF-8") + "=" + URLEncoder.encode(days, "UTF-8")); /*날짜*/
				urlBuilder.append("&" + URLEncoder.encode("location","UTF-8") + "=" + URLEncoder.encode(reg_repname, "UTF-8")); /*지역*/
				URL url = new URL(urlBuilder.toString());
				
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("GET");
				conn.setRequestProperty("Content-type", "application/json");
				BufferedReader rd;
				if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
					rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
				} else {
					rd = new BufferedReader(new InputStreamReader(conn.getErrorStream(), "UTF-8"));
				}
				StringBuilder sb = new StringBuilder();
				String line;
				while ((line = rd.readLine()) != null) {
					sb.append(line);
				}
				rd.close();
				conn.disconnect();
				String result = sb.toString();

				String moonrise = result.substring(result.indexOf("<moonrise>")+10, result.indexOf("</moonrise>") );
				String moonset = result.substring(result.indexOf("<moonset>")+9, result.indexOf("</moonset>") );
				String sunrise = result.substring(result.indexOf("<sunrise>")+9, result.indexOf("</sunrise>") );
				String sunset = result.substring(result.indexOf("<sunset>")+8, result.indexOf("</sunset>") );

				WthrVO wthrvo = new WthrVO();
				wthrvo.setWthrDate(date.toString());
				wthrvo.setWthrMoonrise(moonrise);
				wthrvo.setWthrMoonset(moonset);
				wthrvo.setWthrSunrise(sunrise);
				wthrvo.setWthrSunset(sunset);

				wthrService.insertMoonSunInfo(wthrvo);

			} catch (Exception e) {
				e.printStackTrace();
			}
			i++;
			}
		}
		
		// @GetMapping("/getLunAge")
		public void getLunAge(int regionNum) {
			String region = String.valueOf(regionNum);
			// RegionVO wvo = wthrService.getRegInfo(region);

			int i = 0;

			while(i < 11){
			// 오늘 날짜
			LocalDate date = LocalDate.now().plusDays(i);
			String year = String.valueOf(date.getYear());
			String month = (date.getMonthValue() <10 ? "0"+String.valueOf(date.getMonthValue()) : String.valueOf(date.getMonthValue()));
			String day = (date.getDayOfMonth() <10 ? "0"+String.valueOf(date.getDayOfMonth()) : String.valueOf(date.getDayOfMonth()));
		
			try {
				StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/B090041/openapi/service/LunPhInfoService/getLunPhInfo"); /*URL*/
				urlBuilder.append("?" + URLEncoder.encode("ServiceKey","UTF-8") + "=Phsud7RN2nkw6wPmg2Fa7q%2BQZbDH%2Bnpp3W8%2F4SPFFTtGDYQFO89qKkyqQwLKFEV9cBRvLktzL3E0TueDq3%2B2bw%3D%3D"); /*Service Key*/
				urlBuilder.append("&" + URLEncoder.encode("solYear","UTF-8") + "=" + URLEncoder.encode(year, "UTF-8")); /*연*/
				urlBuilder.append("&" + URLEncoder.encode("solMonth","UTF-8") + "=" + URLEncoder.encode(month, "UTF-8")); /*월*/
				urlBuilder.append("&" + URLEncoder.encode("solDay","UTF-8") + "=" + URLEncoder.encode(day, "UTF-8")); /*일*/
				URL url = new URL(urlBuilder.toString());
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("GET");
				conn.setRequestProperty("Content-type", "application/json");
				BufferedReader rd = null;
				if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
					rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
				} else {
					rd = new BufferedReader(new InputStreamReader(conn.getErrorStream(), "UTF-8"));
				}
				StringBuilder sb = new StringBuilder();
				String line;
				while ((line = rd.readLine()) != null) {
					sb.append(line);
				}
				rd.close();
				conn.disconnect();

				String result = sb.toString();
				String lunAge = result.substring(result.indexOf("<lunAge>")+8, result.indexOf("</lunAge>"));

				WthrVO wthrvo = new WthrVO();
				wthrvo.setWthrDate(date.toString());
				wthrvo.setWthrLunAge(lunAge);
				wthrService.insertLunAgeInfo(wthrvo);
				

			} catch (Exception e) {
				e.printStackTrace();
			}
			 i++;
			}
    }


	// region 별 날씨 정보 가져오기
	public void getWthrDataRegion(int regionNum) {
		String region = String.valueOf(regionNum);
		
		weatherShort(region);
		
		int tMinIdx = 0;
		int skyIdx = 0;
		int dateIdx = 0;
		int ptyIdx = 0;
		int popIdx = 0;
		int tMaxIdx = 0;
		String longs = weatherLong(region);

		LocalDate now = LocalDate.now();
		int i = 4;
		while (i < 11) {
			WthrVO pvo = new WthrVO();
			String wthrDate = now.plusDays(i).toString();

			tMinIdx = longs.indexOf(String.valueOf("<taMin" + i + ">"));
			tMaxIdx = longs.indexOf(String.valueOf("<taMax" + i + ">"));

			String wthrTMin = null;
			String wthrTMax = null;

			if (i == 10) {
				wthrTMin = longs.substring(tMinIdx + 9, longs.indexOf(String.valueOf("</taMin" + i + ">")));
				wthrTMax = longs.substring(tMaxIdx + 9, longs.indexOf(String.valueOf("</taMax" + i + ">")));
			} else {
				wthrTMin = longs.substring(tMinIdx + 8, longs.indexOf(String.valueOf("</taMin" + i + ">")));
				wthrTMax = longs.substring(tMaxIdx + 8, longs.indexOf(String.valueOf("</taMax" + i + ">")));
			}

			String wthrPOP = null;
			String wthrSKY_PTY = null;
			int skyptyIdx = 0;

			if (i < 8) {
				popIdx = longs.indexOf(String.valueOf("<rnSt" + i + "Pm>"));
				skyptyIdx = longs.indexOf(String.valueOf("<wf" + i + "Pm>"));

				wthrPOP = longs.substring(popIdx + 9, longs.indexOf(String.valueOf("</rnSt" + i + "Pm>")));
				wthrSKY_PTY = longs.substring(skyptyIdx + 7, longs.indexOf(String.valueOf("</wf" + i + "Pm>")));
			} else if (i < 10) {
				popIdx = longs.indexOf(String.valueOf("<rnSt" + i + ">"));
				skyptyIdx = longs.indexOf(String.valueOf("<wf" + i + ">"));

				wthrPOP = longs.substring(popIdx + 7, longs.indexOf(String.valueOf("</rnSt" + i + ">")));
				wthrSKY_PTY = longs.substring(skyptyIdx + 5, longs.indexOf(String.valueOf("</wf" + i + ">")));
			} else {
				popIdx = longs.indexOf(String.valueOf("<rnSt" + i + ">"));
				skyptyIdx = longs.indexOf(String.valueOf("<wf" + i + ">"));

				wthrPOP = longs.substring(popIdx + 8, longs.indexOf(String.valueOf("</rnSt" + i + ">")));
				wthrSKY_PTY = longs.substring(skyptyIdx + 6, longs.indexOf(String.valueOf("</wf" + i + ">")));
			}

			pvo.setWthrDate(wthrDate);
			pvo.setWthrTMin(wthrTMin);
			pvo.setWthrTMax(wthrTMax);
			pvo.setWthrSKY_PTY(wthrSKY_PTY);
			pvo.setWthrPOP(wthrPOP);
			pvo.setRegion(region);
			wthrService.insertWthrInfo(pvo);
			i++;
		}
	}

	// 단기예보 API
    public void weatherShort(String region) {
		RegionVO wvo = wthrService.getRegInfo(region);

		String nx = wvo.getNx();
		String ny = wvo.getNy();

		// 오늘 날짜
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date now = new Date();
		String today = sdf.format(now);

		BufferedReader rd = null;
		HttpURLConnection conn = null;
		StringBuilder sb = null;

		// 단기 예보
		try {
			StringBuilder urlBuilder = new StringBuilder(
					"http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst"); /* URL */
			urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8")
					+ "=Phsud7RN2nkw6wPmg2Fa7q%2BQZbDH%2Bnpp3W8%2F4SPFFTtGDYQFO89qKkyqQwLKFEV9cBRvLktzL3E0TueDq3%2B2bw%3D%3D");
			urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8"));
			urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "="+ URLEncoder.encode("1000", "UTF-8"));
			urlBuilder.append("&" + URLEncoder.encode("dataType", "UTF-8") + "="+ URLEncoder.encode("JSON", "UTF-8")); /* 요청자료형식(XML/JSON) */
			urlBuilder.append("&" + URLEncoder.encode("base_date", "UTF-8") + "=" + URLEncoder.encode(today, "UTF-8"));
			urlBuilder.append("&" + URLEncoder.encode("base_time", "UTF-8") + "="
					+ URLEncoder.encode("0200", "UTF-8")); /* 02시 발표 */
			urlBuilder.append(
					"&" + URLEncoder.encode("nx", "UTF-8") + "=" + URLEncoder.encode(nx, "UTF-8")); /* 예보지점의 X 좌표값 */
			urlBuilder.append(
					"&" + URLEncoder.encode("ny", "UTF-8") + "=" + URLEncoder.encode(ny, "UTF-8")); /* 예보지점의 Y 좌표값 */
			
			URL url = new URL(urlBuilder.toString());
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			System.out.println("Response code: " + conn.getResponseCode());

			if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
				rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			} else {
				rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
			}
			sb = new StringBuilder();

			String line;
			while ((line = rd.readLine()) != null) {
				sb.append(line);
			}

			String result = sb.toString();
			JsonObject jsonObj = JsonParser.parseString(result).getAsJsonObject();
			JsonObject body = jsonObj.getAsJsonObject("response")
						.getAsJsonObject("body");

			var items = body.getAsJsonObject("items").getAsJsonArray("item");

			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd");
			String date = LocalDate.now().plusDays(3).format(dtf);

			Map<String, Map<String, String>> shortWthr = new HashMap<>();
			
			for(JsonElement item : items){
				JsonObject itemObj = item.getAsJsonObject();
				String fcstDate = itemObj.get("fcstDate").getAsString();
				String category = itemObj.get("category").getAsString();
				String fcstTime = itemObj.get("fcstTime").getAsString();
				String fcstValue = itemObj.get("fcstValue").getAsString();

				if(category.equals("TMN") || category.equals("TMX")
				||category.equals("SKY") || category.equals("PTY") || category.equals("POP")){
					if(category.equals("SKY") || category.equals("PTY") || category.equals("POP")){
						if(!fcstTime.equals("1300")) continue;
					} 
	
					shortWthr.computeIfAbsent(fcstDate, k -> new HashMap<>()).put(category, fcstValue);
				} 
				if(fcstDate.equals(date)) break;
			}

			for (Map.Entry<String, Map<String, String>> entry : shortWthr.entrySet()) {
				String wthrDate = entry.getKey().substring(0, 4)+"-"+entry.getKey().substring(4, 6)+"-"+entry.getKey().substring(6);
				Map<String, String> categoryData = entry.getValue(); 

				String wthrSKY = categoryData.get("SKY");
				String wthrPTY = categoryData.get("PTY");
				
				String wthrSKY_PTY = "";
				switch (wthrSKY) {
					case "1":
						wthrSKY_PTY += "맑음";
						break;
					case "3":
						wthrSKY_PTY += "구름많음";
						break;
					case "4":
						wthrSKY_PTY += "흐림";
						break;
				}
				switch (wthrPTY) {
					case "1":
						wthrSKY_PTY += " (비)";
						break;
					case "2":
						wthrSKY_PTY += " (비/눈)";
						break;
					case "3":
						wthrSKY_PTY += " (눈)";
						break;
					case "4":
						wthrSKY_PTY += " (소나기)";
						break;
				}

				String wthrPOP = categoryData.get("POP");
				String wthrTMin = categoryData.get("TMN");
				String wthrTMax = categoryData.get("TMX");

				WthrVO wthrvo = new WthrVO();
				wthrvo.setWthrDate(wthrDate);
				wthrvo.setWthrTMin(wthrTMin);
				wthrvo.setWthrTMax(wthrTMax);
				wthrvo.setWthrSKY_PTY(wthrSKY_PTY);
				wthrvo.setWthrPOP(wthrPOP);
				wthrvo.setRegion(region);

				wthrService.insertWthrInfo(wthrvo);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				rd.close();
				conn.disconnect();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	// 중기예보 API - 기온정보
	public String weatherLong(String region) {
		RegionVO wvo = wthrService.getRegInfo(region);
		String regId = wvo.getReg_id_short();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date now = new Date();
		String today = sdf.format(now);

		BufferedReader rd = null;
		HttpURLConnection conn = null;
		StringBuilder sb = null;

		try {
			StringBuilder urlBuilder = new StringBuilder(
					"http://apis.data.go.kr/1360000/MidFcstInfoService/getMidTa");
			urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8")
					+ "=Phsud7RN2nkw6wPmg2Fa7q%2BQZbDH%2Bnpp3W8%2F4SPFFTtGDYQFO89qKkyqQwLKFEV9cBRvLktzL3E0TueDq3%2B2bw%3D%3D"); 
			urlBuilder.append(
					"&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /* 페이지번호 */
			urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "="
					+ URLEncoder.encode("10", "UTF-8")); /* 한 페이지 결과 수 */
			urlBuilder.append("&" + URLEncoder.encode("dataType", "UTF-8") + "="
					+ URLEncoder.encode("XML", "UTF-8")); /* 요청자료형식(XML/JSON)Default: XML */
			urlBuilder.append("&" + URLEncoder.encode("regId", "UTF-8") + "=" + URLEncoder.encode(regId, "UTF-8"));
			urlBuilder.append(
					"&" + URLEncoder.encode("tmFc", "UTF-8") + "=" + URLEncoder.encode(today + "0600", "UTF-8"));
			URL url = new URL(urlBuilder.toString());
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			System.out.println("Response code2: " + conn.getResponseCode());

			if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
				rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			} else {
				rd = new BufferedReader(new InputStreamReader(conn.getErrorStream(), "UTF-8"));
			}

			sb = new StringBuilder();
			String line;

			while ((line = rd.readLine()) != null) {
				if (line.equals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")) {
					sb.append(line);
					continue;
				}
				int start = line.indexOf("<item>");
				int end = line.lastIndexOf("</item>");
				sb.append(line.substring(start, end));
			}

			String result = weatherLong2(region, today);
			sb.append(result);

			return sb.toString();

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				rd.close();
				conn.disconnect();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	// 중기예보 API - 날씨정보
	public String weatherLong2(String region, String today) {
		BufferedReader rd = null;
		HttpURLConnection conn = null;
		StringBuilder sb = null;

		RegionVO wvo = wthrService.getRegInfo(region);
		String regIdLong = wvo.getReg_id_long();
		try {
			StringBuilder urlBuilder = new StringBuilder(
					"http://apis.data.go.kr/1360000/MidFcstInfoService/getMidLandFcst"); /* URL */
			urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8")
					+ "=Phsud7RN2nkw6wPmg2Fa7q%2BQZbDH%2Bnpp3W8%2F4SPFFTtGDYQFO89qKkyqQwLKFEV9cBRvLktzL3E0TueDq3%2B2bw%3D%3D");
			urlBuilder.append(
					"&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /* 페이지번호 */
			urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "="
					+ URLEncoder.encode("10", "UTF-8")); /* 한 페이지 결과 수 */
			urlBuilder.append("&" + URLEncoder.encode("dataType", "UTF-8") + "="
					+ URLEncoder.encode("XML", "UTF-8")); /* 요청자료형식(XML/JSON)Default: XML */
			urlBuilder.append("&" + URLEncoder.encode("regId", "UTF-8") + "="
					+ URLEncoder.encode(regIdLong, "UTF-8")); /* 11B0000 서울, 인천, 경기도 11D10000 등 (활용가이드 하단 참고자료 참조) */
			urlBuilder.append(
					"&" + URLEncoder.encode("tmFc", "UTF-8") + "=" + URLEncoder.encode(today + "0600", "UTF-8"));
			/*-일 2회(06:00,18:00)회 생성 되며 발표시각을 입력 YYYYMMDD0600(1800)-최근 24시간 자료만 제공*/URL url = new URL(
					urlBuilder.toString());
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			System.out.println("Response code3: " + conn.getResponseCode());

			if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
				rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			} else {
				rd = new BufferedReader(new InputStreamReader(conn.getErrorStream(), "UTF-8"));
			}

			sb = new StringBuilder();
			String line;

			while ((line = rd.readLine()) != null) {
				if (line.equals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")) {
					continue;
				}
				int start = line.indexOf("<item>") + 6;
				int end = line.lastIndexOf("</item>") + 7;
				sb.append(line.substring(start, end));
			}
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				rd.close();
				conn.disconnect();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
    
}
