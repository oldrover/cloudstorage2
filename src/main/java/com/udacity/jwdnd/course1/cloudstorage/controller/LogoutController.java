package com.udacity.jwdnd.course1.cloudstorage.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/logout")
public class LogoutController {

    @GetMapping
    public String logout(Authentication auth, HttpServletRequest req, HttpServletResponse res) {
        if(auth != null) {
            new SecurityContextLogoutHandler().logout(req,res,auth);
        }
        return "/login";


    }
}
