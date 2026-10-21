package com.eswar.finnova.ui.profile;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.eswar.finnova.data.repository.FakeBankRepository;
import com.eswar.finnova.ui.profile.name.EditNameContract;
import com.eswar.finnova.ui.profile.name.EditNamePresenter;
import com.eswar.finnova.util.TaskRunner;
import com.eswar.finnova.validation.ProfileValidator;
import com.eswar.finnova.validation.ValidationError;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

/**
 * Presenter test = "given this input, did the Presenter tell the View the right things?"
 * The View is a Mockito mock; the repository is the fake running synchronously (TaskRunner.IMMEDIATE).
 */
public class EditNamePresenterTest {

    private EditNameContract.View view;
    private EditNamePresenter presenter;

    @Before
    public void setUp() {
        view = mock(EditNameContract.View.class);
        presenter = new EditNamePresenter(new FakeBankRepository(TaskRunner.IMMEDIATE), new ProfileValidator());
        presenter.attachView(view);
        presenter.start();                     // loads current name "Alex"
    }

    @Test
    public void start_showsCurrentName() {
        verify(view).showCurrentName("Alex");
    }

    @Test
    public void save_withInvalidName_showsError_andDoesNotCallServer() {
        presenter.onSaveClicked("Alex123");

        verify(view).showNameError(ValidationError.NAME_INVALID_CHARACTERS);
        verify(view, never()).showLoading(true);
        verify(view, never()).showSaved();
    }

    @Test
    public void save_withEmptyName_showsRequired() {
        presenter.onSaveClicked("   ");
        verify(view).showNameError(ValidationError.NAME_REQUIRED);
    }

    @Test
    public void save_withSameName_showsSameAsCurrent() {
        presenter.onSaveClicked("alex");
        verify(view).showNameError(ValidationError.NAME_SAME_AS_CURRENT);
    }

    @Test
    public void save_withValidName_savesAndCloses() {
        presenter.onSaveClicked(" Eswar ");

        InOrder order = inOrder(view);
        order.verify(view).showNameError(null);
        order.verify(view).showLoading(true);
        order.verify(view).showLoading(false);
        order.verify(view).showSaved();
        verify(view, never()).showServerError(anyString());
    }

    @Test
    public void typing_beforeFirstSave_neverShowsErrors() {
        presenter.onNameChanged("Alex123");
        verify(view).showNameError(isNull());
        verify(view, never()).showNameError(ValidationError.NAME_INVALID_CHARACTERS);
    }

    @Test
    public void typing_afterFailedSave_revalidatesLive() {
        presenter.onSaveClicked("Alex1");      // error shown
        presenter.onNameChanged("Eswar");      // user fixes it

        InOrder order = inOrder(view);
        order.verify(view).showNameError(ValidationError.NAME_INVALID_CHARACTERS);
        order.verify(view).showNameError(null);  // error cleared while typing
    }

    @Test
    public void detachedView_isNeverTouched() {
        presenter.detachView();
        presenter.onSaveClicked("Eswar");
        verify(view, never()).showSaved();
    }
}
