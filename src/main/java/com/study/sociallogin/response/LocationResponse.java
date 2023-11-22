package com.study.sociallogin.response;

import lombok.*;

import javax.persistence.Column;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LocationResponse {
    private Long locationId;

    private String locationName;

    private String locationAddr;

    private String locationLat;

    private String locationLon;

    private String locationType;
    private String imgUrl;
}
