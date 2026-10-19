package ca.example.payrollcalculator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.text.DecimalFormat;

public class MainActivity extends Activity {
    private EditText hoursInput, rateInput, taxInput;
    private TextView errorText, regularPayOutput, overtimeOutput, totalPayOutput, taxOutput;
    private final DecimalFormat moneyFormat = new DecimalFormat("0.00");

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hoursInput = findViewById(R.id.hoursInput);
        rateInput = findViewById(R.id.rateInput);
        taxInput = findViewById(R.id.taxInput);
        errorText = findViewById(R.id.errorText);
        regularPayOutput = findViewById(R.id.regularPayOutput);
        overtimeOutput = findViewById(R.id.overtimeOutput);
        totalPayOutput = findViewById(R.id.totalPayOutput);
        taxOutput = findViewById(R.id.taxOutput);

        Button calculateButton = findViewById(R.id.calculateButton);
        calculateButton.setOnClickListener(v -> calculatePay());
        Button aboutButton = findViewById(R.id.aboutButton);
        aboutButton.setOnClickListener(v -> startActivity(new Intent(this, AboutActivity.class)));
    }

    private void calculatePay() {
        try {
            double hours = readNumber(hoursInput, "hours worked");
            double rate = readNumber(rateInput, "hourly rate");
            double taxRatePercent = readNumber(taxInput, "tax rate");
            if (hours < 0 || rate < 0 || taxRatePercent < 0 || taxRatePercent > 100) {
                throw new IllegalArgumentException("Enter non-negative hours and rate, and a tax rate from 0 to 100%.");
            }
            double regularPay = Math.min(hours, 40.0) * rate;
            double overtimePay = Math.max(hours - 40.0, 0.0) * rate * 1.5;
            double totalPay = regularPay + overtimePay;
            // The assignment defines tax on regular pay, not total pay.
            double tax = regularPay * (taxRatePercent / 100.0);
            regularPayOutput.setText("Regular pay: " + moneyFormat.format(regularPay));
            overtimeOutput.setText("Overtime pay: " + moneyFormat.format(overtimePay));
            totalPayOutput.setText("Total pay: " + moneyFormat.format(totalPay));
            taxOutput.setText("Tax: " + moneyFormat.format(tax));
            errorText.setVisibility(View.GONE);
        } catch (IllegalArgumentException ex) {
            errorText.setText(ex.getMessage());
            errorText.setVisibility(View.VISIBLE);
        }
    }

    private double readNumber(EditText input, String label) {
        String value = input.getText().toString().trim();
        if (value.isEmpty()) throw new IllegalArgumentException("Please enter " + label + ".");
        try { return Double.parseDouble(value); }
        catch (NumberFormatException ex) { throw new IllegalArgumentException("Enter a valid number for " + label + "."); }
    }
}
