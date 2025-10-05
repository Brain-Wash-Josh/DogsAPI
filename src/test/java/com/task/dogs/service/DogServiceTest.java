package com.task.dogs.service;

import com.task.dogs.domain.dto.DogDTO;
import com.task.dogs.exception.ResourceNotFoundException;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
class DogServiceTest {

    @Inject
    DogService dogService;

    @Test
    void testCreateDogSuccessfully() {
        DogDTO dogDTO = createTestDogDTO();
        
        DogDTO result = dogService.createDog(dogDTO);

        assertNotNull(result.getId());
        assertEquals("Rex", result.getName());
        assertEquals("German Shepherd", result.getBreed());
        assertEquals("K9 Kennels", result.getSupplier());
        assertEquals("Male", result.getGender());
        assertNotNull(result.getStatusName());
    }

    @Test
    void testCreateDogWithInvalidStatus() {
        DogDTO dogDTO = createTestDogDTO();
        dogDTO.setStatusId(999L); // Non-existent status

        assertThrows(ResourceNotFoundException.class, () -> {
            dogService.createDog(dogDTO);
        });
    }

    @Test
    void testGetAllDogsReturnsNonDeletedOnly() {
        // Create a dog
        DogDTO dogDTO = createTestDogDTO();
        DogDTO created = dogService.createDog(dogDTO);

        // Get all dogs
        Page<DogDTO> dogs = dogService.getAllDogs(Pageable.from(0, 10));

        assertTrue(dogs.getContent().stream()
            .anyMatch(d -> d.getId().equals(created.getId())));

        // Delete the dog
        dogService.deleteDog(created.getId());

        // Get all dogs again - should not include deleted dog
        Page<DogDTO> dogsAfterDelete = dogService.getAllDogs(Pageable.from(0, 10));

        assertFalse(dogsAfterDelete.getContent().stream()
            .anyMatch(d -> d.getId().equals(created.getId())));
    }

    @Test
    void testGetDogByIdSuccess() {
        DogDTO dogDTO = createTestDogDTO();
        DogDTO created = dogService.createDog(dogDTO);

        DogDTO found = dogService.getDogById(created.getId());

        assertEquals(created.getId(), found.getId());
        assertEquals("Rex", found.getName());
    }

    @Test
    void testGetDogByIdNotFound() {
        assertThrows(ResourceNotFoundException.class, () -> {
            dogService.getDogById(99999L);
        });
    }

    @Test
    void testUpdateDogSuccess() {
        // Create a dog
        DogDTO dogDTO = createTestDogDTO();
        DogDTO created = dogService.createDog(dogDTO);

        // Update the dog
        DogDTO updateDTO = createTestDogDTO();
        updateDTO.setName("Max");
        updateDTO.setBreed("Labrador Retriever");
        updateDTO.setStatusId(2L); // Change status

        DogDTO updated = dogService.updateDog(created.getId(), updateDTO);

        assertEquals("Max", updated.getName());
        assertEquals("Labrador Retriever", updated.getBreed());
        assertEquals(2L, updated.getStatusId());
    }

    @Test
    void testUpdateNonExistentDog() {
        DogDTO dogDTO = createTestDogDTO();

        assertThrows(ResourceNotFoundException.class, () -> {
            dogService.updateDog(99999L, dogDTO);
        });
    }

    @Test
    void testDeleteDogSuccess() {
        DogDTO dogDTO = createTestDogDTO();
        DogDTO created = dogService.createDog(dogDTO);

        dogService.deleteDog(created.getId());

        assertThrows(ResourceNotFoundException.class, () -> {
            dogService.getDogById(created.getId());
        });
    }

    @Test
    void testDeleteNonExistentDog() {
        assertThrows(ResourceNotFoundException.class, () -> {
            dogService.deleteDog(99999L);
        });
    }

    @Test
    void testSearchDogsByName() {
        // Create dogs with different names
        DogDTO dog1 = createTestDogDTO();
        dog1.setName("Buddy");
        dogService.createDog(dog1);

        DogDTO dog2 = createTestDogDTO();
        dog2.setName("Charlie");
        dogService.createDog(dog2);

        // Search for "Buddy"
        Page<DogDTO> results = dogService.searchDogs("Buddy", null, null, Pageable.from(0, 10));

        assertTrue(results.getContent().stream()
            .anyMatch(d -> d.getName().equals("Buddy")));
        assertFalse(results.getContent().stream()
            .anyMatch(d -> d.getName().equals("Charlie")));
    }

    @Test
    void testSearchDogsByBreed() {
        DogDTO dog1 = createTestDogDTO();
        dog1.setName("Dog1");
        dog1.setBreed("Beagle");
        dogService.createDog(dog1);

        DogDTO dog2 = createTestDogDTO();
        dog2.setName("Dog2");
        dog2.setBreed("Poodle");
        dogService.createDog(dog2);

        Page<DogDTO> results = dogService.searchDogs(null, "Beagle", null, Pageable.from(0, 10));

        assertTrue(results.getContent().stream()
            .anyMatch(d -> d.getBreed().equals("Beagle")));
    }

    @Test
    void testSearchDogsBySupplier() {
        DogDTO dog1 = createTestDogDTO();
        dog1.setSupplier("ABC Kennels");
        dogService.createDog(dog1);

        DogDTO dog2 = createTestDogDTO();
        dog2.setSupplier("XYZ Breeders");
        dogService.createDog(dog2);

        Page<DogDTO> results = dogService.searchDogs(null, null, "ABC", Pageable.from(0, 10));

        assertTrue(results.getContent().stream()
            .anyMatch(d -> d.getSupplier().contains("ABC")));
    }

    @Test
    void testCreateDogWithLeavingReason() {
        DogDTO dogDTO = createTestDogDTO();
        dogDTO.setStatusId(4L); // "Left" status
        dogDTO.setLeavingDate(LocalDate.now());
        dogDTO.setLeavingReasonId(1L); // "Transferred"

        DogDTO result = dogService.createDog(dogDTO);

        assertNotNull(result.getLeavingReasonId());
        assertNotNull(result.getLeavingReasonName());
    }

    private DogDTO createTestDogDTO() {
        DogDTO dogDTO = new DogDTO();
        dogDTO.setName("Rex");
        dogDTO.setBreed("German Shepherd");
        dogDTO.setSupplier("K9 Kennels");
        dogDTO.setBadgeId("K9-001");
        dogDTO.setGender("Male");
        dogDTO.setBirthDate(LocalDate.of(2020, 5, 15));
        dogDTO.setDateAcquired(LocalDate.of(2021, 1, 10));
        dogDTO.setStatusId(1L);
        dogDTO.setKennellingCharacteristic("Friendly");
        return dogDTO;
    }
}