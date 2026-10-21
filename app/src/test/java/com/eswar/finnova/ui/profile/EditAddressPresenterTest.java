package com.eswar.finnova.ui.profile;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.eswar.finnova.data.repository.FakeBankRepository;
import com.eswar.finnova.ui.profile.address.AddressForm;
import com.eswar.finnova.ui.profile.address.EditAddressContract;
import com.eswar.finnova.ui.profile.address.EditAddressContract.Field;
import com.eswar.finnova.ui.profile.address.EditAddressPresenter;
import com.eswar.finnova.util.TaskRunner;
import com.eswar.finnova.validation.ProfileValidator;
import com.eswar.finnova.validation.ValidationError;

import org.junit.Before;
import org.junit.Test;

public class EditAddressPresenterTest {

    private EditAddressContract.View view;
    private EditAddressPresenter presenter;

    @Before
    public void setUp() {
        view = mock(EditAddressContract.View.class);
        presenter = new EditAddressPresenter(new FakeBankRepository(TaskRunner.IMMEDIATE), new ProfileValidator());
        presenter.attachView(view);
    }

    @Test
    public void multipleErrors_allShown_focusGoesToFirst() {
        presenter.onSaveClicked(new AddressForm("PO Box 12", "", "", "TX", "123"));

        verify(view).showFieldError(Field.LINE1, ValidationError.ADDRESS_PO_BOX_NOT_ALLOWED);
        verify(view).showFieldError(Field.CITY, ValidationError.CITY_REQUIRED);
        verify(view).showFieldError(Field.ZIP, ValidationError.ZIP_INVALID);
        verify(view).showFieldError(Field.STATE, null);
        verify(view).focusField(Field.LINE1);
        verify(view, never()).showSaved();
    }

    @Test
    public void validAddress_saves() {
        presenter.onSaveClicked(new AddressForm("500 W Main St", "Suite 2", "Dallas", "tx", "75201"));
        verify(view).showSaved();
    }
}
