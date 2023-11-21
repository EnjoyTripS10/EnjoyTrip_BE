package com.study.sociallogin.model;

import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Trips {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tripId;

    @Column
    private String tripTitle;

    @Column(length = 500)
    private String tripContent;
    @Column
    private int tripType; // 0 : 계획 1: 후기

    @Column
    private String tripStartDate;

    @Column
    private String tripEndDate;
}
