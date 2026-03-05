package ru.alfabank.campus.test.lesson5;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import ru.alfabank.campus.model.lesson1.lesson2.annotations.TestUser;
import ru.alfabank.campus.model.lesson1.lesson2.extentions.TestUserExtention;
import ru.alfabank.campus.payment.tests.lesson2.gateways.PaymentApiGateway;
import ru.alfabank.campus.payment.tests.lesson2.gateways.SignApiGateway;
import ru.alfabank.campus.payment.tests.lesson2.utils.OperationChecker;
import ru.alfabank.campus.payment.tests.lesson2.utils.PaymentTestData;

import java.util.List;

import static ru.alfabank.campus.payment.tests.lesson2.enums.PaymentStatuses.*;
import static ru.alfabank.campus.payment.tests.lesson2.enums.UserRights.CREATE_PAYMENT;
import static ru.alfabank.campus.payment.tests.lesson2.enums.UserRights.SIGN_PAYMENT;
import static ru.alfabank.campus.payment.tests.lesson2.utils.PaymentTestData.generate;

@ExtendWith(TestUserExtention.class)
public class HomePaymentCreationTests {

    private final PaymentApiGateway paymentApiGateway = new PaymentApiGateway();
    private final OperationChecker checker = new OperationChecker();
    private final SignApiGateway signApiGateway = new SignApiGateway();
    private PaymentTestData user;

    @Test
    @DisplayName("Успешная обработка платежа")
    void shouldCreatePayment() {
        PaymentTestData testData = generate()
            .addRights(List.of(CREATE_PAYMENT, SIGN_PAYMENT));

        String paymentRef = paymentApiGateway.getPaymentRef(testData.toPaymentRequest(4200.00));
        checker.verifyOperationStatus(paymentRef, NEW.getValue());

        String signRef = signApiGateway.initSign(testData, paymentRef);
        checker.verifyOperationStatus(paymentRef, ON_SIGN.getValue());

        signApiGateway.confirmSign(testData, signRef);
        checker.verifyOperationStatus(paymentRef, SUCCESS.getValue());
    }

    @Test
    @TestUser(withRights={SIGN_PAYMENT})
    @DisplayName("Создание платежа без права создания платежа")
    void checkCreatePaymentRule(PaymentTestData testData) {

    }

    @Test
    @DisplayName("Подписание платежа без права подписания платежа")
    void checkSignPaymentRule() {}

    @Test
    @DisplayName("Создание платежа с суммой большей, чем баланс счета")
    void checkPaymentAmount() {}

    @Test
    @DisplayName("Создание платежа со счетом, который не принадлежит организации")
    void checkPaymentAccount() {}

    @Test
    @DisplayName("Создание платежа со получателем, которого не существует в системе")
    void checkPaymentRecipient() {}

}
