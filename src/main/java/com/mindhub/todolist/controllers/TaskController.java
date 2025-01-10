package com.mindhub.todolist.controllers;

import com.mindhub.todolist.dtos.NewTaskDTO;
import com.mindhub.todolist.dtos.TaskDTO;
import com.mindhub.todolist.models.Task;
import com.mindhub.todolist.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/task")
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    @GetMapping("/{id}")
    public TaskDTO getTask() {
        return new TaskDTO(taskRepository.findById(1L).orElse(null));
    }

    @PostMapping
    public ResponseEntity<?> createTask(@RequestBody NewTaskDTO newTaskDTO) {
        Task newTask = new Task(newTaskDTO.title(), newTaskDTO.description(), newTaskDTO.status(), newTaskDTO.user());
        taskRepository.save(newTask);
        return new ResponseEntity<>("Task created", HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateTask(@RequestBody NewTaskDTO updatedTask, @PathVariable Long id) {
        Task task = taskRepository.findById(id).orElse(null);

        if (updatedTask.title() != null && !updatedTask.title().isBlank()) {
            task.setTitle(updatedTask.title());
        }
        if (updatedTask.description() != null && !updatedTask.description().isBlank()) {
            task.setDescription(updatedTask.description());
        }
        if (updatedTask.status() != null) {
            task.setStatus(updatedTask.status());
        }
        if (updatedTask.user() != null) {
            task.setUser(updatedTask.user());
        }

        taskRepository.save(task);
        return new ResponseEntity<>("Task updated", HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable Long id) {
        taskRepository.deleteById(id);
        return new ResponseEntity<>("Task deleted", HttpStatus.OK);
    }

}
