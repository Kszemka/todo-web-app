package ch.cern.todo.dto.taskCategory;

import ch.cern.todo.dto.task.TaskDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
@Builder
public class TaskCategoryDto {

    private Long id;

    private String name;
    private String description;

    private Set<TaskDto> tasks;
}
