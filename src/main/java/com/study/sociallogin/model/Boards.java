package com.study.sociallogin.model;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Boards {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardId;

    @Column(length = 50)
    private String boardTitle;

    @Column(length = 500)
    private String boardContent;

    @CreatedDate
    @Column(updatable = true, nullable = false)
    private LocalDateTime createdAt;

    @Column(length = 50)
    @ColumnDefault("0")
    private int boardHit;

    @Column(length = 50)
    private String userEmail;

    @Column(length = 50)
    private Long locationId;
}
