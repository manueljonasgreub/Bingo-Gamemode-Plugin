package com.github.manueljonasgreub.team;

import com.github.manueljonasgreub.BingoMain;
import com.github.manueljonasgreub.item.BingoItem;
import com.github.manueljonasgreub.utils.TeamColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Team {

    public String name;
    public List<Player> players;
    public String placement;
    public TeamColor color;


    public Team(String name, String color) {
        this.name = name;
        this.players = new ArrayList<>();
        this.color = TeamColor.valueOf(color.toUpperCase());
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
}
