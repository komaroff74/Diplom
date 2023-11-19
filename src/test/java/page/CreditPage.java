package page;

import com.codeborne.selenide.SelenideElement;
import data.DataHelper;

import java.time.Duration;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class CreditPage {

        private SelenideElement heading = $$("h3").find(exactText("Кредит по данным карты"));
        private SelenideElement cardNumberField = $(byText("Номер карты")).parent().$("[class='input__control']");
        private SelenideElement monthField = $(byText("Месяц")).parent().$("[class='input__control']");
        private SelenideElement yearField = $(byText("Год")).parent().$("[class='input__control']");
        private SelenideElement cardholderField = $(byText("Владелец")).parent().$("[class='input__control']");
        private SelenideElement cvcField = $(byText("CVC/CVV")).parent().$("[class='input__control']");
        private SelenideElement continueButton = $$(".button").find(exactText("Продолжить"));

        private SelenideElement successfullyNotification = $(byText("Успешно"));
        private SelenideElement errorNotification = $(byText("Ошибка"));
        private SelenideElement wrongFormatError = $(byText("Неверный формат"));
        private SelenideElement cardExpiredError = $(byText("Истёк срок действия карты"));
        private SelenideElement cardExpirationDateError = $(byText("Неверно указан срок действия карты"));
        private SelenideElement requiredFieldError = $(byText("Поле обязательно для заполнения"));

        public CreditPage() {
            heading.shouldBe(visible);
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

