package com.study.sociallogin.repository;

import com.study.sociallogin.model.Locations;
import com.study.sociallogin.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<Locations, Long> {

    Locations findByLocationNameAndLocationLatAndLocationLon(String locationName, String locationLat, String locationLon);

    Locations findByLocationId(Long locationId);
}
