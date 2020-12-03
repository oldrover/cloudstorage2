package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.NoteForm;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.service.NoteService;
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

    private UserService userService;
    private NoteService noteService;
    //private FileService fileService;
    //private CredentialService credentialService;


    public HomeController(UserService userService, NoteService noteService) {
        this.userService = userService;
        this.noteService = noteService;
    }

    @GetMapping
    public String getHomePage(Authentication auth, NoteForm noteForm, Model model) {
        User user = userService.getUser(auth.getName());
        model.addAttribute("notes", this.noteService.getNotes(user.getUserId()));
        return "home";
    }

    @PostMapping
    public String saveNote(Authentication auth, NoteForm noteForm, Model model) {
        User user = userService.getUser(auth.getName());
        this.noteService.addNote(user, noteForm);
        model.addAttribute("notes", this.noteService.getNotes(user.getUserId()));
        return "home";
    }
}
