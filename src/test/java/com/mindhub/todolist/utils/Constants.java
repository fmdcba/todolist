package com.mindhub.todolist.utils;

import com.mindhub.todolist.models.RoleType;
import com.mindhub.todolist.models.UserEntity;

public class Constants {

    public static final String VALID_USERNAME = "sutUsername";
    public static final String VALID_PASSWORD = "sutPassword";
    public static final String VALID_EMAIL = "subject@underTest.com";
    public static final String NON_EXISTENT_EMAIL = "nonExistentSubject@undeTest.com";
    public static final String NON_EXISTENT_USERNAME = "nonExistentSutUsername";

    //TODO: Move to factory package or something like that later...
    public static UserEntity createValidUserEntity() {
        return new UserEntity(VALID_USERNAME, VALID_PASSWORD, VALID_EMAIL, RoleType.USER);
    }
}
