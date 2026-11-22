package evgenygl907v1.homework2.controller;

import evgenygl907v1.homework2.entity.User;
import evgenygl907v1.homework2.service.DefaultUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DefaultUserService userService;

    @Test
    void getAllUsers_ShouldReturnList() throws Exception {
        User user = new User("Test", "test@test.com", 20);
        user.setId(1L);
        when(userService.listUsers()).thenReturn(List.of(user));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Test"))
                .andExpect(jsonPath("$[0].email").value("test@test.com"));
    }

    @Test
    void createUser_ShouldReturnCreatedStatusAndDto() throws Exception {
        String requestJson = "{\"name\":\"New\",\"email\":\"new@test.com\",\"age\":25}";

        User savedUser = new User("New", "new@test.com", 25);
        savedUser.setId(1L);

        when(userService.createUser(eq("New"), eq("new@test.com"), eq(25))).thenReturn(savedUser);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("New"));
    }

    @Test
    void getUserById_ShouldReturnUser() throws Exception {
        User user = new User("Test", "test@test.com", 20);
        user.setId(1L);
        when(userService.findUser(1L)).thenReturn(user);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test"));
    }

    @Test
    void updateUser_ShouldReturnUpdatedUser() throws Exception {
        String requestJson = "{\"name\":\"Updated\",\"email\":\"upd@test.com\",\"age\":30}";

        User updatedUser = new User("Updated", "upd@test.com", 30);
        updatedUser.setId(1L);

        when(userService.updateUser(eq(1L), eq("Updated"), eq("upd@test.com"), eq(30))).thenReturn(updatedUser);

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated"));
    }

    @Test
    void deleteUser_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNoContent());
    }
}