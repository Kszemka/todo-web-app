package ch.cern.todo.dto.task;

import ch.cern.todo.dto.taskCategory.TaskCategoryDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class TaskDto {

    private final Long id;

    private final String name;

    private final String description;

    private final String deadline;


    private final TaskCategoryDto category;
}
