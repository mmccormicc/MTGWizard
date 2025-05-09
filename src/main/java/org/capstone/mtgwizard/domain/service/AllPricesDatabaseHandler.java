package org.capstone.mtgwizard.domain.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.JSONException;
import org.json.JSONObject;

public class AllPricesDatabaseHandler {

    // This is the date that price data is from
    private String priceDate = "2025-04-08";

    private JSONObject priceData = null;

    public AllPricesDatabaseHandler(String jsonPath) {

        try {
            // Reading json from file as string
            String jsonString = new String(Files.readAllBytes(Paths.get(jsonPath)));
            // Converting string into json object
            JSONObject jsonObject = new JSONObject(jsonString);

            // Accessing data object
            priceData = jsonObject.getJSONObject("data");

        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        } catch (JSONException e) {
            System.err.println("Error parsing JSON: " + e.getMessage());
        }
    }

    // Retrieves price given card uuid and specified seller (tcgplayer or cardkingdom)
    public Float getPrice(String seller, String uuid) {

        // Initialized as -1 to signal that price wasn't found
        Float price = -1f;

        if (priceData != null) {
            try {
                // Getting all prices from uuid
                JSONObject allPrices = priceData.getJSONObject(uuid);
                // Getting prices in paper
                JSONObject paperPrices = allPrices.getJSONObject("paper");
                // Getting prices from specified seller
                JSONObject sellerPrices = paperPrices.getJSONObject(seller);
                // Getting retail price
                JSONObject retailPrice = sellerPrices.getJSONObject("retail");
                // Getting normal printing price
                JSONObject normalPrice = retailPrice.getJSONObject("normal");
                // Getting price from last date
                price = normalPrice.getFloat(priceDate);

            } catch (JSONException e) {
                System.err.println("Error parsing JSON: " + e.getMessage());
                return price;
            }
        }

        return price;
    }


}
