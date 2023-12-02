package az.iktlab.taskmanagment.repository;

import az.iktlab.taskmanagment.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role,String> {
    Role findRoleByName(String name);
}
