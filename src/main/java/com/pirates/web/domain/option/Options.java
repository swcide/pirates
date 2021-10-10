package com.pirates.web.domain.option;

import com.pirates.web.domain.common.Timestamped;
import com.pirates.web.domain.store.Store;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class Options extends Timestamped {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name="option_id")
    private Long id;

    @Column
    private String name;

    @Column
    private Long price;

    @Column
    private Long stock;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="store_id")
    private Store store;

    @Builder
    public Options(String name, Long price, Long stock, Store store) {
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.store = store;
    }

    public void addStore(Store store) {
        this.store = store;
    }
}
