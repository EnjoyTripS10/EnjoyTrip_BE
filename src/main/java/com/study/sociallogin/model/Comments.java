package com.study.sociallogin.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Comments {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @CreatedDate
    @Column(updatable = true, nullable = false)
    private LocalDateTime createdAt;

    @Column
    private String commentContent;

    @Column
    private Long boardId;

    @Column(length = 50)
    private String userEmail;
}
