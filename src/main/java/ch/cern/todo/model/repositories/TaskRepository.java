package ch.cern.todo.model.repositories;

import ch.cern.todo.model.entities.TaskCategoryEntity;
import ch.cern.todo.model.entities.TaskEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface TaskRepository extends CrudRepository<TaskEntity, Long> {
    Optional<TaskEntity> findByName(String name);
    Optional<TaskEntity> findByTaskCategory(TaskCategoryEntity taskCategoryEntity);
}
