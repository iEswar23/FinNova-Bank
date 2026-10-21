package com.eswar.finnova.data.repository;

import com.eswar.finnova.data.model.Address;
import com.eswar.finnova.data.model.Income;
import com.eswar.finnova.data.model.UserProfile;

/**
 * Contract for reading/updating the customer's profile.
 * The Presenter only knows this interface, so the real implementation
 * (Retrofit + REST API in a real bank app) can be swapped with a fake for tests.
 */
public interface ProfileRepository {
    void getProfile(DataCallback<UserProfile> callback);

    void updatePreferredFirstName(String name, DataCallback<UserProfile> callback);

    void updateEmail(String email, DataCallback<UserProfile> callback);

    void updateMobilePhone(String digits, DataCallback<UserProfile> callback);

    void updateAddress(Address address, DataCallback<UserProfile> callback);

    void updateIncome(Income income, DataCallback<UserProfile> callback);
}
