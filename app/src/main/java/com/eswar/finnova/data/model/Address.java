package com.eswar.finnova.data.model;

import androidx.annotation.NonNull;

/** Immutable mailing address. */
public final class Address {
    public final String line1;
    public final String line2;
    public final String city;
    public final String state;
    public final String zip;

    public Address(String line1, String line2, String city, String state, String zip) {
        this.line1 = line1;
        this.line2 = line2 == null ? "" : line2;
        this.city = city;
        this.state = state;
        this.zip = zip;
    }

    /** "123 Main St, Apt 4, Irving, TX 75038" */
    @NonNull
    public String singleLine() {
        StringBuilder sb = new StringBuilder(line1);
        if (!line2.isEmpty()) sb.append(", ").append(line2);
        sb.append(", ").append(city).append(", ").append(state).append(' ').append(zip);
        return sb.toString();
    }
}
