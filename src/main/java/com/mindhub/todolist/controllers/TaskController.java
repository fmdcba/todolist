package com.mindhub.todolist.controllers;

import com.mindhub.todolist.dtos.NewTaskDTO;
import com.mindhub.todolist.dtos.TaskDTO;
import com.mindhub.todolist.exceptions.InvalidArgumentException;
import com.mindhub.todolist.exceptions.NotFoundException;
import com.mindhub.todolist.exceptions.UnauthorizedException;
import com.mindhub.todolist.services.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
    @Operation(summary = "Get a task", description = "Return a task and it's attributes")
        @ApiResponse(responseCode = "200", description = "Return the task with a status code of OK")
        @ApiResponse(responseCode = "400", description = "Error msg when trying to get with non existent or invalid ID")
    public TaskDTO getTask(@PathVariable long id) throws NotFoundException, InvalidArgumentException, UnauthorizedException {
        validateId(id);
        return taskService.getTaskDTOById(id);
    }

    @PostMapping
    @Operation(summary = "Create a task", description = "Recieves a task, posts it and return a confirmation message")
        @ApiResponse(responseCode = "201", description = "confirmation msg on body: Task created")
        @ApiResponse(responseCode = "400", description = "Point a required missing part of the data. E.g: Task title must not be null or empty or user does not exists")
    public ResponseEntity<?> createTask(@RequestBody NewTaskDTO newTaskDTO) throws InvalidArgumentException, NotFoundException {
        validateTask(newTaskDTO);
        taskService.createTask(newTaskDTO);
        return new ResponseEntity<>("Task created", HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Edit a Task", description = "Edit a task or any of it's field")
        @ApiResponse(responseCode = "200", description = "confirmation msg on body: Task updated")
        @ApiResponse(responseCode = "404", description = "When trying to patch non existent task. Confirmation msg on body: Task not found")
    public ResponseEntity<?> updateTask(@RequestBody NewTaskDTO updatedTask, @PathVariable Long id) throws NotFoundException, InvalidArgumentException, UnauthorizedException {
        validateId(id);
        checkEmptyData(updatedTask);
        taskService.updateTask(updatedTask, id);

        return new ResponseEntity<>("Task updated", HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a task", description = "Deletes a task")
        @ApiResponse(responseCode = "200", description = "confirmation msg on body: Task deleted")
        @ApiResponse(responseCode = "400", description = "When trying to delete a task with invalid ID. Confimations msg on body: invalid ID")
    public ResponseEntity<?> deleteTask(@PathVariable Long id) throws InvalidArgumentException, UnauthorizedException, NotFoundException {
        validateId(id);
        taskService.deleteTask(id);
        return new ResponseEntity<>("Task deleted", HttpStatus.OK);
    }

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

        if (newTask.status() == null) {
            throw new InvalidArgumentException("Status must not be null or empty");
        }

        if (newTask.user() == null) {
            throw new InvalidArgumentException("User ID must not be null or empty");
        }
    }

    public void checkEmptyData(NewTaskDTO newTask) throws InvalidArgumentException {
        if (newTask.title() == null && newTask.description() == null && newTask.status() == null && newTask.user() == null) {
            throw new InvalidArgumentException("When editing task must not be null or empty");
        }
    }
}
