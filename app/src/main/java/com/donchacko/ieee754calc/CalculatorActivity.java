package com.donchacko.ieee754calc;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class CalculatorActivity extends AppCompatActivity {
    
    private EditText etInput1, etInput2;
    private TextView tvResult;
    private Spinner spinnerBase;
    private Spinner spinnerOperation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_calculator);
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Calculator");
        }

        etInput1 = findViewById(R.id.etInput1);
        etInput2 = findViewById(R.id.etInput2);
        tvResult = findViewById(R.id.tvResult);
        spinnerBase = findViewById(R.id.spinnerBase);
        spinnerOperation = findViewById(R.id.spinnerOperation);
        
        // Setup spinners
        ArrayAdapter<CharSequence> baseAdapter = ArrayAdapter.createFromResource(this,
                R.array.base_options, android.R.layout.simple_spinner_item);
        baseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBase.setAdapter(baseAdapter);
        
        ArrayAdapter<CharSequence> operationAdapter = ArrayAdapter.createFromResource(this,
                R.array.operation_options, android.R.layout.simple_spinner_item);
        operationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOperation.setAdapter(operationAdapter);
        
        Button btnCalculate = findViewById(R.id.btnCalculate);
        btnCalculate.setOnClickListener(v -> performCalculation());
    }

    private void performCalculation() {
        String input1Str = etInput1.getText().toString().trim();
        String input2Str = etInput2.getText().toString().trim();
        String operation = spinnerOperation.getSelectedItem().toString();
        String base = spinnerBase.getSelectedItem().toString();

        if (input1Str.isEmpty() || input2Str.isEmpty()) {
            tvResult.setText("Please enter both numbers");
            return;
        }

        try {
            double num1 = convertToDecimal(input1Str, base);
            double num2 = convertToDecimal(input2Str, base);
            
            if (Double.isNaN(num1) || Double.isNaN(num2)) {
                tvResult.setText("Invalid input for selected base");
                return;
            }

            double result = 0;
            switch (operation) {
                case "Add (+)":
                    result = num1 + num2;
                    break;
                case "Subtract (-)":
                    result = num1 - num2;
                    break;
                case "Multiply (×)":
                    result = num1 * num2;
                    break;
                case "Divide (÷)":
                    if (num2 == 0) {
                        tvResult.setText("Cannot divide by zero");
                        return;
                    }
                    result = num1 / num2;
                    break;
                default:
                    tvResult.setText("Unknown operation");
                    return;
            }

            String resultStr = convertFromDecimal(result, base);
            tvResult.setText("Result: " + resultStr);
            
        } catch (Exception e) {
            tvResult.setText("Error: " + e.getMessage());
        }
    }

    private double convertToDecimal(String value, String base) {
        try {
            switch (base) {
                case "Decimal":
                    return Double.parseDouble(value);
                case "Binary":
                    String dec = NumberConverter.binaryToDecimal(value);
                    return Double.parseDouble(dec);
                case "Hexadecimal":
                    String decHex = NumberConverter.hexToDecimal(value);
                    return Double.parseDouble(decHex);
                case "Octal":
                    String decOct = NumberConverter.octalToDecimal(value);
                    return Double.parseDouble(decOct);
                default:
                    return Double.NaN;
            }
        } catch (Exception e) {
            return Double.NaN;
        }
    }

    private String convertFromDecimal(double value, String base) {
        switch (base) {
            case "Decimal":
                return String.valueOf(value);
            case "Binary":
                return NumberConverter.decimalToBinary(String.valueOf(value));
            case "Hexadecimal":
                return NumberConverter.decimalToHex(String.valueOf(value));
            case "Octal":
                return NumberConverter.decimalToOctal(String.valueOf(value));
            default:
                return String.valueOf(value);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

