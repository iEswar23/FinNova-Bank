package com.eswar.finnova.ui.profile;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.eswar.finnova.data.repository.FakeBankRepository;
import com.eswar.finnova.ui.profile.email.EditEmailContract;
import com.eswar.finnova.ui.profile.email.EditEmailPresenter;
import com.eswar.finnova.util.TaskRunner;
import com.eswar.finnova.validation.ProfileValidator;
import com.eswar.finnova.validation.ValidationError;

import org.junit.Before;
import org.junit.Test;

public class EditEmailPresenterTest {

    private EditEmailContract.View view;
    private EditEmailPresenter presenter;

    @Before
    public void setUp() {
        view = mock(EditEmailContract.View.class);
        presenter = new EditEmailPresenter(new FakeBankRepository(TaskRunner.IMMEDIATE), new ProfileValidator());
        presenter.attachView(view);
        presenter.start();
    }

    @Test
    public void start_showsMaskedEmail() {
        verify(view).showCurrentEmail("a••••••••••n@example.com");
    }

    @Test
    public void mismatch_showsErrorOnConfirmField() {
        presenter.onSaveClicked("eswar@gmail.com", "eswar@yahoo.com");
        verify(view).showEmailError(null);
        verify(view).showConfirmEmailError(ValidationError.EMAIL_MISMATCH);
        verify(view, never()).showLoading(true);
    }

    @Test
    public void serverSaysEmailInUse_showsFieldError() {
        presenter.onSaveClicked("taken@finnova.com", "taken@finnova.com");
        verify(view).showEmailInUse("This email is already linked to another profile.");
        verify(view, never()).showSaved();
    }

    @Test
    public void validEmail_saves() {
        presenter.onSaveClicked("eswar@gmail.com", "eswar@gmail.com");
        verify(view).showSaved();
    }
}
