package test;


import com.codeborne.selenide.logevents.SelenideLogger;
import data.DataHelper;
import data.SQLHelper;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import page.DashboardPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PaymentTest {
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

    @DisplayName("№1 Оплата по карте 4444 4444 4444 4441 с вводом валидных данных")
    @Test
    void shouldOccurPaymentWithStatusApproved() {
        var dashboardPage = new DashboardPage();
        var payment = dashboardPage.openPaymentForm();
        payment.fillingFields(DataHelper.getApprovedCard());
        payment.waitSuccessfullyNotification();

        String expectedStatus = "APPROVED";
        String actualStatus = SQLHelper.getPaymentStatus();
        assertEquals(expectedStatus, actualStatus);
    }

    @DisplayName("№2 Оплата по карте 4444 4444 4444 4442 с вводом валидных данных")
    @Test
    void shouldNotOccurPaymentWithStatusDeclined() {
        var dashboardPage = new DashboardPage();
        var payment = dashboardPage.openPaymentForm();
        payment.fillingFields(DataHelper.getDeclinedCard());
        payment.waitErrorNotification();

        String expectedStatus = "DECLINED";
        String actualStatus = SQLHelper.getPaymentStatus();
        assertEquals(expectedStatus, actualStatus);
    }

    /*Негативные сценарии с пустыми полями*/

    @DisplayName("№5 При оплате по карте не заполнено поле 'Номер карты'")
    @Test
    void shouldAppearErrorMessageWhenPaymentWithNotFilledCardNumber() {
        var dashboardPage = new DashboardPage();
        var payment = dashboardPage.openPaymentForm();
        payment.fillingFields(DataHelper.getCardWithNotFillingCardNumber());
        payment.waitWrongFormatError();
    }

    @DisplayName("№6 При оплате по карте не заполнено поле 'Месяц'")
    @Test
    void shouldAppearErrorMessageWhenPaymentWithNotFilledMonth() {
        var dashboardPage = new DashboardPage();
        var payment = dashboardPage.openPaymentForm();
        payment.fillingFields(DataHelper.getCardWithNotFillingMonth());
        payment.waitWrongFormatError();
    }

    @DisplayName("№7 При оплате по карте не заполнено поле 'Год'")
    @Test
    void shouldAppearErrorMessageWhenPaymentWithNotFilledYear() {
        var dashboardPage = new DashboardPage();
        var payment = dashboardPage.openPaymentForm();
        payment.fillingFields(DataHelper.getCardWithNotFillingYear());
        payment.waitWrongFormatError();
    }

    @DisplayName("№8 При оплате по карте не заполнено поле 'Владелец'")
    @Test
    void shouldAppearErrorMessageWhenPaymentWithNotFilledCardholder() {
        var dashboardPage = new DashboardPage();
        var payment = dashboardPage.openPaymentForm();
        payment.fillingFields(DataHelper.getCardWithNotFillingCardholder());
        payment.waitRequiredFieldError();
    }

    @DisplayName("№9 При оплате по карте не заполнено поле 'CVC/CVV'")
    @Test
    void shouldAppearErrorMessageWhenPaymentWithNotFilledCvc() {
        var dashboardPage = new DashboardPage();
        var payment = dashboardPage.openPaymentForm();
        payment.fillingFields(DataHelper.getCardWithNotFillingCvc());
        payment.waitWrongFormatError();
    }

    /*Негативные сценарии с невалидным значением полей*/

    @DisplayName("№15 При оплате по карте в поле 'Номер карты' указан номер карты отсутствующий в БД")
    @Test
    void shouldAppearErrorMessageWhenPaymentByRandomValidCard() {
        var dashboardPage = new DashboardPage();
        var payment = dashboardPage.openPaymentForm();
        payment.fillingFields(DataHelper.getRandomValidCard());
        payment.waitErrorNotification();
    }

    @DisplayName("№17 При оплате по карте в поле 'Номер карты' указан номер менее 16 цифр")
    @Test
    void shouldAppearErrorMessageWhenPaymentWithIncompleteCardNumber() {
        var dashboardPage = new DashboardPage();
        var payment = dashboardPage.openPaymentForm();
        payment.fillingFields(DataHelper.getCardWithIncompleteCardNumber());
        payment.waitWrongFormatError();
    }

    @DisplayName("№19 При оплате по карте в поле 'Месяц' указано число больше 12")
    @Test
    void shouldAppearErrorMessageWhenPaymentWithMonthMoreThanTwelve() {
        var dashboardPage = new DashboardPage();
        var payment = dashboardPage.openPaymentForm();
        payment.fillingFields(DataHelper.getCardWithMonthMoreThanTwelve());
        payment.waitCardExpirationDateError();
    }

    @DisplayName("№21 При оплате по карте в поле 'Месяц' указано число 00")
    @Test
    void shouldAppearErrorMessageWhenPaymentWithMonthDoubleZero() {
        var dashboardPage = new DashboardPage();
        var payment = dashboardPage.openPaymentForm();
        payment.fillingFields(DataHelper.getCardWithMonthDoubleZero());
        payment.waitCardExpirationDateError();
    }

    @DisplayName("№23 При оплате по карте в поле 'Месяц' указан месяц меньше текущего, при указанном текущем годе")
    @Test
    void shouldAppearErrorMessageWhenPaymentWithMonthLessThanCurrentMonthForCurrentYear() {
        var dashboardPage = new DashboardPage();
        var payment = dashboardPage.openPaymentForm();
        payment.fillingFields(DataHelper.getCardWithMonthLessThanCurrentMonthForCurrentYear());
        payment.waitCardExpirationDateError();
    }

    @DisplayName("№25 При оплате по карте в поле 'Год' указан год меньше текущего")
    @Test
    void shouldAppearErrorMessageWhenPaymentWithYearLessThanCurrentYear() {
        var dashboardPage = new DashboardPage();
        var payment = dashboardPage.openPaymentForm();
        payment.fillingFields(DataHelper.getCardWithYearLessThanCurrentYear());
        payment.waitCardExpiredError();
    }

    @DisplayName("№27 При оплате по карте в поле 'Год' указан год больше текущего на 6 лет")
    @Test
    void shouldAppearErrorMessageWhenPaymentWithYearMoreBySixYearsThanCurrentYear() {
        var dashboardPage = new DashboardPage();
        var payment = dashboardPage.openPaymentForm();
        payment.fillingFields(DataHelper.getCardWithYearMoreBySixYearsThanCurrentYear());
        payment.waitCardExpirationDateError();
    }

    @DisplayName("№29 При оплате по карте в поле 'Владелец' указано имя на кириллице")
    @Test
    void shouldAppearErrorMessageWhenPaymentWithCardholderByCyrillic() {
        var dashboardPage = new DashboardPage();
        var payment = dashboardPage.openPaymentForm();
        payment.fillingFields(DataHelper.getCardWithCardholderByCyrillic());
        payment.waitWrongFormatError();
    }

    @DisplayName("№31 При оплате по карте в поле 'Владелец' указано имя со спецсимволами")
    @Test
    void shouldAppearErrorMessageWhenPaymentWithCardholderBySpecialSymbol() {
        var dashboardPage = new DashboardPage();
        var payment = dashboardPage.openPaymentForm();
        payment.fillingFields(DataHelper.getCardWithCardholderBySpecialSymbol());
        payment.waitWrongFormatError();
    }

    @DisplayName("№33 При оплате по карте в поле 'Владелец' указано имя с цифрами")
    @Test
    void shouldAppearErrorMessageWhenPaymentWithCardholderByNumbers() {
        var dashboardPage = new DashboardPage();
        var payment = dashboardPage.openPaymentForm();
        payment.fillingFields(DataHelper.getCardWithCardholderByNumbers());
        payment.waitWrongFormatError();
    }

    @DisplayName("№35 При оплате по карте в поле 'CVC/CVV' указано менее 3-х цифр")
    @Test
    void shouldAppearErrorMessageWhenPaymentWithIncompleteCvc() {
        var dashboardPage = new DashboardPage();
        var payment = dashboardPage.openPaymentForm();
        payment.fillingFields(DataHelper.getCardWithIncompleteCvc());
        payment.waitWrongFormatError();
    }
}
