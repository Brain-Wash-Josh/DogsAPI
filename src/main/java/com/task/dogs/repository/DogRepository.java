package com.task.dogs.repository;

import com.task.dogs.domain.entity.Dog;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.PageableRepository;

import java.util.List;
import java.util.Optional;

@JdbcRepository(dialect = Dialect.H2)
public interface DogRepository extends PageableRepository<Dog, Long> {

    @Query("SELECT d.* FROM dog d " +
           "JOIN dog_status ds ON d.status_id = ds.id " +
           "LEFT JOIN leaving_reason lr ON d.leaving_reason_id = lr.id " +
           "WHERE d.deleted = false")
    List<Dog> findAllNonDeleted();

    @Query("SELECT d.* FROM dog d " +
           "JOIN dog_status ds ON d.status_id = ds.id " +
           "LEFT JOIN leaving_reason lr ON d.leaving_reason_id = lr.id " +
           "WHERE d.deleted = false")
    Page<Dog> findAllNonDeleted(Pageable pageable);

    @Query("SELECT d.* FROM dog d " +
           "JOIN dog_status ds ON d.status_id = ds.id " +
           "LEFT JOIN leaving_reason lr ON d.leaving_reason_id = lr.id " +
           "WHERE d.id = :id AND d.deleted = false")
    Optional<Dog> findByIdNonDeleted(Long id);

    @Query("SELECT d.* FROM dog d " +
           "JOIN dog_status ds ON d.status_id = ds.id " +
           "LEFT JOIN leaving_reason lr ON d.leaving_reason_id = lr.id " +
           "WHERE d.deleted = false " +
           "AND (:name IS NULL OR LOWER(d.name) LIKE LOWER(CONCAT('%', :name, '%'))) " +
           "AND (:breed IS NULL OR LOWER(d.breed) LIKE LOWER(CONCAT('%', :breed, '%'))) " +
           "AND (:supplier IS NULL OR LOWER(d.supplier) LIKE LOWER(CONCAT('%', :supplier, '%')))")
    Page<Dog> findByFilters(String name, String breed, String supplier, Pageable pageable);

    @Query("UPDATE dog SET deleted = true, updated_at = CURRENT_TIMESTAMP WHERE id = :id")
    void softDelete(Long id);
}