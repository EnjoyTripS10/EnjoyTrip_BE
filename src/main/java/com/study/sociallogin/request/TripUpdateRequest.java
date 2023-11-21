package com.study.sociallogin.request;

import com.study.sociallogin.dto.TripDetailDto;
import com.study.sociallogin.dto.TripMemberDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class TripUpdateRequest {

    private String title;
    private String content;
    private int type;
    private String startDate;
    private String endDate;
    private String tripDetails;
    private List<List<TripDetailDto>> locationList;
}
