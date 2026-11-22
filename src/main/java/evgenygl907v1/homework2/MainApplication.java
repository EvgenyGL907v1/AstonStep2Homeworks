package evgenygl907v1.homework2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MainApplication {
    //private static final Logger log = LoggerFactory.getLogger(MainApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);


        //java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.OFF);

        /*
        SessionFactory sessionFactory = null;
        Scanner scanner = null;
        try {
            sessionFactory = DBConfiguration.buildSessionFactory();

            UserDao userDao = new UserDataAccessObject(sessionFactory);
            DefaultUserService service = new DefaultUserService(userDao);
            scanner = new Scanner(System.in);

            UIConsole app = new UIConsole(service, scanner);

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
        }*/
    }
}
