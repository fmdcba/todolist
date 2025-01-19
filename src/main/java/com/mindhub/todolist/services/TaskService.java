package com.mindhub.todolist.services;

import com.mindhub.todolist.dtos.NewTaskDTO;
import com.mindhub.todolist.dtos.TaskDTO;
import com.mindhub.todolist.exceptions.InvalidArgumentException;
import com.mindhub.todolist.exceptions.NotFoundException;
import com.mindhub.todolist.exceptions.UnauthorizedAccessException;
import com.mindhub.todolist.models.Task;

import java.util.List;

public interface TaskService extends GenericService<Task> {

    TaskDTO getTask(String authUserEmail, Long id) throws  NotFoundException, InvalidArgumentException, UnauthorizedAccessException;

    List<TaskDTO> getAllTasks(String authUserEmail) throws NotFoundException, UnauthorizedAccessException;

    void createTask(NewTaskDTO task, String authUserEmail) throws InvalidArgumentException, NotFoundException;

    void updateTask(NewTaskDTO updatedTask, String authUserEmail, Long id) throws  NotFoundException, InvalidArgumentException, UnauthorizedAccessException;

    void deleteTask(String authUserEmail, Long id) throws InvalidArgumentException, NotFoundException, UnauthorizedAccessException;
}
