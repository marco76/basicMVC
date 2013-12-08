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

package ch.javaee.basicMvc.config;

import net.tanesha.recaptcha.ReCaptchaImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:${ENV:dev}/recaptcha.properties")
public class ReCaptchaConfig {
    @Value("${key.private}")
    private String keyPrivate;
    @Value("${key.public}")
    private String keyPublic;
    @Value("${recaptcha.active}")
    private String active;


    @Bean(name = "reCaptcha")
    ReCaptchaImpl reCaptcha() {
        if ("TRUE".equalsIgnoreCase(active)) {
            ReCaptchaImpl reCaptcha = new ReCaptchaImpl();
            reCaptcha.setPrivateKey(keyPrivate);
            reCaptcha.setPublicKey(keyPublic);
            return reCaptcha;
        }
        return null;

    }

}
