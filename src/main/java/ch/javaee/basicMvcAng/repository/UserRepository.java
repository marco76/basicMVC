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


import ch.javaee.basicMvcAng.domain.User;
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

