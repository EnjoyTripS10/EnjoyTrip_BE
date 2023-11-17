package com.study.sociallogin.dto;

import lombok.*;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BoardDto {

    private Long boardId;
    private String boardTitle;
    private String boardContent;
    private String createdAt;
    private int boardHit;
    private String userEmail;
    private Long locationId;
    private List<byte[]> image;
    private String locationName;
    private String locationAddr;
    private String locationLat;
    private String locationLon;
    private String locationType;
    private int boardLikes;
    private boolean isLike;

}
