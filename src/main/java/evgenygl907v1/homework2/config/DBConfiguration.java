package evgenygl907v1.homework2.config;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DBConfiguration {
    private static final Logger log = LoggerFactory.getLogger(DBConfiguration.class);

    private DBConfiguration() {}

    public static SessionFactory buildSessionFactory() {
        try {
            return new Configuration()
                    .configure("hibernate.cfg.xml")
                    .buildSessionFactory();
        } catch (RuntimeException e) {
            log.error("Не удалось создать SessionFactory", e);
            throw e;
        }
    }
}
