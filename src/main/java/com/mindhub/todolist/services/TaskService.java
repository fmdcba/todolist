package com.mindhub.todolist.services;

import com.mindhub.todolist.dtos.NewTaskDTO;
import com.mindhub.todolist.dtos.TaskDTO;
import com.mindhub.todolist.dtos.TaskRecordDTO;
import com.mindhub.todolist.exceptions.InvalidArgumentException;
import com.mindhub.todolist.exceptions.NotFoundException;
import com.mindhub.todolist.exceptions.UnauthorizedException;
import com.mindhub.todolist.models.Task;

import java.util.Set;

public interface TaskService extends GenericService {

    TaskDTO getTask(Long id) throws  UnauthorizedException, NotFoundException, InvalidArgumentException;

    void createTask(NewTaskDTO task) throws InvalidArgumentException, NotFoundException;

    void updateTask(NewTaskDTO updatedTask, Long id) throws  UnauthorizedException, NotFoundException, InvalidArgumentException;

    void deleteTask(Long id) throws InvalidArgumentException, UnauthorizedException, NotFoundException;

    Task getTaskById(Long id) throws NotFoundException;

    Task saveTask(Task newTask);

    Set<TaskRecordDTO> getAllTasks();

    void checkIfTaskExistsById (Long id) throws NotFoundException;

    void checkIfUserHasPermissionForTask(Long taskId) throws UnauthorizedException, NotFoundException;
}
