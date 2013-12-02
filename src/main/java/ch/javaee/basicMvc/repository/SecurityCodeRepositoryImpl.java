package ch.javaee.basicMvc.repository;

import ch.javaee.basicMvc.domain.SecurityCode;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class SecurityCodeRepositoryImpl implements SecurityCodeRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void persist(SecurityCode securityCode){
        entityManager.persist(securityCode);
    }

    @Override
    public void deleteSecurityCode(SecurityCode securityCode){
        entityManager.remove(securityCode);

    }
}
