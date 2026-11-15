package evgenygl907v1.homework2.dao;

import evgenygl907v1.homework2.entity.User;
import java.util.List;
import java.util.Optional;

public interface UserDao {
    User save(User user);
    Optional<User> findById(Long id);
    List<User> findAll();
    User update(User user);
    boolean deleteById(Long id);
}
