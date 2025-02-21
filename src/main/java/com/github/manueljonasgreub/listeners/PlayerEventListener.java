package com.github.manueljonasgreub.listeners;

import com.github.manueljonasgreub.BingoMain;
import com.github.manueljonasgreub.item.BingoItem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerAttemptPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

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
    public void onPlayerAttemptPickupItem(@NotNull PlayerAttemptPickupItemEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem().getItemStack();
        BingoItem bingoItem;

        if (BingoMain.getInstance().getGame().isBingoItem(item)) {

            for(BingoItem foundItem : BingoMain.getInstance().getGame().getBingoItems()){
                BingoMain.getInstance().getLogger().info("Teste: " + foundItem.getName() + " mit " + item.getType().name());
                if (item.getType().name().toLowerCase().equals(foundItem.getName())) {
                    bingoItem = foundItem;
                    BingoMain.getInstance().getGame().playerFoundItem(player, bingoItem);
                    player.sendMessage("§aYou found the item " + item.getType().name() + "!");
                    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, SoundCategory.PLAYERS, 1.0f, 1.0f);
                }
            }
        }
        else
        {
            player.sendMessage(item.getType().name() + " is not a bingo item");
        }
    }
}