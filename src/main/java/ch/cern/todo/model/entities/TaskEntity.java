package ch.cern.todo.model.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "tasks")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "task_id")
    private Long id;

    @Column(name = "task_name", length = 100)
    private String name;

    @Column(name = "task_description", length = 500)
    private String description;

    @Column(name = "deadline")
    private String deadline;

    @ManyToOne
    private TaskCategoryEntity taskCategory;

}
