package ch.cern.todo.services;

import ch.cern.todo.dto.task.TaskDto;
import ch.cern.todo.dto.taskCategory.TaskCategoryDto;
import ch.cern.todo.exceptions.NotFoundException;
import ch.cern.todo.model.entities.TaskCategoryEntity;
import ch.cern.todo.model.entities.TaskEntity;
import ch.cern.todo.model.repositories.TaskCategoryRepository;
import ch.cern.todo.model.repositories.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final TaskCategoryRepository taskCategoryRepository;

    public TaskService(TaskRepository taskRepository, TaskCategoryRepository taskCategoryRepository) {
        this.taskRepository = taskRepository;
        this.taskCategoryRepository = taskCategoryRepository;
    }

    public TaskDto createTask(String name, String description, String deadline, String categoryName) throws NotFoundException {
        Optional<TaskCategoryEntity> taskCategoryEntity = taskCategoryRepository.findByName(categoryName);
        if (taskCategoryEntity.isEmpty()){
            throw new NotFoundException();
        }
        TaskEntity taskEntity = TaskEntity.builder()
                .name(name)
                .description(description)
                .deadline(deadline)
                .taskCategory(taskCategoryEntity.get())
                .build();
        taskRepository.save(taskEntity);
        return mapTaskEntityToTaskDto(taskEntity);
    }

    public TaskDto updateTask(Long id, String name, String description, String deadline, String categoryName) throws NotFoundException {
        Optional<TaskCategoryEntity> taskCategoryEntity = taskCategoryRepository.findByName(categoryName);
        if (taskCategoryEntity.isEmpty()){
            throw new NotFoundException();
        }
        TaskEntity taskEntity = TaskEntity.builder()
                .id(id)
                .name(name)
                .description(description)
                .deadline(deadline)
                .taskCategory(taskCategoryEntity.get())
                .build();
        taskRepository.save(taskEntity);
        return mapTaskEntityToTaskDto(taskEntity);
    }

    public Optional<TaskDto> findTaskByName(String name){
        return taskRepository.findByName(name).map(TaskService::mapTaskEntityToTaskDto);
    }

    public Optional<TaskDto> findTaskById(Long id){
        return taskRepository.findById(id).map(TaskService::mapTaskEntityToTaskDto);
    }

    public Set<TaskDto> findTasksDtoForCategory(String categoryName){
        return taskRepository.findByTaskCategory(taskCategoryRepository.findByName(categoryName).get()).stream()
                .map(TaskService::mapTaskEntityToTaskDto)
                .collect(Collectors.toSet());
    }


    public Optional<TaskCategoryDto> deleteTask(Long id) throws NotFoundException {
        final Optional<TaskEntity> task = taskRepository.findById(id);
        if (task.isEmpty()){
            throw new NotFoundException();
        }
        taskRepository.delete(task.get());
        return taskCategoryRepository.findById(task.get().getTaskCategory().getId())
                .map(TaskCategoryService::mapTaskCategoryEntityToTaskCategoryDto);
    }

    public static TaskDto mapTaskEntityToTaskDto(TaskEntity taskEntity){
        TaskCategoryDto taskCategoryDto = TaskCategoryService.mapTaskCategoryEntityToTaskCategoryDto(taskEntity.getTaskCategory());
        return TaskDto.builder()
                .id(taskEntity.getId())
                .name(taskEntity.getName())
                .description(taskEntity.getDescription())
                .deadline(taskEntity.getDeadline())
                .category(taskCategoryDto)
                .build();
    }
}
