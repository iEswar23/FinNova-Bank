package com.eswar.finnova.validation;

import androidx.annotation.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * All client-side validation rules for the Profile & Settings screens.
 *
 * Rules of thumb used here (same as most banking apps):
 *  1. Trim input before validating; never trust leading/trailing spaces.
 *  2. Check "required" first, then length, then format/characters, then business rules.
 *  3. Return the FIRST failure only, so the user sees one clear message per field.
 *  4. No Android imports (except annotations) so this runs in plain JUnit tests.
 *  5. Client validation is for UX; the server must validate again.
 */
public class ProfileValidator {

    public static final int NAME_MAX = 30;
    public static final int EMAIL_MAX = 254;
    public static final int ADDRESS_LINE1_MIN = 5;
    public static final int ADDRESS_MAX = 60;
    public static final int CITY_MAX = 40;
    public static final long INCOME_MAX = 9_999_999L;
    public static final long HOUSING_MAX = 99_999L;
    public static final int USER_ID_MIN = 6;
    public static final int USER_ID_MAX = 30;
    public static final int PASSWORD_MIN = 8;
    public static final int PASSWORD_MAX = 32;

    /** Letters, separated by single space / hyphen / apostrophe. e.g. "Mary-Jane", "O'Neil", "Sai Ram". */
    private static final Pattern NAME = Pattern.compile("^[A-Za-z]+([ '\\-][A-Za-z]+)*$");

    /** Practical email check: local@domain.tld, no spaces, no consecutive dots. */
    private static final Pattern EMAIL = Pattern.compile(
            "^[A-Za-z0-9._%+\\-]+@[A-Za-z0-9\\-]+(\\.[A-Za-z0-9\\-]+)*\\.[A-Za-z]{2,}$");

    private static final Pattern ADDRESS_CHARS = Pattern.compile("^[A-Za-z0-9 .,'#/\\-]+$");
    private static final Pattern PO_BOX = Pattern.compile(
            "\\b(p\\.?\\s*o\\.?\\s*box|post\\s+office\\s+box)\\b", Pattern.CASE_INSENSITIVE);
    private static final Pattern CITY = Pattern.compile("^[A-Za-z]+([ .'\\-]{1,2}[A-Za-z]+)*$");
    private static final Pattern ZIP = Pattern.compile("^\\d{5}(-\\d{4})?$");
    private static final Pattern MONEY = Pattern.compile("^\\d{1,3}(,?\\d{3})*$");
    private static final Pattern USER_ID = Pattern.compile("^[A-Za-z][A-Za-z0-9._]*$");
    private static final Pattern SPECIAL = Pattern.compile("[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?~`]");

