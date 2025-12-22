package com.github.manueljonasgreub.inventory;

import com.github.manueljonasgreub.BingoMain;
import com.github.manueljonasgreub.api.BingoAPI;
import com.github.manueljonasgreub.game.Game;
import com.github.manueljonasgreub.item.BingoItemDTO;
import com.github.manueljonasgreub.utils.Difficulty;
import com.github.manueljonasgreub.utils.PlacementMode;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ItemsView implements Listener {

    int[] slots = {4, 5, 6, 7, 8, 13, 14, 15, 16, 17, 22, 23, 24, 25, 26, 31, 32, 33, 34, 35, 40, 41, 42, 43, 44};
    private Inventory gui;

    private Game game = BingoMain.getInstance().getGame();

    public ItemsView() {
        gui = Bukkit.createInventory(null, 54, "Item Settings");
        setItems();
    }

    public void openGUI(Player player) {
        player.openInventory(gui);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

        if (!event.getView().getTitle().equals("Item Settings")) return;
        event.setCancelled(true);

        Player player = (Player) event.getWhoClicked();
        ItemStack clickedItem = event.getCurrentItem();

        if (clickedItem == null || clickedItem.getType() == Material.AIR) return;

        switch (event.getSlot()) {
            case 9:
                handleVeryHardClick(player, clickedItem);
                break;
            case 11:
                handlePlacementClick(player, clickedItem);
                break;
            case 18:
                handleHardClick(player, clickedItem);
                break;
            case 27:
                handleMediumClick(player, clickedItem);
                break;
            case 36:
                handleEasyClick(player, clickedItem);
                break;
            case 45:
                handleVeryEasyClick(player, clickedItem);
                break;


            default:
                break;
        }


    }


    private void setItems() {
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

        setPreviewField(gui);
        setPlacementItem(gui);
        setDifficultyItem(gui);

        setVeryHardItem(gui);
        setHardItem(gui);
        setMediumItem(gui);
        setEasyItem(gui);
        setVeryEasyItem(gui);


    }


    private void setPreviewField(Inventory inv) {

        BingoAPI b = new BingoAPI();

        List<ItemStack> previewItems = b.fetchTestItems(5, game.getDifficultiesAsCsv(), game.placementMode);

        for (int i = 0; i < previewItems.size(); i++) {
            ItemStack item = previewItems.get(i);
            if (i < slots.length) {
                inv.setItem(slots[i], item);
            }
        }

    }

    private void setDifficultyItem(Inventory inv) {

        ItemStack item = new ItemStack(Material.FLOW_BANNER_PATTERN);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.AQUA + "Modify Difficulties");
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
            meta.lore(List.of(
                    Component.text(ChatColor.GRAY + ""),
                    Component.text(ChatColor.GRAY + "Toggles whether selected"),
                    Component.text(ChatColor.GRAY + "difficulties should appear"),
                    Component.text(ChatColor.GRAY + "on the bingo card")
            ));


        item.setItemMeta(meta);
        inv.setItem(0, item);

    }

    private void setPlacementItem(Inventory inv) {

        ItemStack item = new ItemStack(Material.ENDER_PEARL);
        ItemMeta meta = item.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
        meta.setDisplayName(ChatColor.AQUA + "Item Placement Mode");

        if(game.placementMode.ordinal() == 0) {

            item.setType(Material.ENDER_PEARL);
            meta.lore(List.of(
                    Component.text(ChatColor.GRAY + ""),
                    Component.text(ChatColor.GOLD + "Circular"),
                    Component.text(ChatColor.GRAY + "Flipped"),
                    Component.text(ChatColor.GRAY + "Random"),
                    Component.text(ChatColor.GRAY + ""),
                    Component.text(ChatColor.GRAY + "Modifies item placement"),
                    Component.text(ChatColor.GRAY + "behavior on the bingo card")
            ));
        }
        else if (game.placementMode.ordinal() == 1) {
            item.setType(Material.ENDER_EYE);
            meta.lore(List.of(
                    Component.text(ChatColor.GRAY + ""),
                    Component.text(ChatColor.GRAY + "Circular"),
                    Component.text(ChatColor.GOLD + "Flipped"),
                    Component.text(ChatColor.GRAY + "Random"),
                    Component.text(ChatColor.GRAY + ""),
                    Component.text(ChatColor.GRAY + "Modifies item placement"),
                    Component.text(ChatColor.GRAY + "behavior on the bingo card")
            ));
        }
        else{
            item.setType(Material.TURTLE_EGG);
            meta.lore(List.of(
                    Component.text(ChatColor.GRAY + ""),
                    Component.text(ChatColor.GRAY + "Circular"),
                    Component.text(ChatColor.GRAY + "Flipped"),
                    Component.text(ChatColor.GOLD + "Random"),
                    Component.text(ChatColor.GRAY + ""),
                    Component.text(ChatColor.GRAY + "Modifies item placement"),
                    Component.text(ChatColor.GRAY + "behavior on the bingo card")
            ));
        }

        item.setItemMeta(meta);
        inv.setItem(11, item);

    }


    private void setVeryHardItem(Inventory inv) {

        ItemStack item = new ItemStack(Material.RED_WOOL);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.RED + "Very Hard Items");
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);

        if(BingoMain.getInstance().getGame().difficulties.get(Difficulty.VERY_HARD)) {
            meta.lore(List.of(
                    Component.text(ChatColor.GRAY + ""),
                    Component.text(ChatColor.GOLD + "Enabled"),
                    Component.text(ChatColor.GRAY + "Disabled"),
                    Component.text(ChatColor.GRAY + ""),
                    Component.text(ChatColor.GRAY + "Toggles whether very"),
                    Component.text(ChatColor.GRAY + "hard items should appear")
            ));
            meta.addEnchant(Enchantment.UNBREAKING, 1, true);
        }
        else {
            meta.lore(List.of(
                    Component.text(ChatColor.GRAY + ""),
                    Component.text(ChatColor.GRAY + "Enabled"),
                    Component.text(ChatColor.GOLD + "Disabled"),
                    Component.text(ChatColor.GRAY + ""),
                    Component.text(ChatColor.GRAY + "Toggles whether very"),
                    Component.text(ChatColor.GRAY + "hard items should appear")
            ));
        }

        item.setItemMeta(meta);
        inv.setItem(9, item);

    }

    private void setHardItem(Inventory inv) {

        ItemStack item = new ItemStack(Material.ORANGE_WOOL);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD + "Hard Items");
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);

        if(BingoMain.getInstance().getGame().difficulties.get(Difficulty.HARD)) {
            meta.lore(List.of(
                    Component.text(ChatColor.GRAY + ""),
                    Component.text(ChatColor.GOLD + "Enabled"),
                    Component.text(ChatColor.GRAY + "Disabled"),
                    Component.text(ChatColor.GRAY + ""),
                    Component.text(ChatColor.GRAY + "Toggles whether hard"),
                    Component.text(ChatColor.GRAY + "items should appear")
            ));
            meta.addEnchant(Enchantment.UNBREAKING, 1, true);
        }
        else {
            meta.lore(List.of(
                    Component.text(ChatColor.GRAY + ""),
                    Component.text(ChatColor.GRAY + "Enabled"),
                    Component.text(ChatColor.GOLD + "Disabled"),
                    Component.text(ChatColor.GRAY + ""),
                    Component.text(ChatColor.GRAY + "Toggles whether hard"),
                    Component.text(ChatColor.GRAY + "items should appear")
            ));
        }

        item.setItemMeta(meta);
        inv.setItem(18, item);

    }

    private void setMediumItem(Inventory inv) {

        ItemStack item = new ItemStack(Material.YELLOW_WOOL);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.YELLOW + "Medium Items");
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);

        if(BingoMain.getInstance().getGame().difficulties.get(Difficulty.MEDIUM)) {
            meta.lore(List.of(
                    Component.text(ChatColor.GRAY + ""),
                    Component.text(ChatColor.GOLD + "Enabled"),
                    Component.text(ChatColor.GRAY + "Disabled"),
                    Component.text(ChatColor.GRAY + ""),
                    Component.text(ChatColor.GRAY + "Toggles whether medium"),
                    Component.text(ChatColor.GRAY + "items should appear")
            ));
            meta.addEnchant(Enchantment.UNBREAKING, 1, true);
        }
        else {
            meta.lore(List.of(
                    Component.text(ChatColor.GRAY + ""),
                    Component.text(ChatColor.GRAY + "Enabled"),
                    Component.text(ChatColor.GOLD + "Disabled"),
                    Component.text(ChatColor.GRAY + ""),
                    Component.text(ChatColor.GRAY + "Toggles whether medium"),
                    Component.text(ChatColor.GRAY + "items should appear")
            ));
        }

        item.setItemMeta(meta);
        inv.setItem(27, item);

    }

    private void setEasyItem(Inventory inv) {

        ItemStack item = new ItemStack(Material.LIME_WOOL);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + "Easy Items");
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);

        if(BingoMain.getInstance().getGame().difficulties.get(Difficulty.EASY)) {
            meta.lore(List.of(
                    Component.text(ChatColor.GRAY + ""),
                    Component.text(ChatColor.GOLD + "Enabled"),
                    Component.text(ChatColor.GRAY + "Disabled"),
                    Component.text(ChatColor.GRAY + ""),
                    Component.text(ChatColor.GRAY + "Toggles whether easy"),
                    Component.text(ChatColor.GRAY + "items should appear")
            ));
            meta.addEnchant(Enchantment.UNBREAKING, 1, true);
        }
        else {
            meta.lore(List.of(
                    Component.text(ChatColor.GRAY + ""),
                    Component.text(ChatColor.GRAY + "Enabled"),
                    Component.text(ChatColor.GOLD + "Disabled"),
                    Component.text(ChatColor.GRAY + ""),
                    Component.text(ChatColor.GRAY + "Toggles whether easy"),
                    Component.text(ChatColor.GRAY + "items should appear")
            ));
        }

        item.setItemMeta(meta);
        inv.setItem(36, item);

    }

    private void setVeryEasyItem(Inventory inv) {

        ItemStack item = new ItemStack(Material.LIGHT_BLUE_WOOL);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.AQUA + "Very Easy Items");
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);

        if(BingoMain.getInstance().getGame().difficulties.get(Difficulty.VERY_EASY)) {
            meta.lore(List.of(
                    Component.text(ChatColor.GRAY + ""),
                    Component.text(ChatColor.GOLD + "Enabled"),
                    Component.text(ChatColor.GRAY + "Disabled"),
                    Component.text(ChatColor.GRAY + ""),
                    Component.text(ChatColor.GRAY + "Toggles whether very"),
                    Component.text(ChatColor.GRAY + "easy items should appear")
            ));
            meta.addEnchant(Enchantment.UNBREAKING, 1, true);
        }
        else {
            meta.lore(List.of(
                    Component.text(ChatColor.GRAY + ""),
                    Component.text(ChatColor.GRAY + "Enabled"),
                    Component.text(ChatColor.GOLD + "Disabled"),
                    Component.text(ChatColor.GRAY + ""),
                    Component.text(ChatColor.GRAY + "Toggles whether very"),
                    Component.text(ChatColor.GRAY + "easy items should appear")
            ));
        }

        item.setItemMeta(meta);
        inv.setItem(45, item);

    }

    private void handleVeryHardClick(Player player, ItemStack clickedItem) {

        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, SoundCategory.PLAYERS, 1.0f, 1.0f);
        BingoMain.getInstance().getGame().difficulties.put(Difficulty.VERY_HARD, !BingoMain.getInstance().getGame().difficulties.get(Difficulty.VERY_HARD));
        InventoryView view = player.getOpenInventory();
        Inventory inv = view.getTopInventory();
        setVeryHardItem(inv);
        setPreviewField(inv);
    }

    private void handleHardClick(Player player, ItemStack clickedItem) {

        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, SoundCategory.PLAYERS, 1.0f, 1.0f);
        BingoMain.getInstance().getGame().difficulties.put(Difficulty.HARD, !BingoMain.getInstance().getGame().difficulties.get(Difficulty.HARD));
        InventoryView view = player.getOpenInventory();
        Inventory inv = view.getTopInventory();
        setHardItem(inv);
        setPreviewField(inv);
    }

    private void handleMediumClick(Player player, ItemStack clickedItem) {

        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, SoundCategory.PLAYERS, 1.0f, 1.0f);
        BingoMain.getInstance().getGame().difficulties.put(Difficulty.MEDIUM, !BingoMain.getInstance().getGame().difficulties.get(Difficulty.MEDIUM));
        InventoryView view = player.getOpenInventory();
        Inventory inv = view.getTopInventory();
        setMediumItem(inv);
        setPreviewField(inv);
    }

    private void handleEasyClick(Player player, ItemStack clickedItem) {

        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, SoundCategory.PLAYERS, 1.0f, 1.0f);
        BingoMain.getInstance().getGame().difficulties.put(Difficulty.EASY, !BingoMain.getInstance().getGame().difficulties.get(Difficulty.EASY));
        InventoryView view = player.getOpenInventory();
        Inventory inv = view.getTopInventory();
        setEasyItem(inv);
        setPreviewField(inv);
    }

    private void handleVeryEasyClick(Player player, ItemStack clickedItem) {

        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, SoundCategory.PLAYERS, 1.0f, 1.0f);
        BingoMain.getInstance().getGame().difficulties.put(Difficulty.VERY_EASY, !BingoMain.getInstance().getGame().difficulties.get(Difficulty.VERY_EASY));
        InventoryView view = player.getOpenInventory();
        Inventory inv = view.getTopInventory();
        setVeryEasyItem(inv);
        setPreviewField(inv);
    }

    private void handlePlacementClick(Player player, ItemStack clickedItem)
    {
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, SoundCategory.PLAYERS, 1.0f, 1.0f);
        game.placementMode = PlacementMode.values()[(game.placementMode.ordinal() + 1) % PlacementMode.values().length];
        InventoryView view = player.getOpenInventory();
        Inventory inv = view.getTopInventory();
        setPlacementItem(inv);
        setPreviewField(inv);
    }
}
