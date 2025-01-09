package com.mindhub.todolist;

import com.mindhub.todolist.models.Task;
import com.mindhub.todolist.models.UserEntity;
import com.mindhub.todolist.repositories.TaskRepository;
import com.mindhub.todolist.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TodolistApplication {

	public static void main(String[] args) {
		SpringApplication.run(TodolistApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(UserRepository userRepository, TaskRepository taskRepository) {

		return args -> {
			UserEntity user1 = new UserEntity("pepe", "pepe123", "pepe@pepe.com");
			userRepository.save(user1);
			Task task1 = new Task("Ir al gym", "Hacer rutina de piernas", Task.TaskStatus.PENDING, user1);
			taskRepository.save(task1);
			Task task2 = new Task("Desayunar", "Huevos revueltos", Task.TaskStatus.COMPLETED, user1);
			taskRepository.save(task2);
		};
	}
}
