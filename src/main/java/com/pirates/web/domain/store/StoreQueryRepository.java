package com.pirates.web.domain.store;

import com.pirates.web.dto.query.QStoreQueryDto;
import com.pirates.web.dto.query.StoreQueryDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.pirates.web.domain.option.QOptions.options;
import static com.pirates.web.domain.store.QStore.store;

@Repository
@RequiredArgsConstructor
public class StoreQueryRepository {

    private final JPAQueryFactory queryFactory;

    public List<StoreQueryDto> findAllStore() {

        return queryFactory
                .select(new QStoreQueryDto(
                        store.name,
                        store.description,
                        options.price.min().stringValue()
                )).from(store)
                .join(store.options, options)
                .groupBy(store.id)
                .orderBy(store.createdAt.desc())
                .fetch();
    }
}
