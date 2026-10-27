package wikipedia.tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.Allure;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import wikipedia.drivers.AndroidDriverProvider;
import wikipedia.screens.MainScreen;
import wikipedia.screens.OnboardingScreen;
import wikipedia.screens.SearchScreen;

import java.io.ByteArrayInputStream;

import static com.codeborne.selenide.Selenide.closeWebDriver;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.screenshot;

public class TestBase {
    protected OnboardingScreen onboarding = new OnboardingScreen();
    protected MainScreen mainScreen = new MainScreen();
    protected SearchScreen searchScreen = new SearchScreen();

    @BeforeAll
    static void beforeAll() {
        if (System.getProperty("deviceHost") == null) {
            System.setProperty("deviceHost", "emulation");
        }
        System.setProperty("webdriver.http.factory", "jdk-http-client");
        Configuration.pageLoadTimeout = -1;
        Configuration.browser = AndroidDriverProvider.class.getName();
        Configuration.browserSize = null;
        Configuration.timeout = 30000;
        Configuration.screenshots = true;
        Configuration.savePageSource = false;
    }

    @BeforeEach
    void beforeEach() {
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide()
                .screenshots(true)
                .savePageSource(false));
        open();
    }

    @AfterEach
    void afterEach() {
        // ✅ Делаем финальный скриншот и прикрепляем его в Allure
        if (WebDriverRunner.hasWebDriverStarted()) {
            try {
                byte[] screenshotBytes = screenshot("final_screenshot").getBytes();
                Allure.addAttachment("Final Screenshot", new ByteArrayInputStream(screenshotBytes));
            } catch (Exception e) {
                System.out.println("Не удалось сделать скриншот: " + e.getMessage());
            }
        }
        closeWebDriver();
    }

    protected void passOnboarding() {
        onboarding
                .checkKnowledgeScreen()
                .clickForward()
                .checkDataPrivacyScreen()
                .clickForward()
                .checkLanguagesScreen()
                .clickForward()
                .checkCuriosityScreen()
                .clickSkip();

        mainScreen.checkOpened();
    }
}