import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selectors;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;


public class TestInput {
    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }


    private static RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    @Test
    void shouldCheckRegisteredUser() {
        var registeredUser = DataGenerator.Registration.getRegisteredUser("active");
        $(By.cssSelector("[data-test-id='login'] input")).setValue(registeredUser.getLogin());
        $(By.cssSelector("[data-test-id='password'] input")).setValue(registeredUser.getPassword());
        $(By.cssSelector("[data-test-id='action-login']")).click();
        $(Selectors.byText("Личный кабинет")).should(Condition.appear);
    }

    @Test
    void shouldCheckNotRegisteredUser() {
        open("http://localhost:9999");
        var notRegisteredUser = DataGenerator.Registration.getUser("active");
        $(By.cssSelector("[data-test-id='login'] input")).setValue(notRegisteredUser.getLogin());
        $(By.cssSelector("[data-test-id='password'] input")).setValue(notRegisteredUser.getPassword());
        $(By.cssSelector("[data-test-id='action-login']")).click();
        $(By.cssSelector("[data-test-id='error-notification'] .notification__content")).should(Condition.text("Ошибка! Неверно указан логин или пароль")).shouldBe(Condition.visible);
    }
    @Test
    void shouldCheckBlockedUser() {
        var blockedUser = DataGenerator.Registration.getRegisteredUser("blocked");
        $(By.cssSelector("[data-test-id='login'] input")).setValue(blockedUser.getLogin());
        $(By.cssSelector("[data-test-id='password'] input")).setValue(blockedUser.getPassword());
        $(By.cssSelector("[data-test-id='action-login']")).click();
        $(Selectors.byText("Пользователь заблокирован")).should(Condition.appear);
    }

    @Test
    void shouldCheckLoginUser() {
        var registeredUser = DataGenerator.Registration.getRegisteredUser("active");
        var wrongLogin = DataGenerator.getRandomLogin();
        $(By.cssSelector("[data-test-id='login'] input")).setValue(wrongLogin);
        $(By.cssSelector("[data-test-id='password'] input")).setValue(registeredUser.getPassword());
        $(By.cssSelector("[data-test-id='action-login']")).click();
        $(Selectors.byText("Неверно указан логин или пароль")).should(Condition.appear);
    }

    @Test
    void shouldCheckPasswordUser() {
        var registeredUser = DataGenerator.Registration.getRegisteredUser("active");
        var wrongPassword = DataGenerator.getRandomPassword();
        $(By.cssSelector("[data-test-id='login'] input")).setValue(registeredUser.getLogin());
        $(By.cssSelector("[data-test-id='password'] input")).setValue(wrongPassword);
        $(By.cssSelector("[data-test-id='action-login']")).click();
        $(Selectors.byText("Неверно указан логин или пароль")).should(Condition.appear);
    }
}


