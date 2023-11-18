package com.study.sociallogin.service;

import com.study.sociallogin.model.Locations;
import com.study.sociallogin.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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

    public List<Locations> getStoredLocationList(String userEmail) {
        return locationRepository.findLocationsLikedByUser(userEmail);
    }
}
