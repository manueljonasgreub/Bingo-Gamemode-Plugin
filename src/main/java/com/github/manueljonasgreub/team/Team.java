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
    public String placement;


    public Team(String name) {
        this.name = name;
        this.players = new ArrayList<>();
    }

    public void markItemAsFound(BingoItem item, Team team){

        BingoMain.getInstance().getGame().getMapRAW().getItems();
        item.getCompleted().put(team.name, true);

    }

    public void addPlayer(Player player){
        if(!(players.contains(player))){
            players.add(player);
        }

    }

    public void removePlayer(Player player){
        if(players.contains(player)){
            players.remove(player);
        }

    }

    public void setPlacement(String placement) {
        this.placement = placement;
    }
    public String getPlacement() {
        return placement;
    }
}
