package com.pirates.utill;


import com.pirates.web.domain.holiday.Holiday;
import com.pirates.web.domain.holiday.HolidayRepository;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.context.annotation.Configuration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Configuration
@RequiredArgsConstructor
public class HolidayApi {

    private final HolidayRepository holidayRepository;

    public List<Holiday> holiday() throws IOException, ParseException {
        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/B090041/openapi/service/SpcdeInfoService/getRestDeInfo"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=fx8sqX%2FMzIHUPKP5UJrSaAryxiGMzcRVIr%2BlFDVwQUFggqQ8ZWwgbBl7IAucoZ1%2FswoL%2FzUflcrl00xThDlHaQ%3D%3D"); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("solYear","UTF-8") + "=" + URLEncoder.encode("2021", "UTF-8")); /*연*/
        urlBuilder.append("&" + URLEncoder.encode("solMonth","UTF-8") + "=" + URLEncoder.encode("10", "UTF-8")); /*월*/
        urlBuilder.append("&" + URLEncoder.encode("_type","UTF-8")+ "=" + URLEncoder.encode("json"));

        URL url = new URL(urlBuilder.toString());
        System.out.println("url = " + url);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        System.out.println("Response code: " + conn.getResponseCode());
        BufferedReader rd;
        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();

        JSONParser parser = new JSONParser();
        JSONObject obj = (JSONObject) parser.parse(sb.toString());
        JSONObject parse_response = (JSONObject) obj.get("response");
        JSONObject parse_body = (JSONObject) parse_response.get("body");
        JSONObject parse_items = (JSONObject) parse_body.get("items");
        JSONArray parse_item = (JSONArray) parse_items.get("item");

        JSONObject item;
        List<Holiday> holidayResult = new ArrayList<>();
//         jSonDate;
        for (int i = 0; i < parse_item.size() ; i++) {
            item=  (JSONObject)parse_item.get(i);
            Long jSonDate = (Long) item.get("locdate");
            String dateName =(String) item.get("dateName");
            LocalDate date = LocalDate.parse(String.valueOf(jSonDate), DateTimeFormatter.ofPattern("yyyyMMdd"));

            Holiday holiday = Holiday.builder()
                .dateName(dateName)
                .date(date)
                .type("공휴일")
                .build();
            holidayResult.add(holiday);
        }

        return holidayResult;
    }

    public List<Holiday> weekend(){

        LocalDateTime now = LocalDateTime.now();
        //년
        int year = now.getYear();
        //월
        YearMonth month = YearMonth.from(now);
        // 월의 총 일수
        int length = month.lengthOfMonth();

        List<Holiday> weekendResult = new ArrayList<>();
        for (int i = 1; i < length+1 ; i++) {
            LocalDate date = LocalDate.of(year, Month.from(month), i);
            String dateName = date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.KOREAN);

            if(dateName.equals("토요일") || dateName.equals("일요일")) {
                Holiday holiday = Holiday.builder()
                        .dateName(dateName)
                        .date(date)
                        .type("주말")
                        .build();
                weekendResult.add(holiday);
            }
        }
        return weekendResult;
    }
}