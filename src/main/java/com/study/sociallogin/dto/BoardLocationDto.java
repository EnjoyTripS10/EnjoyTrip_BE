package com.study.sociallogin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
public class BoardLocationDto {

    private Long boardId;
    private String boardTitle;
    private String boardContent;
    private Long locationId;
    private LocalDateTime createdAt;
    private int boardHit;
    private String userEmail;
}
