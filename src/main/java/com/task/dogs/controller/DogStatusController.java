package com.task.dogs.controller;

import com.task.dogs.domain.entity.DogStatus;
import com.task.dogs.service.DogStatusService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;

import java.util.List;

@Controller("/statuses")
public class DogStatusController {

    private final DogStatusService statusService;

    public DogStatusController(DogStatusService statusService) {
        this.statusService = statusService;
    }

    @Get
    @Produces("application/json")
    public HttpResponse<List<DogStatus>> getAllStatuses() {
        List<DogStatus> statuses = statusService.getAllStatuses();
        return HttpResponse.ok(statuses);
    }
}