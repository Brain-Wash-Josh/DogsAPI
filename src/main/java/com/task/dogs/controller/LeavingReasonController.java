package com.task.dogs.controller;

import com.task.dogs.domain.entity.LeavingReason;
import com.task.dogs.service.LeavingReasonService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;

import java.util.List;

@Controller("/leaving-reasons")
public class LeavingReasonController {

    private final LeavingReasonService reasonService;

    public LeavingReasonController(LeavingReasonService reasonService) {
        this.reasonService = reasonService;
    }

    @Get
    @Produces("application/json")
    public HttpResponse<List<LeavingReason>> getAllReasons() {
        List<LeavingReason> reasons = reasonService.getAllReasons();
        return HttpResponse.ok(reasons);
    }
}