package com.github.manueljonasgreub.api;

import com.github.manueljonasgreub.item.BingoItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
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

    public List<BingoItem> fetchBingoItems() {

        List<BingoItem> bingoItems = new ArrayList<>();
        String urlString = "http://167.99.130.136/create/?gridSize=5&gamemode=3P&teams=team1,team2,team4&difficulty=easy";
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = "{}".getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            StringBuilder response;
            try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
            }

            JSONParser parser = new JSONParser();
            JSONObject jsonResponse = (JSONObject) parser.parse(response.toString());
            JSONObject mapRAWJson = (JSONObject) jsonResponse.get("mapRAW");

            MapRAW mapRAW = new MapRAW();
            mapRAW.setSettings((List<Map<String, Object>>) mapRAWJson.get("settings"));
            mapRAW.setItems((List<Map<String, Object>>) mapRAWJson.get("items"));

            for (Map<String, Object> item : mapRAW.getItems()) {
                String itemName = ((String) item.get("name")).toUpperCase();
                Material material = Material.getMaterial(itemName);
                if (material != null && material.isItem()) {
                    BingoItem bingoItem = new BingoItem(new ItemStack(material));
                    bingoItems.add(bingoItem);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return bingoItems;
    }

}