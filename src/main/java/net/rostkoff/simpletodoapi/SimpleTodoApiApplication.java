package net.rostkoff.simpletodoapi;

import net.rostkoff.simpletodoapi.data.model.Category;
import net.rostkoff.simpletodoapi.data.model.Task;
import net.rostkoff.simpletodoapi.data.repositories.CategoryRepository;
import net.rostkoff.simpletodoapi.data.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SimpleTodoApiApplication {
	public static void main(String[] args) {
		SpringApplication.run(SimpleTodoApiApplication.class, args);
	}
}
