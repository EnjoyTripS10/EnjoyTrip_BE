package com.study.sociallogin.response;

import com.study.sociallogin.dto.UserResponse;
import com.study.sociallogin.model.User;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TripListResponse {
    private Long tripId;
    private String tripTitle;
    private int tripType; // 0 : 계획 1: 후기
    private String tripStartDate;
    private String tripEndDate;
    private List<UserResponse> users;
}
