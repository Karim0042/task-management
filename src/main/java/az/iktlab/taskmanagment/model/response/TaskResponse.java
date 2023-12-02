package az.iktlab.taskmanagment.model.response;

import az.iktlab.taskmanagment.enums.TaskPriority;
import az.iktlab.taskmanagment.enums.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskResponse {
    @Schema(description = "Task Name", example = "Add new task")
    private String name;

    private String description;
    private String categoryName;
    private TaskPriority priority;
    private LocalDateTime deadline;
    private TaskStatus status;
}
