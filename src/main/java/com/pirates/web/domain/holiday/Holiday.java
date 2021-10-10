package com.pirates.web.domain.holiday;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Entity
@NoArgsConstructor
public class Holiday {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name="Holiday_id")
    private Long id;
    @Column
    private LocalDate date;
    @Column
    private String dateName;
    @Column
    private String type;

    @Builder
    public Holiday(LocalDate date, String dateName, String type) {
        this.date = date;
        this.dateName = dateName;
        this.type = type;
    }
}
