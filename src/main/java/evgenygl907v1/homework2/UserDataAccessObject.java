package evgenygl907v1.homework2;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class UserDataAccessObject {
    private final SessionFactory sessionFactory;

    public UserDataAccessObject(SessionFactory sessionFactory) {
        this.sessionFactory = Objects.requireNonNull(sessionFactory, "sessionFactory не может быть null");
    }

    public User save(User user) {
        Objects.requireNonNull(user, "user не может быть null");
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.persist(user);
            transaction.commit();
            System.out.println("Сохранен пользователь: " + user);
            return user;
        } catch (RuntimeException e) {
            rollback(transaction, e);
            throw e;
        }
    }

    public Optional<User> findById(Long id) {
        Objects.requireNonNull(id, "id не может быть null");
        try (Session session = sessionFactory.openSession()) {
            return Optional.ofNullable(session.get(User.class, id));
        }
    }

    public List<User> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from User order by id", User.class).list();
        }
    }

    public User update(User user) {
        Objects.requireNonNull(user, "user не может быть null");
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            User merged = session.merge(user);
            transaction.commit();
            System.out.println("Обновлён пользователь: " + merged);
            return merged;
        } catch (RuntimeException e) {
            rollback(transaction, e);
            throw e;
        }
    }

    public boolean deleteById(Long id) {
        Objects.requireNonNull(id, "id не может быть null");
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            User user = session.get(User.class, id);
            if (user == null) {
                transaction.commit();
                return false;
            }
            session.remove(user);
            transaction.commit();
            System.out.println("Удалён пользователь id=" + id);
            return true;
        } catch (RuntimeException e) {
            rollback(transaction, e);
            throw e;
        }
    }

    private void rollback(Transaction transaction, RuntimeException cause) {
        if (transaction != null && transaction.isActive()) {
            try {
                transaction.rollback();
            } catch (HibernateException rb) {
                System.out.println("Ошибка отката транзакции: " + rb.getMessage());
                cause.addSuppressed(rb);
            }
        }
        System.out.println("Ошибка выполнения операции: " + cause.getMessage());
    }
}
