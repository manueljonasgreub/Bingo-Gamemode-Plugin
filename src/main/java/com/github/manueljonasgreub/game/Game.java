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
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
                    }



                },
                0,
                20);
    }

    public void startGame(){

        try{
            BingoAPI api = new BingoAPI();
            BingoAPIResponse apiResponse = api.fetchBingoItems(5, "4P", new String[]{"1", "2", "3", "4"}, "easy", teams);
            mapRAW = apiResponse.getMapRAW();
            bingoItems = apiResponse.getMapRAW().getItems();
            String mapURL = apiResponse.getMapURL();
            resume();

            for (BingoItem item : bingoItems){
                BingoMain.getInstance().getLogger().info(item.getName());
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
                player.getInventory().addItem(mapItem);
            }

        }
        catch (Exception ex){
            BingoMain.getInstance().getLogger().info(ex.getMessage());
        }


        teams = teams.stream()
                .filter(team -> !team.players.isEmpty())
                .collect(Collectors.toList());


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


    public boolean isBingoItem(ItemStack item){

        for (BingoItem bingoItem : bingoItems){
            if(bingoItem.getName().equals(item.getType().name().toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public void playerFoundItem(Player player, BingoItem item){
        for (Team team : teams){
            if(team.players.contains(player)){
                if (item.getCompleted().get(team.name)) {
                    BingoMain.getInstance().getLogger().info("Item has already been found!");
                }
                else{
                    team.markItemAsFound(item, team);
                    try{
                        BingoAPI api = new BingoAPI();
                        BingoAPIUpdateResponse response = api.updateBingoCard(mapRAW);
                    }
                    catch (Exception ex){
                        BingoMain.getInstance().getLogger().info(ex.getMessage());
                    }
                }
            }
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

    public void toggleContdown(){
        if (isCountdown){
            isCountdown = false;
        }
        else
        {
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

