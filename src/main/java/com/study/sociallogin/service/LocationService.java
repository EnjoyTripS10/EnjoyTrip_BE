package com.study.sociallogin.service;

import com.study.sociallogin.model.Locations;
import com.study.sociallogin.repository.LocationRepository;
import com.study.sociallogin.response.LocationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class LocationService {

    private final LocationRepository locationRepository;

    public Locations createLocation(Locations location) {
        Locations isExist = isExistLocation(location.getLocationName(), location.getLocationLat(), location.getLocationLon());
        if (isExist == null) {
            return locationRepository.save(location);
        }
        return isExist;
    }

    private Locations isExistLocation(String locationName, String locationLat, String locationLon) {
        return locationRepository.findByLocationNameAndLocationLatAndLocationLon(locationName, locationLat, locationLon);
    }

    public Locations getLocationId(Long locationId) {
        return locationRepository.findByLocationId(locationId);
    }

    public List<LocationResponse> getStoredLocationList(String userEmail) {
        List<Locations> list =  locationRepository.findLocationsLikedByUser(userEmail);
        List<LocationResponse> responseList = new ArrayList<>();
        for(Locations location : list){
            responseList.add(LocationResponse.builder()
                    .locationId(location.getLocationId())
                    .locationName(location.getLocationName())
                    .locationAddr(location.getLocationAddr())
                    .locationLat(location.getLocationLat())
                    .locationLon(location.getLocationLon())
                    .locationType(location.getLocationType())
                    .imgUrl(null)
                    .build());
        }
        return responseList;
    }
}
