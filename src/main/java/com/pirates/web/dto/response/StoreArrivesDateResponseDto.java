package com.pirates.web.dto.response;

import com.pirates.web.domain.holiday.Holiday;
import com.pirates.web.domain.store.Store;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StoreArrivesDateResponseDto {

    private String date;


    public static  List<StoreArrivesDateResponseDto> getArrivesDate(Store store, List<Holiday> holidayList, LocalDateTime now) {
        Long countDate = 0L;

        // 모든 휴일이 담긴 dateList
        List<LocalDate> dateList = holidayList.stream().map(Holiday::getDate).collect(Collectors.toList());
        // 토요일을 제외한 dateList
        List<LocalDate> dateListExceptSat = holidayList.stream()
                .filter(holiday -> !holiday.getDateName().equals("토요일"))
                .map(Holiday::getDate).collect(Collectors.toList());

        for (LocalDate localDate : dateListExceptSat) {
            System.out.println("localDate = " + localDate);
        }

        // 주말에 주문하게 될 시 월요일 배송이지 않을까?
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
