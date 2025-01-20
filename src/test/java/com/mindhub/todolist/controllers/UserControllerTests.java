package com.mindhub.todolist.controllers;

import com.mindhub.todolist.config.JwtUtils;
import com.mindhub.todolist.config.TestConfig;
import com.mindhub.todolist.services.UserService;
import com.mindhub.todolist.utils.ControllerValidations;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(TestConfig.class)
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private JwtUtils jwtUtil;

    @Mock
    private ControllerValidations controllerValidations;

    @Mock
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils;


    @Test
    void getUser_withMockedJwt_returnsUserDTO() throws Exception {
        when(jwtUtils.generateToken(anyString())).thenReturn("fakeToken");
        // or if your code verifies tokens, you can mock that, too

        mockMvc.perform(get("/api/users/1")
                        .header("Authorization", "Bearer fakeToken"))
                .andExpect(status().isOk());
    }
}

