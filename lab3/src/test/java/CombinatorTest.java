import com.example.Utils;
import com.example.TestConfig;
import com.example.pages.HomePage;
import com.example.pages.CombinatorPage;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CombinatorTest {

    private static Utils utils;
    private static WebDriver driver;
    private static HomePage homePage;
    private static CombinatorPage combinatorPage;

    private static final String WORD1 = "купить";
    private static final String WORD2 = "диван";
    private static final String WORD3 = "недорого";
    private static final String WORD4 = "в Москве";

    @BeforeAll
    public static void setUp() {
        utils = new Utils();
        utils.setupDriver();
        driver = utils.getDriver();
        homePage = new HomePage(driver);
        combinatorPage = new CombinatorPage(driver);

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
    @DisplayName("TS-12-01: Задача создана")
    public void testCombinatorTask() {
        driver.get(TestConfig.getUrlProjects());
        combinatorPage.createCombinatorTask(WORD1, WORD2, WORD3, WORD4);
        boolean completed = combinatorPage.waitForTaskCompletion(300);
        assertTrue(completed);
    }

    @Test
    @Order(2)
    @DisplayName("TS-12-02: Невалидные данные для создания задачи")
    public void testInvalidData() {
        driver.get(TestConfig.getUrlProjects());
        combinatorPage.navigateToCombinator();
        combinatorPage.clickStartButtonWithoutData();
        assertTrue(combinatorPage.isErrorMessageDisplayed());
    }
}