package com.github.manueljonasgreub.api;

import com.github.manueljonasgreub.BingoMain;
import com.github.manueljonasgreub.item.BingoItem;
import com.github.manueljonasgreub.team.Team;
import com.google.gson.*;
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

    private final String API_BASE_URL = "http://167.99.130.136";
    private List<BingoItem> bingoItems;

    public BingoAPIResponse fetchBingoItems(int gridSize, String gamemode, String[] teamNames, String difficulty, List<Team> teams) {
        try {

            String apiUrl = API_BASE_URL + "/create/";

            JsonObject requestBody = new JsonObject();
            requestBody.addProperty("grid_size", gridSize);
            requestBody.addProperty("game_mode", gamemode);
            String teamNamesString = String.join(",", teamNames);
            requestBody.addProperty("team_names", teamNamesString);
            requestBody.addProperty("difficulty", difficulty);

            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; utf-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);


            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = requestBody.toString().getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            connection.disconnect();

            JsonObject jsonResponse = JsonParser.parseString(content.toString()).getAsJsonObject();
            JsonObject mapRAWJson = jsonResponse.getAsJsonObject("mapRAW");

            Gson gson = new Gson();
            MapRAW mapRAW = gson.fromJson(mapRAWJson, MapRAW.class);
            String mapURL = jsonResponse.get("mapURL").getAsString();

            for (JsonElement teamJson : mapRAWJson.getAsJsonObject("settings").getAsJsonArray("teams")) {
                String teamName = teamJson.getAsJsonObject().get("name").getAsString();
                String placement = teamJson.getAsJsonObject().get("placement").getAsString();
                for (Team team : teams) {
                    if (team.name.equals(teamName)) {
                        team.setPlacement(placement);
                    }
                }
            }

            BingoAPIResponse bingoResponse = new BingoAPIResponse();
            bingoResponse.setMapRAW(mapRAW);
            bingoResponse.setMapURL(API_BASE_URL + mapURL);

            return bingoResponse;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public BingoAPIUpdateResponse updateBingoCard(MapRAW mapRAW) {
        try {
            String apiUrl = API_BASE_URL + "/update/";


            JsonObject requestBody = new JsonObject();
            requestBody.add("mapRAW", new Gson().toJsonTree(mapRAW));

            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; utf-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = mapRAW.toString().getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            connection.disconnect();

            JsonObject jsonResponse = JsonParser.parseString(content.toString()).getAsJsonObject();
            String bingo = jsonResponse.has("bingo") && !jsonResponse.get("bingo").isJsonNull() ? jsonResponse.get("bingo").getAsString() : null;
            String urlPath = jsonResponse.get("url").getAsString();

            return new BingoAPIUpdateResponse(bingo, API_BASE_URL + urlPath);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}