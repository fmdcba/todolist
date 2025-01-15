package com.mindhub.todolist.dtos;

public record UserRecordDTO(Long id, String username, String email, Enum role) {
}
