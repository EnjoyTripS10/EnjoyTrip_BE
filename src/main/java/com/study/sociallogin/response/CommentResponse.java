package com.study.sociallogin.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Column;
import java.time.LocalDateTime;

@Builder
@Setter
@Getter
public class CommentResponse {
    private Long commentId;

    private String createdAt;

    private String commentContent;

    private Long boardId;

    private String userEmail;
    private boolean mine;
}
