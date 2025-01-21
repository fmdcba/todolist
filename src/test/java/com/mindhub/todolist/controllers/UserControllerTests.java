package com.mindhub.todolist.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindhub.todolist.config.JwtUtils;
import com.mindhub.todolist.dtos.UserDTO;
import com.mindhub.todolist.services.UserService;
import com.mindhub.todolist.utils.Constants;
import com.mindhub.todolist.utils.ControllerValidations;
import com.mindhub.todolist.utils.Factory;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)  // Inicia un servidor real en un puerto aleatorio
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    UserController userController;

    @MockitoBean
    private ControllerValidations controllerValidations;

    @MockitoBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    JwtUtils jwtUtils;

    @Test
    public void getUserOK() throws Exception {
        Long userId = Constants.VALID_ID;
        String emailUser = Constants.VALID_EMAIL;
        UserDTO expected = new UserDTO(Factory.createValidUserEntity());
        String jwt = jwtUtils.generateToken(emailUser);

        //Mockito.whenllamaauserdetails -> Mockear el check al auth
        Mockito.doNothing().when(controllerValidations).validateId(userId);
        Mockito.when(controllerValidations.getAuthUserEmail()).thenReturn(emailUser);
        Mockito.when(userService.getUser(emailUser, userId)).thenReturn(expected);

        this.mockMvc.perform(get("/api/users/" + userId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expected)));
    }
}

//alt+énter importttttaa
//shift + supr chau linea
// ctrl + n buscar clase
