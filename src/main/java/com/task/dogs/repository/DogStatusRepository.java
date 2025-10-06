package com.task.dogs.repository;

import com.task.dogs.domain.entity.DogStatus;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;

@JdbcRepository(dialect = Dialect.H2)
public interface DogStatusRepository extends CrudRepository<DogStatus, Long> {
}