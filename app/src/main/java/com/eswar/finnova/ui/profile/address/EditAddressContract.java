package com.eswar.finnova.ui.profile.address;

import androidx.annotation.Nullable;

import com.eswar.finnova.base.BaseView;
import com.eswar.finnova.data.model.Address;
import com.eswar.finnova.validation.ValidationError;

public interface EditAddressContract {

    enum Field { LINE1, LINE2, CITY, STATE, ZIP }

    interface View extends BaseView {
        void showAddress(Address address);

        void showFieldError(Field field, @Nullable ValidationError error);

        /** Move focus (and screen reader) to the first invalid field. */
        void focusField(Field field);

        void showServerError(String message);

        void showSaved();
    }

    interface Presenter {
        void start();

        void onInputChanged(AddressForm form);

        void onSaveClicked(AddressForm form);
    }
}
