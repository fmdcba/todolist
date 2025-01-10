package com.mindhub.todolist.services;

import com.mindhub.todolist.dtos.NewTaskDTO;
import com.mindhub.todolist.dtos.TaskDTO;
import com.mindhub.todolist.exceptions.InvalidArgumentException;
import com.mindhub.todolist.exceptions.NotFoundException;
import com.mindhub.todolist.models.Task;

public interface TaskService {

    TaskDTO getTaskDTOById(Long id) throws NotFoundException, InvalidArgumentException;

    void createTask(NewTaskDTO task) throws InvalidArgumentException, NotFoundException;

    void updateTask(NewTaskDTO updatedTask, Long id) throws NotFoundException, InvalidArgumentException;

    void deleteTask(Long id) throws InvalidArgumentException;

    Task getTaskById(Long id) throws NotFoundException;

    Task saveTask(Task newTask);

    void checkIfTaskExistsById (Long id) throws NotFoundException;
}
