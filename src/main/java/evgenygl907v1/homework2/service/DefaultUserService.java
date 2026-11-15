package evgenygl907v1.homework2.service;

import evgenygl907v1.homework2.dao.UserDao;
import evgenygl907v1.homework2.entity.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;

public class DefaultUserService {
    private static final Logger log = LoggerFactory.getLogger(DefaultUserService.class);
    private final UserDao userDao;

    public DefaultUserService(UserDao userDao) {
        this.userDao = Objects.requireNonNull(userDao, "userDao не может быть null");
    }

    public void createUser(String name, String email, String age) {
        userDao.save(new User(readValidName(name),  readValidEmail(email), readValidAge(age)));
    }

    public void findUser(String id) {
        userDao.findById(readValidId(id)).ifPresentOrElse(
                System.out::println,
                () -> System.out.println("Не найден")
        );
    }

    public List<User> listUsers() {
        return userDao.findAll();
    }

    public void updateUser(String idStr, String name, String email, String age) {
        Long id = readValidId(idStr);
        User user = userDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь id=" + id + " не найден"));

        user.setName(readValidName(name));
        user.setEmail(readValidEmail(email));
        user.setAge(readValidAge(age));
        System.out.println("Обновлён: " + userDao.update(user));
    }

    public void deleteUser(String idStr) {
        Long id = readValidId(idStr);
        System.out.println(userDao.deleteById(id) ? "" : "Не найден.");
    }

    private String readValidName(String name) {
        if (name.isEmpty()) {
            throw new IllegalArgumentException("имя не может быть пустым");
        }
        if (name.length() > 50) {
            throw new IllegalArgumentException("имя длиннее 50 символов");
        }
        return name;
    }

    private String readValidEmail(String email) {
        if (email.isEmpty() || !email.contains("@") || email.length() > 100) {
            throw new IllegalArgumentException("некорректный email");
        }
        return email;
    }

    private int readValidAge(String ageStr) {
        int age = parseInteger(ageStr, "возраст");
        if (age <= 0 || age > 130) {
            throw new IllegalArgumentException("возраст должен быть от 1 до 130");
        }
        return age;
    }

    private Long readValidId(String idStr) {
        long id;
        try {
            id = Long.parseLong(idStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("id должен быть числом");
        }
        if (id <= 0) {
            throw new IllegalArgumentException("id должен быть положительным");
        }
        return id;
    }

    private int parseInteger(String raw, String fieldName) {
        try {
            return Integer.parseInt(raw);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(fieldName + " должен быть числом");
        }
    }
}
