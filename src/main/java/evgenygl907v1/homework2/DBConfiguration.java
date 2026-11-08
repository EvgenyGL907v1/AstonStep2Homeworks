package evgenygl907v1.homework2;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class DBConfiguration {
    private DBConfiguration() {}

    public static SessionFactory buildSessionFactory() {
        try {
            return new Configuration()
                    .configure("hibernate.cfg.xml")
                    .buildSessionFactory();
        } catch (RuntimeException e) {
            System.out.println("Не удалось создать SessionFactory: " + e.getMessage());
            throw e;
        }
    }
}
