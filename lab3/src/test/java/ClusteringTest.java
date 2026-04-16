import com.example.Utils;
import com.example.TestConfig;
import com.example.pages.HomePage;
import com.example.pages.ClusteringPage;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ClusteringTest {

    private static Utils utils;
    private static WebDriver driver;
    private static HomePage homePage;
    private static ClusteringPage clusteringPage;
    private static final String CLUSTERING_DATA_1 = "купить11 дива1н недорого11\nди1ван угловой с ящиком\nдиван прямой раскладной\nмягкая мебель от производителя\nремонт стиральных машин на дому\nремонт холодильников с выездом\nремонт стиральных машин lg\nзамена подшипника в стиральной машине\nдоставка пиццы москва\nпицца с доставкой круглосуточно\nсуши заказать с доставкой\nроллы филадельфия цена\nкурсы английского языка онлайн\nрепетитор по математике егэ\nобучение программированию с нуля\nкурсы python для начинающих\nпластиковые окна установка\nокна пвх цена за м2\nостекление балконов под ключ\nремонт квартир в новостройке\nсуши в спб\nкомпьютерный мастер";
    private static final String CLUSTERING_DATA_2 = "купить11 дива1н недорого11\nди1ван угловой с ящиком\nдиван прямой раскладной\nмягкая мебель от производителя\nремонт стиральных машин на дому\nремонт холодильников с выездом\nремонт стиральных машин lg\nзамена подшипника в стиральной машине\nдоставка пиццы москва\nпицца с доставкой круглосуточно\nсуши заказать с доставкой\nроллы филадельфия цена\nкурсы английского языка онлайн\nрепетитор по математике егэ\nобучение программированию с нуля\nкурсы python для начинающих\nпластиковые окна установка\nокна пвх цена за м2\nостекление балконов под ключ\nремонт квартир в новостройке\nсуши в спб\nкомпьютерный мастер\nкупить пельмени\nрестораны спб";
    
    @BeforeAll
    public static void setUp() {
        utils = new Utils();
        utils.setupDriver();
        driver = utils.getDriver();
        homePage = new HomePage(driver);
        clusteringPage = new ClusteringPage(driver);

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
    @DisplayName("TS-08-01: Задача создана при помощи ручного ввода (сравнение топов)")
    public void testManualInputCompareTop() {
        driver.get(TestConfig.getUrlProjects());
        clusteringPage.createTaskWithManualInputCompareTop(CLUSTERING_DATA_1);
        boolean completed = clusteringPage.waitForTaskCompletion(600);
        assertTrue(completed);
    }

    @Test
    @Order(2)
    @DisplayName("TS-08-02: Задача создана при помощи ручного ввода (профессиональная настройка)")
    public void testManualInputProfessional() {
        driver.get(TestConfig.getUrlProjects());
        clusteringPage.createTaskWithManualInputProfessional(CLUSTERING_DATA_2);
        boolean completed = clusteringPage.waitForTaskCompletion(600);
        assertTrue(completed);
    }

    @Test
    @Order(3)
    @DisplayName("TS-08-03: Задача создана при помощи загрузки документа")
    public void testFileUploadTask() {
        driver.get(TestConfig.getUrlProjects());
        clusteringPage.createTaskWithFileUpload(TestConfig.getTestFilePathForClustering());
        boolean completed = clusteringPage.waitForTaskCompletion(600);
        assertTrue(completed);
    }

    @Test
    @Order(4)
    @DisplayName("TS-08-04: Невалидные данные для создания задачи")
    public void testInvalidData() {
        driver.get(TestConfig.getUrlProjects());
        clusteringPage.navigateToClustering();
        clusteringPage.clickStartButtonWithoutData();
        assertTrue(clusteringPage.isErrorMessageDisplayed());
    }
}