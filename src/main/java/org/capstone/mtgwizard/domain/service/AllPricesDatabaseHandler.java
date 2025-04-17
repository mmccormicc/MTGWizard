package org.capstone.mtgwizard.domain.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.JSONException;
import org.json.JSONObject;

public class AllPricesDatabaseHandler {

    private String jsonPath = "";
    private String priceDate = "2025-04-08";

    public AllPricesDatabaseHandler(String jsonPath) {
        this.jsonPath = jsonPath;
    }

    public Float getPrice(String seller, String uuid) {

        // Initialized as -1 to signal that price wasn't found
        Float tcgPrice = -1f;

        try {
            // Reading json from file as string
            String jsonString = new String(Files.readAllBytes(Paths.get(jsonPath)));
            // Converting string into json object
            JSONObject jsonObject = new JSONObject(jsonString);

            // Accessing data object
            JSONObject data = jsonObject.getJSONObject("data");
            // Getting all prices from uuid
            JSONObject allPrices = data.getJSONObject(uuid);
            // Getting prices in paper
            JSONObject paperPrices = allPrices.getJSONObject("paper");
            // Getting prices from tcgplayer
            JSONObject tcgPrices = paperPrices.getJSONObject(seller);
            // Getting retail price
            JSONObject retailPrice = tcgPrices.getJSONObject("retail");
            // Getting normal printing price
            JSONObject normalPrice = retailPrice.getJSONObject("normal");
            // Getting price from last date
            tcgPrice = normalPrice.getFloat(priceDate);

        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
            return tcgPrice;
        } catch (JSONException e) {
            System.err.println("Error parsing JSON: " + e.getMessage());
            return tcgPrice;
        }

        return tcgPrice;
    }


}
