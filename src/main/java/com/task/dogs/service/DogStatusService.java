package com.task.dogs.service;

import com.task.dogs.domain.entity.DogStatus;
import com.task.dogs.repository.DogStatusRepository;
import jakarta.inject.Singleton;

import java.util.List;
import java.util.stream.StreamSupport;

@Singleton
public class DogStatusService {

    private final DogStatusRepository statusRepository;

    public DogStatusService(DogStatusRepository statusRepository) {
        this.statusRepository = statusRepository;
    }

    public List<DogStatus> getAllStatuses() {
        return StreamSupport.stream(statusRepository.findAll().spliterator(), false)
            .toList();
    }
}