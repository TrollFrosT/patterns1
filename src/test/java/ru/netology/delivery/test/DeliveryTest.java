package ru.netology.delivery.test;




import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import ru.netology.delivery.data.DataGenerator;

import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;


public class DeliveryTest {

    @BeforeEach
    void setup() {open("http://localhost:9999/");}

    @Test
    void testTransferAppointmentDate() {

        Configuration.holdBrowserOpen = true;

        DataGenerator.UserInfo validUser = DataGenerator.Registration.generateUser("ru");
        int daysBeforeFirstMeeting = 3;
        String firstMeetingDate = DataGenerator.generateDate(daysBeforeFirstMeeting);
        int daysBeforeSecondMeeting = 7;
        String secondMeetingDate = DataGenerator.generateDate(daysBeforeSecondMeeting);

        $("[data-test-id=city] input").setValue(validUser.getCity());
        $("span[data-test-id=date] input").doubleClick().sendKeys(Keys.DELETE);
        $("span[data-test-id=date] input").setValue(firstMeetingDate);
        $("span[data-test-id=name] input").setValue(validUser.getName());
        $("span[data-test-id=phone] input").setValue(validUser.getPhone());
        $("[data-test-id=agreement]").click();
        $(byText("Запланировать")).click();
        $(byText("Успешно!")).shouldBe(visible, Duration.ofSeconds(15));
        $("[data-test-id='success-notification'] .notification__content")
                .shouldHave(Condition.text("Встреча успешно запланирована на " + firstMeetingDate))
                .shouldBe(visible);

        $("span[data-test-id=date] input").doubleClick().sendKeys(Keys.DELETE);
        $("span[data-test-id=date] input").setValue(secondMeetingDate);
        $(byText("Запланировать")).click();
        $("[data-test-id='replan-notification'] .notification__content")
                .shouldHave(Condition.text("У вас уже запланирована встреча на другую дату. Перепланировать?"))
                .shouldBe(visible);
        $("[data-test-id='replan-notification'] button").click();
        $("[data-test-id='success-notification'] .notification__content")
                .shouldHave(Condition.text("Встреча успешно запланирована на " + secondMeetingDate))
                .shouldBe(visible);


    }

}