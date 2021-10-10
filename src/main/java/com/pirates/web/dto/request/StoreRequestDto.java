package com.pirates.web.dto.request;

import com.pirates.web.domain.delivery.Delivery;
import com.pirates.web.domain.option.Options;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class StoreRequestDto {

    private String name;
    private String description;
    private Delivery delivery;
    private List<Options> options = new ArrayList<>();

    @Builder
    public StoreRequestDto(String name, String description, Delivery delivery, List<Options> options) {
        this.name = name;
        this.description = description;
        this.delivery = delivery;
        this.options = options;
    }
}
