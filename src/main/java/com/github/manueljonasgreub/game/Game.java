package com.github.manueljonasgreub.game;

import com.github.manueljonasgreub.BingoMain;
import com.github.manueljonasgreub.api.BingoAPI;
import com.github.manueljonasgreub.api.BingoAPIResponse;
import com.github.manueljonasgreub.api.BingoAPIUpdateResponse;
import com.github.manueljonasgreub.api.MapRAW;
import com.github.manueljonasgreub.item.BingoItem;
import com.github.manueljonasgreub.map.BingoMapRenderer;
import com.github.manueljonasgreub.team.Team;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Game {

    private boolean isRunning = false;
    private boolean isCountdown = false;
    private int time = 0;
    private List<BingoItem> bingoItems;
    private List<Team> teams;

    private MapRAW mapRAW;


    public List<BingoItem> getBingoItems() {
        return bingoItems;
    }

    public void setBingoItems(List<BingoItem> bingoItems) {
        this.bingoItems = bingoItems;
    }

    public Game() {

        teams = new ArrayList<>();
        teams.add(new Team("1"));
        teams.add(new Team("2"));
        teams.add(new Team("3"));
        teams.add(new Team("4"));

        Bukkit.getScheduler().scheduleSyncRepeatingTask(BingoMain.getInstance(),
                () -> {

                    printTime();

                    if (!isRunning) {
                        return;
                    }

                    if (isCountdown) {
                        time--;
                    } else {
                        time++;
                    }

                    if (time <= 0 && isCountdown) {
                        isRunning = false;
                        determineWinner();
                    }


                },
                0,
                20);
    }

    public void startGame() {

        if (isCountdown){
            time = 3600;
        }

        try {
            if (teams.stream().allMatch(team -> team.players.isEmpty())) {

                List<Player> allPlayers = new ArrayList<>(Bukkit.getOnlinePlayers());
                Collections.shuffle(allPlayers);


                int teamIndex = 0;
                for (Player player : allPlayers) {
                    teams.get(teamIndex).addPlayer(player);
                    teamIndex = (teamIndex + 1) % teams.size();
                    player.sendMessage("§cNo teams were selected, so they were created randomly.");

                }


            }

            teams = teams.stream()
                    .filter(team -> !team.players.isEmpty())
                    .collect(Collectors.toList());

            List<String> teamNames = teams.stream()
                    .map(team -> team.name)
                    .toList();

            String[] teamNamesArray = teamNames.toArray(new String[0]);


            BingoAPI api = new BingoAPI();
            BingoAPIResponse apiResponse = api.fetchBingoItems(5, teams.size() + "P", teamNamesArray, "easy", teams);
            mapRAW = apiResponse.getMapRAW();
            bingoItems = apiResponse.getMapRAW().getItems();
            String mapURL = apiResponse.getMapURL();
            resume();

            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendTitle("§a§lSTART", "", 10, 70, 20);
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, SoundCategory.PLAYERS, 1.0f, 1.0f);
            }

            MapView mapView = Bukkit.createMap(BingoMain.getInstance().getServer().getWorld("world"));
            mapView.getRenderers().clear();

            BingoMapRenderer renderer = new BingoMapRenderer(mapURL);
            mapView.addRenderer(renderer);

            ItemStack mapItem = new ItemStack(Material.FILLED_MAP);
            MapMeta mapMeta = (MapMeta) mapItem.getItemMeta();
            mapMeta.setMapView(mapView);
            mapItem.setItemMeta(mapMeta);

            for (Player player : Bukkit.getOnlinePlayers()) {
                boolean hasMap = false;
                for (ItemStack itemStack : player.getInventory()) {
                    if (itemStack != null) {
                        if (itemStack.getType().equals(Material.FILLED_MAP)) {
                            itemStack.setItemMeta(mapMeta);
                            hasMap = true;
                        }
                    }
                }

                if (!hasMap) player.getInventory().addItem(mapItem);
            }

        } catch (Exception ex) {
            BingoMain.getInstance().getLogger().info(ex.getMessage());
        }


    }

    private void printTime() {

        var players = BingoMain.getInstance().getServer().getOnlinePlayers();
        for (Player player : players) {

            Component message = null;

            if (isRunning) {

                int hours = time / 3600;
                int remainingSeconds = time % 3600;
                int minutes = remainingSeconds / 60;
                int seconds = remainingSeconds % 60;

                String secondsFormatted = String.format("%02d", seconds);
                String minutesFormatted = String.format("%02d", minutes);
                String hoursFormatted = String.format("%02d", hours);

                message = Component
                        .text(hoursFormatted + ":" + minutesFormatted + ":" + secondsFormatted)
                        .color(NamedTextColor.GOLD)
                        .decorate(TextDecoration.BOLD);

            } else {

                message = Component
                        .text("Timer pausiert")
                        .color(NamedTextColor.GOLD)
                        .decorate(TextDecoration.BOLD);

            }

            player.sendActionBar(message);

        }

    }


    public boolean isBingoItem(ItemStack item) {

        for (BingoItem bingoItem : bingoItems) {
            if (bingoItem.getName().equals(item.getType().name().toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public void playerFoundItem(Player player, BingoItem item) {
        for (Team team : teams) {
            if (team.players.contains(player)) {
                if (item.getCompleted().get(team.name)) {
                } else {

                    for (BingoItem bingoItem : bingoItems) {
                        if (bingoItem.getName().equals(item.getName())) {
                            bingoItem.getCompleted().put(team.name, true);
                        }
                    }

                    mapRAW.setItems(bingoItems);

                    for (Player currentPlayer : Bukkit.getOnlinePlayers()) {
                        if (team.players.contains(currentPlayer)) {
                            currentPlayer.sendMessage("§a" + player.getName() + " §ffound the item " + item.getName().replace("_", " ").toLowerCase() + "!");
                            currentPlayer.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, SoundCategory.PLAYERS, 1.0f, 1.0f);
                            currentPlayer.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_WORK_CARTOGRAPHER, SoundCategory.PLAYERS, 1.0f, 1.0f);
                        } else {
                            currentPlayer.sendMessage("§c" + player.getName() + " §ffound the item " + item.getName().replace("_", " ").toLowerCase() + "!");
                            currentPlayer.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, SoundCategory.PLAYERS, 1.0f, 0.9f);
                            currentPlayer.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_WORK_CARTOGRAPHER, SoundCategory.PLAYERS, 1.0f, 1.0f);
                        }
                    }


                    try {
                        BingoAPI api = new BingoAPI();
                        BingoAPIUpdateResponse response = api.updateBingoCard(mapRAW);

                        if (response != null) {
                            var bingo = response.getBingo();
                            String mapURL = response.getImageUrl();
                            BingoMain.getInstance().getLogger().info(mapURL);


                            MapView mapView = Bukkit.createMap(BingoMain.getInstance().getServer().getWorld("world"));
                            mapView.getRenderers().clear();

                            BingoMapRenderer renderer = new BingoMapRenderer(mapURL);
                            mapView.addRenderer(renderer);

                            ItemStack mapItem = new ItemStack(Material.FILLED_MAP);
                            MapMeta mapMeta = (MapMeta) mapItem.getItemMeta();
                            mapMeta.setMapView(mapView);
                            mapItem.setItemMeta(mapMeta);

                            for (Player currentPlayer : Bukkit.getOnlinePlayers()) {
                                for (ItemStack currentMapItem : currentPlayer.getInventory())
                                    if (currentMapItem != null) {
                                        if (currentMapItem.getType().equals(Material.FILLED_MAP)) {
                                            currentMapItem.setItemMeta(mapMeta);
                                        }
                                    }

                            }


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
            }
        }
    }

    private void determineWinner() {
        Team winningTeam = null;
        int maxItemsFound = 0;

        for (Team team : teams) {
            int itemsFound = (int) bingoItems.stream()
                    .filter(item -> item.getCompleted().getOrDefault(team.name, false))
                    .count();

            if (itemsFound > maxItemsFound) {
                maxItemsFound = itemsFound;
                winningTeam = team;
            }
        }

        if (winningTeam != null) {
            gameWon(winningTeam.name, winningTeam);
        } else {
            Random random = new Random();
            int index = random.nextInt(teams.size());
            Team randomTeam = teams.get(index);
            gameWon(randomTeam.name, randomTeam);
        }
    }


    public void gameWon(String winnerName, Team team) {

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendTitle("§aTeam " + winnerName, "has won the game", 10, 70, 20);
            player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, SoundCategory.PLAYERS, 1.0f, 1.0f);
            pause();
        }

    }


    public void pause() {
        isRunning = false;
    }

    public void set(int seconds) {
        time = seconds;
    }

    public void resume() {
        isRunning = true;
    }

    public void toggleContdown() {
        if (isCountdown) {
            isCountdown = false;
        } else {
            isCountdown = true;
        }
    }

    public boolean isRunning() {
        return isRunning;
    }

    public int getTime() {
        return time;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }

    public MapRAW getMapRAW() {
        return mapRAW;
    }

    public void setMapRAW(MapRAW mapRAW) {
        this.mapRAW = mapRAW;
    }
}

