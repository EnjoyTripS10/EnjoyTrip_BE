package com.study.sociallogin.controller;

import com.study.sociallogin.dto.TripDetailDto;
import com.study.sociallogin.dto.TripMemberDto;
import com.study.sociallogin.dto.UserResponse;
import com.study.sociallogin.model.*;
import com.study.sociallogin.request.TripUpdateRequest;
import com.study.sociallogin.response.TripListResponse;
import com.study.sociallogin.service.*;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.study.sociallogin.request.TripRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    private final UserService userService;

    @PostMapping
    public ResponseEntity<HttpStatus> registerTrip(
            @RequestHeader("Authorization") String token,
            @RequestBody TripRequest tripRequest) throws ParseException {
        System.out.println("create trip");

        //login check
        String userEmail = userService.getUserEmailFromToken(token);
        if(userEmail == null){
            return ResponseEntity.ok(HttpStatus.UNAUTHORIZED);
        }

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
            System.out.println(member.getUserEmail() + " " + member.getUserName());
            if (member.getUserEmail().equals(userEmail))
                continue;
            tripMemberService.createTripMember(TripMembers.builder()
                    .tripId(trip.getTripId())
                    .userEmail(member.getUserEmail())
                    .owner(false)
                    .build());
        }

        //trip detail 정보 파싱
        JSONParser jsonParser = new JSONParser();
        System.out.println(tripRequest.getLocationList().size());
        int tripIndex = 0;
        for (List<TripDetailDto> li : tripRequest.getLocationList()) {
            int locationIndex = 0;
            for (TripDetailDto location : li) {
                //장소 체크
                Locations loc = locationService.createLocation(Locations.builder()
                        .locationName(location.getLocationName())
                        .locationAddr(location.getLocationAddr())
                        .locationLat(location.getLocationLat())
                        .locationLon(location.getLocationLon())
                        .build());

                tripDetailService.createLocation(TripDetails.builder()
                        .tripId(trip.getTripId())
                        .locationId(loc.getLocationId())
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

    @GetMapping("/{id}")
    public ResponseEntity<TripRequest> getTrip(@RequestHeader("Authorization") String token,
                                               @PathVariable("id") Long id) throws java.text.ParseException {
        System.out.print("get trip plan /" + id + "///");

        //login check
        String userEmail = userService.getUserEmailFromToken(token);
        if(userEmail == null){
            return null;
        }

        Trips trip = tripService.getTrip(id);
        if (trip == null) {
            return ResponseEntity.notFound().build();
        }
//        List<TripMembers> tripMembers = tripMemberService.getTripMembers(id);

        List<List<TripDetailDto>> locationList = new ArrayList<>();

        String start = trip.getTripStartDate().substring(0, 10);
        String end = trip.getTripEndDate().substring(0, 10);

        // 포맷터
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        // 문자열 -> Date
        Date sdate = format.parse(start);
        Date edate = format.parse(end);

        long Sec = (edate.getTime() - sdate.getTime()) / 1000; // 초
        long Days = Sec / (24 * 60 * 60); // 일자수
        System.out.println("::::::" + Days);

        for (int i = 0; i <= Days; i++) {
            List<TripDetails> detailList = tripDetailService.getDetailList(id, i);
            List<TripDetailDto> dtoList = new ArrayList<>();
            for (TripDetails detail : detailList) {
                Locations loc = locationService.getLocationId(detail.getLocationId());
                TripDetailDto dto = TripDetailDto.builder()
                        .locationId(detail.getLocationId())
                        .memo(detail.getMemo())
                        .locationName(loc.getLocationName())
                        .locationAddr(loc.getLocationAddr())
                        .locationLat(loc.getLocationLat())
                        .locationLon(loc.getLocationLon())
                        .build();
                dtoList.add(dto);
            }
            locationList.add(dtoList);
        }

        List<TripMembers> users = tripMemberService.getTripMembers(id);
        List<TripMemberDto> userList = new ArrayList<>();
        boolean mine = false;
        for (TripMembers member : users) {
            if(userEmail.equals(member.getUserEmail())) mine = true;
            UserResponse user = userService.getUserEmail(member.getUserEmail());
            userList.add(TripMemberDto.builder()
                    .userEmail(member.getUserEmail())
                    .userName(user.getUserName())
                    .picture(user.getPicture())
                    .build());
        }



        return ResponseEntity.ok(
                TripRequest.builder()
                        .title(trip.getTripTitle())
                        .content(trip.getTripContent())
                        .type(trip.getTripType())
                        .startDate(trip.getTripStartDate())
                        .endDate(trip.getTripEndDate())
                        .locationList(locationList)
                        .users(userList)
                        .mine(mine)
                        .build()
        );

    }

    @GetMapping
    public ResponseEntity<List<TripListResponse>> getTripList(@RequestHeader("Authorization") String token) {
        System.out.println("get trip list");
        //login check
        String userEmail = userService.getUserEmailFromToken(token);
        if(userEmail == null){
            return null;
        }
        List<Trips> tripList = tripService.getTripList();
        List<TripListResponse> tripListResponse = new ArrayList<>();
        for (Trips trip : tripList) {
            List<TripMembers> tripMembers = tripMemberService.getTripMembers(trip.getTripId());
            List<UserResponse> users = new ArrayList<>();
            for (TripMembers member : tripMembers) {
                UserResponse user = userService.getUserEmail(member.getUserEmail());
                users.add(UserResponse.builder()
                        .userEmail(member.getUserEmail())
                        .userName(user.getUserName())
                        .picture(user.getPicture())
                        .build());
            }
            tripListResponse.add(
                    TripListResponse.builder()
                            .tripId(trip.getTripId())
                            .tripTitle(trip.getTripTitle())
                            .tripType(trip.getTripType())
                            .tripStartDate(trip.getTripStartDate())
                            .tripEndDate(trip.getTripEndDate())
                            .users(users)
                            .build()
            );
        }
        return ResponseEntity.ok(tripListResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteTrip(@RequestHeader("Authorization") String token,
                                                 @PathVariable("id") Long id) {
        System.out.println("delete trip");
        //login check
        String userEmail = userService.getUserEmailFromToken(token);
        if(userEmail == null){
            return ResponseEntity.ok(HttpStatus.UNAUTHORIZED);
        }

        String owner = tripMemberService.getTripOwner(id).getUserEmail();
        //본인 여부 체크
        if (owner == null || userEmail.equals(owner)) {
            tripMemberService.deleteTripMember(id);
            tripDetailService.deleteTripDetail(id);
            tripService.deleteTrip(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("transReview/{id}")
    public ResponseEntity<HttpStatus> transReview(@RequestHeader("Authorization") String token,
                                                  @PathVariable("id") Long id) {
        System.out.println("transReview");
        //login check
        String userEmail = userService.getUserEmailFromToken(token);
        if(userEmail == null){
            return ResponseEntity.ok(HttpStatus.UNAUTHORIZED);
        }

        String owner = tripMemberService.getTripOwner(id).getUserEmail();
        //본인 여부 체크
        if (owner == null || userEmail.equals(owner)) {
            tripService.transReview(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<HttpStatus> updateTrip(@RequestHeader("Authorization") String token,
                                                 @PathVariable("id") Long tripId, @RequestBody TripUpdateRequest tripRequest) throws ParseException {
        System.out.println("update trip");

        //login check
        String userEmail = userService.getUserEmailFromToken(token);
        if(userEmail == null){
            return ResponseEntity.ok(HttpStatus.UNAUTHORIZED);
        }

        //trip 정보 저장
        Trips trip = Trips.builder()
                .tripId(tripId)
                .tripTitle(tripRequest.getTitle())
                .tripContent(tripRequest.getContent())
                .tripType(tripRequest.getType())
                .tripStartDate(tripRequest.getStartDate())
                .tripEndDate(tripRequest.getEndDate())
                .build();

        trip = tripService.createTrip(trip);

         //trip detail 삭제
        tripDetailService.deleteTripDetail(tripId);

        //trip detail 정보 파싱
        JSONParser jsonParser = new JSONParser();
        System.out.println(tripRequest.getLocationList().size());
        int tripIndex = 0;
        for (List<TripDetailDto> li : tripRequest.getLocationList()) {
            int locationIndex = 0;
            for (TripDetailDto location : li) {
                //장소 체크
                Locations loc = locationService.createLocation(Locations.builder()
                        .locationName(location.getLocationName())
                        .locationAddr(location.getLocationAddr())
                        .locationLat(location.getLocationLat())
                        .locationLon(location.getLocationLon())
                        .build());

                tripDetailService.createLocation(TripDetails.builder()
                        .tripId(trip.getTripId())
                        .locationId(loc.getLocationId())
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