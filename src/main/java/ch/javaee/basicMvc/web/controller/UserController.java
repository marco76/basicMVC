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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class UserController {

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


    public void setMyUserDetailsService(MyUserDetailsService myUserDetailsService) {
        this.myUserDetailsService = myUserDetailsService;
    }

    @RequestMapping("/public/signup")
    public String create(Model model) {
        model.addAttribute("user", new UserForm());

        return "view/public/signup";
    }

    @RequestMapping(value = "/public/signup_confirm", method = RequestMethod.POST)
    @Transactional
    public String createUser(@ModelAttribute("user") @Valid UserForm form, BindingResult result) {
        if (!result.hasErrors()) {


            // check if email already exists
            if (userRepository.isEmailAlreadyExists(form.getEmail())){
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

            return "view/public/signup";

        }

        return "redirect:/login";
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
    public String activation(@RequestParam String mail, @RequestParam String code ){
        if (userRepository.isSecurityCodeValid(mail, code)){
            User user = userRepository.findUserByEmail(mail);
            user.setEnabled(true);

            securityCodeRepository.deleteSecurityCode(user.getSecurityCode());
            user.setSecurityCode(null);
            userRepository.update(user);
            UserDetails userDetails = myUserDetailsService.loadUserByUsername(user.getEmail());

            Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, user.getPassword());
            SecurityContextHolder.getContext().setAuthentication(auth);
            userSessionComponent.setCurrentUser(user);

            return "view/user/profile";
        }
        return "view/error/error";

    }





}
