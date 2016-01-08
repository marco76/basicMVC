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

package ch.javaee.basicMvcAng.web.controller;


import ch.javaee.basicMvcAng.repository.UserRepository;
import ch.javaee.basicMvcAng.web.component.UserSessionComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping
public class AccessController {

    @Autowired
    private UserSessionComponent userSessionComponent;
    @Autowired
    private UserRepository userRepository;


    @RequestMapping("/login")
    public String login(Model model, @RequestParam(required = false) String message) {

        model.addAttribute("message", message);
        return "view/public/login";
    }

    @RequestMapping("/login/success")
    public String loginSuccess() {
        userSessionComponent.setCurrentUser(userRepository.findUserByUsername(((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()));
        return "view/user/profile";
    }

    @RequestMapping(value = "/login/failure")
    public String loginFailure(Model model) {
        String message = "Login Failure!";
        model.addAttribute("loginError", true);
        return "view/public/login";
    }

    @RequestMapping(value = "/logout/success")
    public String logoutSuccess() {
        return "view/public/logout";
    }

    @RequestMapping("/login/error")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        return "view/public/login";
    }

    @RequestMapping("/error")
    public String loginError() {

        return "view/error/error";
    }

}
