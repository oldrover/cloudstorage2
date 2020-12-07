package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.google.common.net.HttpHeaders;
import com.udacity.jwdnd.course1.cloudstorage.model.CredentialForm;
import com.udacity.jwdnd.course1.cloudstorage.model.FileData;
import com.udacity.jwdnd.course1.cloudstorage.model.NoteForm;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.service.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.service.FileService;
import com.udacity.jwdnd.course1.cloudstorage.service.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.service.UserService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/home")
public class HomeController {

    private UserService userService;
    private NoteService noteService;
    private CredentialService credentialService;
    private FileService fileService;


    public HomeController(UserService userService, NoteService noteService, CredentialService credentialService, FileService fileService) {
        this.userService = userService;
        this.noteService = noteService;
        this.credentialService = credentialService;
        this.fileService = fileService;
    }

    @GetMapping
    public String getHomePage(Authentication auth, NoteForm noteForm, CredentialForm credentialForm, Model model) {
        User user = userService.getUser(auth.getName());
        model.addAttribute("files", this.fileService.getFiles(user.getUserId()));
        model.addAttribute("notes", this.noteService.getNotes(user.getUserId()));
        model.addAttribute("credentials", this.credentialService.getCredentials(user.getUserId()));
        return "home";
    }

    @PostMapping
    public String homePageAction(@RequestParam String action, @RequestParam(required = false) Integer id, Authentication auth, NoteForm noteForm, CredentialForm credentialForm, Model model) {
        User user = userService.getUser(auth.getName());

        switch (action) {
            case "delf":
                this.fileService.deleteFile(id);
                break;

            case "addorupc":
                if(credentialForm.getCredentialId() == null) {
                    this.credentialService.addCredential(user, credentialForm);
                }
                else {
                    this.credentialService.updateCredential(credentialForm);
                }
                break;

            case "delc":
                this.credentialService.deleteCredential(id);
                break;

        }
        model.addAttribute("files", this.fileService.getFiles(user.getUserId()));
        model.addAttribute("notes", this.noteService.getNotes(user.getUserId()));
        model.addAttribute("credentials", this.credentialService.getCredentials(user.getUserId()));
        return "home";


    }

    @PostMapping("/note")
    public String addOrUpdateNote(@RequestParam(required = false) Integer id, NoteForm noteForm, Authentication auth) {
        User user = userService.getUser(auth.getName());
        if(noteForm.getNoteId() == null) {
            this.noteService.addNote(user, noteForm);
        }
        else {
            this.noteService.updateNote(noteForm);
        }

        return "result";
    }

    @PostMapping("/note/delete/{noteId}")
    public String deleteNote(@PathVariable Integer noteId, Authentication auth){
        User user = userService.getUser(auth.getName());
        this.noteService.deleteNote(noteId);
        return "result";
    }


    @PostMapping("/file")
    public String uploadFile(@RequestParam("fileUpload") MultipartFile fileUpload, Authentication auth,NoteForm noteForm, CredentialForm credentialForm, Model model) throws IOException {
        User user = userService.getUser(auth.getName());
        if(fileUpload.isEmpty()) {
            model.addAttribute("success",false);
            model.addAttribute("message","No file selected to upload!");
            return "result";

        }
        fileService.addFile(new FileData(null,fileUpload.getOriginalFilename(),fileUpload.getContentType(),Long.toString(fileUpload.getSize()),user.getUserId(),fileUpload.getBytes()));
        return "result";
    }

    @GetMapping("/file/view/{fileId}")
    public ResponseEntity<ByteArrayResource> viewFile(@PathVariable Integer fileId, Authentication auth) {
        User user = userService.getUser(auth.getName());
        FileData file = fileService.viewFile(fileId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\"" + file.getFileName() + "\"")
                .body(new ByteArrayResource(file.getFile()));
    }

}
