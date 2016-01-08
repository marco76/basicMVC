/*
 * =============================================================================
 *
 * Copyright (c) 2013, Marco Molteni ("http://javaee.ch")
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * =============================================================================
 */

package ch.javaee.basicMvcAng.repository;

import ch.javaee.basicMvcAng.domain.SecurityCode;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class SecurityCodeRepositoryImpl implements SecurityCodeRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void persist(SecurityCode securityCode) {
        entityManager.persist(securityCode);
    }

    @Override
    public void deleteSecurityCode(SecurityCode securityCode) {
        entityManager.remove(securityCode);

    }
}
