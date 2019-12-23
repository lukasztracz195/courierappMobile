package com.project.courierapp.model.watchers;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.project.courierapp.model.validators.TextValidator;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class WatcherEditText implements TextWatcher {

    private TextInputLayout textInputLayout;
    private TextView errorTextView;
    private TextValidator validator;

    private WatcherEditText(@Nonnull TextInputEditText textInputEditText,
                            @Nullable TextView errorTextView,@Nonnull TextValidator validator) {
        this.textInputLayout = (TextInputLayout) textInputEditText.getParent().getParent();
        this.errorTextView = errorTextView;
        this.validator = validator;
    }

    public static WatcherEditText of(@Nonnull TextInputEditText textInputEditText,
                                     @Nullable TextView errorTextView,
                                     @Nonnull TextValidator validator){
        return new WatcherEditText(textInputEditText,errorTextView,validator);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if(!s.toString().isEmpty()) {
            validator.setTextToValidation(s.toString());
            validator.validate();
            if (validator.isInvalid()) {
                if (textInputLayout != null && textInputLayout.isErrorEnabled()) {
                    textInputLayout.setError(validator.getErrorMessage());
                } else {
                    errorTextView.setText(validator.getErrorMessage());
                }
            }
        }
    }
}
