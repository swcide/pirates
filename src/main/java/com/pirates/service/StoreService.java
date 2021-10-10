package com.pirates.service;

import com.pirates.exception.ApiRequestException;
import com.pirates.web.domain.holiday.Holiday;
import com.pirates.web.domain.holiday.HolidayQueryRepository;
import com.pirates.web.domain.store.Store;
import com.pirates.web.domain.store.StoreQueryRepository;
import com.pirates.web.domain.store.StoreRepository;
import com.pirates.web.dto.query.StoreQueryDto;
import com.pirates.web.dto.request.StoreRequestDto;
import com.pirates.web.dto.response.StoreArrivesDateResponseDto;
import com.pirates.web.dto.response.StoreDetailResponseDto;
import com.pirates.web.dto.response.StoreResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreService {

    private final StoreRepository storeRepository;
    private final StoreQueryRepository storeQueryRepository;
    private final HolidayQueryRepository holidayQueryRepository;

    /**
     * 점포 추가
     */
    @Transactional
    public Long createStore(StoreRequestDto storeRequestDto) {
        Store store = Store.createStore(storeRequestDto);
        storeRepository.save(store);

        return store.getId();
    }

    /**
     * 상품 목록 조회
     */
    public List<StoreResponseDto> getALlStore() {

        List<StoreQueryDto> storeQueryDtoList = storeQueryRepository.findAllStore();

        return storeQueryDtoList.stream()
                .map(StoreResponseDto::of)
                .collect(Collectors.toList());
    }

    /**
     * 상품 상세조회
     */
    public StoreDetailResponseDto getStoreDetail(Long id) {
        Store store = getStore(id);

        return StoreDetailResponseDto.of(store);
    }

    /**
     *  수령일 선택 목록
     */
    public List<StoreArrivesDateResponseDto> getStoreArrivesDate(Long id) {
        LocalDateTime now = LocalDateTime.now();
        Store store = getStore(id);

        //
        List<Holiday> holidayList = holidayQueryRepository.findHoliday(now.toLocalDate());

        return StoreArrivesDateResponseDto.getArrivesDate(store, holidayList,now);
    }
    /**
     * 점포 삭제
     */
    @Transactional
    public void deleteStore(Long id) {
        Store store = getStore(id);
        storeRepository.delete(store);
    }

    private Store getStore(Long id) {
        return storeRepository.findById(id)
                .orElseThrow(() -> new ApiRequestException("점포를 찾을 수 없습니다."));
    }

}
