package evgenygl907v1.homework2;

import org.hibernate.SessionFactory;
import java.util.Scanner;
import java.util.logging.Level;

public class Main {
    public static void main(String[] args) {
        java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.OFF);

        SessionFactory sessionFactory = null;
        Scanner scanner = null;
        try {
            sessionFactory = DBConfiguration.buildSessionFactory();

            UserDataAccessObject userDao = new UserDataAccessObject(sessionFactory);
            scanner = new Scanner(System.in);
            UIConsole app = new UIConsole(userDao, scanner);

            app.run();
        } catch (Exception e) {
            System.out.println("Фатальная ошибка: " + e.getMessage());
            e.printStackTrace(System.out);
        } finally {
            if (scanner != null) {
                scanner.close();
            }
            if (sessionFactory != null && !sessionFactory.isClosed()) {
                sessionFactory.close();
                System.out.println("SessionFactory закрыта");
            }
        }
    }
}
