package ch.cern.todo.services;

import ch.cern.todo.dto.task.TaskDto;
import ch.cern.todo.dto.taskCategory.TaskCategoryDto;
import ch.cern.todo.exceptions.AlreadyExistsException;
import ch.cern.todo.exceptions.NotEmptyException;
import ch.cern.todo.exceptions.NotFoundException;
import ch.cern.todo.model.entities.TaskCategoryEntity;
import ch.cern.todo.model.repositories.TaskCategoryRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TaskCategoryService {

    private final TaskCategoryRepository taskCategoryRepository;


    public TaskCategoryService(TaskCategoryRepository taskCategoryRepository) {
        this.taskCategoryRepository = taskCategoryRepository;
    }

    public TaskCategoryDto createCategory(String name, String description) throws AlreadyExistsException{
        if(this.findCategoryByName(name).isPresent()){
            throw new AlreadyExistsException();
        }
        TaskCategoryEntity taskCategoryEntity = TaskCategoryEntity.builder()
                .name(name)
                .description(description)
                .build();
        taskCategoryRepository.save(taskCategoryEntity);
        return mapTaskCategoryEntityToTaskCategoryDto(taskCategoryEntity);
    }

    public TaskCategoryDto updateCategory(Long id, String name, String description) throws NotFoundException {
        if(this.findCategoryById(id).isEmpty()){
            throw new NotFoundException();
        }
        TaskCategoryEntity newTaskCategoryEntity = TaskCategoryEntity.builder()
                .id(id)
                .name(name)
                .description(description)
                .build();
        taskCategoryRepository.save(newTaskCategoryEntity);
        return mapTaskCategoryEntityToTaskCategoryDto(newTaskCategoryEntity);
    }

    public Optional<TaskCategoryDto> findCategoryByName(String name){
        return taskCategoryRepository.findByName(name).map(TaskCategoryService::mapTaskCategoryEntityToTaskCategoryDto);
    }

    public Optional<TaskCategoryDto> findCategoryById(Long id){
        return taskCategoryRepository.findById(id).map(TaskCategoryService::mapTaskCategoryEntityToTaskCategoryDto);
    }

    public boolean deleteCategory(Long id, TaskService taskService) throws NotEmptyException {
        if(!taskCategoryRepository.existsById(id)) {
            return false;
        }
        if(!taskService.findTasksDtoForCategory(this.findCategoryById(id).get().getName()).isEmpty()){
            throw new NotEmptyException();
        }
        taskCategoryRepository.deleteById(id);
        return true;
    }

    public static TaskCategoryDto mapTaskCategoryEntityToTaskCategoryDto(TaskCategoryEntity taskCategoryEntity){
        return TaskCategoryDto.builder()
                .id(taskCategoryEntity.getId())
                .name(taskCategoryEntity.getName())
                .description(taskCategoryEntity.getDescription())
                .build();
    }

    public static TaskCategoryDto includeTasksToTaskCategoryDto(TaskCategoryDto taskCategoryDto, TaskService taskService){
        final Set<TaskDto> tasks = taskService.findTasksDtoForCategory(taskCategoryDto.getName()).stream()
                .collect(Collectors.toSet());
        taskCategoryDto.setTasks(tasks);
        return taskCategoryDto;
    }

}
