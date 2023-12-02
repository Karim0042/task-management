package az.iktlab.taskmanagment

import az.iktlab.taskmanagment.converter.TaskResponseConverter
import az.iktlab.taskmanagment.entity.Category
import az.iktlab.taskmanagment.entity.Task
import az.iktlab.taskmanagment.entity.User
import az.iktlab.taskmanagment.model.request.task.TaskRequest
import az.iktlab.taskmanagment.model.request.task.TaskUpdateRequest
import az.iktlab.taskmanagment.model.response.TaskResponse
import az.iktlab.taskmanagment.repository.CategoryRepository
import az.iktlab.taskmanagment.repository.TaskRepository
import az.iktlab.taskmanagment.repository.UserRepository
import az.iktlab.taskmanagment.service.impl.TaskServiceImpl
import org.mockito.Mock
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

import java.security.Principal

class TaskServiceTest extends Specification{
    @Subject
    TaskServiceImpl taskService

    @Mock
    CategoryRepository categoryRepository

    @Mock
    TaskRepository taskRepository

    @Mock
    UserRepository userRepository

    @Mock
    TaskResponseConverter taskResponseConverter

    def "addTask"() {
        given:
        TaskRequest taskRequest = new TaskRequest(
                name: "Task1",
                description: "test",
                priority: TaskPriority.HIGH,
                deadline: LocalDateTime.now().plusDays(7),
                status: TaskStatus.IN_PROGRESS,
                categoryName: "test"
        )
        Principal principal = Mock(Principal)

        when:
        taskService.addTask(taskRequest, principal)

        then:
        1 * taskRepository.save(_ as Task)
    }

    @Unroll
    def "updateTask"() {
        given:
        String taskId = "1"
        TaskUpdateRequest taskRequest = new TaskUpdateRequest(
                name: "Update",
                description: "des",
                priority: TaskPriority.LOW,
                status: TaskStatus.COMPLETED,
                categoryName: "Update"
        )
        Principal principal = Mock(Principal)

        when:
        taskService.updateTask(taskId, taskRequest, principal)

        then:
        1 * taskRepository.findById(taskId) >> Optional.of(new Task(
                id: taskId,
                name: "Task1",
                description: "test",
                priority: TaskPriority.MEDIUM,
                deadline: LocalDateTime.now().plusDays(3),
                status: TaskStatus.IN_PROGRESS,
                createdAt: LocalDateTime.now(),
                isCompleted: false,
                updatedAt: null,
                category: new Category(id: "1", name: "test"),
                user: new User(id: "user1", username: "test", password: "123456")
        ))
        1 * taskRepository.save(_ as Task)
    }

    def "deleteTask"() {
        given:
        String taskId = "1"

        when:
        taskService.deleteTask(taskId)

        then:
        1 * taskRepository.deleteById(taskId)
    }

    def "markAsCompleted"() {
        given:
        String taskId = "1"

        when:
        taskService.markAsCompleted(taskId)

        then:
        1 * taskRepository.findById(taskId) >> Optional.of(new Task(
                id: taskId,
                name: "test",
                description: "Task description",
                priority: TaskPriority.MEDIUM,
                deadline: LocalDateTime.now().plusDays(5),
                status: TaskStatus.IN_PROGRESS,
                createdAt: LocalDateTime.now(),
                isCompleted: false,
                updatedAt: null,
                category: new Category(id: "1", name: "test"),
                user: new User(id: "user1", username: "test", password: "123456")
        ))
        1 * taskRepository.save(_ as Task)
    }

    def "getAllByCategoryId"() {
        given:
        String categoryId = "1"
        List<Task> tasks = [
                new Task(
                        id: "1",
                        name: "Test",
                        description: "test",
                        priority: TaskPriority.HIGH,
                        deadline: LocalDateTime.now().plusDays(7),
                        status: TaskStatus.IN_PROGRESS,
                        createdAt: LocalDateTime.now(),
                        isCompleted: false,
                        updatedAt: null,
                        category: new Category(id: "1", name: "Test"),
                        user: new User(id: "user1", username: "Test", password: "12335545")
                ),
                new Task(
                        id: "2",
                        name: "test",
                        description: "test",
                        priority: TaskPriority.LOW,
                        deadline: LocalDateTime.now().plusDays(3),
                        status: TaskStatus.COMPLETED,
                        createdAt: LocalDateTime.now(),
                        isCompleted: true,
                        updatedAt: null,
                        category: new Category(id: "1", name: "test"),
                        user: new User(id: "user1", username: "test", password: "123456")
                )
        ]
        taskRepository.findAllByCategoryId(categoryId) >> Optional.of(tasks)
        taskResponseConverter.apply(_) >>  initialize TaskResponse instances
        when :
                  List<TaskResponse> result = taskService.getAllByCategoryId(categoryId)

        then:
        result.size() == tasks.size()
        1 * taskResponseConverter.apply(tasks[0])
        1 * taskResponseConverter.apply(tasks[1])
    }

}
