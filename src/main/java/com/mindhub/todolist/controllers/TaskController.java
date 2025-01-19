package com.mindhub.todolist.controllers;

import com.mindhub.todolist.dtos.NewTaskDTO;
import com.mindhub.todolist.dtos.TaskDTO;
import com.mindhub.todolist.exceptions.InvalidArgumentException;
import com.mindhub.todolist.exceptions.NotFoundException;
import com.mindhub.todolist.exceptions.UnauthorizedAccessException;
import com.mindhub.todolist.models.Task;
import com.mindhub.todolist.services.TaskService;
import com.mindhub.todolist.utils.ControllerValidations;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    ControllerValidations controllerValidations;

    @GetMapping("/{id}")
    @Operation(summary = "Get a task", description = "Return a task and it's attributes")
        @ApiResponse(responseCode = "200", description = "Return the task with a status code of OK")
        @ApiResponse(responseCode = "400", description = "Error msg when trying to get with non existent or invalid ID")
    public TaskDTO getTask(@PathVariable long id) throws NotFoundException, InvalidArgumentException, UnauthorizedAccessException {
        controllerValidations.validateId(id);
        String authUserEmail = controllerValidations.getAuthUserEmail();

        return taskService.getTask(authUserEmail, id);
    }

    @GetMapping
    @Operation(summary = "Get all tasks", description = "Retrieves a list of all tasks, posts it and return a confirmation message")
    @ApiResponse(responseCode = "201", description = "confirmation msg on body: Task created")
    @ApiResponse(responseCode = "400", description = "Point a required missing part of the data. E.g: Task title must not be null or empty or user does not exists")
    public ResponseEntity<?> getAllTasks() throws NotFoundException, UnauthorizedAccessException {
        String authUserEmail = controllerValidations.getAuthUserEmail();

        List<TaskDTO> tasks = taskService.getAllTasks(authUserEmail);
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @PostMapping
    @Operation(summary = "Create a task", description = "Recieves a task, posts it and return a confirmation message")
        @ApiResponse(responseCode = "201", description = "confirmation msg on body: Task created")
        @ApiResponse(responseCode = "400", description = "Point a required missing part of the data. E.g: Task title must not be null or empty or user does not exists")
    public ResponseEntity<?> createTask(@RequestBody NewTaskDTO newTaskDTO) throws InvalidArgumentException, NotFoundException {
        validateEntries(newTaskDTO);
        String authUserEmail = controllerValidations.getAuthUserEmail();

        taskService.createTask(newTaskDTO, authUserEmail);
        return new ResponseEntity<>("Task created successfully.", HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Edit a Task", description = "Edit a task or any of it's field")
        @ApiResponse(responseCode = "200", description = "confirmation msg on body: Task updated")
        @ApiResponse(responseCode = "404", description = "When trying to patch non existent task. Confirmation msg on body: Task not found")
    public ResponseEntity<?> updateTask(@RequestBody NewTaskDTO updatedTask, @PathVariable Long id) throws NotFoundException, InvalidArgumentException, UnauthorizedAccessException {
        controllerValidations.validateId(id);
        validateEntries(updatedTask);
        String authUserEmail = controllerValidations.getAuthUserEmail();

        taskService.updateTask(updatedTask, authUserEmail, id);
        return new ResponseEntity<>("Task updated successfully.", HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a task", description = "Deletes a task")
        @ApiResponse(responseCode = "200", description = "confirmation msg on body: Task deleted")
        @ApiResponse(responseCode = "400", description = "When trying to delete a task with invalid ID. Confimations msg on body: invalid ID")
    public ResponseEntity<?> deleteTask(@PathVariable Long id) throws InvalidArgumentException, NotFoundException, UnauthorizedAccessException {
        controllerValidations.validateId(id);
        String authUserEmail = controllerValidations.getAuthUserEmail();

        taskService.deleteTask(authUserEmail, id);
        return new ResponseEntity<>("Task deleted successfully", HttpStatus.OK);
    }

    public void validateTitle(String title)  throws InvalidArgumentException {
        if (title == null || title.isBlank()) {
            throw new InvalidArgumentException("Task title must not be null or empty");
        }
    }

    public void validateDescription(String description)  throws InvalidArgumentException {
        if (description == null || description.isBlank()) {
            throw new InvalidArgumentException("Task description must not be null or empty");
        }
    }

    public void validateStatus(Task.TaskStatus status)  throws InvalidArgumentException {
        if (status == null) {
            throw new InvalidArgumentException("Task must status has to be PENDING, IN_PROGRESS or COMPLETED not null or empty ");
        }
    }

    public void validateEntries(NewTaskDTO newTask) throws InvalidArgumentException {
        validateTitle(newTask.title());
        validateDescription(newTask.description());
        validateStatus(newTask.status());
    }
}
