package com.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.NoSuchElementException;

public class VkPage extends Page {

    private WebDriverWait wait;
    private String lastCreatedTaskId;

    public VkPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @FindBy(xpath = "//*[@id='sp_side_menu']/div[1]/div[1]/section[4]/ul/li[1]/a")
    private WebElement parserMenuLink;

    @FindBy(xpath = "//*[@id='tools']/div[3]/div/div[2]/div[2]/a")
    private WebElement vkCommunityParserTool;

    @FindBy(xpath = "//*[@id='form_request_parameters']/div[1]/div[1]/div/div[3]/textarea")
    private WebElement initialTextarea;

    @FindBy(xpath = "//*[@id='resize_textarea']")
    private WebElement modalTextarea;

    @FindBy(xpath = "//*[@id='lightbox_buttons']/button[1]")
    private WebElement okButton;

    @FindBy(xpath = "//*[@id='form_request_parameters']/div[3]/div[1]")
    private WebElement startParsingButton;

    public void navigateToVkParser() {
        parserMenuLink.click();
        wait.until(ExpectedConditions.elementToBeClickable(vkCommunityParserTool)).click();
    }

    public void openCommunityInputModal() {
        wait.until(ExpectedConditions.elementToBeClickable(initialTextarea)).click();
    }

    public void enterCommunityUrl(String url) {
        WebElement input = wait.until(ExpectedConditions.visibilityOf(modalTextarea));
        input.clear();
        input.sendKeys(url);
    }

    public void clickOkButton() {
        wait.until(ExpectedConditions.elementToBeClickable(okButton)).click();
    }

    public void clickStartParsing() {
        wait.until(ExpectedConditions.elementToBeClickable(startParsingButton)).click();
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

    public void createVkParsingTask(String communityUrl) {
        navigateToVkParser();
        openCommunityInputModal();
        enterCommunityUrl(communityUrl);
        clickOkButton();
        clickStartParsing();
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

                Thread.sleep(10000);
            } catch (Exception e) {
                break;
            }
        }
        return false;
    }
}
