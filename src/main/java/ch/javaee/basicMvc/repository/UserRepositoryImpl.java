package ch.javaee.basicMvc.repository;

import ch.javaee.basicMvc.domain.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Collection;
import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Collection loadUsers() {
        Query query = em.createQuery("from user");
        return query.getResultList();
    }

    @Override
    public void saveUser(User user) {
        em.merge(user);
    }


    @Override
    @Transactional(readOnly = true)
    public String findPasswordByUsername(String username) {
        return (String) em.createQuery("select u.username from user u where username = :username")
                .setParameter("username", username).getSingleResult();

    }


    @Override
    @Transactional(readOnly = true)
    public boolean isEmailAlreadyExists(String email) {
        Long count = (Long) em.createQuery("select count(u.email) from user u where email = :email")
                .setParameter("email", email)
                .getSingleResult();
        if (count.compareTo(0l) > 0){
            return true;
        }
        return false;

    }

    @Override
    @Transactional(readOnly = true)
    public User findUserByUsername(String username){
        return (User) em.createQuery("select u from user u where email = :username")
                .setParameter("username", username).getSingleResult();

    }

    @Override
    @Transactional(readOnly = true)
    public User findUserByEmail(String email){
        return (User) em.createQuery("select u from user u where email = :email")
                .setParameter("email", email).getSingleResult();

    }

    @Override
    public void update(User user) {
        em.merge(user);
    }


    @Override
    @Transactional(readOnly = false)
    public List<String> findUsername(String username) {
        Query query = em.createQuery("select u.username from user u where username like :username")
                .setParameter("username", username+"%")
                .setMaxResults(5);
        return query.getResultList();

    }

    @Override
    @Transactional(readOnly = true)
    public boolean isSecurityCodeValid(String email, String securityCode){
        Query query = em.createQuery("select count(u) from user u join u.securityCode as sec where u.email = :email" +
                " and sec.code = :code")
                .setParameter("email", email)
                .setParameter("code", securityCode);
        Long count = (Long)query.getSingleResult();
        return count == 1;
    }



}


