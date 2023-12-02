package az.iktlab.taskmanagment.repository;


import az.iktlab.taskmanagment.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task,String> {
    @Query("select c from Task as c where c.category.id = :categoryId")
    Optional<List<Task>> findAllByCategoryId(String categoryId);
}
