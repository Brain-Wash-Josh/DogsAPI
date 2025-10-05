package com.task.dogs.controller;

import com.task.dogs.domain.dto.DogDTO;
import com.task.dogs.service.DogService;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;

import jakarta.validation.Valid;
import java.util.Optional;

@Controller("/dogs")
public class DogController {

    private final DogService dogService;

    public DogController(DogService dogService) {
        this.dogService = dogService;
    }

    @Post
    @Consumes("application/json")
    @Produces("application/json")
    public HttpResponse<DogDTO> createDog(@Valid @Body DogDTO dogDTO) {
        DogDTO createdDog = dogService.createDog(dogDTO);
        return HttpResponse.created(createdDog);
    }

    @Get
    @Produces("application/json")
    public HttpResponse<Page<DogDTO>> getAllDogs(
            @QueryValue Optional<String> name,
            @QueryValue Optional<String> breed,
            @QueryValue Optional<String> supplier,
            Pageable pageable) {
        
        Page<DogDTO> dogs;
        
        if (name.isPresent() || breed.isPresent() || supplier.isPresent()) {
            dogs = dogService.searchDogs(
                name.orElse(null),
                breed.orElse(null),
                supplier.orElse(null),
                pageable
            );
        } else {
            dogs = dogService.getAllDogs(pageable);
        }
        
        return HttpResponse.ok(dogs);
    }

    @Get("/{id}")
    @Produces("application/json")
    public HttpResponse<DogDTO> getDogById(@PathVariable Long id) {
        DogDTO dog = dogService.getDogById(id);
        return HttpResponse.ok(dog);
    }

    @Put("/{id}")
    @Consumes("application/json")
    @Produces("application/json")
    public HttpResponse<DogDTO> updateDog(@PathVariable Long id, @Valid @Body DogDTO dogDTO) {
        DogDTO updatedDog = dogService.updateDog(id, dogDTO);
        return HttpResponse.ok(updatedDog);
    }

    @Delete("/{id}")
    public HttpResponse<Void> deleteDog(@PathVariable Long id) {
        dogService.deleteDog(id);
        return HttpResponse.noContent();
    }
}