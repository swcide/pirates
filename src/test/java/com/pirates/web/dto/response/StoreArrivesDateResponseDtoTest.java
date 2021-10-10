package com.pirates.web.dto.response;

import com.pirates.web.domain.delivery.Delivery;
import com.pirates.web.domain.holiday.Holiday;
import com.pirates.web.domain.holiday.HolidayQueryRepository;
import com.pirates.web.domain.option.Options;
import com.pirates.web.domain.store.Store;
import com.pirates.web.domain.store.StoreRepository;
import com.pirates.web.dto.request.StoreRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
class StoreArrivesDateResponseDtoTest {

    @Autowired
    HolidayQueryRepository holidayQueryRepository;

    @Autowired
    StoreRepository storeRepository;

    @BeforeEach
    public void initData() {
        Delivery delivery1 = Delivery.builder()
                .type("fast")
                .closing(LocalTime.parse("12:00"))
                .build();
        Delivery delivery2 = Delivery.builder()
                .type("regular")
                .closing(LocalTime.parse("18:00"))
                .build();

        Options options1 = Options.builder()
                .name("생연어 몸통살 300g")
                .price(10000L)
                .stock(99L)
                .build();
        Options options2 = Options.builder()
                .name("생연어 몸통살 500g")
                .price(17000L)
                .stock(99L)
                .build();
        Options options3 = Options.builder()
                .name("대 7~8미")
                .price(50000L)
                .stock(99L)
                .build();
        Options options4 = Options.builder()
                .name("중 14~15미")
                .price(34000L)
                .stock(99L)
                .build();
        Options options5 = Options.builder()
                .name("소 50~60미")
                .price(20000L)
                .stock(99L)
                .build();

        List<Options> optionsList1 = new ArrayList<>();
        List<Options> optionsList2 = new ArrayList<>();

        optionsList1.add(options1);
        optionsList1.add(options2);
        optionsList2.add(options3);
        optionsList2.add(options4);
        optionsList2.add(options5);

        StoreRequestDto storeDto1 = StoreRequestDto.builder()
                .name("노르웨이산 연어")
                .description("노르웨이산 연어 300g, 500g, 반마리 필렛")
                .delivery(delivery1)
                .options(optionsList1)
                .build();
        StoreRequestDto storeDto2 = StoreRequestDto.builder()
                .name("완도 전복")
                .description("산지직송 완도 전복 1kg (7미~60미)")
                .delivery(delivery2)
                .options(optionsList2)
                .build();

        Store store1 = Store.createStore(storeDto1);
        Store store2 = Store.createStore(storeDto2);


        storeRepository.save(store1);
        storeRepository.save(store2);
    }

