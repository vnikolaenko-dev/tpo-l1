import com.example.Utils;
import com.example.TestConfig;
import com.example.pages.HomePage;
import com.example.pages.WordstatPage;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class WordstatParserTest {

    private static Utils utils;
    private static WebDriver driver;
    private static HomePage homePage;
    private static WordstatPage wordstatPage;

    @BeforeAll
    public static void setUp() {
        utils = new Utils();
        utils.setupDriver();
        driver = utils.getDriver();
        homePage = new HomePage(driver);
        wordstatPage = new WordstatPage(driver);

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
    @DisplayName("TS-04-01: Задача создана при помощи ручного ввода")
    public void testManualInputTask() {
        driver.get(TestConfig.getUrlProjects());
        wordstatPage.createTaskWithManualInput(TestConfig.getTestPhrases());
        boolean completed = wordstatPage.waitForTaskCompletion(600);
        assertTrue(completed);
    }

    @Test
    @Order(2)
    @DisplayName("TS-04-02: Задача создана при помощи загрузки документа")
    public void testFileUploadTask() {
        driver.get(TestConfig.getUrlProjects());
        wordstatPage.createTaskWithFileUpload(TestConfig.getTestFilePath());
        boolean completed = wordstatPage.waitForTaskCompletion(600);
        assertTrue(completed);
    }

    @Test
    @Order(3)
    @DisplayName("TS-04-03: Невалидные данные для создания задачи")
    public void testInvalidData() {
        driver.get(TestConfig.getUrlProjects());
        wordstatPage.navigateToWordstat();
        wordstatPage.clickStartButtonWithoutData();
        assertTrue(wordstatPage.isErrorMessageDisplayed());
    }
}