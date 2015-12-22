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

package ch.javaee.basicMvc.web.controller;

import ch.javaee.basicMvc.domain.Role;
import ch.javaee.basicMvc.domain.SecurityCode;
import ch.javaee.basicMvc.domain.User;
import ch.javaee.basicMvc.repository.SecurityCodeRepository;
import ch.javaee.basicMvc.repository.UserRepository;
import ch.javaee.basicMvc.service.MailSenderService;
import ch.javaee.basicMvc.service.MyUserDetailsService;
import ch.javaee.basicMvc.utility.SecureUtility;
import ch.javaee.basicMvc.utility.TypeActivationEnum;
import ch.javaee.basicMvc.web.component.UserSessionComponent;
import ch.javaee.basicMvc.web.form.UserForm;
import net.tanesha.recaptcha.ReCaptchaImpl;
import net.tanesha.recaptcha.ReCaptchaResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import javax.validation.Valid;
import java.util.*;

@Controller
public class UserController {

    static final Logger logger = LoggerFactory.getLogger(UserController.class);


    @Autowired
    UserRepository userRepository;

    @Autowired
    SecurityCodeRepository securityCodeRepository;

    @Autowired
    MailSenderService mailSenderService;

    @Autowired
    UserDetailsService myUserDetailsService;
    @Autowired
    private UserSessionComponent userSessionComponent;
    @Autowired
    private ReCaptchaImpl reCaptcha;

    private static List<GrantedAuthority> AUTHORITIES = new ArrayList<GrantedAuthority>(1) {{
        add(new SimpleGrantedAuthority("ROLE_USER"));
    }};


    public void setMyUserDetailsService(MyUserDetailsService myUserDetailsService) {
        this.myUserDetailsService = myUserDetailsService;
    }

    @RequestMapping("/public/signup")
    public String create(Model model) {
        logger.debug("Enter: create");
        if (!model.containsAttribute("user")) {
            model.addAttribute("user", new UserForm());
        }
        logger.debug("Check: reCaptcha {}", reCaptcha != null);
        if (reCaptcha != null) {
            model.addAttribute("recaptcha", reCaptcha.createRecaptchaHtml(null, null));
        }
        return "view/public/signup";
    }


    @RequestMapping(value = "/public/signup_confirm", method = RequestMethod.POST)
    @Transactional
    public String createUser(Model model, @ModelAttribute("user") @Valid UserForm form, BindingResult result, @RequestParam(value = "recaptcha_challenge_field", required = false) String challangeField,
                             @RequestParam(value = "recaptcha_response_field", required = false) String responseField, ServletRequest servletRequest) {
        logger.debug("Enter: createUser");
        if (reCaptcha != null) {
            String remoteAdress = servletRequest.getRemoteAddr();
            ReCaptchaResponse reCaptchaResponse = reCaptcha.checkAnswer(remoteAdress, challangeField, responseField);
            if (!reCaptchaResponse.isValid()) {
                this.create(model);
                return "view/public/signup";
            }
        }
        if (!result.hasErrors()) {

            // check if email already exists
            if (userRepository.isEmailAlreadyExists(form.getEmail())) {
                FieldError fieldError = new FieldError("user", "email", "email already exists");
                result.addError(fieldError);
                return "view/public/signup";
            }
            User user = new User();
            Md5PasswordEncoder encoder = new Md5PasswordEncoder();
            user.setUsername(form.getUsername());
            user.setEmail(form.getEmail());
            user.setEnabled(false);

            user.setPassword(encoder.encodePassword(form.getPassword(), user.getEmail()));
            Role role = new Role();
            role.setUser(user);
            role.setRole(2);

            SecurityCode securityCode = new SecurityCode();
            securityCode.setUser(user);
            securityCode.setTimeRequest(new Date());
            securityCode.setTypeActivationEnum(TypeActivationEnum.NEW_ACCOUNT);
            securityCode.setCode(SecureUtility.generateRandomCode());
            user.setRole(role);
            user.setSecurityCode(securityCode);

            userRepository.saveUser(user);
            //securityCodeRepository.persist(securityCode);
            mailSenderService.sendAuthorizationMail(user, user.getSecurityCode());


        } else {
            logger.debug("signup error");
            this.create(model);
            return "view/public/signup";

        }
        logger.debug("Exit: createUser");
        return "view/public/mailSent";
    }

    @RequestMapping(value = "/user/profile")
    public String userProfile() {
        return "view/user/profile";
    }

    @RequestMapping(value = "/user/searchContact")
    public String searchContact() {
        return "view/user/searchContact";
    }

    @RequestMapping(value = "/user/get_contacts_list",
            method = RequestMethod.GET, produces = "application/json")
    public
    @ResponseBody
    Object[] getContactsList(@RequestParam("term") String query) {
        List<String> usernameList = userRepository.findUsername(query);

        return usernameList.toArray();
    }

    @RequestMapping(value = "/pages/base/user/get",
            method = RequestMethod.GET)
    public
    @ResponseBody
    Map<String, String> get() {
        List<String> usernameList = userRepository.findUsername("");
        Map<String, String> result = new HashMap<>();
        for (String username : usernameList) {
            result.put("label", username);

        }
        return result;
    }

    @RequestMapping(value = "/public/activation", method = RequestMethod.GET)
    @Transactional
    public String activation(@RequestParam String mail, @RequestParam String code) {
        logger.debug("Enter: activation");
        if (userRepository.isSecurityCodeValid(mail, code)) {
            User user = userRepository.findUserByEmail(mail);
            user.setEnabled(true);

            securityCodeRepository.deleteSecurityCode(user.getSecurityCode());
            user.setSecurityCode(null);
            userRepository.update(user);
            UserDetails userDetails = myUserDetailsService.loadUserByUsername(user.getEmail());

            Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, user.getPassword(), AUTHORITIES);
            SecurityContextHolder.getContext().setAuthentication(auth);
            userSessionComponent.setCurrentUser(user);
            logger.debug("Exit: activation");
            return "view/user/profile";
        }
        logger.debug("Exit: activation");
        return "view/error/error";

    }


}
