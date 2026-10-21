package com.eswar.finnova.validation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Pure JVM tests for every validation rule. Run: right-click this file → Run 'ProfileValidatorTest'
 * or from terminal: ./gradlew testDebugUnitTest
 */
public class ProfileValidatorTest {

    private final ProfileValidator validator = new ProfileValidator();

    private static void assertValid(ValidationResult r) {
        assertTrue("Expected VALID but was " + r, r.isValid());
    }

    private static void assertError(ValidationError expected, ValidationResult r) {
        assertEquals(expected, r.getError());
    }

    // ------------------------------------------------ Preferred first name

    @Test public void name_valid() {
        assertValid(validator.validatePreferredName("Eswar", "Alex"));
        assertValid(validator.validatePreferredName("Mary-Jane", "Alex"));
        assertValid(validator.validatePreferredName("O'Neil", "Alex"));
        assertValid(validator.validatePreferredName("Sai Ram", "Alex"));
        assertValid(validator.validatePreferredName("  Eswar  ", "Alex"));   // trimmed
    }

    @Test public void name_required() {
        assertError(ValidationError.NAME_REQUIRED, validator.validatePreferredName("", "Alex"));
        assertError(ValidationError.NAME_REQUIRED, validator.validatePreferredName("    ", "Alex"));
        assertError(ValidationError.NAME_REQUIRED, validator.validatePreferredName(null, "Alex"));
    }

    @Test public void name_tooLong() {
        assertError(ValidationError.NAME_TOO_LONG,
                validator.validatePreferredName("Abcdefghijklmnopqrstuvwxyzabcde", "Alex")); // 31 chars
    }

    @Test public void name_invalidCharacters() {
        assertError(ValidationError.NAME_INVALID_CHARACTERS, validator.validatePreferredName("Alex1", "Bob"));
        assertError(ValidationError.NAME_INVALID_CHARACTERS, validator.validatePreferredName("Al@x", "Bob"));
        assertError(ValidationError.NAME_INVALID_CHARACTERS, validator.validatePreferredName("Sai  Ram", "Bob"));
        assertError(ValidationError.NAME_INVALID_CHARACTERS, validator.validatePreferredName("-Alex", "Bob"));
        assertError(ValidationError.NAME_INVALID_CHARACTERS, validator.validatePreferredName("Alex--J", "Bob"));
    }

    @Test public void name_sameAsCurrent_caseInsensitive() {
        assertError(ValidationError.NAME_SAME_AS_CURRENT, validator.validatePreferredName("alex", "Alex"));
    }

    // ------------------------------------------------ Email

    @Test public void email_valid() {
        assertValid(validator.validateEmail("eswar.reddy@gmail.com", "old@x.com"));
        assertValid(validator.validateEmail("a+b@sub.domain.co.in", "old@x.com"));
    }

    @Test public void email_invalidFormats() {
        String[] bad = {"plainaddress", "@gmail.com", "eswar@", "eswar@gmail", "eswar@gmail.c",
                "eswar gmail@x.com", "eswar..r@gmail.com", "eswar@@gmail.com"};
        for (String e : bad) {
            assertEquals(e, ValidationError.EMAIL_INVALID_FORMAT, validator.validateEmail(e, "old@x.com").getError());
        }
    }

    @Test public void email_requiredAndSame() {
        assertError(ValidationError.EMAIL_REQUIRED, validator.validateEmail(" ", "old@x.com"));
        assertError(ValidationError.EMAIL_SAME_AS_CURRENT, validator.validateEmail("OLD@x.com", "old@x.com"));
    }

    @Test public void confirmEmail() {
        assertValid(validator.validateConfirmEmail("a@b.com", "A@B.com"));
        assertError(ValidationError.EMAIL_CONFIRM_REQUIRED, validator.validateConfirmEmail("a@b.com", ""));
        assertError(ValidationError.EMAIL_MISMATCH, validator.validateConfirmEmail("a@b.com", "a@c.com"));
    }

    // ------------------------------------------------ Phone

    @Test public void phone_validFormats() {
        assertValid(validator.validateMobilePhone("(469) 555-0199", "4695550142"));
        assertValid(validator.validateMobilePhone("469-555-0199", "4695550142"));
        assertValid(validator.validateMobilePhone("+1 469 555 0199", "4695550142"));
    }

    @Test public void phone_errors() {
        assertError(ValidationError.PHONE_REQUIRED, validator.validateMobilePhone("", null));
        assertError(ValidationError.PHONE_INVALID_LENGTH, validator.validateMobilePhone("469555", null));
        assertError(ValidationError.PHONE_INVALID_AREA_CODE, validator.validateMobilePhone("0695550142", null));
        assertError(ValidationError.PHONE_INVALID_EXCHANGE, validator.validateMobilePhone("4691550142", null));
        assertError(ValidationError.PHONE_SAME_AS_CURRENT,
                validator.validateMobilePhone("(469) 555-0142", "4695550142"));
    }

    @Test public void normalizePhone() {
        assertEquals("4695550142", ProfileValidator.normalizePhone("+1 (469) 555-0142"));
        assertEquals("", ProfileValidator.normalizePhone(null));
    }

    // ------------------------------------------------ Address

