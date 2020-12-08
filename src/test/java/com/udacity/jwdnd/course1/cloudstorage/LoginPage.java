package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage {

    @FindBy(id= "inputUsername")
    private WebElement inputUsername;

    @FindBy(id= "inputPassword")
    private WebElement inputPassword;

    @FindBy(id= "submitButton")
    private WebElement submitButton;

    @FindBy(id= "signupLink")
    private WebElement signupLink;

    public LoginPage(WebDriver webDriver) {
        PageFactory.initElements(webDriver, this);
    }

    public void setUsername(String username) {
        inputUsername.sendKeys(username);
    }

    public void setPassword(String password) {
        inputPassword.sendKeys(password);
    }

    public void clickLoginButton() {
        submitButton.click();
    }

    public void clickSignupLink() {
        signupLink.click();

    }
}
