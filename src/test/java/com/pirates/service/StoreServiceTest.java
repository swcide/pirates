package com.pirates.service;

import com.pirates.web.domain.delivery.Delivery;
import com.pirates.web.domain.holiday.Holiday;
import com.pirates.web.domain.holiday.HolidayQueryRepository;
import com.pirates.web.domain.option.Options;
import com.pirates.web.domain.store.Store;
import com.pirates.web.domain.store.StoreQueryRepository;
import com.pirates.web.domain.store.StoreRepository;
import com.pirates.web.dto.query.StoreQueryDto;
import com.pirates.web.dto.request.StoreRequestDto;
import com.pirates.web.dto.response.StoreArrivesDateResponseDto;
import com.pirates.web.dto.response.StoreDetailResponseDto;
import com.pirates.web.dto.response.StoreResponseDto;
import org.junit.jupiter.api.Assertions;
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
class StoreServiceTest {

    @Autowired
    StoreRepository storeRepository;
    @Autowired
    StoreQueryRepository storeQueryRepository;
    @Autowired
    HolidayQueryRepository holidayQueryRepository;


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
    @DisplayName("상품 목록 조회")
    public void getALlStore() {

        //when
        List<StoreQueryDto> storeQueryDtoList = storeQueryRepository.findAllStore();
        List<StoreResponseDto> storeResponseDtoList = storeQueryDtoList.stream()
                .map(StoreResponseDto::of)
                .collect(Collectors.toList());

        //then
        assertEquals(storeResponseDtoList.get(0).getName(),"완도 전복");
        assertEquals(storeResponseDtoList.get(0).getPrice(),"20,000 ~");
        assertEquals(storeResponseDtoList.get(1).getName(),"노르웨이산 연어");
        assertEquals(storeResponseDtoList.get(1).getPrice(),"10,000 ~");
    }
    
    @Test
    @DisplayName("상품 상세조회")
    public void getStoreDetail (){
        //given
        Store store = getStore(1L);

        //when
        StoreDetailResponseDto storeDetailResponseDto = StoreDetailResponseDto.of(store);

        //then
        assertEquals(storeDetailResponseDto.getName(),"노르웨이산 연어");
    }

    @Test
    @DisplayName("수령일 선택 목록")
    public void getStoreArrivesDate(){
        //given
        LocalDateTime now = LocalDateTime.now(); //10월 10일 오전 9시 (일요일)
        Store store = getStore(1L);
        List<Holiday> holidayList = holidayQueryRepository.findHoliday(now.toLocalDate());


        List<StoreArrivesDateResponseDto> arrivesDate = StoreArrivesDateResponseDto.getArrivesDate(store, holidayList, now);

        // 화요일 시작이므로 +1
        assertEquals(arrivesDate.get(0).getDate(),"10월 13일 수요일");

    }

    @Test
    @DisplayName("점포 삭제")
    public void deleteStore(){
        //given
        Store store = getStore(1L);
        Store store2 = getStore(2L);

        //when
        storeRepository.delete(store);
        storeRepository.delete(store2);

        //then
        List<Store> afterDelete = storeRepository.findAll();
        Assertions.assertEquals(afterDelete.size(),0);

    }


    private Store getStore(Long id) {
        return storeRepository.getById(id);
    }
}