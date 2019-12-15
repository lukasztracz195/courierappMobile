package com.project.courierapp.model.watchers;

import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

import com.project.courierapp.R;
import com.project.courierapp.applications.CourierApplication;

public class WatcherEditText implements TextWatcher {

    private Editabled editabled;
    private String fieldText;
    private TextView errorTextView;

    public WatcherEditText(Editabled editabled, String fieldText, TextView errorTextView) {
        this.editabled = editabled;
        this.fieldText = fieldText;
        this.errorTextView = errorTextView;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        editabled.setEditabled(true);
        fieldText = s.toString();
        errorTextView.setTextColor(Color.RED);
        if(editabled.isSaved()) {
            errorTextView.setText(CourierApplication.getContext().getText(R.string.saved_but_not_yet_validated));
        }else{
            errorTextView.setText(CourierApplication.getContext().getText(R.string.not_saved_and_not_yet_validated));
        }
        errorTextView.setBackgroundResource(R.color.white);
    }
}
