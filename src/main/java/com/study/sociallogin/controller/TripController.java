package com.study.sociallogin.controller;

import com.study.sociallogin.dto.TripDetailDto;
import com.study.sociallogin.dto.TripMemberDto;
import com.study.sociallogin.model.TripDetails;
import com.study.sociallogin.model.TripMembers;
import com.study.sociallogin.model.Trips;
import com.study.sociallogin.model.Locations;
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
    public ResponseEntity<TripRequest> getTrip(@PathVariable("id") Long id) throws java.text.ParseException {
        System.out.print("get trip plan /" + id + "//");

        String userEmail = "1";
        Trips trip = tripService.getTrip(id);
        if(trip == null){
            return ResponseEntity.notFound().build();
        }
        List<TripMembers> tripMembers = tripMemberService.getTripMembers(id);

        List<List<TripDetailDto>> locationList = new ArrayList<>();

        String dateStr = "2023-11-01T15:00:00.000Z";


        String start = trip.getTripStartDate().substring(0,10);
        String end = trip.getTripEndDate().substring(0,10);

        // 포맷터
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        // 문자열 -> Date
        Date sdate = format.parse(start);
        Date edate = format.parse(end);

        long Sec = (edate.getTime() - sdate.getTime()) / 1000; // 초
        long Days = Sec / (24*60*60); // 일자수
        System.out.println("::::::"+Days);

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

        return ResponseEntity.ok(
                TripRequest.builder()
                        .title(trip.getTripTitle())
                        .content(trip.getTripContent())
                        .type(trip.getTripType())
                        .startDate(trip.getTripStartDate())
                        .endDate(trip.getTripEndDate())
                        .locationList(locationList)
                        .build()
        );

    }

}
