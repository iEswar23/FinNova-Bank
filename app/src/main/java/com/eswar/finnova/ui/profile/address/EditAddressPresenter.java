package com.eswar.finnova.ui.profile.address;

import com.eswar.finnova.base.BasePresenter;
import com.eswar.finnova.data.model.Address;
import com.eswar.finnova.data.model.UserProfile;
import com.eswar.finnova.data.repository.DataCallback;
import com.eswar.finnova.data.repository.ProfileRepository;
import com.eswar.finnova.ui.profile.address.EditAddressContract.Field;
import com.eswar.finnova.validation.ProfileValidator;
import com.eswar.finnova.validation.ValidationResult;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

public class EditAddressPresenter extends BasePresenter<EditAddressContract.View>
        implements EditAddressContract.Presenter {

    private final ProfileRepository repository;
    private final ProfileValidator validator;
    private boolean submitted;

    public EditAddressPresenter(ProfileRepository repository, ProfileValidator validator) {
        this.repository = repository;
        this.validator = validator;
    }

    @Override
    public void start() {
        repository.getProfile(new DataCallback<UserProfile>() {
            @Override
            public void onSuccess(UserProfile profile) {
                if (getView() != null) getView().showAddress(profile.address);
            }

            @Override
            public void onError(Throwable error) {
                if (getView() != null) getView().showServerError(error.getMessage());
            }
        });
    }

    /** Ordered top-to-bottom like the form, so the first invalid entry is the first on screen. */
    Map<Field, ValidationResult> validateAll(AddressForm f) {
        Map<Field, ValidationResult> results = new LinkedHashMap<>();
        results.put(Field.LINE1, validator.validateAddressLine1(f.line1));
        results.put(Field.LINE2, validator.validateAddressLine2(f.line2));
        results.put(Field.CITY, validator.validateCity(f.city));
        results.put(Field.STATE, validator.validateState(f.state));
        results.put(Field.ZIP, validator.validateZip(f.zip));
        return results;
    }

    @Override
    public void onInputChanged(AddressForm form) {
        if (getView() == null) return;
        for (Map.Entry<Field, ValidationResult> e : validateAll(form).entrySet()) {
            getView().showFieldError(e.getKey(), submitted ? e.getValue().getError() : null);
        }
    }

    @Override
    public void onSaveClicked(AddressForm form) {
        EditAddressContract.View view = getView();
        if (view == null) return;
        submitted = true;

        Field firstInvalid = null;
        for (Map.Entry<Field, ValidationResult> e : validateAll(form).entrySet()) {
            view.showFieldError(e.getKey(), e.getValue().getError());
            if (firstInvalid == null && !e.getValue().isValid()) firstInvalid = e.getKey();
        }
        if (firstInvalid != null) {
            view.focusField(firstInvalid);
            return;
        }

        Address address = new Address(form.line1.trim(), form.line2.trim(), form.city.trim(),
                form.state.trim().toUpperCase(Locale.US), form.zip.trim());
        view.showLoading(true);
        repository.updateAddress(address, new DataCallback<UserProfile>() {
            @Override
            public void onSuccess(UserProfile profile) {
                if (getView() == null) return;
                getView().showLoading(false);
                getView().showSaved();
            }

            @Override
            public void onError(Throwable error) {
                if (getView() == null) return;
                getView().showLoading(false);
                getView().showServerError(error.getMessage());
            }
        });
    }
}
