package ch.javaee.basicMvc.repository;


import ch.javaee.basicMvc.domain.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

public interface UserRepository {
    Collection loadUsers();

    void saveUser(User user);

    @Transactional(readOnly = true)
    String findPasswordByUsername(String username);

    @Transactional(readOnly = true)
    User findUserByUsername(String username);

    List<String> findUsername(String query);

    @Transactional(readOnly = true)
    boolean isEmailAlreadyExists(String email);

    @Transactional(readOnly = true)
    boolean isSecurityCodeValid(String email, String securityCode);

    @Transactional(readOnly = true)
    User findUserByEmail(String email);


    void update(User user);
}

