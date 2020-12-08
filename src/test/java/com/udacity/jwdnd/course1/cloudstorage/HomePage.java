package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class HomePage {

    @FindBy(id= "logoutButton")
    private WebElement logoutButton;

    @FindBy(id= "nav-notes-tab")
    private WebElement navNotesTab;

    @FindBy(id= "nav-credentials-tab")
    private WebElement navCredentialsTab;

    @FindBy(id= "buttonCreateNote")
    private WebElement buttonCreateNote;

    @FindBy(id= "buttonCreateCredential")
    private WebElement buttonCreateCredential;

    @FindBy(id= "note-description")
    private WebElement noteDescription;

    @FindBy(id= "note-title")
    private WebElement noteTitle;

    @FindBy(id= "submitNote")
    private WebElement submitNote;

    @FindBy(id= "buttonDeleteNote")
    private WebElement buttonDeleteNote;

    @FindBy(id= "buttonEditNote")
    private WebElement buttonEditNote;

    @FindBy(id= "credential-url")
    private WebElement credentialUrl;

    @FindBy(id= "credential-username")
    private WebElement credentialUsername;

    @FindBy(id= "credential-password")
    private WebElement credentialPassword;

    @FindBy(id= "submitCredential")
    private WebElement submitCredential;

    @FindBy(id= "buttonDeleteCredential")
    private WebElement buttonDeleteCredential;

    @FindBy(id= "buttonEditCredential")
    private WebElement buttonEditCredential;


    public HomePage(WebDriver webDriver) {
        PageFactory.initElements(webDriver, this);
    }

    public void clickLogoutButton() {
        logoutButton.click();
    }

    public void clickNotesTab() {
        navNotesTab.click();
    }

    public void clickCredentialsTab() {
        navCredentialsTab.click();
    }

    public void clickCreateNote() {
        buttonCreateNote.click();
    }

    public void clickCreateCredential() {
        buttonCreateCredential.click();
    }

    public void setNoteTitle(String title) {
        noteTitle.sendKeys(title);
    }

    public void setNoteDescription(String description) {
        noteDescription.sendKeys(description);
    }

    public void clickNoteSubmit() {
        submitNote.click();
    }

    public void clickDeleteNote() {
        buttonDeleteNote.click();
    }

    public void clickEditNote() {
        buttonEditNote.click();
    }

    public void clearNoteTitle() {
        noteTitle.clear();
    }

    public void clearNoteDescription() {
        noteDescription.clear();
    }

    public void setCredentialUrl(String url) {
        credentialUrl.sendKeys(url);

    }

    public void setCredentialUsername(String username) {
        credentialUsername.sendKeys(username);
    }

    public void setCredentialPassword(String password) {
        credentialPassword.sendKeys(password);
    }

    public void clickCredentialSubmit() {
        submitCredential.click();
    }

    public void clickDeleteCredential() {
        buttonDeleteCredential.click();
    }

    public void clickEditCredential() {
        buttonEditCredential.click();
    }

    public void clearCredentialUrl() {
        credentialUrl.clear();
    }

    public void clearCredentialUsername() {
        credentialUsername.clear();
    }

    public void clearCredentialPassword() {
        credentialPassword.clear();
    }



}
