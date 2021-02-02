package com.example.timecurrencyconverter;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



public class MainActivity extends AppCompatActivity {
    private Spinner spinner1, spinner2;
    private HashMap<String, Double> currencyvalues = new HashMap<String, Double>();

    public void onClick (View view){
        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.moneysound);
        mediaPlayer.start();
        EditText currency_input = (EditText) findViewById(R.id.dollars);
        try {
            Double currency_result = CalculateCurrency(Double.parseDouble(currency_input.getText().toString()));
            Toast.makeText(this, String.format("%.2f", currency_result)+" "+String.valueOf(spinner2.getSelectedItem()), Toast.LENGTH_SHORT).show();
        }
        catch (java.lang.NumberFormatException e){
            Toast.makeText(this, "Invalid characters entered, please try again.", Toast.LENGTH_SHORT).show();
        }


    }
    public double CalculateCurrency(Double currency_input){
        double conversion1 = currencyvalues.get(String.valueOf(spinner1.getSelectedItem()));
        double conversion2 = currencyvalues.get(String.valueOf(spinner2.getSelectedItem()));
        return currency_input*conversion1/conversion2;
    }
    public void setSpinners(){
        spinner1 = (Spinner) findViewById(R.id.spinner);
        spinner2 = (Spinner) findViewById(R.id.spinner2);
        List<String> currency = new ArrayList<>();
        currency.add("NZD");
        currency.add("EUR");
        currency.add("USD");
        ArrayAdapter<String> currencyadapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, currency);
        currencyadapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner1.setAdapter(currencyadapter);
        spinner1.setSelection(0);
        spinner2.setAdapter(currencyadapter);
        spinner2.setSelection(1);
    }
    public void createCurrencyConversionValues() {
        currencyvalues.put("USD", 1.0);
        currencyvalues.put("NZD", 0.72);
        currencyvalues.put("EUR", 1.21);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSpinners();
        createCurrencyConversionValues();


    }}
