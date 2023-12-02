package az.iktlab.taskmanagment.service;

import az.iktlab.taskmanagment.model.request.task.TaskRequest;
import az.iktlab.taskmanagment.model.request.task.TaskUpdateRequest;
import az.iktlab.taskmanagment.model.response.TaskResponse;

import java.security.Principal;
import java.util.List;

public interface TaskService {
    void addTask(TaskRequest taskCreateRequest, Principal principal);
    void updateTask(String id, TaskUpdateRequest taskRequest, Principal principal);
    void deleteTask(String id);

    void markAsCompleted(String id);

    List<TaskResponse> getAllByCategoryId(String id);
}
