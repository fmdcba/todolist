package com.mindhub.todolist.services.impl;

import com.mindhub.todolist.Mappers.TaskMapper;
import com.mindhub.todolist.dtos.NewTaskDTO;
import com.mindhub.todolist.dtos.TaskDTO;
import com.mindhub.todolist.exceptions.NotFoundException;
import com.mindhub.todolist.exceptions.UnauthorizedAccessException;
import com.mindhub.todolist.models.Task;
import com.mindhub.todolist.models.UserEntity;
import com.mindhub.todolist.repositories.TaskRepository;
import com.mindhub.todolist.repositories.UserRepository;
import com.mindhub.todolist.services.TaskService;
import com.mindhub.todolist.utils.ServiceValidations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    ServiceValidations serviceValidations;

    @Autowired
    TaskMapper taskMapper;

    @Override
    public TaskDTO getTask(String authUserEmail, Long id) throws NotFoundException, UnauthorizedAccessException {
        UserEntity authUser = serviceValidations.validateExistsAuthenticatedUser(authUserEmail);

        serviceValidations.validateExistsId(id);
        serviceValidations.validateIsAuthorized(authUser, id);

        return taskMapper.taskToDTO((findById(id)));
    }

    @Override
    public List<TaskDTO> getAllTasks(String authUserEmail) throws NotFoundException, UnauthorizedAccessException {
        UserEntity authUser = serviceValidations.validateExistsAuthenticatedUser(authUserEmail);

        serviceValidations.validateIsAdmin(authUser);

        return taskMapper.tasksListToDTO(findAll());
    }

    @Override
    public void createTask(NewTaskDTO newTask, String authUserEmail) throws NotFoundException {
        UserEntity authUser = serviceValidations.validateExistsAuthenticatedUser(authUserEmail);

        save(taskMapper.taskToEntity(newTask, authUser));
    }

    @Override
    public void updateTask(NewTaskDTO updatedTask, String authUserEmail, Long id) throws NotFoundException, UnauthorizedAccessException {
        UserEntity authUser = serviceValidations.validateExistsAuthenticatedUser(authUserEmail);

        serviceValidations.validateExistsId(id);
        serviceValidations.validateIsAuthorized(authUser, id);

        Task task = findById(id);

        save(taskMapper.updateTaskToEntity(updatedTask, task));
    }

    @Override
    public void deleteTask(String authUserEmail, Long id) throws NotFoundException, UnauthorizedAccessException {
        UserEntity authUser = serviceValidations.validateExistsAuthenticatedUser(authUserEmail);

        serviceValidations.validateExistsId(id);
        serviceValidations.validateIsAuthorized(authUser, id);

        deleteById(id);
    }

    @Override
    public Task findById(Long id) throws NotFoundException {
        return taskRepository.findById(id).orElseThrow(() -> new NotFoundException("Task not found."));
    }

    @Override
    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    @Override
    public void deleteById(long id) {
        taskRepository.deleteById(id);
    }

    @Override
    public Task save(Task task) {
        return taskRepository.save(task);
    }
}
