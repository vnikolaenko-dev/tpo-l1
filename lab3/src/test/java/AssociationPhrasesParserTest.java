import com.example.Utils;
import com.example.TestConfig;
import com.example.pages.HomePage;
import com.example.pages.AssociationPhrasesPage;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AssociationPhrasesParserTest {

    private static Utils utils;
    private static WebDriver driver;
    private static HomePage homePage;
    private static AssociationPhrasesPage associationPage;
    private static final String ASSOCIATION_PHRASES = "купить авто\nремонт квартир\nдоставка пиццы";

    @BeforeAll
    public static void setUp() {
        utils = new Utils();
        utils.setupDriver();
        driver = utils.getDriver();
        homePage = new HomePage(driver);
        associationPage = new AssociationPhrasesPage(driver);

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

    @Test
    @Order(1)
    @DisplayName("TS-07-01: Задача создана при помощи ручного ввода")
    public void testManualInputTask() {
        driver.get(TestConfig.getUrlProjects());
        associationPage.createTaskWithManualInput(ASSOCIATION_PHRASES);
        boolean completed = associationPage.waitForTaskCompletion(600);
        assertTrue(completed);
    }

    @Test
    @Order(2)
    @DisplayName("TS-07-02: Задача создана при помощи загрузки документа")
    public void testFileUploadTask() {
        driver.get(TestConfig.getUrlProjects());
        associationPage.createTaskWithFileUpload(TestConfig.getTestFilePathForAssociation());
        boolean completed = associationPage.waitForTaskCompletion(600);
        assertTrue(completed);
    }

    @Test
    @Order(3)
    @DisplayName("TS-07-03: Невалидные данные для создания задачи")
    public void testInvalidData() {
        driver.get(TestConfig.getUrlProjects());
        associationPage.navigateToAssociation();
        associationPage.clickStartButtonWithoutData();
        assertTrue(associationPage.isErrorMessageDisplayed());
    }
}