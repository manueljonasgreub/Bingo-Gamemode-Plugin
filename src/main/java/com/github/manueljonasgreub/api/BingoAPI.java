package com.github.manueljonasgreub.api;

import com.github.manueljonasgreub.BingoMain;
import com.github.manueljonasgreub.item.BingoItem;
import com.github.manueljonasgreub.item.BingoItemDTO;
import com.github.manueljonasgreub.team.Team;
import com.github.manueljonasgreub.utils.PlacementMode;
import com.google.gson.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class BingoAPI {

    private final String API_BASE_URL = BingoMain.getInstance().getApiBaseUrl();
    private List<BingoItemDTO> bingoItems;

    public BingoAPIResponse fetchBingoItems(int gridSize, String gamemode, String[] teamNames, String difficulty, List<Team> teams, PlacementMode placementMode) {
        try {

            String apiUrl = API_BASE_URL + "/create/";

            JsonObject requestBody = new JsonObject();
            requestBody.addProperty("grid_size", gridSize);
            requestBody.addProperty("game_mode", gamemode);
            String teamNamesString = String.join(",", teamNames);
            requestBody.addProperty("team_names", teamNamesString);
            requestBody.addProperty("difficulty", difficulty);
            requestBody.addProperty("placement_mode", placementMode.toString().toLowerCase(Locale.ROOT));

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

            int code = connection.getResponseCode();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    (code >= 400) ? connection.getErrorStream() : connection.getInputStream(),
                    java.nio.charset.StandardCharsets.UTF_8
            ));

            StringBuilder content = new StringBuilder();
            for (String line; (line = in.readLine()) != null; ) content.append(line);
            in.close();
            connection.disconnect();

            if (code >= 400) {
                JsonObject err = JsonParser.parseString(content.toString()).getAsJsonObject();
                if (err.has("errors") && err.get("errors").isJsonArray()) {
                    for (JsonElement e : err.getAsJsonArray("errors")) {
                        System.out.println("[BingoAPI] " + e.getAsString());
                    }
                } else {
                    System.out.println("[BingoAPI] HTTP " + code + " " + content);
                }
                return null;
            }


            JsonObject jsonResponse = JsonParser.parseString(content.toString()).getAsJsonObject();
            JsonObject mapRAWJson = jsonResponse.getAsJsonObject("map_raw");

            Gson gson = new Gson();
            MapRAW mapRAW = gson.fromJson(mapRAWJson, MapRAW.class);
            String mapURL = jsonResponse.get("map_url").getAsString();

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
            requestBody.add("settings", new Gson().toJsonTree(mapRAW.getSettings()));
            requestBody.add("items", new Gson().toJsonTree(mapRAW.getItems()));

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
            String bingo = jsonResponse.has("bingo") && !jsonResponse.get("bingo").isJsonNull() ? jsonResponse.get("bingo").getAsString() : null;
            String urlPath = jsonResponse.get("url").getAsString();

            return new BingoAPIUpdateResponse(bingo, API_BASE_URL + urlPath);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public List<ItemStack> fetchTestItems(int gridSize, String difficultyCsv, PlacementMode placementMode) {
        try {
            String apiUrl = API_BASE_URL + "/create/";

            JsonObject requestBody = new JsonObject();

            requestBody.addProperty("grid_size", gridSize);


            requestBody.addProperty("difficulty", difficultyCsv);

            requestBody.addProperty("placement_mode", placementMode.toString().toLowerCase(Locale.ROOT));

            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = requestBody.toString().getBytes(StandardCharsets.UTF_8);
                os.write(input);
            }

            int code = connection.getResponseCode();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    (code >= 400) ? connection.getErrorStream() : connection.getInputStream(),
                    StandardCharsets.UTF_8
            ));

            StringBuilder content = new StringBuilder();
            for (String line; (line = in.readLine()) != null; ) content.append(line);
            in.close();
            connection.disconnect();

            if (code >= 400) {
                try {
                    JsonObject err = JsonParser.parseString(content.toString()).getAsJsonObject();
                    if (err.has("errors") && err.get("errors").isJsonArray()) {
                        for (JsonElement e : err.getAsJsonArray("errors")) {
                            System.out.println("[BingoAPI] " + e.getAsString());
                        }
                    } else {
                        System.out.println("[BingoAPI] HTTP " + code + " " + content);
                    }
                } catch (Exception ignored) {
                    System.out.println("[BingoAPI] HTTP " + code + " " + content);
                }
                return Collections.emptyList();
            }

            JsonObject jsonResponse = JsonParser.parseString(content.toString()).getAsJsonObject();
            JsonObject mapRAW = jsonResponse.getAsJsonObject("map_raw");
            JsonArray items = mapRAW.getAsJsonArray("items");

            int expected = gridSize * gridSize;
            if (items == null || items.size() != expected) {
                System.out.println("[BingoAPI] Expected " + expected + " items, got " + (items == null ? "null" : items.size()));
                return Collections.emptyList();
            }

            ItemStack[] preview = new ItemStack[expected];

            for (JsonElement el : items) {
                JsonObject it = el.getAsJsonObject();
                int row = it.get("row").getAsInt();
                int col = it.get("column").getAsInt();
                String diff = it.get("difficulty").getAsString();

                int idx = row * gridSize + col;
                if (idx < 0 || idx >= expected) continue;

                preview[idx] = makeDifficultyPane(diff);
            }

            for (int i = 0; i < preview.length; i++) {
                if (preview[i] == null) preview[i] = makeDifficultyPane("unknown");
            }

            return Arrays.asList(preview);

        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    private ItemStack makeDifficultyPane(String difficultyRaw) {
        String d = (difficultyRaw == null ? "" : difficultyRaw.trim().toLowerCase(Locale.ROOT));

        return switch (d) {
            case "very easy" -> makePane(Material.LIGHT_BLUE_STAINED_GLASS_PANE, "Very Easy", NamedTextColor.AQUA);
            case "easy"      -> makePane(Material.LIME_STAINED_GLASS_PANE, "Easy", NamedTextColor.GREEN);
            case "medium"    -> makePane(Material.YELLOW_STAINED_GLASS_PANE, "Medium", NamedTextColor.YELLOW);
            case "hard"      -> makePane(Material.ORANGE_STAINED_GLASS_PANE, "Hard", NamedTextColor.GOLD);
            case "very hard" -> makePane(Material.RED_STAINED_GLASS_PANE, "Very Hard", NamedTextColor.RED);
            default          -> makePane(Material.GRAY_STAINED_GLASS_PANE, "Unknown", NamedTextColor.GRAY);
        };
    }

    private ItemStack makePane(Material material, String title, NamedTextColor color) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(Component.text(title, color));
            meta.addItemFlags(ItemFlag.values());
            item.setItemMeta(meta);
        }
        return item;
    }

}