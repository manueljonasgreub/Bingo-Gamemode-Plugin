package com.github.manueljonasgreub.game;

import com.github.manueljonasgreub.BingoMain;
import com.github.manueljonasgreub.api.BingoAPI;
import com.github.manueljonasgreub.api.BingoAPIResponse;
import com.github.manueljonasgreub.api.BingoAPIUpdateResponse;
import com.github.manueljonasgreub.api.MapRAW;
import com.github.manueljonasgreub.item.BingoItem;
import com.github.manueljonasgreub.item.BingoItemDTO;
import com.github.manueljonasgreub.map.BingoMapRenderer;
import com.github.manueljonasgreub.team.Team;
import com.github.manueljonasgreub.team.TeamManager;
import com.github.manueljonasgreub.utils.Difficulty;
import com.github.manueljonasgreub.utils.PlacementMode;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.title.Title;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapView;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;
import java.util.stream.Collectors;

public class Game {


    public EnumMap<Difficulty, Boolean> difficulties =
            new EnumMap<>(Difficulty.class);
    public PlacementMode placementMode = PlacementMode.CIRCULAR;
    private GameState state = GameState.IDLE;
    private GameTimer timer;
    private GameNotifier notifier = new GameNotifier();
    private TeamManager teamManager = new TeamManager();
    private BingoAPI api = new BingoAPI();
    private List<BingoItemDTO> bingoItems;
    private MapRAW mapRAW;


    public Game() {


        for (Difficulty d : Difficulty.values()) {
            difficulties.put(d, true);
        }

        timer = new GameTimer(this::onTimeUp);

    }


    public void startGame() {

        if(state != GameState.IDLE) {
            return;
        }

        try {

            if (teamManager.allTeamsEmpty()) {
                teamManager.distributePlayersRandomly(Bukkit.getOnlinePlayers());
            }

            List<Team> teams = teamManager.getTeams();
            String[] teamNames = teams.stream().map(team -> team.name).toArray(String[]::new);

            BingoAPIResponse apiResponse = api.fetchBingoItems(5, teams.size() + "P", teamNames, getDifficultiesAsArray(), teams, placementMode);
            mapRAW = apiResponse.getMapRAW();
            bingoItems = apiResponse.getMapRAW().getItems();
            String mapURL = apiResponse.getMapURL();

            timer.start();
            state = GameState.RUNNING;


            notifier.broadcastGameStart();


            ItemStack mapItem = MapItemService.createMapItem(mapURL);
            MapItemService.giveMapItemsToAll(mapItem);

        } catch (Exception ex) {
            BingoMain.getInstance().getLogger().warning("fetchbingoitems failed" + ex.getMessage());
        }

    }


    public void pause() {
        state = GameState.PAUSED;
        timer.pause();
    }

    public void resume() {
        state = GameState.RUNNING;
        timer.resume();
    }

    public void stopGame() {
        state = GameState.FINISHED;
        timer.pause();
    }


    public boolean isBingoItem(ItemStack item) {

        if (state != GameState.RUNNING) return false;
        String id = item.getType().name().toLowerCase();
        return bingoItems.stream().anyMatch(b -> b.getId().equals(id));

    }


    public void playerFoundItem(Player player, BingoItemDTO item) {


        Team team = teamManager.getTeamOf(player).orElse(null);

        if (team == null) return;
        if (item.getCompleted().getOrDefault(team.name, false)) return;

        bingoItems.stream()
                .filter(b -> b.getName().equals(item.getName()))
                .forEach(b -> b.getCompleted().put(team.name, true));

        mapRAW.setItems(bingoItems);
        notifier.broadcastItemFound(player, item, team);


        try {
            BingoAPI api = new BingoAPI();
            BingoAPIUpdateResponse response = api.updateBingoCard(mapRAW, teamManager.getTeams());

            if (response != null) {
                var bingo = response.getBingo();
                String mapURL = response.getImageUrl();
                BingoMain.getInstance().getLogger().info(mapURL);

                MapItemService.updateMapItemsFromAll(mapURL);


                if (bingo != null) {
                    gameWon(bingo, team);
                }

            } else {
                BingoMain.getInstance().getLogger().info("§cFailed to update the bingo card.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            BingoMain.getInstance().getLogger().info("§cAn error occurred while updating the bingo card.");
        }
    }


    public void determineWinner() {
        teamManager.getTeams().stream()
                .max(Comparator.comparingLong(t ->
                        bingoItems.stream()
                                .filter(item -> item.getCompleted().getOrDefault(t.name, false))
                                .count()))
                .ifPresent(winner -> gameWon(winner.name, winner));
    }


    public void gameWon(String winnerName, Team team) {
        stopGame();
        notifier.broadcastGameWon(winnerName);
        MapItemService.removeMapItemsFromAll();
    }


    public boolean isRunning() {
        return state == GameState.RUNNING;
    }

    public GameState getState() {
        return state;
    }

    public GameTimer getTimer() {
        return timer;
    }

    public TeamManager getTeamManager() {
        return teamManager;
    }

    public List<BingoItemDTO> getBingoItems() {
        return bingoItems;
    }

    public void setBingoItems(List<BingoItemDTO> items) {
        this.bingoItems = items;
    }

    public MapRAW getMapRAW() {
        return mapRAW;
    }

    public void setMapRAW(MapRAW mapRAW) {
        this.mapRAW = mapRAW;
    }

    private void onTimeUp() {
        determineWinner();
    }

    public String[] getDifficultiesAsArray() {
        return difficulties.entrySet().stream()
                .filter(Map.Entry::getValue)
                .map(e -> e.getKey().name()
                        .toLowerCase()
                        .replace("_", " "))
                .toArray(String[]::new);
    }

    public void cleanupAllPlayers() {
        MapItemService.removeMapItemsFromAll();
    }


}

