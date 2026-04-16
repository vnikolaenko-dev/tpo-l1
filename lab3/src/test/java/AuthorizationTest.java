import com.example.Utils;
import com.example.TestConfig;
import com.example.pages.HomePage;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthorizationTest {

    private static Utils utils;
    private static WebDriver driver;
    private static HomePage homePage;

    @BeforeAll
    public static void setUp() {
        utils = new Utils();
        utils.setupDriver();
        driver = utils.getDriver();
        homePage = new HomePage(driver);
    }

    @AfterAll
    public static void tearDown() {
        if (utils != null) {
            utils.quitDriver();
        }
    }

    @Test
    @Order(1)
    @DisplayName("TS-01-03: Невалидные данные для входа")
    public void invalidLoginTest() {
        driver.get(TestConfig.getUrlHome());
        homePage.clickLoginOpenButton();
        utils.getWaitTime().until(d -> homePage.isLoginFormDisplayed());
        homePage.enterCredentialsAndSubmit(TestConfig.getInvalidLogin(), TestConfig.getInvalidPassword());
        utils.getWaitTime().until(d -> homePage.isErrorMessageDisplayed());
        assertTrue(homePage.isErrorMessageDisplayed());
    }

    @Test
    @Order(2)
    @DisplayName("TS-01-01: Успешная авторизация")
    public void successfulLoginTest() {
        driver.get(TestConfig.getUrlHome());
        homePage.clickLoginOpenButton();
        utils.getWaitTime().until(d -> homePage.isLoginFormDisplayed());
        homePage.enterCredentialsAndSubmit(TestConfig.getValidLogin(), TestConfig.getValidPassword());
        utils.getWaitTime().until(d -> driver.getCurrentUrl().startsWith(TestConfig.getUrlProjects()));
        assertEquals(TestConfig.getUrlProjects(), driver.getCurrentUrl());
    }

    @Test
    @Order(3)
    @DisplayName("TS-01-02: Выход из системы")
    public void logoutTest() {
        homePage.clickLogoutButton();
        utils.getWaitTime().until(d -> driver.getCurrentUrl().equals(TestConfig.getUrlHome()) ||
                                        driver.getCurrentUrl().startsWith(TestConfig.getUrlHome() + "?"));
        assertTrue(driver.getCurrentUrl().startsWith(TestConfig.getUrlHome()));
    }
}