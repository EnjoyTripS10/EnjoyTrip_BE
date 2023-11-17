package com.study.sociallogin.controller;

import com.study.sociallogin.dto.TripMemberDto;
import com.study.sociallogin.model.TripMembers;
import com.study.sociallogin.model.Trips;
import com.study.sociallogin.service.LocationService;
import com.study.sociallogin.service.TripMemberService;
import com.study.sociallogin.service.TripService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import request.TripRequest;

@CrossOrigin
@RestController
@RequestMapping("/trip")
@RequiredArgsConstructor
public class TripController {

    private final TripService tripService;
    private final TripMemberService tripMemberService;

    private final LocationService locationService;

    @PostMapping
    public ResponseEntity<HttpStatus> registerTrip(@RequestBody TripRequest tripRequest)  {
        System.out.println("create trip");

        //login check 해야함....
        String userEmail = "1";

        //trip 정보 저장
        Trips trip = Trips.builder()
                .tripTitle(tripRequest.getTitle())
                .tripContent(tripRequest.getContent())
                .tripType(tripRequest.getType())
                .tripStartDate(tripRequest.getStartDate())
                .tripEndDate(tripRequest.getEndDate())
                .build();

        trip = tripService.createTrip(trip);

        System.out.println(tripRequest.getStartDate());
        //사용자 trip member
//        users.add(userEmail);
        TripMembers tripMember = TripMembers.builder()
                .tripId(trip.getTripId())
                .build();

        for (TripMemberDto member : tripRequest.getUsers()) {
              System.out.println(member.getEmail() + " " + member.getName());
//            tripMember.setUserEmail(email);
//            tripMemberService.createTripMember(tripMember);
        }

        //trip detail 정보 파싱
//        JSONParser jsonParser = new JSONParser();
//        JSONObject detailJson = (JSONObject) jsonParser.parse(tripRequest.getTripDetails());



        return new ResponseEntity<>(HttpStatus.OK);
    }

}
