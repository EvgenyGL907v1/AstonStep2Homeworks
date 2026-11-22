package evgenygl907v1.homework2.service;

import evgenygl907v1.homework2.dao.UserRepository;
import evgenygl907v1.homework2.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultUserService {
    private final UserRepository userRepository;

    public DefaultUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(String name, String email, Integer age) {
        User user = new User(name, email, age);
        return userRepository.save(user);
    }

    public User findUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Пользователь с id " + id + " не найден"));
    }

    public List<User> listUsers() {
        return userRepository.findAll();
    }

    public User updateUser(Long id, String name, String email, Integer age) {
        User user = findUser(id);
        user.setName(name);
        user.setEmail(email);
        user.setAge(age);
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}