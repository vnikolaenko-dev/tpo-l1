import com.example.Utils;
import com.example.TestConfig;
import com.example.pages.HomePage;
import com.example.pages.VkPage;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class VkParserTest {

    private static Utils utils;
    private static WebDriver driver;
    private static HomePage homePage;
    private static VkPage vkPage;

    @BeforeAll
    public static void setUp() {
        utils = new Utils();
        utils.setupDriver();
        driver = utils.getDriver();
        homePage = new HomePage(driver);
        vkPage = new VkPage(driver);

        driver.get(TestConfig.getUrlHome());
        homePage.clickLoginOpenButton();
        utils.getWaitTime().until(d -> homePage.isLoginFormDisplayed());
        homePage.enterCredentialsAndSubmit(TestConfig.getValidLogin(), TestConfig.getValidPassword());
        utils.getWaitTime().until(d -> driver.getCurrentUrl().startsWith(TestConfig.getUrlProjects()));
    }

    @AfterAll
    public static void tearDown() {
        if (utils != null) {
            utils.quitDriver();
        }
    }

    // @BeforeEach
    // public void ensureLoggedIn() {
    //     if (!driver.getCurrentUrl().startsWith(TestConfig.getUrlProjects())) {
    //         driver.get(TestConfig.getUrlHome());
    //         homePage.clickLoginOpenButton();
    //         utils.getWaitTime().until(d -> homePage.isLoginFormDisplayed());
    //         homePage.enterCredentialsAndSubmit(TestConfig.getValidLogin(), TestConfig.getValidPassword());
    //         utils.getWaitTime().until(d -> driver.getCurrentUrl().startsWith(TestConfig.getUrlProjects()));
    //     }
    // }

    @Test
    @Order(1)
    @DisplayName("TS-09-01: Задача на парсинг сообщества ВК создана и выполнена")
    public void testVkCommunityParsingSuccess() {
        driver.get(TestConfig.getUrlProjects());
        vkPage.createVkParsingTask(TestConfig.getTestVkCommunityUrl());
        boolean completed = vkPage.waitForTaskCompletion(300);
        assertTrue(completed);
    }
}