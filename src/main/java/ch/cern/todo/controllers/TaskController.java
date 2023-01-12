package ch.cern.todo.controllers;

import ch.cern.todo.dto.task.TaskDto;
import ch.cern.todo.dto.task.TaskRequest;
import ch.cern.todo.dto.taskCategory.TaskCategoryDto;
import ch.cern.todo.exceptions.NotFoundException;
import ch.cern.todo.services.TaskCategoryService;
import ch.cern.todo.services.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class TaskController {
    private TaskService taskService;

    public TaskController(
            TaskService taskService
    ){
        this.taskService = taskService;
    }

    @GetMapping("/task/{id}")
    public ResponseEntity<?> getTaskById(@PathVariable Long id){
        final Optional<TaskDto> potentialTask = taskService.findTaskById(id);
        if(potentialTask.isPresent()){
            return ResponseEntity.ok(potentialTask.get());
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/task")
    public ResponseEntity<?> getTaskByName(@RequestParam String name){
        final Optional<TaskDto> potentialTask = taskService.findTaskByName(name);
        if(potentialTask.isPresent()){
            return ResponseEntity.ok(potentialTask.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/task")
    public ResponseEntity<?> createTask(@Validated @RequestBody TaskRequest request){
        try{
            final TaskDto taskDto = taskService.createTask(
                    request.getName(),
                    request.getDescription(),
                    request.getDeadline(),
                    request.getCategoryName()
            );
            return ResponseEntity.ok(taskDto);
        }catch (NotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/task/{id}")
    public ResponseEntity<TaskCategoryDto> deleteTask(@PathVariable Long id){
        try {
            final Optional<TaskCategoryDto> taskCategoryDto = taskService.deleteTask(id);
            return ResponseEntity.ok(
                    TaskCategoryService.includeTasksToTaskCategoryDto(
                            taskCategoryDto.get(),
                            taskService
                    ));
        } catch (NotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/task/{id}")
    public ResponseEntity<?> updateTask(@PathVariable Long id, @Validated @RequestBody TaskRequest request){
        try{
            final TaskDto taskDto = taskService.updateTask(
                    id,
                    request.getName(),
                    request.getDescription(),
                    request.getDeadline(),
                    request.getCategoryName()
            );
            TaskCategoryService.includeTasksToTaskCategoryDto(
                    taskDto.getCategory(),
                    taskService);
            return ResponseEntity.ok(taskDto);

        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
