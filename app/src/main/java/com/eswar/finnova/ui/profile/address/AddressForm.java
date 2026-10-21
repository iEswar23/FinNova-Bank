package com.eswar.finnova.ui.profile.address;

/** Raw text from the address form, exactly as typed. */
public final class AddressForm {
    public final String line1;
    public final String line2;
    public final String city;
    public final String state;
    public final String zip;

    public AddressForm(String line1, String line2, String city, String state, String zip) {
        this.line1 = line1;
        this.line2 = line2;
        this.city = city;
        this.state = state;
        this.zip = zip;
    }
}
