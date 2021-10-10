package com.pirates.web.domain.store;

import com.pirates.web.domain.common.Timestamped;
import com.pirates.web.domain.delivery.Delivery;
import com.pirates.web.domain.option.Options;
import com.pirates.web.dto.request.StoreRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
public class Store extends Timestamped {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name="store_id")
    private Long id;

    @Column
    private String name;

    @Column(columnDefinition="TEXT")
    private String description;

    @Embedded
    private Delivery delivery;

    // 연관관계의 주인 설정
    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL)
    // 컬렉션의 필드초기화 .
    private List<Options> options = new ArrayList<>();

    @Builder
    public Store(String name, String description, Delivery delivery) {
        this.name = name;
        this.description = description;
        this.delivery = delivery;
    }

    //==연관관계 편의 메서드==//
    public void addOptions(Options options) {
        this.options.add(options);
        options.addStore(this);
    }

    public static Store createStore(StoreRequestDto storeRequestDto) {

        Store store = new Store(
                storeRequestDto.getName(),
                storeRequestDto.getDescription(),
                storeRequestDto.getDelivery()
        );

        for (Options options :storeRequestDto.getOptions()){
            store.addOptions(options);
        }
        return store;
    }
}
