package com.mindhub.todolist.Mappers;

import com.mindhub.todolist.dtos.NewTaskDTO;
import com.mindhub.todolist.dtos.TaskDTO;
import com.mindhub.todolist.models.Task;
import com.mindhub.todolist.models.UserEntity;
import com.mindhub.todolist.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TaskMapper {

    @Autowired
    private TaskRepository taskRepository;

    public Task taskToEntity (NewTaskDTO task, UserEntity user) {
        Task taskEntity = new Task(task.title(), task.description(), task.status(), user);

        return taskEntity;
    }

    public Task updateTaskToEntity(NewTaskDTO updatedTask, Task task) {
        task.setTitle(updatedTask.title());
        task.setDescription(updatedTask.description());
        task.setStatus(updatedTask.status());

        return task;
    }

    public TaskDTO taskToDTO (Task task) {
        return new TaskDTO(task);
    }

    public List<TaskDTO> tasksListToDTO (List<Task> tasksList) {
        return tasksList.stream().map(task -> new TaskDTO(task)).toList();
    }
}
