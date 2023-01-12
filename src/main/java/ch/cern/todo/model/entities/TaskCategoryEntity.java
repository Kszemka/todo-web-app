package ch.cern.todo.model.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name="task_categories")
public class TaskCategoryEntity  {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "category_id")
    private Long id;

    @Column(name = "category_name", length=100, unique = true)
    private String name;

    @Column(name = "category_description", length = 500)
    private String description;

    @OneToMany(mappedBy = "taskCategory", cascade = {CascadeType.REMOVE})
    private Set<TaskEntity> tasks = new HashSet<>();
}
