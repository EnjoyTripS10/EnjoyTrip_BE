package com.study.sociallogin.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Locations {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long locationId;

    @Column(length = 150)
    private String locationName;

    @Column(length = 150)
    private String locationAddr;

    @Column(length = 50)
    private String locationLat;

    @Column(length = 50)
    private String locationLon;

    @Column(length = 50)
    private String locationType;
}
