package com.eswar.finnova.ui.common;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.eswar.finnova.R;
import com.eswar.finnova.validation.ValidationError;
import com.google.android.material.textfield.TextInputLayout;

/**
 * The ONLY place that maps validation codes to user-facing text.
 * If QA or the content team asks to change an error message, you change strings.xml — nothing else.
 */
public final class ErrorMessages {

    private ErrorMessages() {
    }

    /** Shows the message for {@code error} under the field, or clears it when error is null. */
    public static void apply(TextInputLayout layout, @Nullable ValidationError error) {
        if (error == null) {
            layout.setError(null);
        } else {
            layout.setError(layout.getContext().getString(messageFor(error)));
        }
    }

    @StringRes
    public static int messageFor(ValidationError error) {
        switch (error) {
            case NAME_REQUIRED: return R.string.err_name_required;
            case NAME_TOO_LONG: return R.string.err_name_too_long;
            case NAME_INVALID_CHARACTERS: return R.string.err_name_invalid;
            case NAME_SAME_AS_CURRENT: return R.string.err_name_same;

            case EMAIL_REQUIRED: return R.string.err_email_required;
            case EMAIL_TOO_LONG: return R.string.err_email_too_long;
            case EMAIL_INVALID_FORMAT: return R.string.err_email_invalid;
            case EMAIL_CONFIRM_REQUIRED: return R.string.err_email_confirm_required;
            case EMAIL_MISMATCH: return R.string.err_email_mismatch;
            case EMAIL_SAME_AS_CURRENT: return R.string.err_email_same;

            case PHONE_REQUIRED: return R.string.err_phone_required;
            case PHONE_INVALID_LENGTH: return R.string.err_phone_length;
            case PHONE_INVALID_AREA_CODE: return R.string.err_phone_area_code;
            case PHONE_INVALID_EXCHANGE: return R.string.err_phone_exchange;
            case PHONE_SAME_AS_CURRENT: return R.string.err_phone_same;

            case ADDRESS_LINE1_REQUIRED: return R.string.err_line1_required;
            case ADDRESS_LINE1_TOO_SHORT: return R.string.err_line1_short;
            case ADDRESS_TOO_LONG: return R.string.err_address_long;
            case ADDRESS_INVALID_CHARACTERS: return R.string.err_address_chars;
            case ADDRESS_PO_BOX_NOT_ALLOWED: return R.string.err_po_box;
            case CITY_REQUIRED: return R.string.err_city_required;
            case CITY_INVALID: return R.string.err_city_invalid;
            case STATE_REQUIRED: return R.string.err_state_required;
            case STATE_INVALID: return R.string.err_state_invalid;
            case ZIP_REQUIRED: return R.string.err_zip_required;
            case ZIP_INVALID: return R.string.err_zip_invalid;

            case EMPLOYMENT_STATUS_REQUIRED: return R.string.err_employment_required;
            case INCOME_REQUIRED: return R.string.err_income_required;
            case INCOME_INVALID: return R.string.err_income_invalid;
            case INCOME_OUT_OF_RANGE: return R.string.err_income_range;
            case HOUSING_REQUIRED: return R.string.err_housing_required;
            case HOUSING_INVALID: return R.string.err_housing_invalid;
            case HOUSING_OUT_OF_RANGE: return R.string.err_housing_range;
            case HOUSING_EXCEEDS_INCOME: return R.string.err_housing_exceeds;

            case USER_ID_REQUIRED: return R.string.err_user_id_required;
            case USER_ID_LENGTH: return R.string.err_user_id_length;
            case USER_ID_INVALID: return R.string.err_user_id_invalid;
            case CURRENT_PASSWORD_REQUIRED: return R.string.err_current_password_required;
            case PASSWORD_REQUIRED: return R.string.err_password_required;
            case PASSWORD_LENGTH: return R.string.err_password_length;
            case PASSWORD_HAS_SPACE: return R.string.err_password_space;
            case PASSWORD_MISSING_UPPERCASE: return R.string.err_password_upper;
            case PASSWORD_MISSING_LOWERCASE: return R.string.err_password_lower;
            case PASSWORD_MISSING_DIGIT: return R.string.err_password_digit;
            case PASSWORD_MISSING_SPECIAL: return R.string.err_password_special;
            case PASSWORD_CONTAINS_USER_ID: return R.string.err_password_user_id;
            case PASSWORD_SAME_AS_CURRENT: return R.string.err_password_same;
            case PASSWORD_CONFIRM_REQUIRED: return R.string.err_password_confirm_required;
            case PASSWORD_MISMATCH: return R.string.err_password_mismatch;
            default: return R.string.something_went_wrong;
        }
    }
}
