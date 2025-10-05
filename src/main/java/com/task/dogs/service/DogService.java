package com.task.dogs.service;

import com.task.dogs.domain.dto.DogDTO;
import com.task.dogs.domain.entity.Dog;
import com.task.dogs.domain.entity.DogStatus;
import com.task.dogs.domain.entity.LeavingReason;
import com.task.dogs.domain.mapper.DogMapper;
import com.task.dogs.exception.ResourceNotFoundException;
import com.task.dogs.repository.DogRepository;
import com.task.dogs.repository.DogStatusRepository;
import com.task.dogs.repository.LeavingReasonRepository;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;

@Singleton
public class DogService {

    private final DogRepository dogRepository;
    private final DogStatusRepository statusRepository;
    private final LeavingReasonRepository reasonRepository;
    private final DogMapper dogMapper;

    public DogService(DogRepository dogRepository,
                      DogStatusRepository statusRepository,
                      LeavingReasonRepository reasonRepository,
                      DogMapper dogMapper) {
        this.dogRepository = dogRepository;
        this.statusRepository = statusRepository;
        this.reasonRepository = reasonRepository;
        this.dogMapper = dogMapper;
    }

    @Transactional
    public DogDTO createDog(DogDTO dogDTO) {
        Dog dog = dogMapper.toEntity(dogDTO);
        
        DogStatus status = statusRepository.findById(dogDTO.getStatusId())
            .orElseThrow(() -> new ResourceNotFoundException("Status not found with id: " + dogDTO.getStatusId()));
        
        LeavingReason leavingReason = null;
        if (dogDTO.getLeavingReasonId() != null) {
            leavingReason = reasonRepository.findById(dogDTO.getLeavingReasonId())
                .orElseThrow(() -> new ResourceNotFoundException("Leaving reason not found with id: " + dogDTO.getLeavingReasonId()));
        }
        
        dog.setStatus(status);
        dog.setLeavingReason(leavingReason);
        dog.setDeleted(false);
        
        Dog savedDog = dogRepository.save(dog);
        return dogMapper.toDTO(savedDog);
    }

    public Page<DogDTO> getAllDogs(Pageable pageable) {
        Page<Dog> dogs = dogRepository.findAllNonDeleted(pageable);
        return dogs.map(dogMapper::toDTO);
    }

    public Page<DogDTO> searchDogs(String name, String breed, String supplier, Pageable pageable) {
        Page<Dog> dogs = dogRepository.findByFilters(name, breed, supplier, pageable);
        return dogs.map(dogMapper::toDTO);
    }

    public DogDTO getDogById(Long id) {
        Dog dog = dogRepository.findByIdNonDeleted(id)
            .orElseThrow(() -> new ResourceNotFoundException("Dog not found with id: " + id));
        return dogMapper.toDTO(dog);
    }

    @Transactional
    public DogDTO updateDog(Long id, DogDTO dogDTO) {
        Dog existingDog = dogRepository.findByIdNonDeleted(id)
            .orElseThrow(() -> new ResourceNotFoundException("Dog not found with id: " + id));
        
        dogMapper.updateEntityFromDTO(dogDTO, existingDog);
        
        DogStatus status = statusRepository.findById(dogDTO.getStatusId())
            .orElseThrow(() -> new ResourceNotFoundException("Status not found with id: " + dogDTO.getStatusId()));
        existingDog.setStatus(status);
        
        if (dogDTO.getLeavingReasonId() != null) {
            LeavingReason leavingReason = reasonRepository.findById(dogDTO.getLeavingReasonId())
                .orElseThrow(() -> new ResourceNotFoundException("Leaving reason not found with id: " + dogDTO.getLeavingReasonId()));
            existingDog.setLeavingReason(leavingReason);
        } else {
            existingDog.setLeavingReason(null);
        }
        
        Dog updatedDog = dogRepository.update(existingDog);
        return dogMapper.toDTO(updatedDog);
    }

    @Transactional
    public void deleteDog(Long id) {
        dogRepository.findByIdNonDeleted(id)
            .orElseThrow(() -> new ResourceNotFoundException("Dog not found with id: " + id));
        dogRepository.softDelete(id);
    }
}