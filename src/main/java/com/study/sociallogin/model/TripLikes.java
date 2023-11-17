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
public class TripLikes {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tripLikeId;

    @Column
    private Long tripId;

    @Column(length = 50)
    private String userEmail;

}
