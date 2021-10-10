package com.pirates.web.dto.query;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StoreQueryDto {

    private String name;
    private String description;
    private String price;

    @QueryProjection
    public StoreQueryDto( String name, String description,String price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }



}
