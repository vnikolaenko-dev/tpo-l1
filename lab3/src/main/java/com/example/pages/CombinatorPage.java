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

public class CombinatorPage extends Page {

    private WebDriverWait wait;
    private String lastCreatedTaskId;

    public CombinatorPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @FindBy(xpath = "//*[@id='sp_side_menu']/div[1]/div[1]/section[4]/ul/li[1]/a")
    private WebElement parserMenuLink;

    @FindBy(xpath = "//*[@id='tools']/div[5]/div/div[2]/div[2]/a")
    private WebElement combinatorTool;

    @FindBy(xpath = "//*[@id='combinator_block']/div[1]/textarea")
    private WebElement field1;

    @FindBy(xpath = "//*[@id='combinator_block']/div[3]/textarea")
    private WebElement field2;

    @FindBy(xpath = "//*[@id='combinator_block']/div[5]/textarea")
    private WebElement field3;

    @FindBy(xpath = "//*[@id='combinator_block']/div[7]/textarea")
    private WebElement field4;

    @FindBy(xpath = "//*[@id='form_request_parameters']/div[3]/div[1]")
    private WebElement startButton;

    @FindBy(xpath = "//*[@id='lightbox-message']/span")
    private WebElement popupErrorMessage;

    public void navigateToCombinator() {
        parserMenuLink.click();
        wait.until(ExpectedConditions.elementToBeClickable(combinatorTool)).click();
    }

    public void enterWords(String word1, String word2, String word3, String word4) {
        field1.clear();
        field1.sendKeys(word1);
        field2.clear();
        field2.sendKeys(word2);
        field3.clear();
        field3.sendKeys(word3);
        field4.clear();
        field4.sendKeys(word4);
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

    public void createCombinatorTask(String word1, String word2, String word3, String word4) {
        navigateToCombinator();
        enterWords(word1, word2, word3, word4);
        clickStartButton();
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