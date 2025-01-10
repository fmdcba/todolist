package com.mindhub.todolist.services;

import com.mindhub.todolist.dtos.NewTaskDTO;
import com.mindhub.todolist.dtos.TaskDTO;
import com.mindhub.todolist.models.Task;

public interface TaskService {

    TaskDTO getTaskDTOById(Long id);

    void createTask(NewTaskDTO task);

    void updateTask(NewTaskDTO updatedTask, Long id);

    void deleteTask(Long id);

    Task getTaskById(Long id);

    Task saveTask(Task newTask);
}
