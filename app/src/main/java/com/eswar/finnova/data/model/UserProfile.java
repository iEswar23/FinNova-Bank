package com.eswar.finnova.data.model;

/**
 * The signed-in customer's profile. Immutable: every "update" creates a new copy
 * through the with...() methods, which keeps the in-memory repository thread-safe
 * and easy to reason about.
 */
public final class UserProfile {
    public final String userId;
    public final String legalName;
    public final String preferredFirstName;
    public final String email;
    public final String mobilePhone;   // 10 digits, no formatting, e.g. "4695550142"
    public final Address address;
    public final Income income;

    public UserProfile(String userId, String legalName, String preferredFirstName, String email,
                       String mobilePhone, Address address, Income income) {
        this.userId = userId;
        this.legalName = legalName;
        this.preferredFirstName = preferredFirstName;
        this.email = email;
        this.mobilePhone = mobilePhone;
        this.address = address;
        this.income = income;
    }

    public UserProfile withPreferredFirstName(String v) {
        return new UserProfile(userId, legalName, v, email, mobilePhone, address, income);
    }

    public UserProfile withEmail(String v) {
        return new UserProfile(userId, legalName, preferredFirstName, v, mobilePhone, address, income);
    }

    public UserProfile withMobilePhone(String v) {
        return new UserProfile(userId, legalName, preferredFirstName, email, v, address, income);
    }

    public UserProfile withAddress(Address v) {
        return new UserProfile(userId, legalName, preferredFirstName, email, mobilePhone, v, income);
    }

    public UserProfile withIncome(Income v) {
        return new UserProfile(userId, legalName, preferredFirstName, email, mobilePhone, address, v);
    }

    public UserProfile withUserId(String v) {
        return new UserProfile(v, legalName, preferredFirstName, email, mobilePhone, address, income);
    }
}
