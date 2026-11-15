package evgenygl907v1.homework2.dao;

import evgenygl907v1.homework2.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
class UserDataAccessObjectIT {
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:18-alpine")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    private static SessionFactory sessionFactory;
    private static UserDataAccessObject userDao;

    @BeforeAll
    static void setUp() {
        Configuration configuration = new Configuration();
        configuration.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver");
        configuration.setProperty("hibernate.connection.url", postgres.getJdbcUrl());
        configuration.setProperty("hibernate.connection.username", postgres.getUsername());
        configuration.setProperty("hibernate.connection.password", postgres.getPassword());
        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");

        configuration.setProperty("hibernate.hbm2ddl.auto", "create-drop");

        configuration.addAnnotatedClass(User.class);

        sessionFactory = configuration.buildSessionFactory();
        userDao = new UserDataAccessObject(sessionFactory);
    }

    @AfterAll
    static void tearDown() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    @BeforeEach
    void cleanDatabase() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.createMutationQuery("DELETE FROM User").executeUpdate();
            session.getTransaction().commit();
        }
    }

    @Test
    void saveAndFindById_ShouldWork() {
        User newUser = new User("Name1", "qwer1@gmail.com", 30);

        User savedUser = userDao.save(newUser);
        Optional<User> foundUser = userDao.findById(savedUser.getId());

        assertTrue(foundUser.isPresent());
        assertEquals("Name1", foundUser.get().getName());
        assertNotNull(foundUser.get().getId());
    }

    @Test
    void findAll_ShouldReturnList() {
        userDao.save(new User("qwe1", "qwe1@qwe.com", 25));
        userDao.save(new User("qwe2", "qwe2@gmail.com", 35));

        List<User> users = userDao.findAll();

        assertEquals(2, users.size());
        assertEquals("qwe1", users.get(0).getName());
        assertEquals("qwe2", users.get(1).getName());
    }

    @Test
    void update_ShouldChangeDataInDb() {
        User user = userDao.save(new User("Evgeny", "evgeny@gmail.com", 23));
        user.setName("Evgeny1");
        user.setAge(25);

        User updatedUser = userDao.update(user);

        assertEquals("Evgeny1", updatedUser.getName());
        assertEquals(25, updatedUser.getAge());
    }

    @Test
    void deleteById_ShouldRemoveUser() {
        User user = userDao.save(new User("deleteName", "etyhhwth@gmail.com", 22));
        Long id = user.getId();

        boolean deleted = userDao.deleteById(id);

        assertTrue(deleted);
        assertTrue(userDao.findById(id).isEmpty());
    }
}