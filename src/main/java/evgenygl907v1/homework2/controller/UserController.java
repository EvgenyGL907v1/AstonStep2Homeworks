package evgenygl907v1.homework2.controller;

import evgenygl907v1.homework2.dto.UserRequest;
import evgenygl907v1.homework2.dto.UserResponse;
import evgenygl907v1.homework2.entity.User;
import evgenygl907v1.homework2.service.DefaultUserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final DefaultUserService userService;

    public UserController(DefaultUserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserResponse> getAllUsers() {
        return userService.listUsers().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public UserResponse getUserById(@PathVariable Long id) {
        return toResponse(userService.findUser(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse createUser(@RequestBody UserRequest request) {
        User user = userService.createUser(request.getName(), request.getEmail(), request.getAge());
        return toResponse(user);
    }

    @PutMapping("/{id}")
    public UserResponse updateUser(@PathVariable Long id, @RequestBody UserRequest request) {
        User user = userService.updateUser(id, request.getName(), request.getEmail(), request.getAge());
        return toResponse(user);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // Вернет 204 No Content
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    private UserResponse toResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setAge(user.getAge());
        response.setCreatedAt(user.getCreatedAt());
        return response;
    }
}