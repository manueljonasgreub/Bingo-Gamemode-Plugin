package com.github.manueljonasgreub.team;

import com.github.manueljonasgreub.BingoMain;
import com.github.manueljonasgreub.item.BingoItem;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Team {

    public String name;
    public List<Player> players;
    public List<BingoItem> progress;
    public String placement;


    public Team(String name, List<BingoItem> bingoItems) {
        this.name = name;
        this.progress = bingoItems;
        this.players = new ArrayList<>();
    }

    public void markItemAsFound(BingoItem item){
        for (BingoItem bingoItem : progress) {
            if (bingoItem.getItemStack().getType() == item.getItemStack().getType()) {
                bingoItem.setFound(true);
            }
        }

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

    public String getPlacement() {
        return placement;
    }
}
