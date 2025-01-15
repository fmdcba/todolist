package com.mindhub.todolist.services.impl;

import com.mindhub.todolist.dtos.NewTaskDTO;
import com.mindhub.todolist.dtos.TaskDTO;
import com.mindhub.todolist.dtos.TaskRecordDTO;
import com.mindhub.todolist.exceptions.NotFoundException;
import com.mindhub.todolist.exceptions.UnauthorizedException;
import com.mindhub.todolist.models.Task;
import com.mindhub.todolist.models.UserEntity;
import com.mindhub.todolist.repositories.TaskRepository;
import com.mindhub.todolist.repositories.UserRepository;
import com.mindhub.todolist.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public TaskDTO getTaskDTOById(Long id) throws UnauthorizedException, NotFoundException {
        checkIfUserHasPermissionForTask(id);

        return new TaskDTO(getTaskById(id));
    }

    @Override
    public Set<TaskRecordDTO> getAllTasks(){
        List<Task> tasks = taskRepository.findAll();
        Set<TaskRecordDTO>  taskRecords = tasks
                .stream()
                .map(task -> new TaskRecordDTO(task.getId(), task.getTitle(), task.getDescription(), task.getStatus(), task.getUser())).collect(Collectors.toSet());
        return taskRecords;
    }

    @Override
    public void createTask(NewTaskDTO newTask) throws NotFoundException {
        Task task = new Task(newTask.title(), newTask.description(), newTask.status(), newTask.user());
        checkIfTaskExistsById(newTask.user().getId());
        Task savedTask = saveTask(task);
        return new TaskDTO(savedTask);
    }

    @Override
    public void updateTask(NewTaskDTO updatedTask, Long id) throws  UnauthorizedException, NotFoundException {
        Task task = getTaskById(id);
        checkIfUserHasPermissionForTask(id);

        saveTask(task);
    }

    @Override
    public void deleteTask(Long id) throws NotFoundException, UnauthorizedException {
        getTaskById(id);
        checkIfUserHasPermissionForTask(id);
        taskRepository.deleteById(id);
    }

    //Validations

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

    public void checkIfUserHasPermissionForTask(Long taskId) throws UnauthorizedException, NotFoundException {
        Task task = getTaskById(taskId);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();

        UserEntity currentUser = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new NotFoundException("Authenticated user not found"));

        boolean isAdmin = currentUser.getRole() != null && currentUser.getRole().toString().equals("ADMIN");

        if (!task.getUser().getId().equals(currentUser.getId()) && !isAdmin) {
            throw new UnauthorizedException("Unauthorized");
        }
    }
}
