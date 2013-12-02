package ch.javaee.basicMvc.repository;

import ch.javaee.basicMvc.domain.SecurityCode;

public interface SecurityCodeRepository {
    void persist(SecurityCode securityCode);

    void deleteSecurityCode(SecurityCode securityCode);
}
