package com.pirates.web.dto.response;

import com.pirates.web.domain.option.Options;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OptionsResponseDto {

    private String name;
    private Long price;

    @Builder
    public OptionsResponseDto(String name, Long price) {
        this.name = name;
        this.price = price;
    }

    public static OptionsResponseDto of(Options options) {
        return OptionsResponseDto.builder()
                .name(options.getName())
                .price(options.getPrice())
                .build();
    }
}
