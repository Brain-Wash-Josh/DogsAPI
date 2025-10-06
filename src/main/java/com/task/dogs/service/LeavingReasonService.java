package com.task.dogs.service;

import com.task.dogs.domain.entity.LeavingReason;
import com.task.dogs.repository.LeavingReasonRepository;
import jakarta.inject.Singleton;

import java.util.List;
import java.util.stream.StreamSupport;

@Singleton
public class LeavingReasonService {

    private final LeavingReasonRepository reasonRepository;

    public LeavingReasonService(LeavingReasonRepository reasonRepository) {
        this.reasonRepository = reasonRepository;
    }

    public List<LeavingReason> getAllReasons() {
        return StreamSupport.stream(reasonRepository.findAll().spliterator(), false)
            .toList();
    }
}