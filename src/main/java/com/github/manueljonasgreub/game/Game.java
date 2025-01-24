package com.github.manueljonasgreub.game;

import com.github.manueljonasgreub.BingoMain;
import com.github.manueljonasgreub.api.BingoAPI;
import com.github.manueljonasgreub.item.BingoItem;
import com.github.manueljonasgreub.team.Team;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Game {

    public boolean isRunning = false;
    public boolean isCountdown = false;
    public int time = 0;
    public List<BingoItem> bingoItems;
    public List<Team> teams;



    public Game() {

        teams = new ArrayList<>();
        teams.add(new Team("1", new ArrayList<>()));
        teams.add(new Team("2", new ArrayList<>()));
        teams.add(new Team("3", new ArrayList<>()));
        teams.add(new Team("4", new ArrayList<>()));

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
            bingoItems = api.fetchBingoItems();
            resume();

            for (BingoItem item : bingoItems){
                BingoMain.getInstance().getLogger().info(item.getItemStack().getType().toString());
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
            if(bingoItem.getItemStack().getType() == item.getType()) return true;
        }
        return false;
    }

    public void playerFoundItem(Player player, BingoItem item){
        for (Team team : teams){
            if(team.players.contains(player)){
                if (item.isFound()) {
                    BingoMain.getInstance().getLogger().info("Item has already been found!");
                }
                else{
                    team.markItemAsFound(item);
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


}

