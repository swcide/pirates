package com.pirates.utill;

import com.pirates.web.domain.holiday.Holiday;
import com.pirates.web.domain.holiday.HolidayRepository;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Component
public class Scheduler {

    private final HolidayApi holidayApi;
    private final HolidayRepository holidayRepository;

    // 매월 1일
    @Scheduled(cron = "1 0 0 1 * *")
    @Transactional
    public void holidayCheck() throws IOException, ParseException {


        // 오픈 api로 공휴일 정보.
        List<Holiday> holidayList = holidayApi.holiday();
        // 현재 month의 주말.
        List<Holiday> weekendList = holidayApi.weekend();

        holidayList.addAll(weekendList);

        List<Holiday> deduplication = DeduplicationUtils.deduplication(holidayList, Holiday::getDate);

        holidayRepository.saveAll(deduplication);


    }

    // 차후 데이터베이스 마이그레이션 이후 삭제.
    @PostConstruct
    public void installHolidayData() throws IOException, ParseException {

        // 오픈 api로 공휴일 정보.
        List<Holiday> holidayList = holidayApi.holiday();
        // 현재 month의 주말.
        List<Holiday> weekendList = holidayApi.weekend();

        holidayList.addAll(weekendList);

        List<Holiday> deduplication = DeduplicationUtils.deduplication(holidayList, Holiday::getDate);

        holidayRepository.saveAll(deduplication);


    }
}
