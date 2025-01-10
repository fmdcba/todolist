package com.mindhub.todolist.services.impl;

import com.mindhub.todolist.dtos.NewTaskDTO;
import com.mindhub.todolist.dtos.TaskDTO;
import com.mindhub.todolist.exceptions.NotFoundException;
import com.mindhub.todolist.models.Task;
import com.mindhub.todolist.repositories.TaskRepository;
import com.mindhub.todolist.repositories.UserRepository;
import com.mindhub.todolist.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public TaskDTO getTaskDTOById(Long id) throws NotFoundException {

        return new TaskDTO(getTaskById(id));
    }

    @Override
    public void createTask(NewTaskDTO newTask) throws NotFoundException {
        Task task = new Task(newTask.title(), newTask.description(), newTask.status(), newTask.user());
        checkIfTaskExistsById(newTask.user().getId());
        saveTask(task);
    }

    @Override
    public void updateTask(NewTaskDTO updatedTask, Long id) throws NotFoundException {
        Task task = getTaskById(id);
        //TODO: Fix validation messages for patch, should say something like: "No input data" o change for PUT
        // validateTask(updatedTask);

        saveTask(task);
    }

    @Override
    public void deleteTask(Long id) {
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

    public void checkIfTaskExistsById(Long id) throws NotFoundException {
        if(!userRepository.existsById(id)) {
            throw new NotFoundException("User does not exists");
        }
    }
}
