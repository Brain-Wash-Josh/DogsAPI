package com.task.dogs.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.micronaut.serde.annotation.Serdeable;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Serdeable
public class DogDTO {

    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Breed is required")
    private String breed;

    @NotBlank(message = "Supplier is required")
    private String supplier;

    private String badgeId;

    @NotBlank(message = "Gender is required")
    private String gender;

    @NotNull(message = "Birth date is required")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    @NotNull(message = "Date acquired is required")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateAcquired;

    @NotNull(message = "Status is required")
    private Long statusId;
    
    private String statusName;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate leavingDate;

    private Long leavingReasonId;
    
    private String leavingReasonName;

    private String kennellingCharacteristic;

    public DogDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getBadgeId() {
        return badgeId;
    }

    public void setBadgeId(String badgeId) {
        this.badgeId = badgeId;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public LocalDate getDateAcquired() {
        return dateAcquired;
    }

    public void setDateAcquired(LocalDate dateAcquired) {
        this.dateAcquired = dateAcquired;
    }

    public Long getStatusId() {
        return statusId;
    }

    public void setStatusId(Long statusId) {
        this.statusId = statusId;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public LocalDate getLeavingDate() {
        return leavingDate;
    }

    public void setLeavingDate(LocalDate leavingDate) {
        this.leavingDate = leavingDate;
    }

    public Long getLeavingReasonId() {
        return leavingReasonId;
    }

    public void setLeavingReasonId(Long leavingReasonId) {
        this.leavingReasonId = leavingReasonId;
    }

    public String getLeavingReasonName() {
        return leavingReasonName;
    }

    public void setLeavingReasonName(String leavingReasonName) {
        this.leavingReasonName = leavingReasonName;
    }

    public String getKennellingCharacteristic() {
        return kennellingCharacteristic;
    }

    public void setKennellingCharacteristic(String kennellingCharacteristic) {
        this.kennellingCharacteristic = kennellingCharacteristic;
    }
}
