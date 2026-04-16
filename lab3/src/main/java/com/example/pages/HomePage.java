package com.example.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;


public class HomePage extends Page {

    public HomePage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//*[@id='top-panel']/div/div/div/div[3]/div/ul/li[1]/a")
    private WebElement loginOpenButton;

    @FindBy(xpath = "//*[@id='login-uname']")
    private WebElement loginInput;

    @FindBy(xpath = "//*[@id='login-pass']")
    private WebElement passwordInput;

    @FindBy(xpath = "//*[@id='login-btn']")
    private WebElement submitLoginButton;

    @FindBy(xpath = "//*[@id='login-pass-error']")
    private WebElement loginErrorMessage;

    @FindBy(xpath = "//*[@id='right-top-menu-uname']")
    private WebElement profileButton;

    @FindBy(xpath = "//*[@id='logout']")
    private WebElement logoutButton;

    public void clickLoginOpenButton() {
        loginOpenButton.click();
    }

    public void enterCredentialsAndSubmit(String login, String password) {
        loginInput.clear();
        loginInput.sendKeys(login);
        passwordInput.clear();
        passwordInput.sendKeys(password);
        submitLoginButton.click();
    }

    public boolean isLoginFormDisplayed() {
        try {
            return loginInput.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isErrorMessageDisplayed() {
        try {
            return loginErrorMessage.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getErrorMessageText() {
        return loginErrorMessage.getText();
    }

    public void clickLogoutButton() {
        profileButton.click();
        logoutButton.click();
    }
}