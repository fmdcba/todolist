package com.mindhub.todolist;

import com.mindhub.todolist.models.RoleType;
import com.mindhub.todolist.models.Task;
import com.mindhub.todolist.models.UserEntity;
import com.mindhub.todolist.repositories.TaskRepository;
import com.mindhub.todolist.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class TodolistApplication {

	public static void main(String[] args) {
		SpringApplication.run(TodolistApplication.class, args);
	}

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Bean
	public CommandLineRunner initData(UserRepository userRepository, TaskRepository taskRepository) {

		return args -> {
			UserEntity user = new UserEntity("pepe", passwordEncoder.encode("pepe123"), "pepe@pepe.com", RoleType.USER);
			UserEntity user1 = new UserEntity("fulano", passwordEncoder.encode("fulano123"), "fulano@fulano.com", RoleType.USER);
			UserEntity user2 = new UserEntity("juan", passwordEncoder.encode("juan123"), "juan@juan.com", RoleType.USER);
			UserEntity user3 = new UserEntity("admin", passwordEncoder.encode("admin123"), "admin@admin.com", RoleType.ADMIN);
			userRepository.save(user);
			userRepository.save(user1);
			userRepository.save(user2);
			userRepository.save(user3);
			Task task1 = new Task("Ir al gym", "Hacer rutina de piernas", Task.TaskStatus.PENDING, user);
			Task task2 = new Task("Desayunar", "Huevos revueltos", Task.TaskStatus.COMPLETED, user1);
			taskRepository.save(task1);
			taskRepository.save(task2);
		};
	}
}
