package com.mindhub.todolist.controllers;

import com.mindhub.todolist.dtos.NewTaskDTO;
import com.mindhub.todolist.dtos.TaskDTO;
import com.mindhub.todolist.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/task")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @GetMapping("/{id}")
    public TaskDTO getTask(@PathVariable long id) {
        return taskService.getTaskDTOById(id);
    }

    @PostMapping
    public ResponseEntity<?> createTask(@RequestBody NewTaskDTO newTaskDTO) {
        taskService.createTask(newTaskDTO);
        return new ResponseEntity<>("Task created", HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateTask(@RequestBody NewTaskDTO updatedTask, @PathVariable Long id) {
        taskService.updateTask(updatedTask, id);
        return new ResponseEntity<>("Task updated", HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return new ResponseEntity<>("Task deleted", HttpStatus.OK);
    }

}
