package az.iktlab.taskmanagment.service.impl;

import az.iktlab.taskmanagment.converter.TaskResponseConverter;
import az.iktlab.taskmanagment.entity.Category;
import az.iktlab.taskmanagment.entity.Task;
import az.iktlab.taskmanagment.entity.User;
import az.iktlab.taskmanagment.enums.TaskPriority;
import az.iktlab.taskmanagment.enums.TaskStatus;
import az.iktlab.taskmanagment.error.exception.ResourceNotFoundException;
import az.iktlab.taskmanagment.model.request.task.TaskRequest;
import az.iktlab.taskmanagment.model.request.task.TaskUpdateRequest;
import az.iktlab.taskmanagment.model.response.TaskResponse;
import az.iktlab.taskmanagment.repository.CategoryRepository;
import az.iktlab.taskmanagment.repository.TaskRepository;
import az.iktlab.taskmanagment.repository.UserRepository;
import az.iktlab.taskmanagment.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final CategoryRepository categoryRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TaskResponseConverter taskResponseConverter;

    @Override
    public void addTask(TaskRequest taskCreateRequest, Principal principal) {
        Task task = buildTask(taskCreateRequest, principal.getName());
        taskRepository.save(task);
    }

    @Override
    public void updateTask(String id, TaskUpdateRequest taskRequest, Principal principal) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("task not found"));
        Category category = null;
        if (!taskRequest.getCategoryName().isEmpty()) {
            category = categoryRepository.findByName(taskRequest.getCategoryName()).orElseThrow(() -> new ResourceNotFoundException("category not found"));
        } else {
            category = task.getCategory();
        }
        task.setName(taskRequest.getName().isEmpty() ? task.getName() : taskRequest.getName());
        task.setDescription(taskRequest.getDescription().isEmpty() ? task.getDescription() : taskRequest.getDescription());
        task.setCategory(category);
        if (TaskPriority.find(taskRequest.getName()))
            task.setPriority(TaskPriority.find(taskRequest.getName()) ? TaskPriority.getByName(taskRequest.getName()) : task.getPriority());
        task.setStatus(TaskStatus.find(taskRequest.getStatus()) ? TaskStatus.getByName(taskRequest.getStatus()) : task.getStatus());
        taskRepository.save(task);
    }

    @Override
    public void deleteTask(String id) {
        taskRepository.deleteById(id);
    }

    @Override
    public void markAsCompleted(String id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("user not found"));
        task.setIsCompleted(true);
        taskRepository.save(task);
    }

    @Override
    public List<TaskResponse> getAllByCategoryId(String id) {
        List<Task> tasks = taskRepository.findAllByCategoryId(id)
                .orElseThrow(ResourceNotFoundException::new);
        return tasks.stream()
                .map(taskResponseConverter)
                .collect(Collectors.toList());
    }

    private Task buildTask(TaskRequest taskRequest, String userId) {
        Category category = categoryRepository.findByName(taskRequest.getCategoryName())
                .orElseThrow(() -> new ResourceNotFoundException("category not found"));
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("user not found"));
        Task task = Task.builder()
                .priority(taskRequest.getPriority())
                .description(taskRequest.getDescription())
                .name(taskRequest.getName())
                .user(user)
                .isCompleted(false)
                .deadline(taskRequest.getDeadline())
                .status(taskRequest.getStatus())
                .category(category)
                .build();
        return task;
    }


}
