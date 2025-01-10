package com.mindhub.todolist.services.impl;

import com.mindhub.todolist.dtos.NewTaskDTO;
import com.mindhub.todolist.dtos.TaskDTO;
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
    public TaskDTO getTaskDTOById(Long id) throws NotFoundException {
        return new TaskDTO(getTaskById(id));
    }

    @Override
    public void createTask(NewTaskDTO newTask) {
        Task task = new Task(newTask.title(), newTask.description(), newTask.status(), newTask.user());
        saveTask(task);
    }

    @Override
    public void updateTask(NewTaskDTO updatedTask, Long id) throws NotFoundException {
        Task task = getTaskById(id);

        if (updatedTask.title() != null && !updatedTask.title().isBlank()) {
            task.setTitle(updatedTask.title());
        }
        if (updatedTask.description() != null && !updatedTask.description().isBlank()) {
            task.setDescription(updatedTask.description());
        }
        if (updatedTask.status() != null) {
            task.setStatus(updatedTask.status());
        }
        if (updatedTask.user() != null) {
            task.setUser(updatedTask.user());
        }

        saveTask(task);
    }

    @Override
    public void deleteTask(Long id) {
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
}
