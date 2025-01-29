package com.example.calculator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    private TextView display;
    private String currentNumber = "0";
    private String operation = "";
    private double firstNumber = 0;
    private boolean isNewNumber = true;
    private DecimalFormat formatter = new DecimalFormat("#,##0.########");
    private StringBuilder displayBuilder = new StringBuilder();
    private boolean hasResult = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        display = findViewById(R.id.display);
        
        // Set up number buttons
        int[] numberIds = {R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
                          R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9};
        
        for (int id : numberIds) {
            Button button = findViewById(id);
            button.setOnClickListener(v -> numberPressed(button.getText().toString()));
        }

        // Set up operator buttons
        findViewById(R.id.btnPlus).setOnClickListener(v -> operatorPressed("+"));
        findViewById(R.id.btnMinus).setOnClickListener(v -> operatorPressed("-"));
        findViewById(R.id.btnMultiply).setOnClickListener(v -> operatorPressed("×"));
        findViewById(R.id.btnDivide).setOnClickListener(v -> operatorPressed("÷"));
        findViewById(R.id.btnEquals).setOnClickListener(v -> calculateResult());
        
        // Set up special buttons
        findViewById(R.id.btnClear).setOnClickListener(v -> clearCalculator());
        findViewById(R.id.btnDot).setOnClickListener(v -> addDecimalPoint());
        findViewById(R.id.btnPlusMinus).setOnClickListener(v -> toggleSign());
        findViewById(R.id.btnPercent).setOnClickListener(v -> calculatePercentage());
    }

    private void numberPressed(String number) {
        if (hasResult) {
            clearCalculator();
            hasResult = false;
        }
        
        if (isNewNumber) {
            currentNumber = number;
            isNewNumber = false;
        } else {
            currentNumber += number;
        }
        updateDisplay();
    }

    private void operatorPressed(String op) {
        if (!operation.isEmpty()) {
            calculateResult();
        }
        firstNumber = Double.parseDouble(currentNumber);
        operation = op;
        isNewNumber = true;
        hasResult = false;
        updateDisplay();
    }

    private void calculateResult() {
        if (operation.isEmpty()) return;

        double secondNumber = Double.parseDouble(currentNumber);
        double result = 0;

        switch (operation) {
            case "+":
                result = firstNumber + secondNumber;
                break;
            case "-":
                result = firstNumber - secondNumber;
                break;
            case "×":
                result = firstNumber * secondNumber;
                break;
            case "÷":
                if (secondNumber != 0) {
                    result = firstNumber / secondNumber;
                } else {
                    display.setText("Error");
                    return;
                }
                break;
        }

        currentNumber = formatter.format(result);
        operation = "";
        isNewNumber = true;
        hasResult = true;
        updateDisplay();
    }

    private void clearCalculator() {
        currentNumber = "0";
        operation = "";
        firstNumber = 0;
        isNewNumber = true;
        hasResult = false;
        updateDisplay();
    }

    private void addDecimalPoint() {
        if (hasResult) {
            clearCalculator();
            hasResult = false;
        }
        
        if (!currentNumber.contains(".")) {
            currentNumber += ".";
            updateDisplay();
        }
    }

    private void toggleSign() {
        if (currentNumber.startsWith("-")) {
            currentNumber = currentNumber.substring(1);
        } else {
            currentNumber = "-" + currentNumber;
        }
        updateDisplay();
    }

    private void calculatePercentage() {
        double number = Double.parseDouble(currentNumber);
        currentNumber = formatter.format(number / 100);
        updateDisplay();
    }

    private void updateDisplay() {
        displayBuilder.setLength(0);
        
        if (operation.isEmpty()) {
            displayBuilder.append(currentNumber);
        } else {
            displayBuilder.append(formatter.format(firstNumber))
                         .append(" ")
                         .append(operation)
                         .append(" ");
            if (!isNewNumber) {
                displayBuilder.append(currentNumber);
            }
        }
        
        display.setText(displayBuilder.toString());
    }
}