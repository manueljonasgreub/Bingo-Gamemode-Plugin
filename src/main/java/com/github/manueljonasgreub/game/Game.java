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

    private boolean isRunning = false;

    public boolean isCountdown() {
        return isCountdown;
    }

    public EnumMap<Difficulty, Boolean> difficulties =
            new EnumMap<>(Difficulty.class);
    public PlacementMode placementMode = PlacementMode.CIRCULAR;
    private boolean isCountdown = false;

    public boolean isShowingTimer() {
        return isShowingTimer;
    }

    public void setShowingTimer(boolean showingTimer) {
        isShowingTimer = showingTimer;
    }

    private boolean isShowingTimer = true;
    private int time = 0;
    public int getStartTime() {
        return startTime;
    }
    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }
    private int startTime = 3600;
    private List<BingoItemDTO> bingoItems;
    private List<Team> teams;
    private MapRAW mapRAW;


    public List<BingoItemDTO> getBingoItems() {
        return bingoItems;
    }

    public void setBingoItems(List<BingoItemDTO> bingoItems) {
        this.bingoItems = bingoItems;
    }

    private static final NamespacedKey BINGO_MAP_KEY =
            new NamespacedKey(BingoMain.getInstance(), "bingo_preview_map");

    public Game() {

        teams = new ArrayList<>();
        teams.add(new Team("1", "red"));
        teams.add(new Team("2", "yellow"));
        teams.add(new Team("3", "green"));
        teams.add(new Team("4", "blue"));

        for (Difficulty d : Difficulty.values()) {
            difficulties.put(d, true);
        }

        Bukkit.getScheduler().scheduleSyncRepeatingTask(BingoMain.getInstance(),
                () -> {

                    if(isShowingTimer) printTime();

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
                        DetermineWinner();
                    }


                },
                0,
                20);
    }

    public void startGame() {

        if (isCountdown){
            time = startTime;
        }
        else{
            time = 0;
        }

        try {
            if (teams.stream().allMatch(team -> team.players.isEmpty())) {

                List<Player> allPlayers = new ArrayList<>(Bukkit.getOnlinePlayers());
                Collections.shuffle(allPlayers);


                int teamIndex = 0;
                for (Player player : allPlayers) {
                    teams.get(teamIndex).addPlayer(player);
                    teamIndex = (teamIndex + 1) % teams.size();
                    player.sendMessage("§eNo teams were selected, so they were created randomly.");

                }


            }

            List<String> teamNames = teams.stream()
                    .map(team -> team.name)
                    .toList();

            String[] teamNamesArray = teamNames.toArray(new String[0]);


            BingoAPI api = new BingoAPI();
            BingoAPIResponse apiResponse = api.fetchBingoItems(5, teams.size() + "P", teamNamesArray, getDifficultiesAsArray(), teams, placementMode);
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
            mapMeta.getPersistentDataContainer().set(BINGO_MAP_KEY, PersistentDataType.BYTE, (byte) 1);
            mapItem.setItemMeta(mapMeta);

            for (Player player : Bukkit.getOnlinePlayers()) {
                player.getInventory().addItem(mapItem);
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
                        .color(NamedTextColor.WHITE)
                        .decorate(TextDecoration.BOLD);

            } else {

                int phase = (int) (player.getWorld().getGameTime() % 80);

                String dots;

                if (phase <= 20) {
                    dots = "";
                } else if (phase <= 40) {
                    dots = "·";
                } else if (phase <= 60) {
                    dots = "· ·";
                } else {
                    dots = "· · ·";
                }

                message = Component
                        .text(dots +" Timer paused "+ dots)
                        .color(NamedTextColor.YELLOW)
                        .decorate(TextDecoration.ITALIC);

            }

            player.sendActionBar(message);

        }

    }


    public boolean isBingoItem(ItemStack item) {

        if(!isRunning) return false;

        for (BingoItemDTO bingoItem : bingoItems) {
            if (bingoItem.getId().equals(item.getType().name().toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public void playerFoundItem(Player player, BingoItemDTO item) {
        for (Team team : teams) {
            if (team.players.contains(player)) {
                if (item.getCompleted().get(team.name)) {
                } else {

                    for (BingoItemDTO bingoItem : bingoItems) {
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
                        BingoAPIUpdateResponse response = api.updateBingoCard(mapRAW, teams);

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

    public void DetermineWinner() {
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

        cleanupAllPlayers();

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

    public String getDifficultiesAsCsv(){
        return difficulties.entrySet().stream()
                .filter(Map.Entry::getValue)
                .map(e -> e.getKey().name()
                        .toLowerCase()
                        .replace("_", " "))
                .reduce((a, b) -> a + "," + b)
                .orElse("");
    }

    public String[] getDifficultiesAsArray(){
        return difficulties.entrySet().stream()
                .filter(Map.Entry::getValue)
                .map(e -> e.getKey().name()
                        .toLowerCase()
                        .replace("_", " "))
                .toArray(String[]::new);
    }

    public void removeBingoMaps(Player player) {
        PlayerInventory inv = player.getInventory();
        for (int i = 0; i < inv.getSize(); i++) {
            ItemStack it = inv.getItem(i);
            if (isBingoMap(it)) inv.setItem(i, null);
        }
    }

    public void cleanupAllPlayers() {
        for (Player p : Bukkit.getOnlinePlayers()) removeBingoMaps(p);
    }

    private boolean isBingoMap(ItemStack it) {
        if (it == null || it.getType() != Material.FILLED_MAP) return false;
        ItemMeta im = it.getItemMeta();
        if (im == null) return false;
        Byte v = im.getPersistentDataContainer().get(BINGO_MAP_KEY, PersistentDataType.BYTE);
        return v != null && v == 1;
    }

}

