package ch.cern.todo.controllers;

import ch.cern.todo.dto.taskCategory.TaskCategoryDto;
import ch.cern.todo.dto.taskCategory.TaskCategoryRequest;
import ch.cern.todo.exceptions.AlreadyExistsException;
import ch.cern.todo.exceptions.NotEmptyException;
import ch.cern.todo.exceptions.NotFoundException;
import ch.cern.todo.services.TaskCategoryService;
import ch.cern.todo.services.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class TaskCategoryController {
    private final TaskCategoryService taskCategoryService;

    private final TaskService taskService;

    public TaskCategoryController(
            TaskCategoryService taskCategoryService,
            TaskService taskService
    ){
        this.taskCategoryService = taskCategoryService;
        this.taskService = taskService;
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<?> getTaskCategoryById(@PathVariable Long id){
        final Optional<TaskCategoryDto> potentialTaskCategory = taskCategoryService.findCategoryById(id);
        if(potentialTaskCategory.isPresent()){
            return ResponseEntity.ok(potentialTaskCategory.get());
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/category")
    public ResponseEntity<?> getTaskCategoryByName(@RequestParam String name){
        final Optional<TaskCategoryDto> potentialTaskCategory = taskCategoryService.findCategoryByName(name);
        if(potentialTaskCategory.isPresent()){
            return ResponseEntity.ok(TaskCategoryService.includeTasksToTaskCategoryDto(potentialTaskCategory.get(),taskService));
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/category")
    public ResponseEntity<?> createTaskCategory(@Validated @RequestBody TaskCategoryRequest request){
        if(taskCategoryService.findCategoryByName(request.getName()).isPresent()){
            return ResponseEntity.badRequest().body("Category already exists");
        }
        try{
            final TaskCategoryDto taskCategoryDto = taskCategoryService.createCategory(
                    request.getName(),
                    request.getDescription()
            );
            return ResponseEntity.ok(taskCategoryDto);
        }catch (AlreadyExistsException e){
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/category/{id}")
    public ResponseEntity<Boolean> deleteTaskCategory(@PathVariable Long id){
        try{
            return ResponseEntity.ok(taskCategoryService.deleteCategory(id, taskService));
        } catch (NotEmptyException e){
            return ResponseEntity.unprocessableEntity().build();
        }
    }

    @PutMapping("/category/{id}")
    public ResponseEntity<?> updateTaskCategory(@PathVariable Long id, @Validated @RequestBody TaskCategoryRequest request){
        if(taskCategoryService.findCategoryById(id).isEmpty()){
            return ResponseEntity.notFound().build();
        }
        try {
            final TaskCategoryDto taskCategoryDto = taskCategoryService.updateCategory(
                    id,
                    request.getName(),
                    request.getDescription()
            );
            return ResponseEntity.ok(taskCategoryDto);
        } catch (NotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }

}
