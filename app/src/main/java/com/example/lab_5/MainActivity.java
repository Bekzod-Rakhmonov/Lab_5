package com.example.lab_5;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Spinner spinnerBaseCurrency;
    private Button btnShowRates;
    private ListView lvCurrencies;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> currencyList = new ArrayList<>();
    private HashMap<String, Double> rates;

    private String selectedBaseCurrency = "USD";
    private final String API_KEY = "BBfndotEiicjeo3f97SxcGRk4waROnqO";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinnerBaseCurrency = findViewById(R.id.spinnerBaseCurrency);
        btnShowRates = findViewById(R.id.btnShowRates);
        lvCurrencies = findViewById(R.id.lvCurrencies);

        List<String> mainCurrencies = Arrays.asList("USD", "EUR", "GBP", "JPY", "AUD");
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mainCurrencies);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBaseCurrency.setAdapter(spinnerAdapter);

        spinnerBaseCurrency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedBaseCurrency = mainCurrencies.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        btnShowRates.setOnClickListener(view -> fetchAndDisplayRates(selectedBaseCurrency));
    }

    private void fetchAndDisplayRates(String baseCurrency) {
        String apiUrl = "https://api.apilayer.com/exchangerates_data/latest?base=" + baseCurrency + "&apikey=" + API_KEY;

        new DataLoader(result -> {
            if (result != null) {
                try {
                    rates = Parser.parseJSON(result);
                    populateList(rates, baseCurrency);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Error parsing data", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Failed to load data", Toast.LENGTH_SHORT).show();
            }
        }).execute(apiUrl);
    }

    private void populateList(HashMap<String, Double> rates, String baseCurrency) {
        if (rates == null || rates.isEmpty()) {
            Toast.makeText(this, "No rates available.", Toast.LENGTH_SHORT).show();
            return;
        }

        currencyList.clear();
        for (String currency : rates.keySet()) {
            if (!currency.equalsIgnoreCase(baseCurrency)) {
                currencyList.add(baseCurrency + " to " + currency + ": " + rates.get(currency));
            }
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, currencyList);
        lvCurrencies.setAdapter(adapter);
    }
}
