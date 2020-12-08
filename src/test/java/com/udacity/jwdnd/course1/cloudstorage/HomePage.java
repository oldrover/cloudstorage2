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

}
