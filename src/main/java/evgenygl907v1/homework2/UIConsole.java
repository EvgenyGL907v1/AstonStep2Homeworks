package evgenygl907v1.homework2;

import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class UIConsole {
    private final UserDataAccessObject userDao;
    private final Scanner scanner;

    public UIConsole(UserDataAccessObject userDao, Scanner scanner) {
        this.userDao = Objects.requireNonNull(userDao, "userDao не может быть null");
        this.scanner = Objects.requireNonNull(scanner, "scanner не может быть null");
    }

    public void run() {
        boolean running = true;
        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();
            try {
                switch (choice) {
                    case "1" -> createUser();
                    case "2" -> findUser();
                    case "3" -> listUsers();
                    case "4" -> updateUser();
                    case "5" -> deleteUser();
                    case "0" -> {
                        System.out.println("Выход.");
                        running = false;
                    }
                    default -> System.out.println("Неизвестная команда.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("[WARN] Ошибка валидации: " + e.getMessage());
            } catch (IllegalStateException e) {
                System.out.println("[ERROR] " + e.getMessage());
            } catch (Exception e) {
                System.out.println("[ERROR] Непредвиденная ошибка: " + e.getMessage());
                e.printStackTrace(System.out);
            }
        }
    }

    private void printMenu() {
        System.out.println();
        System.out.println("======== Меню =========");
        System.out.println("1. Создать пользователя");
        System.out.println("2. Найти по id");
        System.out.println("3. Список всех");
        System.out.println("4. Обновить");
        System.out.println("5. Удалить");
        System.out.println("0. Выход");
        System.out.print("> ");
    }

    private void createUser() {
        String name = readValidName();
        String email = readValidEmail();
        int age = readValidAge();
        User created = userDao.save(new User(name, email, age));
        System.out.println("Создан: " + created);
    }

    private void findUser() {
        Long id = readValidId();
        userDao.findById(id).ifPresentOrElse(
                System.out::println,
                () -> System.out.println("Не найден")
        );
    }

    private void listUsers() {
        List<User> users = userDao.findAll();
        if (users.isEmpty()) {
            System.out.println("Список пуст.");
        } else {
            users.forEach(System.out::println);
        }
    }

    private void updateUser() {
        Long id = readValidId();
        User user = userDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь id=" + id + " не найден"));
        String name = readValidName();
        String email = readValidEmail();
        int age = readValidAge();
        user.setName(name);
        user.setEmail(email);
        user.setAge(age);
        System.out.println("Обновлён: " + userDao.update(user));
    }

    private void deleteUser() {
        Long id = readValidId();
        System.out.println(userDao.deleteById(id) ? "Удалён." : "Не найден.");
    }

    private String readValidName() {
        System.out.print("Имя: ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) {
            throw new IllegalArgumentException("имя не может быть пустым");
        }
        if (name.length() > 50) {
            throw new IllegalArgumentException("имя длиннее 50 символов");
        }
        return name;
    }

    private String readValidEmail() {
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        if (email.isEmpty() || !email.contains("@") || email.length() > 100) {
            throw new IllegalArgumentException("некорректный email");
        }
        return email;
    }

    private int readValidAge() {
        System.out.print("Возраст: ");
        int age = parseInteger(scanner.nextLine().trim(), "возраст");
        if (age <= 0 || age > 130) {
            throw new IllegalArgumentException("возраст должен быть от 1 до 130");
        }
        return age;
    }

    private Long readValidId() {
        System.out.print("id: ");
        long id;
        try {
            id = Long.parseLong(scanner.nextLine().trim());
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
