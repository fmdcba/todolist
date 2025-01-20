package com.mindhub.todolist.utils;

import com.mindhub.todolist.dtos.NewUserDTO;
import com.mindhub.todolist.models.RoleType;
import com.mindhub.todolist.models.UserEntity;

public class Factory {

    public static UserEntity createValidUserEntity() {
        return new UserEntity(
                Constants.VALID_USERNAME,
                Constants.VALID_PASSWORD,
                Constants.VALID_EMAIL,
                RoleType.USER
        );
    }

    public static UserEntity createValidTargetUserEntity() {
        return new UserEntity(
                Constants.VALID_USERNAME_TARGET,
                Constants.VALID_PASSWORD,
                Constants.VALID_EMAIL_TARGET,
                RoleType.USER
        );
    }
}
