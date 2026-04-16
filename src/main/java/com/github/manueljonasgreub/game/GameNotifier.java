package com.github.manueljonasgreub.game;

import com.github.manueljonasgreub.item.BingoItemDTO;
import com.github.manueljonasgreub.team.Team;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

public class GameNotifier {

    public void broadcastItemFound(Player finder, BingoItemDTO item, Team team){

        String itemName = item.getName().replace("_", " ").toLowerCase();

        for (Player currentPlayer : Bukkit.getOnlinePlayers()) {

            boolean isTeammate = team.players.contains(currentPlayer);

            Component msg = isTeammate
                    ? Component.text(finder.getName(), NamedTextColor.GREEN)
                    .append(Component.text(" found the item " + itemName + "!", NamedTextColor.WHITE))
                    : Component.text(finder.getName(), NamedTextColor.RED)
                    .append(Component.text(" found the item " + itemName + "!", NamedTextColor.WHITE));

            currentPlayer.sendMessage(msg);
            currentPlayer.playSound(finder.getLocation(), Sound.ENTITY_VILLAGER_WORK_CARTOGRAPHER, SoundCategory.PLAYERS, 1.0f, 1.0f);

            if (isTeammate) {

                currentPlayer.playSound(finder.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, SoundCategory.PLAYERS, 1.0f, 1.0f);
            } else {
                currentPlayer.playSound(finder.getLocation(), Sound.ENTITY_VILLAGER_NO, SoundCategory.PLAYERS, 1.0f, 0.9f);
            }
        }

    }

    public void broadcastGameWon(String teamName) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.sendTitle("§aTeam " + teamName, "has won the game", 10, 70, 20);
            p.playSound(p.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, SoundCategory.PLAYERS, 1f, 1f);
        }
    }

    public void broadcastGameStart() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.sendTitle("§a§lSTART", "", 10, 70, 20);
            p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, SoundCategory.PLAYERS, 1f, 1f);
        }
    }

}
