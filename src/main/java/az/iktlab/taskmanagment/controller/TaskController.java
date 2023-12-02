package az.iktlab.taskmanagment.controller;

import az.iktlab.taskmanagment.model.request.task.TaskRequest;
import az.iktlab.taskmanagment.model.request.task.TaskUpdateRequest;
import az.iktlab.taskmanagment.model.response.TaskResponse;
import az.iktlab.taskmanagment.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;
    @PostMapping
    @Operation(summary = "This endpoint help us to add new task",
            responses = {
                    @ApiResponse(responseCode = "200", description = "The request was successful"),
                    @ApiResponse(responseCode = "400",
                            description = "There is incoming request validation error"),
                    @ApiResponse(responseCode = "409",
                            description = "There is a conflict with the current state of the resource, " +
                                    "preventing the request from being completed."),
                    @ApiResponse(responseCode = "417",
                            description = "The server cannot meet the expectations specified in the request"),
                    @ApiResponse(responseCode = "500", description = "An unexpected error occurred on the server.")
            })
    public void addTask(@RequestBody @Valid TaskRequest task, Principal principal) {
        taskService.addTask(task,principal);
    }
    @PutMapping("{id}")
    public void updateTask(@PathVariable("id") String id, @RequestBody @Valid TaskUpdateRequest taskRequest, Principal principal){
        taskService.updateTask(id,taskRequest,principal);
    }
    @DeleteMapping("{id}")
    public void deleteTask(@PathVariable("id") String id){
        taskService.deleteTask(id);
    }
    @PatchMapping("{id}")
    public void markAsCompletedTask(@PathVariable("id") String id){
        taskService.markAsCompleted(id);
    }
    @GetMapping("/{category-id}")
    public List<TaskResponse> getAllByCategoryId(@PathVariable("category-id") String id){
        return taskService.getAllByCategoryId(id);
    }
}
