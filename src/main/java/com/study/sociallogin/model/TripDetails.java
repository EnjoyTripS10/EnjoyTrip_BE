package com.study.sociallogin.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TripDetails {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tripDetailId;

    @Column
    private Long tripId;

    @Column
    private Long locationId;

    @Column
    private int orderIndex;

    @Column
    private int reviewType; // 계획일때 0 || 11 : 추천 12 : 비추천 13: 방문하지 않음 14: 보통

    @Column
    private int tripDate;
    @Column(length = 500)
    private String memo;
}
