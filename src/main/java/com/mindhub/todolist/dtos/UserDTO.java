package com.mindhub.todolist.dtos;

import com.mindhub.todolist.models.UserEntity;

import java.util.List;

public class UserDTO {

    private Long id;

    private String username;

    private String email;

    private List<String> tasks;

    public UserDTO (UserEntity user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.tasks = user
                .getTasks()
                .stream()
                .map(task -> task.getTitle())
                .toList();
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public List<String> getTasks() {
        return tasks;
    }
}
