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
        validateId(id);
        return taskService.getTaskDTOById(id);
    }

    @PostMapping
    public ResponseEntity<?> createTask(@RequestBody NewTaskDTO newTaskDTO) throws InvalidArgumentException {
        taskService.createTask(newTaskDTO);
        validateTask(newTaskDTO);
        return new ResponseEntity<>("Task created", HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateTask(@RequestBody NewTaskDTO updatedTask, @PathVariable Long id) throws NotFoundException, InvalidArgumentException {
        taskService.updateTask(updatedTask, id);
        //TODO: data validation not working on patch, consider change for PUT
        validateId(id);
        //checkEmptyData(updatedTask);
        return new ResponseEntity<>("Task updated", HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable Long id) throws InvalidArgumentException {
        taskService.deleteTask(id);
        validateId(id);
        return new ResponseEntity<>("Task deleted", HttpStatus.OK);
    }

    // TODO: Consider extract this method DRY! the two services use the same syntax.
    public void validateId(Long id) throws InvalidArgumentException {
        if (id == null  || id <= 0) {
            throw new InvalidArgumentException("Invalid ID");
        }
    }

    public void validateTask(NewTaskDTO newTask) throws InvalidArgumentException {
        if (newTask.title() == null || newTask.title().isBlank()) {
            throw new InvalidArgumentException("Task title must not be null or empty");
        }

        if (newTask.description() == null || newTask.description().isBlank()) {
            throw new InvalidArgumentException("Task description must not be null or empty");
        }

        // TODO: Find better validations for this arguments
        if (newTask.status() == null) {
            throw new InvalidArgumentException("Status must not be null or empty");
        }

        if (newTask.user() == null) {
            throw new InvalidArgumentException("User ID must not be null or empty");
        }
    }

    //public void checkEmptyData(NewTaskDTO newTask) throws InvalidArgumentException {
    //    if (newTask.title() == null || newTask.title().isBlank() && newTask.description() == null || newTask.description().isBlank() && newTask.status() == null && newTask.user() == null) {
    //        throw new InvalidArgumentException("Task must not be null or empty");
    //    }
    //}
}
