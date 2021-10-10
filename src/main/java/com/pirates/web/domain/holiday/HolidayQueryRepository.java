package com.pirates.web.domain.holiday;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import static com.pirates.web.domain.holiday.QHoliday.holiday;

@Repository
@RequiredArgsConstructor
public class HolidayQueryRepository {

    private final JPAQueryFactory queryFactory;

    public List<Holiday> findHoliday(LocalDate today) {

        return queryFactory
                .select(holiday)
                .from(holiday)
                .where(holiday.date.month().eq(today.getMonth().getValue()),
                        holiday.date.goe(today))
                .fetch();
    }
}
