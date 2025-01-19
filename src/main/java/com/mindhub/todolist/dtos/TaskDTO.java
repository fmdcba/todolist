package com.mindhub.todolist.dtos;

import com.mindhub.todolist.models.Task;
import com.mindhub.todolist.models.UserEntity;

public class TaskDTO {

    private Long id;

    private String title, description;

    private Task.TaskStatus status;

    private Long userId;

    public TaskDTO(Task task) {
        id = task.getId();
        title = task.getTitle();
        description = task.getDescription();
        status = task.getStatus();
        userId = task.getUserId();
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Task.TaskStatus getStatus() {
        return status;
    }

    public Long getUserId() { return userId; }

}