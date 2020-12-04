package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.CredentialForm;
import com.udacity.jwdnd.course1.cloudstorage.model.NoteForm;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.service.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.service.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/home")
public class HomeController {

    private UserService userService;
    private NoteService noteService;
    private CredentialService credentialService;
    //private FileService fileService;


    public HomeController(UserService userService, NoteService noteService, CredentialService credentialService) {
        this.userService = userService;
        this.noteService = noteService;
        this.credentialService = credentialService;
    }

    @GetMapping
    public String getHomePage(Authentication auth, NoteForm noteForm, CredentialForm credentialForm, Model model) {
        User user = userService.getUser(auth.getName());
        model.addAttribute("notes", this.noteService.getNotes(user.getUserId()));
        model.addAttribute("credentials", this.credentialService.getCredentials(user.getUserId()));
        return "home";
    }


    @PostMapping
    public String homePageAction(@RequestParam String action, Authentication auth, NoteForm noteForm, CredentialForm credentialForm, Model model) {
        User user = userService.getUser(auth.getName());

        switch (action) {
            case "addn":
                this.noteService.addNote(user, noteForm);
                break;

            case "deln":
                break;

            case "addc":
                this.credentialService.addCredential(user, credentialForm);
                break;

            case "delc":
                break;

            default:

        }
        model.addAttribute("notes", this.noteService.getNotes(user.getUserId()));
        model.addAttribute("credentials", this.credentialService.getCredentials(user.getUserId()));
        return "home";
    }


    /*
    @RequestMapping(value="/note", method = RequestMethod.POST)
    public String saveNote(Authentication auth, NoteForm noteForm, CredentialForm credentialForm, Model model) {
        User user = userService.getUser(auth.getName());

        this.noteService.addNote(user, noteForm);
        model.addAttribute("notes", this.noteService.getNotes(user.getUserId()));

        return "home";
    }

    @RequestMapping(value="/credential", method = RequestMethod.POST)
    public String saveCredential(Authentication auth,NoteForm noteForm, CredentialForm credentialForm, Model model) {
        User user = userService.getUser(auth.getName());



        return "home";
    }
    */
}
