package com.github.manueljonasgreub.api;

import com.github.manueljonasgreub.BingoMain;
import com.github.manueljonasgreub.item.BingoItem;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BingoAPI {

    private final String API_BASE_URL = "http://167.99.130.13";
    private List<BingoItem> bingoItems;

    public List<BingoItem> fetchBingoItems(int gridSize, String gamemode, String[] teamNames, String difficulty) {
        try {
            // Erstelle die URL für die Anfrage
            String apiUrl = API_BASE_URL + "/create/";

            // Erstelle das JSON-Objekt für den Request Body
            JsonObject requestBody = new JsonObject();
            requestBody.addProperty("GridSize", gridSize);
            requestBody.addProperty("GameMode", gamemode);

            // TeamNames als JSON-Array
            JsonArray teamNamesArray = new JsonArray();
            for (String teamName : teamNames) {
                teamNamesArray.add(teamName);
            }
            requestBody.add("TeamNames", teamNamesArray);

            requestBody.addProperty("Difficulty", difficulty);

            // Sende die POST-Anfrage
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; utf-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);

            // Sende den Request Body
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = requestBody.toString().getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // Lese die Antwort
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            connection.disconnect();

            // Parse die JSON-Antwort
            JsonObject jsonResponse = JsonParser.parseString(content.toString()).getAsJsonObject();
            JsonObject mapRAW = jsonResponse.getAsJsonObject("mapRAW");

            // Verwende Gson, um die Antwort in ein BingoResponse-Objekt umzuwandeln
            Gson gson = new Gson();
            BingoAPIResponse bingoResponse = gson.fromJson(mapRAW, BingoAPIResponse.class);

            // Speichere die Bingo-Items
            this.bingoItems = bingoResponse.getItems();
            return bingoItems;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String updateBingoCard(JsonObject mapRAW) {
        try {
            // Erstelle die URL für die Anfrage
            String apiUrl = API_BASE_URL + "/update/";

            // Sende die POST-Anfrage
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; utf-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);

            // Sende den mapRAW-Teil als JSON
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = mapRAW.toString().getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // Lese die Antwort
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            connection.disconnect();

            // Parse die JSON-Antwort
            JsonObject jsonResponse = JsonParser.parseString(content.toString()).getAsJsonObject();
            return jsonResponse.get("url").getAsString();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}