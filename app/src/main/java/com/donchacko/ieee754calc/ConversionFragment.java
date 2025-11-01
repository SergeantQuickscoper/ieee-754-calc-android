package com.donchacko.ieee754calc;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ConversionFragment extends Fragment {
    
    private EditText etDecimal, etBinary, etIEEE754, etHex, etOctal;
    private boolean isUpdating = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_conversion, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        initializeViews(view);
        setupTextWatchers();
    }

    private void initializeViews(View view){
        etDecimal = view.findViewById(R.id.etDecimal);
        etBinary = view.findViewById(R.id.etBinary);
        etIEEE754 = view.findViewById(R.id.etIEEE754);
        etHex = view.findViewById(R.id.etHex);
        etOctal = view.findViewById(R.id.etOctal);
    }

    private void setupTextWatchers(){
        etDecimal.addTextChangedListener(createTextWatcher("decimal", this::updateFromDecimal));
        etBinary.addTextChangedListener(createTextWatcher("binary", this::updateFromBinary));
        etIEEE754.addTextChangedListener(createTextWatcher("ieee754", this::updateFromIEEE754));
        etHex.addTextChangedListener(createTextWatcher("hex", this::updateFromHex));
        etOctal.addTextChangedListener(createTextWatcher("octal", this::updateFromOctal));
    }

    private TextWatcher createTextWatcher(String fieldName, Runnable updateFunction){
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (isUpdating) return;
                
                if (s.length() > 0 && !s.toString().trim().isEmpty()) {
                    updateFunction.run();
                } else if (s.length() == 0) {
                    clearAllFieldsExcept(fieldName);
                }
            }
        };
    }

    private void updateFromDecimal() {
        updateFromDecimal(etDecimal.getText().toString());
    }

    private void updateFromBinary() {
        updateFromBinary(etBinary.getText().toString());
    }

    private void updateFromIEEE754() {
        updateFromIEEE754(etIEEE754.getText().toString());
    }

    private void updateFromHex() {
        updateFromHex(etHex.getText().toString());
    }

    private void updateFromOctal() {
        updateFromOctal(etOctal.getText().toString());
    }

    private void updateFromDecimal(String decimal) {
        isUpdating = true;
        try {
            if (!NumberConverter.isValidDecimal(decimal)) {
                setAllOtherFieldsInvalid("decimal");
                return;
            }
            
            updateFieldsFromDecimal(decimal);
        } catch (Exception e) {
            setAllOtherFieldsInvalid("decimal");
        } finally {
            isUpdating = false;
        }
    }

    private void updateFromBinary(String binary) {
        isUpdating = true;
        try {
            if (!NumberConverter.isValidBinary(binary)) {
                setAllOtherFieldsInvalid("binary");
                return;
            }
            
            String decimal = NumberConverter.binaryToDecimal(binary);
            if (decimal.equals("Error")) {
                setAllOtherFieldsInvalid("binary");
                return;
            }
            
            etDecimal.setText(decimal);
            updateFieldsFromDecimal(decimal);
        } catch (Exception e) {
            setAllOtherFieldsInvalid("binary");
        } finally {
            isUpdating = false;
        }
    }

    private void updateFromIEEE754(String ieee754) {
        isUpdating = true;
        try {
            if (!NumberConverter.isValidIEEE754(ieee754)) {
                setAllOtherFieldsInvalid("ieee754");
                return;
            }
            
            String decimal = NumberConverter.ieee754ToDecimal(ieee754);
            if (decimal.equals("Error")) {
                setAllOtherFieldsInvalid("ieee754");
                return;
            }
            
            etDecimal.setText(decimal);
            updateFieldsFromDecimal(decimal);
        } catch (Exception e) {
            setAllOtherFieldsInvalid("ieee754");
        } finally {
            isUpdating = false;
        }
    }

    private void updateFromHex(String hex) {
        isUpdating = true;
        try {
            if (!NumberConverter.isValidHex(hex)) {
                setAllOtherFieldsInvalid("hex");
                return;
            }
            
            String decimal = NumberConverter.hexToDecimal(hex);
            if (decimal.equals("Error")) {
                setAllOtherFieldsInvalid("hex");
                return;
            }
            
            etDecimal.setText(decimal);
            updateFieldsFromDecimal(decimal);
        } catch (Exception e) {
            setAllOtherFieldsInvalid("hex");
        } finally {
            isUpdating = false;
        }
    }

    private void updateFromOctal(String octal) {
        isUpdating = true;
        try {
            if (!NumberConverter.isValidOctal(octal)) {
                setAllOtherFieldsInvalid("octal");
                return;
            }
            
            String decimal = NumberConverter.octalToDecimal(octal);
            if (decimal.equals("Error")) {
                setAllOtherFieldsInvalid("octal");
                return;
            }
            
            etDecimal.setText(decimal);
            updateFieldsFromDecimal(decimal);
        } catch (Exception e) {
            setAllOtherFieldsInvalid("octal");
        } finally {
            isUpdating = false;
        }
    }

    private void updateFieldsFromDecimal(String decimal) {
        setFieldSafely(etBinary, NumberConverter.decimalToBinary(decimal));
        setFieldSafely(etHex, NumberConverter.decimalToHex(decimal));
        setFieldSafely(etOctal, NumberConverter.decimalToOctal(decimal));
        setFieldSafely(etIEEE754, NumberConverter.decimalToIEEE754(decimal));
    }

    private void setFieldSafely(EditText field, String value) {
        if (value.equals("Error")) {
            field.setText("Invalid");
        } else {
            field.setText(value);
        }
    }

    private void clearAllFieldsExcept(String except) {
        isUpdating = true;
        if (!except.equals("decimal")) etDecimal.setText("");
        if (!except.equals("binary")) etBinary.setText("");
        if (!except.equals("ieee754")) etIEEE754.setText("");
        if (!except.equals("hex")) etHex.setText("");
        if (!except.equals("octal")) etOctal.setText("");
        isUpdating = false;
    }
    
    private void setAllOtherFieldsInvalid(String except) {
        if (!except.equals("decimal")) etDecimal.setText("Invalid");
        if (!except.equals("binary")) etBinary.setText("Invalid");
        if (!except.equals("ieee754")) etIEEE754.setText("Invalid");
        if (!except.equals("hex")) etHex.setText("Invalid");
        if (!except.equals("octal")) etOctal.setText("Invalid");
    }
}

