package com.pirates.utill;

import com.pirates.web.domain.holiday.Holiday;
import com.pirates.web.domain.holiday.HolidayRepository;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@SpringBootTest
@Transactional
class SchedulerTest {

    @Autowired
    HolidayApi holidayApi;
    @Autowired
    HolidayRepository holidayRepository;

    @Test
    @DisplayName("공휴일, 주말 저장")
    public void holidayCheck() throws IOException, ParseException {

        //10월 공휴일 4개
        //10월 주말 10개
        //중복 2개
        /*
        2021-10-03	개천절
        2021-10-03	일요일
        2021-10-09	한글날
        2021-10-09	토요일
         */

        //given
        List<Holiday> holidayList = holidayApi.holiday();
        List<Holiday> weekendList = holidayApi.weekend();
        holidayList.addAll(weekendList);

        //when
        Holiday holiday = holidayRepository.getById(1L);
        List<Holiday> deduplication = DeduplicationUtils.deduplication(holidayList, Holiday::getDate);

        holidayRepository.saveAll(deduplication);
        //then

        Assertions.assertEquals(holidayList.size(),14);
        Assertions.assertEquals(deduplication.size(),12);
        Assertions.assertEquals(holiday.getId(),1L);

    }
}