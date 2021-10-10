package com.pirates.web.dto.response;

import com.pirates.web.domain.option.Options;
import com.pirates.web.domain.store.Store;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class StoreDetailResponseDto {

    private String name;
    private String description;
    private String delivery;
    private List<OptionsResponseDto> options = new ArrayList<>();

    @Builder
    public StoreDetailResponseDto(String name, String description, String delivery, List<OptionsResponseDto> options) {
        this.name = name;
        this.description = description;
        this.delivery = delivery;
        this.options = options;
    }

    public static StoreDetailResponseDto of(Store store) {
        return StoreDetailResponseDto.builder()
                .name(store.getName())
                .description(store.getDescription())
                .delivery(store.getDelivery().getType())
                .options(changeDto(store.getOptions()))
                .build();
    }

    /**
     * 엔티티인 options의 직접노출을 피하기 위해 DTO 변환.
     */
    private static List<OptionsResponseDto> changeDto(List<Options> optionsList) {

        return optionsList.stream()
                .map(OptionsResponseDto::of)
                .collect(Collectors.toList());
    }
}
