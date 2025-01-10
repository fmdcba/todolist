package com.mindhub.todolist.repositories;

import com.mindhub.todolist.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsById(Long id);
}
