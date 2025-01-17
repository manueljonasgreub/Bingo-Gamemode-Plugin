package com.github.manueljonasgreub.team;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Team {

    public String name;
    public List<Player> players;
    public List<ItemStack> progress;


    public Team(String name, List<ItemStack> bingoItems) {
        this.name = name;
        this.progress = bingoItems;
        this.players = new ArrayList<>();
    }

    public void markItemAsFound(ItemStack item){
        progress.remove(item);
    }

    public void addPlayer(Player player){
        if(!(players.contains(player))){
            players.add(player);
        }
        player.sendMessage("§aYou are now in team " + name + "!");
    }

    public void removePlayer(Player player){
        if(players.contains(player)){
            players.remove(player);
        }
        player.sendMessage("§cYou are no longer in team " + name + "!");
    }
}
