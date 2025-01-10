package com.mindhub.todolist.dtos;

import com.mindhub.todolist.models.Task;

public class TaskDTO {

    private Long id;
    private String title, description;
    private Task.TaskStatus status;

    public TaskDTO(Task task) {
        id = task.getId();
        title = task.getTitle();
        description = task.getDescription();
        status = task.getStatus();
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
}