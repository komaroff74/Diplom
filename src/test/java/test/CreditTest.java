package test;

import com.codeborne.selenide.logevents.SelenideLogger;
import data.DataHelper;
import data.SQLHelper;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import page.DashboardPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CreditTest {
    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @BeforeEach
    void setUp() {
        open("http://localhost:8080");
    }

    @BeforeEach
    void cleanDatabase() {
        SQLHelper.cleanDatabase();
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    /*Позитивные сценарии*/

    @DisplayName("№3 Купить в кредит по карте 4444 4444 4444 4441 с вводом валидных данных")
    @Test
    void shouldOccurCreditWithStatusApproved() {
        var dashboardPage = new DashboardPage();
        var credit = dashboardPage.openCreditForm();
        credit.fillingFields(DataHelper.getApprovedCard());
        credit.waitSuccessfullyNotification();

        String expectedStatus = "APPROVED";
        String actualStatus = SQLHelper.getCreditStatus();
        assertEquals(expectedStatus, actualStatus);
    }

    @DisplayName("№4 Купить в кредит по карте 4444 4444 4444 4442 с вводом валидных данных")
    @Test
    void shouldNotOccurCreditWithStatusDeclined() {
        var dashboardPage = new DashboardPage();
        var credit = dashboardPage.openCreditForm();
        credit.fillingFields(DataHelper.getDeclinedCard());
        credit.waitErrorNotification();

        String expectedStatus = "DECLINED";
        String actualStatus = SQLHelper.getCreditStatus();
        assertEquals(expectedStatus, actualStatus);
    }

    /*Негативные сценарии с пустыми полями*/

    @DisplayName("№10 При покупке в кредит не заполнено поле 'Номер карты'")
    @Test
    void shouldAppearErrorMessageWhenCreditWithNotFilledCardNumber() {
        var dashboardPage = new DashboardPage();
        var credit = dashboardPage.openCreditForm();
        credit.fillingFields(DataHelper.getCardWithNotFillingCardNumber());
        credit.waitWrongFormatError();
    }

    @DisplayName("№11 При покупке в кредит не заполнено поле 'Месяц'")
    @Test
    void shouldAppearErrorMessageWhenCreditWithNotFilledMonth() {
        var dashboardPage = new DashboardPage();
        var credit = dashboardPage.openCreditForm();
        credit.fillingFields(DataHelper.getCardWithNotFillingMonth());
        credit.waitWrongFormatError();
    }

    @DisplayName("№12 При покупке в кредит не заполнено поле 'Год'")
    @Test
    void shouldAppearErrorMessageWhenCreditWithNotFilledYear() {
        var dashboardPage = new DashboardPage();
        var credit = dashboardPage.openCreditForm();
        credit.fillingFields(DataHelper.getCardWithNotFillingYear());
        credit.waitWrongFormatError();
    }

    @DisplayName("№13 При покупке в кредит не заполнено поле 'Владелец'")
    @Test
    void shouldAppearErrorMessageWhenCreditWithNotFilledCardholder() {
        var dashboardPage = new DashboardPage();
        var credit = dashboardPage.openCreditForm();
        credit.fillingFields(DataHelper.getCardWithNotFillingCardholder());
        credit.waitRequiredFieldError();
    }

    @DisplayName("№14 При покупке в кредит не заполнено поле 'CVC/CVV'")
    @Test
    void shouldAppearErrorMessageWhenCreditWithNotFilledCvc() {
        var dashboardPage = new DashboardPage();
        var credit = dashboardPage.openCreditForm();
        credit.fillingFields(DataHelper.getCardWithNotFillingCvc());
        credit.waitWrongFormatError();
    }

    /*Негативные сценарии с невалидным значением полей*/

    @DisplayName("№16 При покупке в кредит в поле 'Номер карты' указан номер карты отсутствующий в БД")
    @Test
    void shouldAppearErrorMessageWhenCreditByRandomValidCard() {
        var dashboardPage = new DashboardPage();
        var credit = dashboardPage.openCreditForm();
        credit.fillingFields(DataHelper.getRandomValidCard());
        credit.waitErrorNotification();
    }

    @DisplayName("№18 При покупке в кредит в поле 'Номер карты' указан номер менее 16 цифр")
    @Test
    void shouldAppearErrorMessageWhenCreditWithIncompleteCardNumber() {
        var dashboardPage = new DashboardPage();
        var credit = dashboardPage.openCreditForm();
        credit.fillingFields(DataHelper.getCardWithIncompleteCardNumber());
        credit.waitWrongFormatError();
    }

    @DisplayName("№20 При покупке в кредит в поле 'Месяц' указано число больше 12")
    @Test
    void shouldAppearErrorMessageWhenCreditWithMonthMoreThanTwelve() {
        var dashboardPage = new DashboardPage();
        var credit = dashboardPage.openCreditForm();
        credit.fillingFields(DataHelper.getCardWithMonthMoreThanTwelve());
        credit.waitCardExpirationDateError();
    }

    @DisplayName("№22 При покупке в кредит в поле 'Месяц' указано число больше 00")
    @Test
    void shouldAppearErrorMessageWhenCreditWithMonthMoreDoubleZero() {
        var dashboardPage = new DashboardPage();
        var credit = dashboardPage.openCreditForm();
        credit.fillingFields(DataHelper.getCardWithMonthDoubleZero());
        credit.waitCardExpirationDateError();
    }

    @DisplayName("№24 При покупке в кредит в поле 'Месяц' указан месяц меньше текущего, при указанном текущем годе")
    @Test
    void shouldAppearErrorMessageWhenCreditWithMonthLessThanCurrentMonthForCurrentYear() {
        var dashboardPage = new DashboardPage();
        var credit = dashboardPage.openCreditForm();
        credit.fillingFields(DataHelper.getCardWithMonthLessThanCurrentMonthForCurrentYear());
        credit.waitCardExpirationDateError();
    }

    @DisplayName("№26 При покупке в кредит в поле 'Год' указан год меньше текущего")
    @Test
    void shouldAppearErrorMessageWhenCreditWithYearLessThanCurrentYear() {
        var dashboardPage = new DashboardPage();
        var credit = dashboardPage.openCreditForm();
        credit.fillingFields(DataHelper.getCardWithYearLessThanCurrentYear());
        credit.waitCardExpiredError();
    }

    @DisplayName("№28 При покупке в кредит в поле 'Год' указан год больше текущего на 6 лет")
    @Test
    void shouldAppearErrorMessageWhenCreditWithYearMoreBySixYearsThanCurrentYear() {
        var dashboardPage = new DashboardPage();
        var credit = dashboardPage.openCreditForm();
        credit.fillingFields(DataHelper.getCardWithYearMoreBySixYearsThanCurrentYear());
        credit.waitCardExpirationDateError();
    }

    @DisplayName("№30 При покупке в кредит в поле 'Владелец' указано имя на кириллице")
    @Test
    void shouldAppearErrorMessageWhenCreditWithCardholderByCyrillic() {
        var dashboardPage = new DashboardPage();
        var credit = dashboardPage.openCreditForm();
        credit.fillingFields(DataHelper.getCardWithCardholderByCyrillic());
        credit.waitWrongFormatError();
    }

    @DisplayName("№32 При покупке в кредит в поле 'Владелец' указано имя со спецсимволами")
    @Test
    void shouldAppearErrorMessageWhenCreditWithCardholderBySpecialSymbol() {
        var dashboardPage = new DashboardPage();
        var credit = dashboardPage.openCreditForm();
        credit.fillingFields(DataHelper.getCardWithCardholderBySpecialSymbol());
        credit.waitWrongFormatError();
    }

    @DisplayName("№34 При покупке в кредит в поле 'Владелец' указано имя с цифрами")
    @Test
    void shouldAppearErrorMessageWhenCreditWithCardholderByNumbers() {
        var dashboardPage = new DashboardPage();
        var credit = dashboardPage.openCreditForm();
        credit.fillingFields(DataHelper.getCardWithCardholderByNumbers());
        credit.waitWrongFormatError();
    }

    @DisplayName("№36 При покупке в кредит в поле 'CVC/CVV' указано менее 3-х цифр")
    @Test
    void shouldAppearErrorMessageWhenCreditWithIncompleteCvc() {
        var dashboardPage = new DashboardPage();
        var credit = dashboardPage.openCreditForm();
        credit.fillingFields(DataHelper.getCardWithIncompleteCvc());
        credit.waitWrongFormatError();
    }
}

