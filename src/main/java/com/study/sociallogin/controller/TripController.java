package com.study.sociallogin.controller;

import com.study.sociallogin.model.TripMembers;
import com.study.sociallogin.model.Trips;
import com.study.sociallogin.repository.TripMemberRepository;
import com.study.sociallogin.service.LocationService;
import com.study.sociallogin.service.TripMemberService;
import com.study.sociallogin.service.TripService;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/trip")
@RequiredArgsConstructor
public class TripController {

    private final TripService tripService;
    private final TripMemberService tripMemberService;

    private final LocationService locationService;

    @PostMapping
    public ResponseEntity<HttpStatus> registerTrip(@RequestParam("title") String title,
                                                   @RequestParam("content") String content,
                                                   @RequestParam("type") int type,
                                                   @RequestParam("startDate")  String startDate,
                                                   @RequestParam("endDate") String endDate,
                                                   @RequestBody List<String> members,
                                                   @RequestParam("tripDetails") String tripDetails) throws ParseException {
        System.out.println("create trip");

        //login check 해야함....
        String userEmail = "1";

        //trip 정보 저장
        Trips trip = Trips.builder()
                .tripTitle(title)
                .tripContent(content)
                .tripType(type)
                .tripStartDate(startDate)
                .tripEndDate(endDate)
                .build();

        trip = tripService.createTrip(trip);


        //사용자 trip member
        members.add(userEmail);
        TripMembers tripMember = TripMembers.builder()
                .tripId(trip.getTripId())
                .build();

        for (String email: members) {
            tripMember.setUserEmail(email);
            tripMemberService.createTripMember(tripMember);
        }

        //trip detail 정보 파싱
        JSONParser jsonParser = new JSONParser();
        JSONObject detailJson = (JSONObject) jsonParser.parse(tripDetails);



        return new ResponseEntity<>(HttpStatus.OK);
    }

}
