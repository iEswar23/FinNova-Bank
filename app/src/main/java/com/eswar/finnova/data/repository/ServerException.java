package com.eswar.finnova.data.repository;

/**
 * Error returned by the (fake) server. Has a code so the UI can react to
 * specific server-side validation failures, e.g. "email already in use".
 */
public class ServerException extends Exception {
    public static final String EMAIL_IN_USE = "EMAIL_IN_USE";
    public static final String WRONG_PASSWORD = "WRONG_PASSWORD";
    public static final String INVALID_CREDENTIALS = "INVALID_CREDENTIALS";
    public static final String USER_ID_TAKEN = "USER_ID_TAKEN";

    private final String code;

    public ServerException(String code, String message) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
