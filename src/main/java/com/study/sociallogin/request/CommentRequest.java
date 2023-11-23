package com.study.sociallogin.request;

import lombok.Getter;

@Getter
public class CommentRequest {
    private Long boardId;
    private String content;
}
