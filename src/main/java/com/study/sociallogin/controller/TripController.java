package com.study.sociallogin.controller;

import com.study.sociallogin.dto.TripDetailDto;
import com.study.sociallogin.dto.TripMemberDto;
import com.study.sociallogin.model.TripDetails;
import com.study.sociallogin.model.TripMembers;
import com.study.sociallogin.model.Trips;
import com.study.sociallogin.service.LocationService;
import com.study.sociallogin.service.TripDetailService;
import com.study.sociallogin.service.TripMemberService;
import com.study.sociallogin.service.TripService;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.study.sociallogin.request.TripRequest;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/trip")
@RequiredArgsConstructor
public class TripController {

    private final TripService tripService;
    private final TripMemberService tripMemberService;
    private final LocationService locationService;
    private final TripDetailService tripDetailService;

    @PostMapping
    public ResponseEntity<HttpStatus> registerTrip(@RequestBody TripRequest tripRequest) throws ParseException {
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
        tripMemberService.createTripMember(
                TripMembers.builder()
                .tripId(trip.getTripId())
                .userEmail(userEmail)
                .owner(true)
                .build()
        );



        for (TripMemberDto member : tripRequest.getUsers()) {
              System.out.println(member.getEmail() + " " + member.getName());
              if(member.getEmail().equals(userEmail))
                  continue;
              tripMemberService.createTripMember(TripMembers.builder()
                      .tripId(trip.getTripId())
                      .userEmail(member.getEmail())
                      .owner(false)
                      .build());
        }

        //trip detail 정보 파싱
        JSONParser jsonParser = new JSONParser();
        System.out.println(tripRequest.getLocationList().size());
        int tripIndex = 0;
        for(List<TripDetailDto> li : tripRequest.getLocationList()){
            int locationIndex = 0;
            for(TripDetailDto location : li){
                tripDetailService.createLocation(TripDetails.builder()
                        .tripId(trip.getTripId())
                        .locationId(location.getLocationId())
                        .orderIndex(locationIndex++)
                        .reviewType(0)
                        .tripDate(tripIndex)
                        .memo(location.getMemo())
                        .build());
            }
            tripIndex++;
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
