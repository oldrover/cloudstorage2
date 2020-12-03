package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
public class HomeController {

    //private NoteService noteService;
    //private FileService fileService;
    //private CredentialService credentialService;
    private UserService userService;

    public HomeController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String getHomePage(Model model) {
        //model.addAttribute("notes", this.noteService.getNotes());
        return "home";
    }

    @PostMapping
    public String saveNote(Authentication auth, Model model) {
        User user = userService.getUser(auth.getName());
        //model.addAttribute("notes", this.noteService.getNotes());
        return "home";
    }
}
