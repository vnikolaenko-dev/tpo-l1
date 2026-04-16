import com.example.Utils;
import com.example.TestConfig;
import com.example.pages.HomePage;
import com.example.pages.CompetitorAdsPage;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CompetitorAdsTest {

    private static Utils utils;
    private static WebDriver driver;
    private static HomePage homePage;
    private static CompetitorAdsPage competitorAdsPage;

    private static final String TEST_DOMAIN = "youtube.com";

    @BeforeAll
    public static void setUp() {
        utils = new Utils();
        utils.setupDriver();
        driver = utils.getDriver();
        homePage = new HomePage(driver);
        competitorAdsPage = new CompetitorAdsPage(driver);

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
    @DisplayName("TS-11-01: Задача создана при помощи ручного ввода")
    public void testManualInputTask() {
        driver.get(TestConfig.getUrlProjects());
        competitorAdsPage.createTaskWithManualInput(TEST_DOMAIN);
        boolean completed = competitorAdsPage.waitForTaskCompletion(600);
        assertTrue(completed);
    }

    @Test
    @Order(2)
    @DisplayName("TS-11-02: Задача создана при помощи загрузки документа")
    public void testFileUploadTask() {
        driver.get(TestConfig.getUrlProjects());
        competitorAdsPage.createTaskWithFileUpload(TestConfig.getTestFilePathForCompetitorAds());
        boolean completed = competitorAdsPage.waitForTaskCompletion(600);
        assertTrue(completed);
    }

}