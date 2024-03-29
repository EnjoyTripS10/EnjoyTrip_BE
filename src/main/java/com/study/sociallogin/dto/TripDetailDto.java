package com.study.sociallogin.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TripDetailDto {

    private Long locationId;

    private String locationName;

    private String locationAddr;

    private String locationLat;

    private String locationLon;

    private String locationType;

    private String memo;
}
