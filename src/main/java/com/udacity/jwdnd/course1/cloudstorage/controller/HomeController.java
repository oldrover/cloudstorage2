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

    //Mapping for adding or updating a Note
    @PostMapping("/note")
    public String addOrUpdateNote(@RequestParam(required = false) Integer id, NoteForm noteForm, Authentication auth, Model model) {
        User user = userService.getUser(auth.getName());
        if(noteForm.getNoteId() == null) {
            if(this.noteService.addNote(user, noteForm) == 1) {
                model.addAttribute("success",true);
                model.addAttribute("successMessage","Note saved!");

            }else{
                model.addAttribute("success",false);
                model.addAttribute("errorMessage","Note was not saved!");
            }
        }
        else {
            if(this.noteService.updateNote(noteForm) == 1) {
                model.addAttribute("success",true);
                model.addAttribute("successMessage","Your changes were successfully saved!");

            }else{
                model.addAttribute("success",false);
                model.addAttribute("errorMessage","Changes were not saved!");
            }

        }
        return "result";
    }

    //Mapping for deleting a Note
    @PostMapping("/note/delete/{noteId}")
    public String deleteNote(@PathVariable Integer noteId, Authentication auth, Model model){
        User user = userService.getUser(auth.getName());
        if(this.noteService.deleteNote(noteId) == 1) {
                model.addAttribute("success",true);
                model.addAttribute("successMessage","Note deleted!");

            }else{
                model.addAttribute("success",false);
                model.addAttribute("errorMessage","Note was not deleted!");
            }
        return "result";
    }

    //Mapping for adding or updating a credential
    @PostMapping("/credential")
    public String addOrUpdateCredential(@RequestParam(required = false) Integer credentialId, CredentialForm credentialForm, Authentication auth, Model model) {
        User user = userService.getUser(auth.getName());
        if(credentialForm.getCredentialId() == null) {
            if(this.credentialService.addCredential(user, credentialForm) == 1) {
                model.addAttribute("success",true);
                model.addAttribute("successMessage","Credential saved!");

            }else {
                model.addAttribute("success",false);
                model.addAttribute("successMessage","Credential not saved!");

            }
        }
        else {
            if(this.credentialService.updateCredential(credentialForm) == 1) {
                model.addAttribute("success",true);
                model.addAttribute("successMessage","Your changes were successfully saved!");
            }else {
                model.addAttribute("success",false);
                model.addAttribute("successMessage","Changes were not saved!");
            }
        }
        return "result";
    }

    //Mapping for deleting a credential
    @PostMapping("/credential/delete/{credentialId}")
    public String deleteCredential(@PathVariable Integer credentialId, Authentication auth, Model model){
        User user = userService.getUser(auth.getName());
        if(this.credentialService.deleteCredential(credentialId) == 1) {
            model.addAttribute("success",true);
            model.addAttribute("successMessage","Credential deleted!");

        }else{
            model.addAttribute("success",false);
            model.addAttribute("errorMessage","Credential was not deleted!");
        }
        return "result";
    }


    //Mapping for uploading a file
    @PostMapping("/file")
    public String uploadFile(@RequestParam("fileUpload") MultipartFile fileUpload, Authentication auth,NoteForm noteForm, CredentialForm credentialForm, Model model) throws IOException {
        User user = userService.getUser(auth.getName());
        if(fileUpload.isEmpty()) {
            model.addAttribute("success",false);
            model.addAttribute("errorMessage","No file selected to upload!");
            return "result";

        }
        if(!fileService.isFileNameAvailable(user.getUserId(), fileUpload.getOriginalFilename())){
            model.addAttribute("success",false);
            model.addAttribute("errorMessage","Filename already exists!");
            return "result";

        }

        if(fileService.addFile(new FileData(null,fileUpload.getOriginalFilename(),fileUpload.getContentType(),Long.toString(fileUpload.getSize()),user.getUserId(),fileUpload.getBytes())) == 1) {
            model.addAttribute("success",true);
            model.addAttribute("successMessage","File saved!");
            return "result";
        }else {
            model.addAttribute("success",false);
            model.addAttribute("successMessage","File was not saved!");
            return "result";

        }

    }

    //Mapping for downloading a file
    @GetMapping("/file/view/{fileId}")
    public ResponseEntity<ByteArrayResource> viewFile(@PathVariable Integer fileId, Authentication auth) {
        User user = userService.getUser(auth.getName());
        FileData file = fileService.viewFile(fileId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\"" + file.getFileName() + "\"")
                .body(new ByteArrayResource(file.getFile()));
    }

    //Mapping for deleting a file
    @PostMapping("/file/delete/{fileId}")
    public String deleteFile(@PathVariable Integer fileId, Authentication auth, Model model) {
        User user = userService.getUser(auth.getName());
        if(this.fileService.deleteFile(fileId) == 1) {
            model.addAttribute("success",true);
            model.addAttribute("successMessage","File successfully deleted!");

        }else {
            model.addAttribute("success",false);
            model.addAttribute("successMessage","File not deleted!");
        }


        return "result";
    }


}
