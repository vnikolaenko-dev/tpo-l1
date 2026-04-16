import com.example.Utils;
import com.example.TestConfig;
import com.example.pages.HomePage;
import com.example.pages.MetaTagsPage;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MetaTagsParserTest {

    private static Utils utils;
    private static WebDriver driver;
    private static HomePage homePage;
    private static MetaTagsPage metaTagsPage;
    private static final String URL_TAGS = "se.ifmo.ru\nvk.com\ngithub.com";

    @BeforeAll
    public static void setUp() {
        utils = new Utils();
        utils.setupDriver();
        driver = utils.getDriver();
        homePage = new HomePage(driver);
        metaTagsPage = new MetaTagsPage(driver);

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

    // @Test
    // @Order(1)
    // @DisplayName("TS-05-01: Задача создана при помощи ручного ввода")
    // public void testManualInputTask() {
    //     driver.get(TestConfig.getUrlProjects());
    //     metaTagsPage.createTaskWithManualInput(URL_TAGS);
    //     boolean completed = metaTagsPage.waitForTaskCompletion(600);
    //     assertTrue(completed);
    // }

    @Test
    @Order(1)
    @DisplayName("TS-05-02: Задача создана при помощи загрузки документа")
    public void testFileUploadTask() {
        driver.get(TestConfig.getUrlProjects());
        metaTagsPage.createTaskWithFileUpload(TestConfig.getTestFilePathForMetaTags());
        boolean completed = metaTagsPage.waitForTaskCompletion(600);
        assertTrue(completed);
    }

    @Test
    @Order(2)
    @DisplayName("TS-05-03: Невалидные данные для создания задачи")
    public void testInvalidData() {
        driver.get(TestConfig.getUrlProjects());
        metaTagsPage.navigateToMetaTags();
        metaTagsPage.clickStartButtonWithoutData();
        assertTrue(metaTagsPage.isErrorMessageDisplayed());
    }
}