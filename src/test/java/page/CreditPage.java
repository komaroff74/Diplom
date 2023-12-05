package page;

import com.codeborne.selenide.SelenideElement;
import data.DataHelper;
import org.openqa.selenium.By;

import java.time.Duration;
import java.util.Objects;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$$;

public class CreditPage {
    private final SelenideElement cardNumberField = Objects.requireNonNull($(byText("Номер карты"))).parent().$("[class='input__control']");
        private final SelenideElement monthField = Objects.requireNonNull($(byText("Месяц"))).parent().$("[class='input__control']");
        private final SelenideElement yearField = Objects.requireNonNull($(byText("Год"))).parent().$("[class='input__control']");
        private final SelenideElement cardholderField = Objects.requireNonNull($(byText("Владелец"))).parent().$("[class='input__control']");
        private final SelenideElement cvcField = Objects.requireNonNull($(byText("CVC/CVV"))).parent().$("[class='input__control']");

    private SelenideElement $(By byText) {
        return null;
    }

    private final SelenideElement continueButton;

        private final SelenideElement successfullyNotification = $(byText("Успешно"));
        private final SelenideElement errorNotification = $(byText("Ошибка"));
        private final SelenideElement wrongFormatError = $(byText("Неверный формат"));
        private final SelenideElement cardExpiredError = $(byText("Истёк срок действия карты"));
        private final SelenideElement cardExpirationDateError = $(byText("Неверно указан срок действия карты"));
        private final SelenideElement requiredFieldError = $(byText("Поле обязательно для заполнения"));

        public CreditPage() {
            SelenideElement heading = $$("h3").find(exactText("Кредит по данным карты"));
            heading.shouldBe(visible);
            continueButton = $$(".button").find(exactText("Продолжить"));
        }

        public void fillingFields(DataHelper.Card card) {
            cardNumberField.setValue(card.getNumber());
            monthField.setValue(card.getMonth());
            yearField.setValue(card.getYear());
            cardholderField.setValue(card.getCardholder());
            cvcField.setValue(card.getCvc());
            continueButton.click();
        }

        public void waitSuccessfullyNotification() {
            successfullyNotification.shouldBe(visible, Duration.ofSeconds(16));
        }

        public void waitErrorNotification() {
            errorNotification.shouldBe(visible, Duration.ofSeconds(16));
        }

        public void waitWrongFormatError() {
            wrongFormatError.shouldBe(visible);
        }

        public void waitCardExpiredError() {
            cardExpiredError.shouldBe(visible);
        }

        public void waitCardExpirationDateError() {
            cardExpirationDateError.shouldBe(visible);
        }

        public void waitRequiredFieldError() {
            requiredFieldError.shouldBe(visible);
        }
    }