    public static final Set<String> US_STATES = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
            "AL", "AK", "AZ", "AR", "CA", "CO", "CT", "DE", "DC", "FL", "GA", "HI", "ID", "IL", "IN",
            "IA", "KS", "KY", "LA", "ME", "MD", "MA", "MI", "MN", "MS", "MO", "MT", "NE", "NV", "NH",
            "NJ", "NM", "NY", "NC", "ND", "OH", "OK", "OR", "PA", "RI", "SC", "SD", "TN", "TX", "UT",
            "VT", "VA", "WA", "WV", "WI", "WY", "PR")));

    // ---------------------------------------------------------------- Name

    public ValidationResult validatePreferredName(@Nullable String input, @Nullable String current) {
        String name = trim(input);
        if (name.isEmpty()) return invalid(ValidationError.NAME_REQUIRED);
        if (name.length() > NAME_MAX) return invalid(ValidationError.NAME_TOO_LONG);
        if (!NAME.matcher(name).matches()) return invalid(ValidationError.NAME_INVALID_CHARACTERS);
        if (name.equalsIgnoreCase(trim(current))) return invalid(ValidationError.NAME_SAME_AS_CURRENT);
        return ok();
    }

    // ---------------------------------------------------------------- Email

    public ValidationResult validateEmail(@Nullable String input, @Nullable String current) {
        String email = trim(input);
        if (email.isEmpty()) return invalid(ValidationError.EMAIL_REQUIRED);
        if (email.length() > EMAIL_MAX) return invalid(ValidationError.EMAIL_TOO_LONG);
        if (!EMAIL.matcher(email).matches() || email.contains("..")) {
            return invalid(ValidationError.EMAIL_INVALID_FORMAT);
        }
        if (email.equalsIgnoreCase(trim(current))) return invalid(ValidationError.EMAIL_SAME_AS_CURRENT);
        return ok();
    }

    public ValidationResult validateConfirmEmail(@Nullable String email, @Nullable String confirm) {
        String c = trim(confirm);
        if (c.isEmpty()) return invalid(ValidationError.EMAIL_CONFIRM_REQUIRED);
        if (!c.equalsIgnoreCase(trim(email))) return invalid(ValidationError.EMAIL_MISMATCH);
        return ok();
    }

    // ---------------------------------------------------------------- Phone

    /**
     * Accepts "(469) 555-0142", "469-555-0142", "4695550142" or "+1 469 555 0142".
     * Follows NANP rules: area code and exchange can't start with 0 or 1.
     */
    public ValidationResult validateMobilePhone(@Nullable String input, @Nullable String currentDigits) {
        String digits = normalizePhone(input);
        if (digits.isEmpty()) return invalid(ValidationError.PHONE_REQUIRED);
        if (digits.length() != 10) return invalid(ValidationError.PHONE_INVALID_LENGTH);
        char area = digits.charAt(0);
        char exchange = digits.charAt(3);
        if (area == '0' || area == '1') return invalid(ValidationError.PHONE_INVALID_AREA_CODE);
        if (exchange == '0' || exchange == '1') return invalid(ValidationError.PHONE_INVALID_EXCHANGE);
        if (digits.equals(currentDigits)) return invalid(ValidationError.PHONE_SAME_AS_CURRENT);
        return ok();
    }

    /** Keeps digits only and drops a leading US country code "1" from 11-digit numbers. */
    public static String normalizePhone(@Nullable String input) {
        if (input == null) return "";
        String digits = input.replaceAll("\\D", "");
        if (digits.length() == 11 && digits.startsWith("1")) digits = digits.substring(1);
        return digits;
    }

    // ---------------------------------------------------------------- Address

    public ValidationResult validateAddressLine1(@Nullable String input) {
        String v = trim(input);
        if (v.isEmpty()) return invalid(ValidationError.ADDRESS_LINE1_REQUIRED);
        if (v.length() < ADDRESS_LINE1_MIN) return invalid(ValidationError.ADDRESS_LINE1_TOO_SHORT);
        if (v.length() > ADDRESS_MAX) return invalid(ValidationError.ADDRESS_TOO_LONG);
        if (!ADDRESS_CHARS.matcher(v).matches()) return invalid(ValidationError.ADDRESS_INVALID_CHARACTERS);
        if (PO_BOX.matcher(v).find()) return invalid(ValidationError.ADDRESS_PO_BOX_NOT_ALLOWED);
        return ok();
    }

    /** Line 2 (apt / suite) is optional, but if present it must follow the same character rules. */
    public ValidationResult validateAddressLine2(@Nullable String input) {
        String v = trim(input);
        if (v.isEmpty()) return ok();
        if (v.length() > ADDRESS_MAX) return invalid(ValidationError.ADDRESS_TOO_LONG);
        if (!ADDRESS_CHARS.matcher(v).matches()) return invalid(ValidationError.ADDRESS_INVALID_CHARACTERS);
        if (PO_BOX.matcher(v).find()) return invalid(ValidationError.ADDRESS_PO_BOX_NOT_ALLOWED);
        return ok();
    }

    public ValidationResult validateCity(@Nullable String input) {
        String v = trim(input);
        if (v.isEmpty()) return invalid(ValidationError.CITY_REQUIRED);
        if (v.length() > CITY_MAX || !CITY.matcher(v).matches()) return invalid(ValidationError.CITY_INVALID);
        return ok();
    }

    public ValidationResult validateState(@Nullable String input) {
        String v = trim(input).toUpperCase(Locale.US);
        if (v.isEmpty()) return invalid(ValidationError.STATE_REQUIRED);
        if (!US_STATES.contains(v)) return invalid(ValidationError.STATE_INVALID);
        return ok();
    }

    public ValidationResult validateZip(@Nullable String input) {
        String v = trim(input);
        if (v.isEmpty()) return invalid(ValidationError.ZIP_REQUIRED);
        if (!ZIP.matcher(v).matches() || v.startsWith("00000")) return invalid(ValidationError.ZIP_INVALID);
        return ok();
    }

    // ---------------------------------------------------------------- Income

    public ValidationResult validateEmploymentStatus(@Nullable String input) {
        return trim(input).isEmpty() ? invalid(ValidationError.EMPLOYMENT_STATUS_REQUIRED) : ok();
    }

    public ValidationResult validateAnnualIncome(@Nullable String input) {
        String v = trim(input).replace("$", "");
        if (v.isEmpty()) return invalid(ValidationError.INCOME_REQUIRED);
        if (!MONEY.matcher(v).matches()) return invalid(ValidationError.INCOME_INVALID);
        long amount = parseMoney(v);
        if (amount < 0 || amount > INCOME_MAX) return invalid(ValidationError.INCOME_OUT_OF_RANGE);
        return ok();
    }

    public ValidationResult validateHousingPayment(@Nullable String input, @Nullable String annualIncome) {
        String v = trim(input).replace("$", "");
        if (v.isEmpty()) return invalid(ValidationError.HOUSING_REQUIRED);
        if (!MONEY.matcher(v).matches()) return invalid(ValidationError.HOUSING_INVALID);
        long monthly = parseMoney(v);
        if (monthly < 0 || monthly > HOUSING_MAX) return invalid(ValidationError.HOUSING_OUT_OF_RANGE);
        // Cross-field rule: only checked when income itself is valid.
        if (validateAnnualIncome(annualIncome).isValid()) {
            long yearly = parseMoney(trim(annualIncome).replace("$", ""));
            if (yearly > 0 && monthly * 12 > yearly) return invalid(ValidationError.HOUSING_EXCEEDS_INCOME);
        }
        return ok();
    }

    /** "85,000" -> 85000. Returns -1 when the value can't be parsed. */
    public static long parseMoney(@Nullable String input) {
        if (input == null) return -1;
        String digits = input.replace(",", "").replace("$", "").trim();
        if (digits.isEmpty() || digits.length() > 12) return -1;
        try {
            return Long.parseLong(digits);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    // ---------------------------------------------------------------- User ID & password

    public ValidationResult validateUserId(@Nullable String input) {
        String v = trim(input);
        if (v.isEmpty()) return invalid(ValidationError.USER_ID_REQUIRED);
        if (v.length() < USER_ID_MIN || v.length() > USER_ID_MAX) return invalid(ValidationError.USER_ID_LENGTH);
        if (!USER_ID.matcher(v).matches()) return invalid(ValidationError.USER_ID_INVALID);
        return ok();
    }

    public ValidationResult validateCurrentPassword(@Nullable String input) {
        // Never trim passwords: spaces are part of what the user typed.
        return input == null || input.isEmpty() ? invalid(ValidationError.CURRENT_PASSWORD_REQUIRED) : ok();
    }

    public ValidationResult validateNewPassword(@Nullable String password, @Nullable String userId,
                                                @Nullable String currentPassword) {
        if (password == null || password.isEmpty()) return invalid(ValidationError.PASSWORD_REQUIRED);
        if (password.length() < PASSWORD_MIN || password.length() > PASSWORD_MAX) {
            return invalid(ValidationError.PASSWORD_LENGTH);
        }
        if (password.chars().anyMatch(Character::isWhitespace)) return invalid(ValidationError.PASSWORD_HAS_SPACE);
        if (!hasUppercase(password)) return invalid(ValidationError.PASSWORD_MISSING_UPPERCASE);
        if (!hasLowercase(password)) return invalid(ValidationError.PASSWORD_MISSING_LOWERCASE);
        if (!hasDigit(password)) return invalid(ValidationError.PASSWORD_MISSING_DIGIT);
        if (!hasSpecial(password)) return invalid(ValidationError.PASSWORD_MISSING_SPECIAL);
        String uid = trim(userId);
        if (!uid.isEmpty() && password.toLowerCase(Locale.US).contains(uid.toLowerCase(Locale.US))) {
            return invalid(ValidationError.PASSWORD_CONTAINS_USER_ID);
        }
        if (password.equals(currentPassword)) return invalid(ValidationError.PASSWORD_SAME_AS_CURRENT);
        return ok();
    }

    public ValidationResult validateConfirmPassword(@Nullable String password, @Nullable String confirm) {
        if (confirm == null || confirm.isEmpty()) return invalid(ValidationError.PASSWORD_CONFIRM_REQUIRED);
        if (!confirm.equals(password)) return invalid(ValidationError.PASSWORD_MISMATCH);
        return ok();
    }

    // Password rule helpers, shared with the live checklist so both always agree.
    public static boolean hasValidPasswordLength(@Nullable String s) {
        return s != null && s.length() >= PASSWORD_MIN && s.length() <= PASSWORD_MAX;
    }

    public static boolean hasUppercase(@Nullable String s) {
        return s != null && s.chars().anyMatch(c -> c >= 'A' && c <= 'Z');
    }

    public static boolean hasLowercase(@Nullable String s) {
        return s != null && s.chars().anyMatch(c -> c >= 'a' && c <= 'z');
    }

    public static boolean hasDigit(@Nullable String s) {
        return s != null && s.chars().anyMatch(c -> c >= '0' && c <= '9');
    }

    public static boolean hasSpecial(@Nullable String s) {
        return s != null && SPECIAL.matcher(s).find();
    }

    // ---------------------------------------------------------------- helpers

    private static String trim(@Nullable String s) {
        return s == null ? "" : s.trim();
    }

    private static ValidationResult ok() {
        return ValidationResult.valid();
    }

    private static ValidationResult invalid(ValidationError e) {
        return ValidationResult.invalid(e);
    }
}
