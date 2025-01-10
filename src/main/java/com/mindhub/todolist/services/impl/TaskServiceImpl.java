package com.mindhub.todolist.services.impl;

import com.mindhub.todolist.dtos.NewTaskDTO;
import com.mindhub.todolist.dtos.TaskDTO;
import com.mindhub.todolist.exceptions.InvalidArgumentException;
import com.mindhub.todolist.exceptions.NotFoundException;
import com.mindhub.todolist.models.Task;
import com.mindhub.todolist.repositories.TaskRepository;
import com.mindhub.todolist.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Override
    public TaskDTO getTaskDTOById(Long id) throws NotFoundException, InvalidArgumentException {
        validateId(id);

        return new TaskDTO(getTaskById(id));
    }

    @Override
    public void createTask(NewTaskDTO newTask) throws InvalidArgumentException {
        validateTask(newTask);
        Task task = new Task(newTask.title(), newTask.description(), newTask.status(), newTask.user());

        saveTask(task);
    }

    @Override
    public void updateTask(NewTaskDTO updatedTask, Long id) throws NotFoundException, InvalidArgumentException {
        Task task = getTaskById(id);
        //TODO: Fix validation messages for patch, should say something like: "No input data" o change for PUT
        // validateTask(updatedTask);

        saveTask(task);
    }

    @Override
    public void deleteTask(Long id) throws InvalidArgumentException {
        validateId(id);
        // TODO: Add validations if task does not exist
        taskRepository.deleteById(id);
    }

    @Override
    public Task getTaskById(Long id) throws NotFoundException {
        return taskRepository.findById(id).orElseThrow(() -> new NotFoundException("Task not found"));
    }

    @Override
    public Task saveTask(Task task) {
        return taskRepository.save(task);
    }

    // TODO: Reconsider extract this method DRY! the two services use the same syntax.
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
}
