package com.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class Utils {

    private static final Browser SELECTED_BROWSER = Browser.CHROME;

    public enum Browser {
        CHROME,
        FIREFOX
    }

    public WebDriver driver;
    public JavascriptExecutor js;
    public WebDriverWait wait;

    public void setupDriver() {
        switch (SELECTED_BROWSER) {
            case CHROME -> {
                WebDriverManager.chromedriver().browserVersion("146").setup();
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--disable-blink-features=AutomationControlled");
                options.addArguments("--disable-infobars");
                options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
                options.setExperimentalOption("useAutomationExtension", false);
                options.addArguments("--no-sandbox");
                options.addArguments("--disable-dev-shm-usage");
                options.addArguments("--disable-gpu");
                options.addArguments("--remote-allow-origins=*");
                driver = new ChromeDriver(options);
            }
            case FIREFOX -> {
                WebDriverManager.firefoxdriver().setup();
                driver = new FirefoxDriver();
            }
        }

        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(15));
        driver.manage().window().setSize(new Dimension(1280, 800));
        js = (JavascriptExecutor) driver;
        driver.get("https://promopult.ru/");
    }

    public WebDriver getDriver() {
        return driver;
    }

    public WebDriverWait getWaitTime() {
        return wait;
    }

    public JavascriptExecutor getJsExecutor() {
        return js;
    }

    public void quitDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
}