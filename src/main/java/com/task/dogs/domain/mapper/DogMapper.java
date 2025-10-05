package com.task.dogs.domain.mapper;

import com.task.dogs.domain.dto.DogDTO;
import com.task.dogs.domain.entity.Dog;
import com.task.dogs.domain.entity.DogStatus;
import com.task.dogs.domain.entity.LeavingReason;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper
public interface  DogMapper{
    @Mapping(target = "statusId", source = "status.id")
    @Mapping(target = "statusName", source = "status.statusName")
    @Mapping(target = "leavingReasonId", source = "leavingReason.id")
    @Mapping(target = "leavingReasonName", source = "leavingReason.reasonName")
    DogDTO toDTO(Dog dog);

    List<DogDTO> toDTOList(List<Dog> dogs);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "leavingReason", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Dog toEntity(DogDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "leavingReason", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDTO(DogDTO dto, @MappingTarget Dog dog);

    default Dog setRelations(Dog dog, DogStatus status, LeavingReason leavingReason) {
        dog.setStatus(status);
        dog.setLeavingReason(leavingReason);
        return dog;
    }
}
