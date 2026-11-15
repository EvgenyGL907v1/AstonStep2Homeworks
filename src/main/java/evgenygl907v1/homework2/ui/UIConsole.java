package evgenygl907v1.homework2.ui;

import evgenygl907v1.homework2.entity.User;
import evgenygl907v1.homework2.service.DefaultUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class UIConsole {
    private static final Logger log = LoggerFactory.getLogger(UIConsole.class);
    private final Scanner scanner;
    private final DefaultUserService service;

    public UIConsole(DefaultUserService service, Scanner scanner) {
        this.service = Objects.requireNonNull(service, "service не может быть null");
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
                log.warn("Ошибка валидации: {}", e.getMessage());
                System.out.println("Ошибка валидации: " + e.getMessage());
            } catch (IllegalStateException e) {
                log.warn("{}", e.getMessage());
                System.out.println("[ERROR] " + e.getMessage());
            } catch (Exception e) {
                log.warn("{}", e.getMessage());
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
        System.out.print("Имя: ");
        String name = scanner.nextLine().trim();
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        System.out.print("Возраст: ");
        String age = scanner.nextLine().trim();

        service.createUser(name, email, age);
    }

    private void findUser() {
        System.out.print("id: ");
        String id = scanner.nextLine().trim();

        service.findUser(id);
    }

    private void listUsers() {
        List<User> users = service.listUsers();

        if (users.isEmpty()) {
            System.out.println("Список пуст.");
        } else {
            users.forEach(System.out::println);
        }
    }

    private void updateUser() {
        System.out.print("id: ");
        String id = scanner.nextLine().trim();

        System.out.print("Имя: ");
        String name = scanner.nextLine().trim();
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        System.out.print("Возраст: ");
        String age = scanner.nextLine().trim();

        service.updateUser(id, name, email, age);
    }

    private void deleteUser() {
        System.out.print("id: ");
        String id = scanner.nextLine().trim();

        service.deleteUser(id);
    }
}
