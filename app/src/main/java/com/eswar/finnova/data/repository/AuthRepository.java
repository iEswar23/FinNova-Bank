package com.eswar.finnova.data.repository;

/** Sign-in and credential management. */
public interface AuthRepository {
    void login(String userId, String password, DataCallback<Void> callback);

    void updateCredentials(String currentPassword, String newUserId, String newPassword,
                           DataCallback<Void> callback);

    void logout();
}
