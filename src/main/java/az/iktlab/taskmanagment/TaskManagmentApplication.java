package az.iktlab.taskmanagment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
public class TaskManagmentApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaskManagmentApplication.class, args);
	}

}
