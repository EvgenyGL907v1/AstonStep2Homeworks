package evgenygl907v1.homework2.dao;

import evgenygl907v1.homework2.entity.User;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class UserDataAccessObject implements UserDao {
    private static final Logger log = LoggerFactory.getLogger(UserDataAccessObject.class);
    private final SessionFactory sessionFactory;

    public UserDataAccessObject(SessionFactory sessionFactory) {
        this.sessionFactory = Objects.requireNonNull(sessionFactory, "sessionFactory не может быть null");
    }

    @Override
    public User save(User user) {
        Objects.requireNonNull(user, "user не может быть null");
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.persist(user);
                transaction.commit();
                log.info("Пользователь сохранен: {}", user);
                return user;
            } catch (RuntimeException e) {
                rollback(transaction, e);
                log.error("Ошибка при сохранении пользователя {}", user, e);
                throw e;
            }
        }
    }

    @Override
    public Optional<User> findById(Long id) {
        Objects.requireNonNull(id, "id не может быть null");
        try (Session session = sessionFactory.openSession()) {
            User user = session.get(User.class, id);
            log.debug("Поиск по id={}: {}", id, (user != null ? "найден" : "не найден"));
            return Optional.ofNullable(user);
        }
    }

    @Override
    public List<User> findAll() {
        try (Session session = sessionFactory.openSession()) {
            List<User> users = session.createQuery("from User order by id", User.class).list();
            log.debug("Получен список пользователей, количество: {}", users.size());
            return users;
        }
    }

    @Override
    public User update(User user) {
        Objects.requireNonNull(user, "user не может быть null");
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                User merged = session.merge(user);
                transaction.commit();
                log.info("Пользователь обновлен: {}", merged);
                return merged;
            } catch (RuntimeException e) {
                rollback(transaction, e);
                log.error("Ошибка при обновлении пользователя {}", user, e);
                throw e;
            }
        }
    }

    @Override
    public boolean deleteById(Long id) {
        Objects.requireNonNull(id, "id не может быть null");

        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                User user = session.get(User.class, id);
                if (user == null) {
                    transaction.commit();
                    log.warn("Попытка удаления несуществующего пользователя id={}", id);
                    return false;
                }
                session.remove(user);
                transaction.commit();
                log.info("Пользователь удален id={}", id);
                return true;
            } catch (RuntimeException e) {
                rollback(transaction, e);
                log.error("Ошибка при обновлении пользователя id={}", id, e);
                throw e;
            }
        }
    }

    private void rollback(Transaction transaction, RuntimeException cause) {
        if (transaction != null && transaction.isActive()) {
            try {
                transaction.rollback();
                log.debug("rollback");
            } catch (HibernateException rb) {
                log.error("Ошибка отката транзакции", rb);
                cause.addSuppressed(rb);
            }
        }
    }
}
