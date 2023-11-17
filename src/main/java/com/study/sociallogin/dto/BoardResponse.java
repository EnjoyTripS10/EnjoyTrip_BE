package com.study.sociallogin.dto;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BoardResponse {
    private Long boardId;
    private String boardTitle;
    private String boardContent;
    private String createdAt;
    private int boardHit;
    private String userEmail;
    private Long locationId;
    private byte[] image;


}
