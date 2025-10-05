package com.task.dogs.controller;

import com.task.dogs.domain.dto.DogDTO;
import io.micronaut.data.model.Page;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DogControllerTest {

    @Inject
    @Client("/")
    HttpClient client;

    @Test
    void testCreateDog() {
        DogDTO dogDTO = createTestDogDTO();

        HttpRequest<DogDTO> request = HttpRequest.POST("/api/dogs/dogs", dogDTO);
        HttpResponse<DogDTO> response = client.toBlocking().exchange(request, DogDTO.class);

        assertEquals(HttpStatus.CREATED, response.getStatus());
        assertNotNull(response.body());
        assertEquals("Rex", response.body().getName());
        assertEquals("German Shepherd", response.body().getBreed());
        assertNotNull(response.body().getId());
    }

    @Test
    void testCreateDogWithInvalidData() {
        DogDTO dogDTO = new DogDTO();
        dogDTO.setName(""); // Invalid: blank name

        HttpRequest<DogDTO> request = HttpRequest.POST("/api/dogs/dogs", dogDTO);
        
        try {
            client.toBlocking().exchange(request, DogDTO.class);
            fail("Should have thrown an exception");
        } catch (Exception e) {
            assertThat(e.getMessage()).contains("Bad Request");
        }
    }

    @Test
    void testGetAllDogs() {
        // Create a dog first
        DogDTO dogDTO = createTestDogDTO();
        client.toBlocking().exchange(HttpRequest.POST("/api/dogs/dogs", dogDTO), DogDTO.class);

        HttpRequest<Object> request = HttpRequest.GET("/api/dogs/dogs");
        HttpResponse<Page> response = client.toBlocking().exchange(request, Page.class);

        assertEquals(HttpStatus.OK, response.getStatus());
        assertNotNull(response.body());
        assertTrue(response.body().getTotalSize() > 0);
    }

    @Test
    void testGetDogById() {
        // Create a dog first
        DogDTO dogDTO = createTestDogDTO();
        HttpResponse<DogDTO> createResponse = client.toBlocking()
            .exchange(HttpRequest.POST("/api/dogs/dogs", dogDTO), DogDTO.class);
        Long dogId = createResponse.body().getId();

        HttpRequest<Object> request = HttpRequest.GET("/api/dogs/dogs/" + dogId);
        HttpResponse<DogDTO> response = client.toBlocking().exchange(request, DogDTO.class);

        assertEquals(HttpStatus.OK, response.getStatus());
        assertNotNull(response.body());
        assertEquals(dogId, response.body().getId());
        assertEquals("Rex", response.body().getName());
    }

    @Test
    void testGetDogByIdNotFound() {
        HttpRequest<Object> request = HttpRequest.GET("/api/dogs/dogs/99999");
        
        try {
            client.toBlocking().exchange(request, DogDTO.class);
            fail("Should have thrown an exception");
        } catch (Exception e) {
            assertThat(e.getMessage()).contains("Not Found");
        }
    }

    @Test
    void testUpdateDog() {
        // Create a dog first
        DogDTO dogDTO = createTestDogDTO();
        HttpResponse<DogDTO> createResponse = client.toBlocking()
            .exchange(HttpRequest.POST("/api/dogs/dogs", dogDTO), DogDTO.class);
        Long dogId = createResponse.body().getId();

        // Update the dog
        DogDTO updateDTO = createTestDogDTO();
        updateDTO.setName("Max");
        updateDTO.setBreed("Labrador");

        HttpRequest<DogDTO> request = HttpRequest.PUT("/api/dogs/dogs/" + dogId, updateDTO);
        HttpResponse<DogDTO> response = client.toBlocking().exchange(request, DogDTO.class);

        assertEquals(HttpStatus.OK, response.getStatus());
        assertNotNull(response.body());
        assertEquals("Max", response.body().getName());
        assertEquals("Labrador", response.body().getBreed());
    }

    @Test
    void testDeleteDog() {
        // Create a dog first
        DogDTO dogDTO = createTestDogDTO();
        HttpResponse<DogDTO> createResponse = client.toBlocking()
            .exchange(HttpRequest.POST("/api/dogs/dogs", dogDTO), DogDTO.class);
        Long dogId = createResponse.body().getId();

        // Delete the dog
        HttpRequest<Object> deleteRequest = HttpRequest.DELETE("/api/dogs/dogs/" + dogId);
        HttpResponse<Void> deleteResponse = client.toBlocking().exchange(deleteRequest, Void.class);

        assertEquals(HttpStatus.NO_CONTENT, deleteResponse.getStatus());

        // Verify the dog is no longer accessible
        HttpRequest<Object> getRequest = HttpRequest.GET("/api/dogs/dogs/" + dogId);
        try {
            client.toBlocking().exchange(getRequest, DogDTO.class);
            fail("Should have thrown an exception");
        } catch (Exception e) {
            assertThat(e.getMessage()).contains("Not Found");
        }
    }

    @Test
    void testSearchDogsByName() {
        // Create dogs with different names
        DogDTO dog1 = createTestDogDTO();
        dog1.setName("Buddy");
        client.toBlocking().exchange(HttpRequest.POST("/api/dogs/dogs", dog1), DogDTO.class);

        DogDTO dog2 = createTestDogDTO();
        dog2.setName("Charlie");
        client.toBlocking().exchange(HttpRequest.POST("/api/dogs/dogs", dog2), DogDTO.class);

        // Search for "Buddy"
        HttpRequest<Object> request = HttpRequest.GET("/api/dogs/dogs?name=Buddy");
        HttpResponse<Page> response = client.toBlocking().exchange(request, Page.class);

        assertEquals(HttpStatus.OK, response.getStatus());
        assertNotNull(response.body());
    }

    @Test
    void testPagination() {
        // Create multiple dogs
        for (int i = 0; i < 5; i++) {
            DogDTO dog = createTestDogDTO();
            dog.setName("Dog" + i);
            client.toBlocking().exchange(HttpRequest.POST("/api/dogs/dogs", dog), DogDTO.class);
        }

        // Test pagination
        HttpRequest<Object> request = HttpRequest.GET("/api/dogs/dogs?page=0&size=2");
        HttpResponse<Page> response = client.toBlocking().exchange(request, Page.class);

        assertEquals(HttpStatus.OK, response.getStatus());
        assertNotNull(response.body());
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
        dogDTO.setStatusId(1L); // "In Training" status
        dogDTO.setKennellingCharacteristic("Friendly, high energy");
        return dogDTO;
    }
}