    @Test public void address_line1() {
        assertValid(validator.validateAddressLine1("1200 Lakeside Blvd"));
        assertError(ValidationError.ADDRESS_LINE1_REQUIRED, validator.validateAddressLine1(""));
        assertError(ValidationError.ADDRESS_LINE1_TOO_SHORT, validator.validateAddressLine1("12 A"));
        assertError(ValidationError.ADDRESS_INVALID_CHARACTERS, validator.validateAddressLine1("12 Main St $"));
        assertError(ValidationError.ADDRESS_PO_BOX_NOT_ALLOWED, validator.validateAddressLine1("P.O. Box 123"));
        assertError(ValidationError.ADDRESS_PO_BOX_NOT_ALLOWED, validator.validateAddressLine1("PO BOX 77"));
    }

    @Test public void address_line2_isOptional() {
        assertValid(validator.validateAddressLine2(""));
        assertValid(validator.validateAddressLine2("Apt #310"));
        assertError(ValidationError.ADDRESS_INVALID_CHARACTERS, validator.validateAddressLine2("Apt <3>"));
    }

    @Test public void city_state_zip() {
        assertValid(validator.validateCity("Irving"));
        assertValid(validator.validateCity("St. Louis"));
        assertValid(validator.validateCity("Winston-Salem"));
        assertError(ValidationError.CITY_REQUIRED, validator.validateCity(""));
        assertError(ValidationError.CITY_INVALID, validator.validateCity("Dallas 1"));

        assertValid(validator.validateState("tx"));
        assertError(ValidationError.STATE_REQUIRED, validator.validateState(""));
        assertError(ValidationError.STATE_INVALID, validator.validateState("XX"));

        assertValid(validator.validateZip("75038"));
        assertValid(validator.validateZip("75038-1234"));
        assertError(ValidationError.ZIP_REQUIRED, validator.validateZip(""));
        assertError(ValidationError.ZIP_INVALID, validator.validateZip("7503"));
        assertError(ValidationError.ZIP_INVALID, validator.validateZip("75038-12"));
        assertError(ValidationError.ZIP_INVALID, validator.validateZip("00000"));
    }

    // ------------------------------------------------ Income

    @Test public void income() {
        assertValid(validator.validateAnnualIncome("95000"));
        assertValid(validator.validateAnnualIncome("95,000"));
        assertValid(validator.validateAnnualIncome("0"));
        assertError(ValidationError.INCOME_REQUIRED, validator.validateAnnualIncome(""));
        assertError(ValidationError.INCOME_INVALID, validator.validateAnnualIncome("95k"));
        assertError(ValidationError.INCOME_INVALID, validator.validateAnnualIncome("-5"));
        assertError(ValidationError.INCOME_INVALID, validator.validateAnnualIncome("95000.50"));
        assertError(ValidationError.INCOME_OUT_OF_RANGE, validator.validateAnnualIncome("10000000"));
    }

    @Test public void housing() {
        assertValid(validator.validateHousingPayment("1850", "95000"));
        assertValid(validator.validateHousingPayment("0", "0"));
        assertError(ValidationError.HOUSING_REQUIRED, validator.validateHousingPayment("", "95000"));
        assertError(ValidationError.HOUSING_OUT_OF_RANGE, validator.validateHousingPayment("100000", "9000000"));
        assertError(ValidationError.HOUSING_EXCEEDS_INCOME, validator.validateHousingPayment("9000", "95000"));
    }

    @Test public void employmentStatus() {
        assertValid(validator.validateEmploymentStatus("Student"));
        assertError(ValidationError.EMPLOYMENT_STATUS_REQUIRED, validator.validateEmploymentStatus(""));
    }

    // ------------------------------------------------ User ID & password

    @Test public void userId() {
        assertValid(validator.validateUserId("eswar_23"));
        assertError(ValidationError.USER_ID_REQUIRED, validator.validateUserId(""));
        assertError(ValidationError.USER_ID_LENGTH, validator.validateUserId("abc"));
        assertError(ValidationError.USER_ID_INVALID, validator.validateUserId("1eswar"));
        assertError(ValidationError.USER_ID_INVALID, validator.validateUserId("eswar-reddy"));
    }

    @Test public void password_rulesInOrder() {
        String uid = "eswar23";
        String current = "Old@Pass1";
        assertValid(validator.validateNewPassword("Bank@2026x", uid, current));
        assertError(ValidationError.PASSWORD_REQUIRED, validator.validateNewPassword("", uid, current));
        assertError(ValidationError.PASSWORD_LENGTH, validator.validateNewPassword("Ab@1", uid, current));
        assertError(ValidationError.PASSWORD_HAS_SPACE, validator.validateNewPassword("Bank @2026", uid, current));
        assertError(ValidationError.PASSWORD_MISSING_UPPERCASE, validator.validateNewPassword("bank@2026", uid, current));
        assertError(ValidationError.PASSWORD_MISSING_LOWERCASE, validator.validateNewPassword("BANK@2026", uid, current));
        assertError(ValidationError.PASSWORD_MISSING_DIGIT, validator.validateNewPassword("Bank@Bank", uid, current));
        assertError(ValidationError.PASSWORD_MISSING_SPECIAL, validator.validateNewPassword("Bank20266", uid, current));
        assertError(ValidationError.PASSWORD_CONTAINS_USER_ID, validator.validateNewPassword("Eswar23@x", uid, current));
        assertError(ValidationError.PASSWORD_SAME_AS_CURRENT, validator.validateNewPassword(current, uid, current));
    }

    @Test public void confirmPassword() {
        assertValid(validator.validateConfirmPassword("Bank@2026", "Bank@2026"));
        assertError(ValidationError.PASSWORD_CONFIRM_REQUIRED, validator.validateConfirmPassword("Bank@2026", ""));
        assertError(ValidationError.PASSWORD_MISMATCH, validator.validateConfirmPassword("Bank@2026", "bank@2026"));
    }
}
