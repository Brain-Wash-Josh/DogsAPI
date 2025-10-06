package com.task.dogs.domain.entity;

import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.serde.annotation.Serdeable;

@Serdeable
@MappedEntity("leaving_reason")
public class LeavingReason {
    @Id
    @GeneratedValue(GeneratedValue.Type.AUTO)
    private Long id;

    private String reasonName;

    public LeavingReason() {}

    public LeavingReason(Long id, String reasonName) {
        this.id = id;
        this.reasonName = reasonName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReasonName() {
        return reasonName;
    }

    public void setReasonName(String reasonName) {
        this.reasonName = reasonName;
    }
}
