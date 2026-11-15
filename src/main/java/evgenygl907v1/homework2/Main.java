package evgenygl907v1.homework2;

import evgenygl907v1.homework2.config.DBConfiguration;
import evgenygl907v1.homework2.dao.UserDao;
import evgenygl907v1.homework2.dao.UserDataAccessObject;
import evgenygl907v1.homework2.ui.UIConsole;
import org.hibernate.SessionFactory;
import java.util.Scanner;
import java.util.logging.Level;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        //java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.OFF);

        SessionFactory sessionFactory = null;
        Scanner scanner = null;
        try {
            sessionFactory = DBConfiguration.buildSessionFactory();

            UserDao userDao = new UserDataAccessObject(sessionFactory);
            scanner = new Scanner(System.in);
            UIConsole app = new UIConsole(userDao, scanner);

            app.run();
        } catch (Exception e) {
            log.error("Фатальная ошибка", e);
            e.printStackTrace(System.out);
        } finally {
            if (scanner != null) scanner.close();
            if (sessionFactory != null && !sessionFactory.isClosed()) {
                sessionFactory.close();
                log.info("SessionFactory закрыта");
            }
        }
    }
}
