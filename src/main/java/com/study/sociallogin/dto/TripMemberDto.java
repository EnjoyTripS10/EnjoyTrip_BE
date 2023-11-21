package com.study.sociallogin.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TripMemberDto {
    String userEmail;
    String userName;
    String picture;
}
