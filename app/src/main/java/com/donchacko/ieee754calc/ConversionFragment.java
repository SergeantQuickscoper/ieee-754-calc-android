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
import android.text.InputFilter;

public class ConversionFragment extends Fragment {
    
    private EditText etDecimal, etBinary, etTwosComplement, etIEEE754Sign, etIEEE754Exponent, etIEEE754Mantissa, etHex, etOctal;
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
        etTwosComplement = view.findViewById(R.id.etTwosComplement);
        etIEEE754Sign = view.findViewById(R.id.etIEEE754Sign);
        etIEEE754Exponent = view.findViewById(R.id.etIEEE754Exponent);
        etIEEE754Mantissa = view.findViewById(R.id.etIEEE754Mantissa);
        etHex = view.findViewById(R.id.etHex);
        etOctal = view.findViewById(R.id.etOctal);

        InputFilter binaryFilter = (source, start, end, dest, dstart, dend) -> {
            for(int i = start; i < end; i++){
                char c = source.charAt(i);
                if(c != '0' && c != '1'){
                    return "";
                }
            }
            return null;
        };
        etTwosComplement.setFilters(new InputFilter[]{binaryFilter, new InputFilter.LengthFilter(32)});
        etIEEE754Sign.setFilters(new InputFilter[]{binaryFilter, new InputFilter.LengthFilter(1)});
        etIEEE754Exponent.setFilters(new InputFilter[]{binaryFilter, new InputFilter.LengthFilter(8)});
        etIEEE754Mantissa.setFilters(new InputFilter[]{binaryFilter, new InputFilter.LengthFilter(23)});
    }

    private void setupTextWatchers(){
        etDecimal.addTextChangedListener(createTextWatcher("decimal", this::updateFromDecimal));
        etBinary.addTextChangedListener(createTextWatcher("binary", this::updateFromBinary));
        etTwosComplement.addTextChangedListener(createTextWatcher("twosComplement", this::updateFromTwosComplement));
        etIEEE754Sign.addTextChangedListener(createTextWatcher("ieee754", this::updateFromIEEE754));
        etIEEE754Exponent.addTextChangedListener(createTextWatcher("ieee754", this::updateFromIEEE754));
        etIEEE754Mantissa.addTextChangedListener(createTextWatcher("ieee754", this::updateFromIEEE754));
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

    private void updateFromTwosComplement() {
        updateFromTwosComplement(etTwosComplement.getText().toString());
    }

    private void updateFromIEEE754() {
        String sign = etIEEE754Sign.getText().toString().trim();
        String exponent = etIEEE754Exponent.getText().toString().trim();
        String mantissa = etIEEE754Mantissa.getText().toString().trim();

        String ieee754 = combineIEEE754Fields(sign, exponent, mantissa);
        if(ieee754 != null && !ieee754.isEmpty()){
            updateFromIEEE754(ieee754);
        }
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

    private void updateFromTwosComplement(String twosComp) {
        isUpdating = true;
        try {
            if (!NumberConverter.isValidTwosComplement(twosComp)) {
                setAllOtherFieldsInvalid("twosComplement");
                return;
            }
            
            String decimal = NumberConverter.twosComplementToDecimal(twosComp);
            if (decimal.equals("Error")) {
                setAllOtherFieldsInvalid("twosComplement");
                return;
            }
            
            etDecimal.setText(decimal);
            updateFieldsFromDecimal(decimal);
        } catch (Exception e) {
            setAllOtherFieldsInvalid("twosComplement");
        } finally {
            isUpdating = false;
        }
    }

    private void updateFromIEEE754(String ieee754){
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

    private void updateFromHex(String hex){
        isUpdating = true;
        try{
            if(!NumberConverter.isValidHex(hex)){
                setAllOtherFieldsInvalid("hex");
                return;
            }
            
            String decimal = NumberConverter.hexToDecimal(hex);
            if(decimal.equals("Error")){
                setAllOtherFieldsInvalid("hex");
                return;
            }
            
            etDecimal.setText(decimal);
            updateFieldsFromDecimal(decimal);
        }
        catch(Exception e){
            setAllOtherFieldsInvalid("hex");
        }finally{
            isUpdating = false;
        }
    }

    private void updateFromOctal(String octal){
        isUpdating = true;
        try{
            if(!NumberConverter.isValidOctal(octal)){
                setAllOtherFieldsInvalid("octal");
                return;
            }
            String decimal = NumberConverter.octalToDecimal(octal);
            if(decimal.equals("Error")) {
                setAllOtherFieldsInvalid("octal");
                return;
            }
            etDecimal.setText(decimal);
            updateFieldsFromDecimal(decimal);
        } 
        catch(Exception e){
            setAllOtherFieldsInvalid("octal");
        } 
        finally{
            isUpdating = false;
        }
    }

    private void updateFieldsFromDecimal(String decimal){
        setFieldSafely(etBinary, NumberConverter.decimalToBinary(decimal));
        
        String twosComp = NumberConverter.decimalToTwosComplement(decimal);
        setFieldSafely(etTwosComplement, twosComp);
        
        setFieldSafely(etHex, NumberConverter.decimalToHex(decimal));
        setFieldSafely(etOctal, NumberConverter.decimalToOctal(decimal));
        setIEEE754Fields(NumberConverter.decimalToIEEE754(decimal));
    }

    private void setFieldSafely(EditText field, String value){
        if(value.equals("Error")){
            field.setText("Invalid");
        } 
        else{
            field.setText(value);
        }
    }

    private void setIEEE754Fields(String ieee754){
        if (ieee754.equals("Error")) {
            etIEEE754Sign.setText("Invalid");
            etIEEE754Exponent.setText("Invalid");
            etIEEE754Mantissa.setText("Invalid");
        } 
        else{
            parseIEEE754String(ieee754);
        }
    }

    private void parseIEEE754String(String ieee754){
        String normalized = ieee754;
        if(normalized.length() < 32){
            normalized = String.format("%32s", normalized).replace(' ', '0');
        } 
        else if(normalized.length() > 32){
            normalized = normalized.substring(normalized.length() - 32);
        }

        if(normalized.length() == 32){
            etIEEE754Sign.setText(normalized.substring(0, 1));
            etIEEE754Exponent.setText(normalized.substring(1, 9));
            etIEEE754Mantissa.setText(normalized.substring(9, 32));
        }
    }

    private String combineIEEE754Fields(String sign, String exponent, String mantissa) {
        if(sign.isEmpty() && exponent.isEmpty() && mantissa.isEmpty()){
            return null;
        }
        String normalizedSign = "0";
        if(!sign.isEmpty()){
            char signChar = sign.charAt(sign.length() - 1);
            normalizedSign = (signChar == '1') ? "1" : "0";
        }

        String normalizedExponent = exponent.replaceAll("[^01]", "");
        if(normalizedExponent.isEmpty()){
            normalizedExponent = "00000000";
        } 
        else if (normalizedExponent.length() < 8){
            normalizedExponent = String.format("%8s", normalizedExponent).replace(' ', '0');
        } 
        else if(normalizedExponent.length() > 8){
            normalizedExponent = normalizedExponent.substring(normalizedExponent.length() - 8);
        }
        
        String normalizedMantissa = mantissa.replaceAll("[^01]", "");
        if (normalizedMantissa.isEmpty()) {
            normalizedMantissa = "00000000000000000000000";
        } 
        else if (normalizedMantissa.length() < 23){
            normalizedMantissa = String.format("%23s", normalizedMantissa).replace(' ', '0');
        } 
        else if (normalizedMantissa.length() > 23){
            normalizedMantissa = normalizedMantissa.substring(normalizedMantissa.length() - 23);
        }
        return normalizedSign + normalizedExponent + normalizedMantissa;
    }

    private void clearAllFieldsExcept(String except){
        isUpdating = true;
        if (!except.equals("decimal")) etDecimal.setText("");
        if (!except.equals("binary")) etBinary.setText("");
        if (!except.equals("twosComplement")) etTwosComplement.setText("");
        if (!except.equals("ieee754")){
            etIEEE754Sign.setText("");
            etIEEE754Exponent.setText("");
            etIEEE754Mantissa.setText("");
        }
        if (!except.equals("hex")) etHex.setText("");
        if (!except.equals("octal")) etOctal.setText("");
        isUpdating = false;
    }
    
    private void setAllOtherFieldsInvalid(String except){
        if(!except.equals("decimal")) etDecimal.setText("Invalid");
        if(!except.equals("binary")) etBinary.setText("Invalid");
        if(!except.equals("twosComplement")) etTwosComplement.setText("Invalid");
        if(!except.equals("ieee754")){
            etIEEE754Sign.setText("Invalid");
            etIEEE754Exponent.setText("Invalid");
            etIEEE754Mantissa.setText("Invalid");
        }
        if(!except.equals("hex")) etHex.setText("Invalid");
        if(!except.equals("octal")) etOctal.setText("Invalid");
    }
}

