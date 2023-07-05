import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selectors;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static io.restassured.RestAssured.given;


public class TestInput {
    private static RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    @BeforeAll
    static void setUpAll() {
        // сам запрос
        given() // "дано"
                .spec(requestSpec) // указываем, какую спецификацию используем
                .body(new RegistrationDto("vasya", "password", "active")) // передаём в теле объект, который будет преобразован в JSON
                .when() // "когда"
                .post("/api/system/users") // на какой путь относительно BaseUri отправляем запрос
                .then() // "тогда ожидаем"
                .statusCode(200); // код 200 OK
        given() // "дано"
                .spec(requestSpec) // указываем, какую спецификацию используем
                .body(new RegistrationDto("ivan", "password", "blocked")) // передаём в теле объект, который будет преобразован в JSON
                .when() // "когда"
                .post("/api/system/users") // на какой путь относительно BaseUri отправляем запрос
                .then() // "тогда ожидаем"
                .statusCode(200); // код 200 OK
    }

    @Test
    void shouldSomethingCheck() {
        Configuration.headless = true;
        open("http://localhost:9999");
        $(By.cssSelector("[data-test-id='login'] input")).setValue("vasya");
        $(By.cssSelector("[data-test-id='password'] input")).setValue("password");
        $(By.cssSelector("[data-test-id='action-login']")).click();
        $(Selectors.byText("Личный кабинет")).should(Condition.appear);
    }

    @Test
    void shouldCheckBlockedUser() {
        Configuration.headless = true;
        open("http://localhost:9999");
        $(By.cssSelector("[data-test-id='login'] input")).setValue("ivan");
        $(By.cssSelector("[data-test-id='password'] input")).setValue("password");
        $(By.cssSelector("[data-test-id='action-login']")).click();
        $(Selectors.byText("Пользователь заблокирован")).should(Condition.appear);
    }

    @Test
    void shouldCheckLoginUser() {
        Configuration.headless = true;
        open("http://localhost:9999");
        $(By.cssSelector("[data-test-id='login'] input")).setValue("mark");
        $(By.cssSelector("[data-test-id='password'] input")).setValue("password");
        $(By.cssSelector("[data-test-id='action-login']")).click();
        $(Selectors.byText("Неверно указан логин или пароль")).should(Condition.appear);
    }

    @Test
    void shouldCheckPasswordUser() {
        Configuration.headless = true;
        open("http://localhost:9999");
        $(By.cssSelector("[data-test-id='login'] input")).setValue("vasya");
        $(By.cssSelector("[data-test-id='password'] input")).setValue("01010101");
        $(By.cssSelector("[data-test-id='action-login']")).click();
        $(Selectors.byText("Неверно указан логин или пароль")).should(Condition.appear);
    }
}


