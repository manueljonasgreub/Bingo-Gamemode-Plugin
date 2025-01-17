package com.github.manueljonasgreub.game;

import com.github.manueljonasgreub.BingoMain;
import com.github.manueljonasgreub.team.Team;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Game {

    public boolean isRunning = false;
    public boolean isCountdown = false;
    public int time = 0;
    public List<ItemStack> bingoItems;
    public List<Team> teams;



    public Game() {

        teams = new ArrayList<>();

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

        bingoItems = getBingoItems();
        resume();




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

    private List<ItemStack> getBingoItems(){
        List<ItemStack> items = new ArrayList<>();
        Material[] materials = Material.values();
        List<Material> materialList = new ArrayList<>();
        Collections.addAll(materialList, materials);
        Collections.shuffle(materialList);

        for (Material material : materialList) {
            if(material.isItem()){
                System.out.println("Item " + material);
                items.add(new ItemStack(material));
                if(items.size() >= 25) break;
            }
        }

        return items;
    }

    public boolean isBingoItem(ItemStack item){
        for (ItemStack bingoItem : bingoItems){
            if(bingoItem.getType() == item.getType()) return true;
        }
        return false;
    }

    public void playerFoundItem(Player player, ItemStack item){
        for (Team team : teams){
            if(team.players.contains(player)){
                team.markItemAsFound(item);
                return;
            }
        }
    }

    public void pause() {
        isRunning = false;
    }

    public void set(int seconds) {
        time = seconds;
        pause();
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

