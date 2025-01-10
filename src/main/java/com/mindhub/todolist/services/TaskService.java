package com.mindhub.todolist.services;

import com.mindhub.todolist.dtos.NewTaskDTO;
import com.mindhub.todolist.dtos.TaskDTO;
import com.mindhub.todolist.exceptions.NotFoundException;
import com.mindhub.todolist.models.Task;

public interface TaskService {

    TaskDTO getTaskDTOById(Long id) throws NotFoundException;

    void createTask(NewTaskDTO task);

    void updateTask(NewTaskDTO updatedTask, Long id) throws NotFoundException;

    void deleteTask(Long id);

    Task getTaskById(Long id) throws NotFoundException;

    Task saveTask(Task newTask);
}
