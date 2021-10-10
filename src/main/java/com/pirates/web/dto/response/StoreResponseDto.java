package com.pirates.web.dto.response;

import com.pirates.web.dto.query.StoreQueryDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.text.DecimalFormat;

@Getter
@NoArgsConstructor
public class StoreResponseDto {


    private String name;
    private String description;
    private String price;


    @Builder
    public StoreResponseDto(String name, String description, String price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public static StoreResponseDto of(StoreQueryDto storeQueryDto) {
        return StoreResponseDto.builder()
                .name(storeQueryDto.getName())
                .description(storeQueryDto.getDescription())
                .price(formatting(storeQueryDto.getPrice()))
                .build();
    }

    /*
     *요구사항에 맞는 형태를 위한 formatting
     */
    private static String formatting(String price) {
        DecimalFormat Commas = new DecimalFormat("#,### ~");

        return Commas.format(Integer.parseInt(price));
    }


}
