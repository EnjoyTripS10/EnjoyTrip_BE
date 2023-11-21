package com.study.sociallogin.service;

import com.study.sociallogin.dto.TripDetailDto;
import com.study.sociallogin.model.TripDetails;
import com.study.sociallogin.repository.TripDetailRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TripDetailService {

    private final TripDetailRepository tripDetailRepository;
        public TripDetails createLocation(TripDetails details) {
            return tripDetailRepository.save(details);
    }

    public List<TripDetails> getDetailList(Long tripId, int tripDate) {
            return tripDetailRepository.findByTripIdAndTripDateOrderByOrderIndexAsc(tripId, tripDate);
    }

    @Transactional
    public void deleteTripDetail(Long id) {
            tripDetailRepository.deleteByTripId(id);
    }
}
