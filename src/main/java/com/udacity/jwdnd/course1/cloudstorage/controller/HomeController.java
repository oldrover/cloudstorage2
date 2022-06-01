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
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    UserService userService;

    @Autowired
    NoteService noteService;

    @Autowired
    CredentialService credentialService;

    @Autowired
    FileService fileService;

    @GetMapping
    public String getHomePage(Authentication auth, NoteForm noteForm, CredentialForm credentialForm, Model model) {
        User user = userService.getUser(auth.getName());
        model.addAttribute("files", fileService.getFiles(user.getUserId()));
        model.addAttribute("notes", noteService.getNotes(user.getUserId()));
        model.addAttribute("credentials", credentialService.getCredentials(user.getUserId()));
        return "home";
    }

    //Mapping for adding or updating a Note
    @PostMapping("/note")
    public String addOrUpdateNote(NoteForm noteForm, Authentication auth, Model model) {
        User user = userService.getUser(auth.getName());
        if(noteForm.getNoteId() == null) {
            if(this.noteService.addNote(user, noteForm) != null) {
                model.addAttribute("success",true);
                model.addAttribute("successMessage","Note saved!");

            }else{
                model.addAttribute("success",false);
                model.addAttribute("errorMessage","Note was not saved!");
            }
        }
        else {
            if(this.noteService.updateNote(user, noteForm) != null) {
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
    public String deleteNote(@PathVariable Long noteId, Model model){
        this.noteService.deleteNote(noteId);
                model.addAttribute("success",true);
                model.addAttribute("successMessage","Note deleted!");

        return "result";
    }

    //Mapping for adding or updating a credential
    @PostMapping("/credential")
    public String addOrUpdateCredential(CredentialForm credentialForm, Authentication auth, Model model) {
        User user = userService.getUser(auth.getName());
        if(credentialForm.getCredentialId() == null) {
            if(credentialService.addCredential(user, credentialForm) != null) {
                model.addAttribute("success",true);
                model.addAttribute("successMessage","Credential saved!");

            }else {
                model.addAttribute("success",false);
                model.addAttribute("successMessage","Credential not saved!");

            }
        }
        else {
            if(this.credentialService.updateCredential(user, credentialForm) != null) {
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
    public String deleteCredential(@PathVariable Long credentialId, Model model){
        credentialService.deleteCredential(credentialId);
        model.addAttribute("success",true);
        model.addAttribute("successMessage","Credential deleted!");

        return "result";
    }

    //Mapping for uploading a file
    @PostMapping("/file")
    public String uploadFile(@RequestParam("fileUpload") MultipartFile fileUpload, Authentication auth, Model model) throws IOException {
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

        if(fileService.addFile(
                new FileData(null,
                        fileUpload.getOriginalFilename(),
                        fileUpload.getContentType(),
                        Long.toString(fileUpload.getSize()),
                        user.getUserId(),
                        fileUpload.getBytes())) == null) {
            model.addAttribute("success",true);
            model.addAttribute("successMessage","File saved!");

        }else {
            model.addAttribute("success",false);
            model.addAttribute("successMessage","File was not saved!");


        }
        return "result";

    }

    //Mapping for downloading a file
    @GetMapping("/file/view/{fileId}")
    public ResponseEntity<ByteArrayResource> viewFile(@PathVariable Long fileId, Authentication auth, Model model) {
        User user = userService.getUser(auth.getName());
        FileData file = fileService.viewFile(user.getUserId(), fileId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\"" + file.getFileName() + "\"")
                .body(new ByteArrayResource(file.getFile()));
    }

    //Mapping for deleting a file
    @PostMapping("/file/delete/{fileId}")
    public String deleteFile(@PathVariable Long fileId, Model model) {
        //if(this.fileService.deleteFile(fileId)) {
            model.addAttribute("success",true);
            model.addAttribute("successMessage","File successfully deleted!");
        return "result";
    }


}
