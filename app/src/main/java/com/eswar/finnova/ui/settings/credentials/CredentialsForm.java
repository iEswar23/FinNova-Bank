package com.eswar.finnova.ui.settings.credentials;

public final class CredentialsForm {
    public final String currentPassword;
    public final String newUserId;
    public final String newPassword;
    public final String confirmPassword;

    public CredentialsForm(String currentPassword, String newUserId, String newPassword, String confirmPassword) {
        this.currentPassword = currentPassword;
        this.newUserId = newUserId;
        this.newPassword = newPassword;
        this.confirmPassword = confirmPassword;
    }
}
