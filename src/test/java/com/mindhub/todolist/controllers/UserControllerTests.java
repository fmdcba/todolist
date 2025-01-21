package com.mindhub.todolist.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindhub.todolist.config.TestConfig;
import com.mindhub.todolist.dtos.UserDTO;
import com.mindhub.todolist.services.UserService;
import com.mindhub.todolist.utils.ControllerValidations;
import com.mindhub.todolist.utils.Factory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@Import(TestConfig.class)
class UserControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private ControllerValidations controllerValidations;

    @Mock
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getUserOK() throws Exception {
        Long userId = 7L;
        String emailUser = "pepe@pepe.com";
        UserDTO expected = new UserDTO(Factory.createValidUserEntity());

        Mockito.doNothing().when(controllerValidations).validateId(userId);
        Mockito.when(controllerValidations.getAuthUserEmail()).thenReturn(emailUser);
        Mockito.when(userService.getUser(emailUser, userId)).thenReturn(expected);

        this.mockMvc.perform(get("/api/users/" + userId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expected)));
    }
}

//alt+énter importttttaa
//shift + suopr chau linea com el diego
