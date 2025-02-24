package com.github.manueljonasgreub.commands;

import com.github.manueljonasgreub.BingoMain;
import com.github.manueljonasgreub.team.Team;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BingoCommand implements CommandExecutor, TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {

        if (!(commandSender instanceof Player)) {
            return false;
        }

        Player player = (Player) commandSender;

        if (args.length == 0) {
            sendUsage(player);
            return false;
        }

        switch (args[0]) {

            case "resume":
                BingoMain.getInstance().getGame().resume();
                return true;

            case "set":
                if (args.length == 2) {
                    try {
                        BingoMain.getInstance().getGame().set(Integer.parseInt(args[1]));
                    } catch (Exception ex) {
                        sendUsage(player);
                    }
                } else {
                    sendUsage(player);
                }
                return true;

            case "team":
                if (args.length == 3) {
                    try {
                        if (args[1].equals("join")) {
                            for (Team team : BingoMain.getInstance().getGame().getTeams()) {
                                if (team.name.equals(args[2])) {
                                    team.addPlayer(player);
                                    player.sendMessage("§aYou are now in team " + team.name + "!");
                                    player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_YES, SoundCategory.PLAYERS, 1.0f, 1.0f);
                                    return true;
                                }
                            }
                            player.sendMessage("Team " + args[2] + " does not exist.");
                            return true;
                        }
                        if (args[1].equals("leave")) {
                            for (Team team : BingoMain.getInstance().getGame().getTeams()) {
                                if (team.name.equals(args[2])) {
                                    team.removePlayer(player);
                                    player.sendMessage("§cYou are no longer in team " + team.name + "!");
                                    player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, SoundCategory.PLAYERS, 1.0f, 1.0f);
                                    return true;
                                }
                            }
                            player.sendMessage("Team " + args[2] + " does not exist.");
                            return true;
                        }
                        if (args[1].equals("list")) {
                            for (Team team : BingoMain.getInstance().getGame().getTeams()) {
                                player.sendMessage(team.name);
                            }
                            return true;
                        }
                    } catch (Exception ex) {
                        sendUsage(player);
                    }
                } else {
                    sendUsage(player);
                }
                return true;


            case "pause":
                BingoMain.getInstance().getGame().pause();
                return true;

            case "toggle":
                BingoMain.getInstance().getGame().toggleContdown();
                return true;
            case "start":
                BingoMain.getInstance().getGame().startGame();
                return true;
            case "help":
                sendHelp(player);
                return true;
            default:
                sendUsage(player);
                return false;
        }


    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {

        if (args.length == 1) {
            return List.of("help", "pause", "resume", "set", "start", "team", "toggle" );
        }
        if (args.length == 2 && args[0].equals("set")) {
            return List.of("<time>");
        }
        if (args.length == 2 && args[0].equals("team")) {
            return List.of("join", "leave", "list");
        }
        if (args.length == 3 && args[0].equals("team") && args[1].equals("join") || args[1].equals("leave")) {
            List<Team> teams = BingoMain.getInstance().getGame().getTeams();
            return teams.stream().map(team -> team.name).toList();
        }

        if (args.length == 4 && args[0].equals("team") && args[1].equals("join") || args[1].equals("leave")) {
            return null;
        }

        return List.of("");
    }

    public void sendUsage(Player player){
        player.sendMessage(Component
                .text("That didn't work. Try /bingo help")
                .color(NamedTextColor.RED));
    }

    public void sendHelp(Player player) {
        player.sendMessage(Component
                .text("Usage: ")
                .color(NamedTextColor.YELLOW));
        player.sendMessage(Component
                .text("    /bingo start - start the game")
                .color(NamedTextColor.YELLOW));
        player.sendMessage(Component
                .text("    /bingo pause - pause the game")
                .color(NamedTextColor.YELLOW));
        player.sendMessage(Component
                .text("    /bingo pause - resume the game after pause")
                .color(NamedTextColor.YELLOW));
        player.sendMessage(Component
                .text("    /bingo toggle - toggles whether the timer counts up or down")
                .color(NamedTextColor.YELLOW));
        player.sendMessage(Component
                .text("    /bingo set <time> - sets the timer to the given time")
                .color(NamedTextColor.YELLOW));
        player.sendMessage(Component
                .text("    /bingo team <join|leave|list> - manage the teams")
                .color(NamedTextColor.YELLOW));
        player.sendMessage(Component
                .text("    /bingo help - shows this list")
                .color(NamedTextColor.YELLOW));
    }
}
