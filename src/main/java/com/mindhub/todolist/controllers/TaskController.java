package com.mindhub.todolist.controllers;

import com.mindhub.todolist.dtos.NewTaskDTO;
import com.mindhub.todolist.dtos.TaskDTO;
import com.mindhub.todolist.exceptions.InvalidArgumentException;
import com.mindhub.todolist.exceptions.NotFoundException;
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
    public TaskDTO getTask(@PathVariable long id) throws NotFoundException, InvalidArgumentException {
        return taskService.getTaskDTOById(id);
    }

    @PostMapping
    public ResponseEntity<?> createTask(@RequestBody NewTaskDTO newTaskDTO) throws InvalidArgumentException {
        taskService.createTask(newTaskDTO);
        return new ResponseEntity<>("Task created", HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateTask(@RequestBody NewTaskDTO updatedTask, @PathVariable Long id) throws NotFoundException, InvalidArgumentException {
        taskService.updateTask(updatedTask, id);
        return new ResponseEntity<>("Task updated", HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable Long id) throws InvalidArgumentException {
        taskService.deleteTask(id);
        return new ResponseEntity<>("Task deleted", HttpStatus.OK);
    }
}
