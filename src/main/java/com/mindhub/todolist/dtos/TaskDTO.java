package com.mindhub.todolist.dtos;

import com.mindhub.todolist.models.Task;

public record TaskDTO(Long id, String title, String description, Task.TaskStatus status) {
}