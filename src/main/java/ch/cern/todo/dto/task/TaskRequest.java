package ch.cern.todo.dto.task;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@Builder
public class TaskRequest {

    @NotBlank
    private final String name;

    private final String description;

    @NotBlank
    private final String deadline;

    @NotBlank
    private final String categoryName;
}
