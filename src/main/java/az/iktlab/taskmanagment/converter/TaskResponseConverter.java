package az.iktlab.taskmanagment.converter;

import az.iktlab.taskmanagment.entity.Task;
import az.iktlab.taskmanagment.model.response.TaskResponse;
import org.springframework.stereotype.Component;
import java.util.function.Function;
@Component
public class TaskResponseConverter implements Function<Task, TaskResponse> {
    @Override
    public TaskResponse apply(Task task) {
        TaskResponse taskResponse = TaskResponse.builder()
                .id(task.getId())
                .categoryName(task.getCategory().getName())
                .deadline(task.getDeadline())
                .description(task.getDescription())
                .status(task.getStatus())
                .priority(task.getPriority())
                .name(task.getName())
                .build();
        return taskResponse;

    }
}
