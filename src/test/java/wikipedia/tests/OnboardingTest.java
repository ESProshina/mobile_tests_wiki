package wikipedia.tests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wikipedia.screens.OnboardingScreen;

import static io.qameta.allure.Allure.step;

@DisplayName("Онбординг Wikipedia")
public class OnboardingTest extends TestBase {

    @Test
    @DisplayName("Прохождение 4 экранов онбординга с проверками")
    public void onboardingTest() {
        step("Пройти онбординг и проверить открытие главного экрана", () -> {
            // Создаем экран, проходим его и проверяем, что открылся MainScreen
            new OnboardingScreen().passOnboarding().checkOpened();
        });
    }
}