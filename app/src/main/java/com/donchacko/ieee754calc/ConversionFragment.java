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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etDecimal = view.findViewById(R.id.etDecimal);
        etBinary = view.findViewById(R.id.etBinary);
        etIEEE754 = view.findViewById(R.id.etIEEE754);
        etHex = view.findViewById(R.id.etHex);
        etOctal = view.findViewById(R.id.etOctal);

        setupTextWatchers();
    }

    private void clearAllFieldsExcept(String except) {
        isUpdating = true;
        if(!except.equals("decimal")) etDecimal.setText("");
        if(!except.equals("binary")) etBinary.setText("");
        if(!except.equals("ieee754")) etIEEE754.setText("");
        if(!except.equals("hex")) etHex.setText("");
        if(!except.equals("octal")) etOctal.setText("");
        isUpdating = false;
    }
    
    private void setAllOtherFieldsInvalid(String except) {
        if(!except.equals("decimal")) etDecimal.setText("Invalid");
        if(!except.equals("binary")) etBinary.setText("Invalid");
        if(!except.equals("ieee754")) etIEEE754.setText("Invalid");
        if(!except.equals("hex")) etHex.setText("Invalid");
        if(!except.equals("octal")) etOctal.setText("Invalid");
    }

    private void setupTextWatchers(){
        etDecimal.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                if (!isUpdating && s.length() > 0 && !s.toString().trim().isEmpty()) {
                    updateFromDecimal(s.toString());
                } else if (!isUpdating && s.length() == 0) {
                    clearAllFieldsExcept("decimal");
                }
            }
        });

        etBinary.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                if (!isUpdating && s.length() > 0 && !s.toString().trim().isEmpty()) {
                    updateFromBinary(s.toString());
                } else if (!isUpdating && s.length() == 0) {
                    clearAllFieldsExcept("binary");
                }
            }
        });

        etIEEE754.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count){}
            @Override
            public void afterTextChanged(Editable s){
                if (!isUpdating && s.length() > 0 && !s.toString().trim().isEmpty()) {
                    updateFromIEEE754(s.toString());
                } else if (!isUpdating && s.length() == 0) {
                    clearAllFieldsExcept("ieee754");
                }
            }
        });

        etHex.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count){}
            @Override
            public void afterTextChanged(Editable s){
                if (!isUpdating && s.length() > 0 && !s.toString().trim().isEmpty()) {
                    updateFromHex(s.toString());
                } else if (!isUpdating && s.length() == 0) {
                    clearAllFieldsExcept("hex");
                }
            }
        });

        etOctal.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count){}
            @Override
            public void afterTextChanged(Editable s){
                if (!isUpdating && s.length() > 0 && !s.toString().trim().isEmpty()) {
                    updateFromOctal(s.toString());
                } else if (!isUpdating && s.length() == 0) {
                    clearAllFieldsExcept("octal");
                }
            }
        });
    }

    private void updateFromDecimal(String decimal){
        isUpdating = true;
        
        if (!NumberConverter.isValidDecimal(decimal)){
            setAllOtherFieldsInvalid("decimal");
            isUpdating = false;
            return;
        }
        try{
            String binary = NumberConverter.decimalToBinary(decimal);
            String hex = NumberConverter.decimalToHex(decimal);
            String octal = NumberConverter.decimalToOctal(decimal);
            String ieee754 = NumberConverter.decimalToIEEE754(decimal);
            if (!binary.equals("Error")) etBinary.setText(binary);
            else etBinary.setText("Invalid");
            if (!hex.equals("Error")) etHex.setText(hex);
            else etHex.setText("Invalid");
            if (!octal.equals("Error")) etOctal.setText(octal);
            else etOctal.setText("Invalid");
            if (!ieee754.equals("Error")) etIEEE754.setText(ieee754);
            else etIEEE754.setText("Invalid");
        } catch (Exception e) {
            setAllOtherFieldsInvalid("decimal");
        }
        
        isUpdating = false;
    }

    private void updateFromBinary(String binary){
        isUpdating = true;
        if(!NumberConverter.isValidBinary(binary)){
            setAllOtherFieldsInvalid("binary");
            isUpdating = false;
            return;
        }
        try{
            String decimal = NumberConverter.binaryToDecimal(binary);
            if (!decimal.equals("Error")) {
                etDecimal.setText(decimal);
                String hex = NumberConverter.decimalToHex(decimal);
                String octal = NumberConverter.decimalToOctal(decimal);
                String ieee754 = NumberConverter.decimalToIEEE754(decimal);
                if (!hex.equals("Error")) etHex.setText(hex);
                else etHex.setText("Invalid");
                if (!octal.equals("Error")) etOctal.setText(octal);
                else etOctal.setText("Invalid");
                if (!ieee754.equals("Error")) etIEEE754.setText(ieee754);
                else etIEEE754.setText("Invalid");
            } else {
                setAllOtherFieldsInvalid("binary");
            }
        } catch (Exception e) {
            setAllOtherFieldsInvalid("binary");
        }
        isUpdating = false;
    }

    private void updateFromIEEE754(String ieee754){
        isUpdating = true;
        if (!NumberConverter.isValidIEEE754(ieee754)) {
            setAllOtherFieldsInvalid("ieee754");
            isUpdating = false;
            return;
        }
        try{
            String decimal = NumberConverter.ieee754ToDecimal(ieee754);
            if (!decimal.equals("Error")) {
                etDecimal.setText(decimal);
                String binary = NumberConverter.decimalToBinary(decimal);
                String hex = NumberConverter.decimalToHex(decimal);
                String octal = NumberConverter.decimalToOctal(decimal);
                if (!binary.equals("Error")) etBinary.setText(binary);
                else etBinary.setText("Invalid");
                if (!hex.equals("Error")) etHex.setText(hex);
                else etHex.setText("Invalid");
                if (!octal.equals("Error")) etOctal.setText(octal);
                else etOctal.setText("Invalid");
            } 
            else{
                setAllOtherFieldsInvalid("ieee754");
            }
        } 
        catch (Exception e){
            setAllOtherFieldsInvalid("ieee754");
        }
        
        isUpdating = false;
    }

    private void updateFromHex(String hex){
        isUpdating = true;
        if(!NumberConverter.isValidHex(hex)){
            setAllOtherFieldsInvalid("hex");
            isUpdating = false;
            return;
        }
        try{
            String decimal = NumberConverter.hexToDecimal(hex);
            if (!decimal.equals("Error")) {
                etDecimal.setText(decimal);
                String binary = NumberConverter.decimalToBinary(decimal);
                String octal = NumberConverter.decimalToOctal(decimal);
                String ieee754 = NumberConverter.decimalToIEEE754(decimal);
                if (!binary.equals("Error")) etBinary.setText(binary);
                else etBinary.setText("Invalid");
                if (!octal.equals("Error")) etOctal.setText(octal);
                else etOctal.setText("Invalid");
                if (!ieee754.equals("Error")) etIEEE754.setText(ieee754);
                else etIEEE754.setText("Invalid");
            } else {
                setAllOtherFieldsInvalid("hex");
            }
        } catch (Exception e) {
            setAllOtherFieldsInvalid("hex");
        }
        isUpdating = false;
    }

    private void updateFromOctal(String octal) {
        isUpdating = true;
        if (!NumberConverter.isValidOctal(octal)) {
            setAllOtherFieldsInvalid("octal");
            isUpdating = false;
            return;
        }
        try{
            String decimal = NumberConverter.octalToDecimal(octal);
            if (!decimal.equals("Error")) {
                etDecimal.setText(decimal);
                String binary = NumberConverter.decimalToBinary(decimal);
                String hex = NumberConverter.decimalToHex(decimal);
                String ieee754 = NumberConverter.decimalToIEEE754(decimal);
                if (!binary.equals("Error")) etBinary.setText(binary);
                else etBinary.setText("Invalid");
                if (!hex.equals("Error")) etHex.setText(hex);
                else etHex.setText("Invalid");
                if (!ieee754.equals("Error")) etIEEE754.setText(ieee754);
                else etIEEE754.setText("Invalid");
            } else {
                setAllOtherFieldsInvalid("octal");
            }
        } catch (Exception e) {
            setAllOtherFieldsInvalid("octal");
        }
        isUpdating = false;
    }

}

