package com.github.manueljonasgreub.listeners;

import com.github.manueljonasgreub.BingoMain;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static org.bukkit.Registry.MATERIAL;

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


    @EventHandler
    public void onPlayerPickupItem(@NotNull PlayerPickupItemEvent event) {

        Player player = event.getPlayer();
        ItemStack item = event.getItem().getItemStack();

        if (BingoMain.getInstance().getGame().isBingoItem(item)){
            BingoMain.getInstance().getGame().playerFoundItem(player, item);
            player.sendMessage("You found the item: " + item.getType().name());
        }

    }
}
