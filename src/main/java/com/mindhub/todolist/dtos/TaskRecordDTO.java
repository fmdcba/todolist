package com.mindhub.todolist.dtos;

import com.mindhub.todolist.models.Task;
import com.mindhub.todolist.models.UserEntity;

public record TaskRecordDTO(Long id, String title, String description, Task.TaskStatus status, UserEntity user) {
}
