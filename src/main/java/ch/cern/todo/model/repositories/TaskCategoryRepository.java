package ch.cern.todo.model.repositories;

import ch.cern.todo.model.entities.TaskCategoryEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface TaskCategoryRepository extends CrudRepository<TaskCategoryEntity, Long> {
    Optional<TaskCategoryEntity> findByName(String name);

}