    @Test
    @DisplayName("수령일 계산")
    public void getArrivesDate() {

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime now2 = LocalDateTime.now().plusDays(2);
        LocalDateTime now3 = LocalDateTime.now().minusDays(2);
        Store store = storeRepository.getById(1L);
        Store store2 = storeRepository.getById(2L);
        List<Holiday> holidayList = holidayQueryRepository.findHoliday(now.toLocalDate());
        List<Holiday> holidayList2 = holidayQueryRepository.findHoliday(now2.toLocalDate());
        List<Holiday> holidayList3 = holidayQueryRepository.findHoliday(now3.toLocalDate());

        List<StoreArrivesDateResponseDto> storeArrivesDateResponseDtoList = getStoreArrivesDateResponseDtos(now, store, holidayList);
        List<StoreArrivesDateResponseDto> storeArrivesDateResponseDtoList2 = getStoreArrivesDateResponseDtos(now2, store, holidayList2);
        List<StoreArrivesDateResponseDto> storeArrivesDateResponseDtoList3 = getStoreArrivesDateResponseDtos(now3, store, holidayList3);

        List<StoreArrivesDateResponseDto> storeArrivesDateResponseDtoList4 = getStoreArrivesDateResponseDtos(now, store2, holidayList);
        List<StoreArrivesDateResponseDto> storeArrivesDateResponseDtoList5 = getStoreArrivesDateResponseDtos(now2, store2, holidayList2);
        List<StoreArrivesDateResponseDto> storeArrivesDateResponseDtoList6 = getStoreArrivesDateResponseDtos(now3, store2, holidayList3);



        assertEquals(storeArrivesDateResponseDtoList.size(),5);
        // 노르웨이 연어
        assertEquals(storeArrivesDateResponseDtoList.get(0).getDate(),"10월 13일 수요일");
        assertEquals(storeArrivesDateResponseDtoList.get(1).getDate(),"10월 14일 목요일");

        assertEquals(storeArrivesDateResponseDtoList2.get(0).getDate(),"10월 13일 수요일");
        assertEquals(storeArrivesDateResponseDtoList2.get(1).getDate(),"10월 14일 목요일");

        assertEquals(storeArrivesDateResponseDtoList3.get(0).getDate(),"10월 12일 화요일");
        assertEquals(storeArrivesDateResponseDtoList3.get(1).getDate(),"10월 13일 수요일");

        // 완도 전복
        assertEquals(storeArrivesDateResponseDtoList4.get(0).getDate(),"10월 13일 수요일");
        assertEquals(storeArrivesDateResponseDtoList4.get(1).getDate(),"10월 14일 목요일");

        assertEquals(storeArrivesDateResponseDtoList5.get(0).getDate(),"10월 14일 목요일");
        assertEquals(storeArrivesDateResponseDtoList5.get(1).getDate(),"10월 15일 금요일");

        assertEquals(storeArrivesDateResponseDtoList6.get(0).getDate(),"10월 13일 수요일");
        assertEquals(storeArrivesDateResponseDtoList6.get(1).getDate(),"10월 14일 목요일");
    }

    private List<StoreArrivesDateResponseDto> getStoreArrivesDateResponseDtos(LocalDateTime now, Store store, List<Holiday> holidayList) {
        Long countDate = 0L;

        // 모든 휴일이 담긴 dateList
        List<LocalDate> dateList = holidayList.stream().map(Holiday::getDate).collect(Collectors.toList());
        // 토요일을 제외한 dateList
        List<LocalDate> dateListExceptSat = holidayList.stream()
                .filter(holiday -> !holiday.getDateName().equals("토요일"))
                .map(Holiday::getDate).collect(Collectors.toList());

        // 주말에 주문하게 될 시 다음 주 평일 배송이지 않을까?
        if(!dateList.contains(now.toLocalDate())) {
            // 배송 방식에 따른 date 증가
            if(store.getDelivery().getType().equals("regular")){
                countDate +=1L;
            }
            // 마감 시간 비교
            if(store.getDelivery().getClosing().isBefore(now.toLocalTime())){
                countDate +=1L;
            }
        }

        // db에서 가져온 holidayList로 now 에서 일주일 내에 휴일 존재시 date 추가.
        for (int i = 0; i < 7 ; i++) {
            if(dateList.contains(now.toLocalDate().plusDays(countDate))){
                countDate ++;
            }
        }

        List<StoreArrivesDateResponseDto> storeArrivesDateResponseDtoList = new ArrayList<>();

        int i = 1;
        // size 5의 리스트를 리스폰스 해야하므로
        while(storeArrivesDateResponseDtoList.size()<5) {
            LocalDate arrivesDate = now.toLocalDate().plusDays(countDate+i);
            // 휴일인 날에는 받을 수 없다. 하지만 토요일은 받을 수 있다.
            if(!dateListExceptSat.contains(arrivesDate)) {
                String formatDate = arrivesDate.format(DateTimeFormatter.ofPattern("MM월 dd일 E요일"));
                StoreArrivesDateResponseDto storeArrivesDateResponseDto = new StoreArrivesDateResponseDto(formatDate);
                storeArrivesDateResponseDtoList.add(storeArrivesDateResponseDto);
            }
            i++;
        }
        return storeArrivesDateResponseDtoList;
    }
}
