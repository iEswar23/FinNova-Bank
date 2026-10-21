package com.eswar.finnova.validation;

import androidx.annotation.Nullable;

/** Result of validating ONE field: either valid, or invalid with a single error code. */
public final class ValidationResult {

    private static final ValidationResult VALID = new ValidationResult(null);

    @Nullable
    private final ValidationError error;

    private ValidationResult(@Nullable ValidationError error) {
        this.error = error;
    }

    public static ValidationResult valid() {
        return VALID;
    }

    public static ValidationResult invalid(ValidationError error) {
        return new ValidationResult(error);
    }

    public boolean isValid() {
        return error == null;
    }

    @Nullable
    public ValidationError getError() {
        return error;
    }

    @Override
    public String toString() {
        return isValid() ? "VALID" : "INVALID(" + error + ")";
    }
}
