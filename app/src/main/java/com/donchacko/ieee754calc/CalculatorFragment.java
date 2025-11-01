package com.donchacko.ieee754calc;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class CalculatorFragment extends Fragment {
    
    private EditText etInput1, etInput2;
    private TextView tvResult;
    private Spinner spinnerBase;
    private Spinner spinnerOperation;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_calculator, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        initializeViews(view);
        setupSpinners();
        setupButtonListener(view);
    }

    private void initializeViews(View view){
        etInput1 = view.findViewById(R.id.etInput1);
        etInput2 = view.findViewById(R.id.etInput2);
        tvResult = view.findViewById(R.id.tvResult);
        spinnerBase = view.findViewById(R.id.spinnerBase);
        spinnerOperation = view.findViewById(R.id.spinnerOperation);
    }

    private void setupSpinners(){
        ArrayAdapter<CharSequence> baseAdapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.base_options,
                android.R.layout.simple_spinner_item
        );
        baseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBase.setAdapter(baseAdapter);
        
        ArrayAdapter<CharSequence> operationAdapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.operation_options,
                android.R.layout.simple_spinner_item
        );
        operationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOperation.setAdapter(operationAdapter);
    }

    private void setupButtonListener(View view){
        Button btnCalculate = view.findViewById(R.id.btnCalculate);
        btnCalculate.setOnClickListener(v -> performCalculation());
    }

    private void performCalculation(){
        String input1Str = etInput1.getText().toString().trim();
        String input2Str = etInput2.getText().toString().trim();
        String operation = spinnerOperation.getSelectedItem().toString();
        String base = spinnerBase.getSelectedItem().toString();

        if (input1Str.isEmpty() || input2Str.isEmpty()){
            tvResult.setText("Please enter both numbers");
            return;
        }

        try {
            double num1 = convertToDecimal(input1Str, base);
            double num2 = convertToDecimal(input2Str, base);
            
            if (Double.isNaN(num1) || Double.isNaN(num2)){
                tvResult.setText("Invalid input for selected base");
                return;
            }

            double result = calculateResult(num1, num2, operation);
            if(Double.isNaN(result)){
                return; 
            }
            String resultStr = convertFromDecimal(result, base);
            tvResult.setText("Result: " + resultStr);
            
        } catch (Exception e) {
            tvResult.setText("Error: " + e.getMessage());
        }
    }

    private double calculateResult(double num1, double num2, String operation){
        switch(operation){
            case "Add (+)":
                return num1 + num2;
            case "Subtract (-)":
                return num1 - num2;
            case "Multiply (×)":
                return num1 * num2;
            case "Divide (÷)":
                if (num2 == 0) {
                    tvResult.setText("Cannot divide by zero");
                    return Double.NaN;
                }
                return num1 / num2;
            default:
                tvResult.setText("Unknown operation");
                return Double.NaN;
        }
    }

    private double convertToDecimal(String value, String base){
        try{
            switch (base) {
                case "Decimal":
                    return Double.parseDouble(value);
                case "Binary":
                    String dec = NumberConverter.binaryToDecimal(value);
                    if(dec.equals("Error")) return Double.NaN;
                    return Double.parseDouble(dec);
                case "Hexadecimal":
                    String decHex = NumberConverter.hexToDecimal(value);
                    if(decHex.equals("Error")) return Double.NaN;
                    return Double.parseDouble(decHex);
                case "Octal":
                    String decOct = NumberConverter.octalToDecimal(value);
                    if(decOct.equals("Error")) return Double.NaN;
                    return Double.parseDouble(decOct);
                default:
                    return Double.NaN;
            }
        } catch (Exception e) {
            return Double.NaN;
        }
    }

    private String convertFromDecimal(double value, String base){
        String decimalStr = String.valueOf(value);
        switch(base){
            case "Decimal":
                return decimalStr;
            case "Binary":
                String binary = NumberConverter.decimalToBinary(decimalStr);
                return binary.equals("Error") ? decimalStr : binary;
            case "Hexadecimal":
                String hex = NumberConverter.decimalToHex(decimalStr);
                return hex.equals("Error") ? decimalStr : hex;
            case "Octal":
                String octal = NumberConverter.decimalToOctal(decimalStr);
                return octal.equals("Error") ? decimalStr : octal;
            default:
                return decimalStr;
        }
    }
}

