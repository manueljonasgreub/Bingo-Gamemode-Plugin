package com.github.manueljonasgreub.inventory;

import com.github.manueljonasgreub.BingoMain;
import com.github.manueljonasgreub.game.Game;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;


public class SettingsView implements Listener {

    private Inventory gui;
    private Game game = BingoMain.getInstance().getGame();

    public SettingsView() {
        gui = Bukkit.createInventory(null, 27, "Settings");
        setItems();
    }

    public void openGUI(Player player) {
        player.openInventory(gui);
    }

    private void setItems(){

        ItemStack filler = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta fillerMeta = filler.getItemMeta();
        if (fillerMeta != null) {
            fillerMeta.displayName(Component.empty());
            fillerMeta.setHideTooltip(true);
            filler.setItemMeta(fillerMeta);
        }

        for (int i = 0; i < gui.getSize(); i++) {
            gui.setItem(i, filler);
        }

        setStartItem(gui);
        setTimeItem(gui);
        setCountdownItem(gui);
        setItemsItem(gui);

    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

        if (!event.getView().getTitle().equals("Settings")) return;
        event.setCancelled(true);

        Player player = (Player) event.getWhoClicked();
        ItemStack clickedItem = event.getCurrentItem();
        event.getClick();

        if (clickedItem == null || clickedItem.getType() == Material.AIR) return;

        switch (event.getSlot()) {
            case 10:
                handleTimeClick(player, clickedItem);
                break;
            case 11:
                handleCountdownClick(player, event.getClick());
                break;
            case 12:
                handleItemsClick(player, clickedItem);
                break;
            case 22:
                handleStartGameClick(player, clickedItem);
                break;

            default:
                break;
        }
    }

    private void setStartItem(Inventory inv) {
        ItemStack item = new ItemStack(Material.NETHER_STAR);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.AQUA + "Start Game");
        meta.lore(List.of(
                Component.text(ChatColor.GRAY + ""),
                Component.text(ChatColor.GRAY + "Click to start the Bingo game.")
        ));
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(meta);
        inv.setItem(22, item);
    }

    private void setItemsItem(Inventory inv) {
        ItemStack item = new ItemStack(Material.ITEM_FRAME);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD + "Items Settings");
        meta.lore(List.of(
                Component.text(ChatColor.GRAY + ""),
                Component.text(ChatColor.GRAY + "Click to edit Bingo items.")
        ));
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(meta);
        inv.setItem(12, item);
    }

    private void setTimeItem(Inventory inv) {

        ItemStack item = new ItemStack(Material.BELL);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.YELLOW + "Toggle Timemode");

        if(BingoMain.getInstance().getGame().isCountdown()){
            meta.lore(List.of(
                    Component.text(ChatColor.GRAY + ""),
                    Component.text(ChatColor.GRAY + "Timer"),
                    Component.text(ChatColor.GOLD + "Countdown"),
                    Component.text(ChatColor.GRAY + ""),
                    Component.text(ChatColor.GRAY + "Toggles whether time"),
                    Component.text(ChatColor.GRAY + "counts up or down. The"),
                    Component.text(ChatColor.GRAY + "game ends when time runs"),
                    Component.text(ChatColor.GRAY + "out.")
            ));
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
        }
        else {
            meta.lore(List.of(
                    Component.text(ChatColor.GRAY + ""),
                    Component.text(ChatColor.GOLD + "Timer"),
                    Component.text(ChatColor.GRAY + "Countdown"),
                    Component.text(ChatColor.GRAY + ""),
                    Component.text(ChatColor.GRAY + "Toggles whether time"),
                    Component.text(ChatColor.GRAY + "counts up or down. The"),
                    Component.text(ChatColor.GRAY + "game ends when time runs"),
                    Component.text(ChatColor.GRAY + "out.")
            ));
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
        }


        item.setItemMeta(meta);
        inv.setItem(10, item);
    }

    private void setCountdownItem(Inventory inv) {

        int time = game.getStartTime();
        int hours = time / 3600;
        int remainingSeconds = time % 3600;
        int minutes = remainingSeconds / 60;
        int seconds = remainingSeconds % 60;

        String secondsFormatted = String.format("%02d", seconds);
        String minutesFormatted = String.format("%02d", minutes);
        String hoursFormatted = String.format("%02d", hours);

        ItemStack item = new ItemStack(Material.CLOCK);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.YELLOW + "Modify Starttime");


            meta.lore(List.of(
                    Component.text(ChatColor.GRAY + ""),
                    Component.text(ChatColor.GOLD + "Current Time:"),
                    Component.text(ChatColor.YELLOW + hoursFormatted + ":" + minutesFormatted + ":" + secondsFormatted),
                    Component.text(ChatColor.GRAY + ""),
                    Component.text("§6+ 10 §8» §7Click"),
                    Component.text("§6- 10 §8» §7Right Click"),
                    Component.text("§6+ 1 §8» §7Shift + Click"),
                    Component.text("§6- 1 §8» §7Shift + Right Click")
            ));
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);



        item.setItemMeta(meta);
        inv.setItem(11, item);
    }

    private void handleStartGameClick(Player player, ItemStack clickedItem) {

        BingoMain.getInstance().getGame().startGame();
        InventoryView view = player.getOpenInventory();
        Inventory inv = view.getTopInventory();
        setStartItem(inv);
        player.closeInventory();
    }

    private void handleItemsClick(Player player, ItemStack clickedItem) {

        InventoryView view = player.getOpenInventory();
        Inventory inv = view.getTopInventory();
        setStartItem(inv);
        player.closeInventory();
        ItemsView iv = new ItemsView();
        iv.openGUI(player);

    }

    private void handleTimeClick(Player player, ItemStack clickedItem) {

        game.toggleContdown();
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, SoundCategory.PLAYERS, 1.0f, 1.0f);

        InventoryView view = player.getOpenInventory();
        Inventory inv = view.getTopInventory();
        setTimeItem(inv);

    }

    private void handleCountdownClick(Player player, ClickType clickType) {
        if(clickType.isLeftClick()){
            if(clickType.isShiftClick())
            {
                game.setStartTime(game.getStartTime() + 60);
            }
            else{
                game.setStartTime(game.getStartTime() + 600);
            }
        }
        else if(clickType.isRightClick()){
            if(clickType.isShiftClick())
            {
                game.setStartTime(Math.max(0, game.getStartTime() - 60));
            }
            else{
                game.setStartTime(Math.max(0, game.getStartTime() - 600));
            }
        }

        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, SoundCategory.PLAYERS, 1.0f, 1.0f);

        InventoryView view = player.getOpenInventory();
        Inventory inv = view.getTopInventory();
        setCountdownItem(inv);

    }

}
