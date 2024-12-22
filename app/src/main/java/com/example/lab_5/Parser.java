package com.example.lab_5;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

public class Parser {

    public static HashMap<String, Double> parseJSON(String json) throws JSONException {
        HashMap<String, Double> rates = new HashMap<>();
        JSONObject jsonObject = new JSONObject(json);

        if (jsonObject.has("rates")) {
            JSONObject ratesObject = jsonObject.getJSONObject("rates");

            Iterator<String> keys = ratesObject.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                double value = ratesObject.getDouble(key);
                rates.put(key, value);
            }
        }
        return rates;
    }
}
