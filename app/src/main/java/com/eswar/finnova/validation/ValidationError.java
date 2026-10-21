package com.eswar.finnova.validation;

/**
 * Every client-side validation failure the app knows about.
 *
 * The validator returns one of these codes; the View turns the code into a
 * user-facing message (see ui/common/ErrorMessages). Keeping codes separate
 * from strings means:
 *  - the validator stays pure Java and is unit-testable on the JVM,
 *  - tests assert on stable codes instead of English text,
 *  - copy/translations can change without touching logic.
 */
public enum ValidationError {
    // Preferred first name
    NAME_REQUIRED,
    NAME_TOO_LONG,
    NAME_INVALID_CHARACTERS,
    NAME_SAME_AS_CURRENT,

    // Email
    EMAIL_REQUIRED,
    EMAIL_TOO_LONG,
    EMAIL_INVALID_FORMAT,
    EMAIL_CONFIRM_REQUIRED,
    EMAIL_MISMATCH,
    EMAIL_SAME_AS_CURRENT,

    // Mobile phone (US)
    PHONE_REQUIRED,
    PHONE_INVALID_LENGTH,
    PHONE_INVALID_AREA_CODE,
    PHONE_INVALID_EXCHANGE,
    PHONE_SAME_AS_CURRENT,

    // Address
    ADDRESS_LINE1_REQUIRED,
    ADDRESS_LINE1_TOO_SHORT,
    ADDRESS_TOO_LONG,
    ADDRESS_INVALID_CHARACTERS,
    ADDRESS_PO_BOX_NOT_ALLOWED,
    CITY_REQUIRED,
    CITY_INVALID,
    STATE_REQUIRED,
    STATE_INVALID,
    ZIP_REQUIRED,
    ZIP_INVALID,

    // Income
    EMPLOYMENT_STATUS_REQUIRED,
    INCOME_REQUIRED,
    INCOME_INVALID,
    INCOME_OUT_OF_RANGE,
    HOUSING_REQUIRED,
    HOUSING_INVALID,
    HOUSING_OUT_OF_RANGE,
    HOUSING_EXCEEDS_INCOME,

    // User ID & password
    USER_ID_REQUIRED,
    USER_ID_LENGTH,
    USER_ID_INVALID,
    CURRENT_PASSWORD_REQUIRED,
    PASSWORD_REQUIRED,
    PASSWORD_LENGTH,
    PASSWORD_HAS_SPACE,
    PASSWORD_MISSING_UPPERCASE,
    PASSWORD_MISSING_LOWERCASE,
    PASSWORD_MISSING_DIGIT,
    PASSWORD_MISSING_SPECIAL,
    PASSWORD_CONTAINS_USER_ID,
    PASSWORD_SAME_AS_CURRENT,
    PASSWORD_CONFIRM_REQUIRED,
    PASSWORD_MISMATCH
}
