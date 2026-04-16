package com.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.NoSuchElementException;

public class MetaTagsPage extends Page {

    private WebDriverWait wait;
    private String lastCreatedTaskId;

    public MetaTagsPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @FindBy(xpath = "//*[@id='sp_side_menu']/div[1]/div[1]/section[4]/ul/li[1]/a")
    private WebElement parserMenuLink;

    @FindBy(xpath = "//*[@id='tools']/div[8]/div/div[2]/div[2]/a")
    private WebElement metaTagsTool;

    @FindBy(xpath = "//*[@id='form_request_parameters']/div/div[5]/textarea")
    private WebElement manualInputTrigger;

    @FindBy(xpath = "//*[@id='resize_textarea']")
    private WebElement modalTextarea;

    @FindBy(xpath = "//*[@id='lightbox_buttons']/button[1]")
    private WebElement okButton;

    @FindBy(xpath = "//*[@id='accordion_settings']/div[2]/div[1]")
    private WebElement startButton;

    @FindBy(xpath = "//*[@id='form_request_parameters']/div/div[3]/div[3]")
    private WebElement fileUploadButton;

    @FindBy(xpath = "//input[@type='file']")
    private WebElement fileInput;

    @FindBy(xpath = "//*[@id='lightbox-message']/span")
    private WebElement popupErrorMessage;

    @FindBy(xpath = "//*[@id='lightbox_buttons']/button[1]")
    private WebElement submitButton;


    public void navigateToMetaTags() {
        parserMenuLink.click();
        wait.until(ExpectedConditions.elementToBeClickable(metaTagsTool)).click();
    }

    public void openManualInputModal() {
        wait.until(ExpectedConditions.elementToBeClickable(manualInputTrigger)).click();
    }

    public void enterUrls(String urls) {
        WebElement input = wait.until(ExpectedConditions.visibilityOf(modalTextarea));
        input.clear();
        input.sendKeys(urls);
    }

    public void clickSubmitButton() {
        wait.until(ExpectedConditions.elementToBeClickable(submitButton)).click();
    }

    public void clickOkButton() {
        wait.until(ExpectedConditions.elementToBeClickable(okButton)).click();
    }

    public void uploadFile(String absoluteFilePath) {
        fileUploadButton.click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@type='file']")));
        fileInput.sendKeys(absoluteFilePath);
    }

    public void clickStartButton() {
        wait.until(ExpectedConditions.elementToBeClickable(startButton)).click();
    }

    public void clickStartButtonWithoutData() {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(startButton));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
    }

    public void waitForTasksTable() {
        wait.until(driver -> {
            try {
                WebElement table = driver.findElement(By.xpath("//table[@id='request_list_grid']"));
                return table.findElements(By.xpath(".//tbody/tr")).size() > 0;
            } catch (NoSuchElementException e) {
                return false;
            }
        });
    }

    public void createTaskWithManualInput(String urls) {
        navigateToMetaTags();
        openManualInputModal();
        enterUrls(urls);
        clickOkButton();
        clickStartButton();
        clickSubmitButton();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        waitForTasksTable();
        captureTaskId();
    }

    public void createTaskWithFileUpload(String filePath) {
        navigateToMetaTags();
        uploadFile(filePath);
        clickStartButton();
        clickSubmitButton();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        waitForTasksTable();
        captureTaskId();
    }

    private void captureTaskId() {
        WebElement firstRow = driver.findElement(By.xpath("//table[@id='request_list_grid']/tbody/tr[1]"));
        lastCreatedTaskId = firstRow.getAttribute("id");
    }

    public boolean waitForTaskCompletion(int timeoutSeconds) {
        long endTime = System.currentTimeMillis() + timeoutSeconds * 1000L;
        int refreshCount = 0;

        while (System.currentTimeMillis() < endTime) {
            try {
                if (refreshCount % 2 == 0) {
                    driver.navigate().refresh();
                    waitForTasksTable();
                }
                refreshCount++;

                WebElement table = driver.findElement(By.xpath("//table[@id='request_list_grid']"));
                WebElement firstRow = table.findElement(By.xpath(".//tbody/tr[1]"));
                WebElement statusCell = firstRow.findElement(By.xpath("./td[4]/label"));
                String statusText = statusCell.getText();

                if (statusText.toLowerCase().contains("выполнен")) {
                    return true;
                }

                Thread.sleep(5000);
            } catch (Exception e) {
                break;
            }
        }
        return false;
    }

    public boolean isErrorMessageDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(popupErrorMessage));
            return popupErrorMessage.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}