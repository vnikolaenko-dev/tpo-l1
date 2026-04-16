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

public class CompetitorAdsPage extends Page {

    private WebDriverWait wait;
    private String lastCreatedTaskId;

    public CompetitorAdsPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @FindBy(xpath = "//*[@id='sp_side_menu']/div[1]/div[1]/section[4]/ul/li[1]/a")
    private WebElement parserMenuLink;

    @FindBy(xpath = "//*[@id='tools']/div[15]/div/div[2]/div[2]/a")
    private WebElement competitorAdsTool;

    @FindBy(xpath = "//*[@id='tool_search_competitor_ads']/div[1]/div/div[2]/div/div[1]/div/div[3]/div/div[3]/textarea")
    private WebElement manualInputTrigger;

    @FindBy(xpath = "/html/body/div[14]/div/div[2]/div/div[2]/div[2]/form/textarea")
    private WebElement modalTextarea;

    @FindBy(xpath = "/html/body/div[14]/div/div[2]/div/div[3]/div/button[1]")
    private WebElement addButton;

    @FindBy(xpath = "//*[@id='tool_search_competitor_ads']/div[1]/div/div[2]/div/div[5]/div/button")
    private WebElement startButton;

    @FindBy(xpath = "//*[@id='tool_search_competitor_ads']/div[1]/div/div[2]/div/div[1]/div/div[1]/div/div[3]/div/label")
    private WebElement fileUploadButton;

    @FindBy(xpath = "//input[@type='file']")
    private WebElement fileInput;

    @FindBy(xpath = "//*[@id='section_requests']/div/span")
    private WebElement tasksTable;

    public void navigateToCompetitorAds() {
        parserMenuLink.click();
        wait.until(ExpectedConditions.elementToBeClickable(competitorAdsTool)).click();
    }

    public void openManualInputModal() {
        wait.until(ExpectedConditions.elementToBeClickable(manualInputTrigger)).click();
    }

    public void enterDomains(String domains) {
        WebElement input = wait.until(ExpectedConditions.visibilityOf(modalTextarea));
        input.clear();
        input.sendKeys(domains);
    }

    public void clickTasksTable() {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(tasksTable));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    public void clickAddButton() {
        wait.until(ExpectedConditions.elementToBeClickable(addButton)).click();
    }

    public void clickStartButton() {
        wait.until(ExpectedConditions.elementToBeClickable(startButton)).click();
    }

    public void uploadFile(String absoluteFilePath) {
        fileUploadButton.click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@type='file']")));
        fileInput.sendKeys(absoluteFilePath);
    }

    public void waitForTasksTable() {
        wait.until(driver -> {
            try {
                WebElement table = driver.findElement(By.xpath("//*[@id='tool_search_competitor_ads']/div[1]/div/div[4]/div/table"));
                return table.findElements(By.xpath(".//tbody/tr")).size() > 0;
            } catch (NoSuchElementException e) {
                return false;
            }
        });
    }

    public void createTaskWithManualInput(String domains) {
        navigateToCompetitorAds();
        openManualInputModal();
        enterDomains(domains);
        clickAddButton();
        clickStartButton();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        clickTasksTable();
        waitForTasksTable();
        captureTaskId();
    }

    public void createTaskWithFileUpload(String filePath) {
        navigateToCompetitorAds();
        uploadFile(filePath);
        clickStartButton();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        clickTasksTable();
        waitForTasksTable();
        captureTaskId();
    }

    private void captureTaskId() {
        WebElement firstRow = driver.findElement(By.xpath("//*[@id='tool_search_competitor_ads']/div[1]/div/div[4]/div/table/tbody/tr[1]"));
        lastCreatedTaskId = firstRow.getAttribute("id");
    }

    public boolean waitForTaskCompletion(int timeoutSeconds) {
        long endTime = System.currentTimeMillis() + timeoutSeconds * 1000L;
        int refreshCount = 0;

        while (System.currentTimeMillis() < endTime) {
            try {
                if (refreshCount % 2 == 0) {
                    driver.navigate().refresh();
                    try {
                        Thread.sleep(2000); 
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    clickTasksTable(); 
                    waitForTasksTable(); 
                }
                refreshCount++;

                WebElement firstRow = driver.findElement(By.xpath("//*[@id='tool_search_competitor_ads']/div[1]/div/div[4]/div/table/tbody/tr[1]"));
                WebElement statusCell = firstRow.findElement(By.xpath("./td[4]"));
                String statusText = statusCell.getText();

                if (statusText.toLowerCase().contains("выполнен") || statusText.toLowerCase().contains("нет данных")) {
                    return true;
                }

                Thread.sleep(5000);
            } catch (Exception e) {
                break;
            }
        }
        return false;
    }
}