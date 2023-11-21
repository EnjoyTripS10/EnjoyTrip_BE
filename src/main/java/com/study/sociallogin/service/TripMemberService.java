package com.study.sociallogin.service;

import com.study.sociallogin.model.TripMembers;
import com.study.sociallogin.repository.TripMemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TripMemberService {
    private final TripMemberRepository tripMemberRepository;

    public void createTripMember(TripMembers tripMember) {
        tripMemberRepository.save(tripMember);
    }

    public List<TripMembers> getTripMembers(Long id) {
        return tripMemberRepository.findAllByTripId(id);
    }

    public void deleteTripMember(Long id) {
        tripMemberRepository.deleteByTripId(id);
    }

    public TripMembers getTripOwner(Long id) {
        return tripMemberRepository.findByTripIdAndOwner(id, true);
    }
}
