package com.study.sociallogin.request;

import com.study.sociallogin.dto.TripDetailDto;
import com.study.sociallogin.dto.TripMemberDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
@Getter
@Builder
public class TripRequest {

    private String title;
    private String content;
    private int type;
    private String startDate;
    private String endDate;
    private List<TripMemberDto> users;
    private String tripDetails;
    private List<List<TripDetailDto>> locationList;
    private boolean mine;
    private boolean owner;
}
