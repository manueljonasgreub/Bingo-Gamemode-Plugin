package com.github.manueljonasgreub.listeners;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerEventListener implements Listener {

    @EventHandler
    public void onPlayerJoin(@NotNull PlayerJoinEvent event) {
        Player player = event.getPlayer();
        event.joinMessage(Component
                .text("+ ")
                .color(NamedTextColor.DARK_GREEN)
                .decorate(TextDecoration.BOLD)
                .append(Component
                        .text(player.getName())
                        .color(NamedTextColor.GREEN)
                        .decorate(TextDecoration.BOLD)
                )
        );
    }
    @EventHandler
    public void onPlayerQuit(@NotNull PlayerQuitEvent event) {
        Player player = event.getPlayer();
        event.quitMessage(Component
                .text("- ")
                .color(NamedTextColor.DARK_RED)
                .decorate(TextDecoration.BOLD)
                .append(Component
                        .text(player.getName())
                        .color(NamedTextColor.RED)
                        .decorate(TextDecoration.BOLD)
                )
        );
    }
}
