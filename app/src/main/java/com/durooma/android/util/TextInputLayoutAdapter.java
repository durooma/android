package com.durooma.android.util;

import android.support.design.widget.TextInputLayout;
import android.widget.EditText;
import com.mobsandgeeks.saripaar.adapter.ViewDataAdapter;
import com.mobsandgeeks.saripaar.exception.ConversionException;

public class TextInputLayoutAdapter implements ViewDataAdapter<TextInputLayout, String> {

    @Override
    public String getData(TextInputLayout view) throws ConversionException {
        EditText editText = view.getEditText();
        if (editText != null) {
            return editText.getText().toString();
        } else {
            return null;
        }
    }

}
