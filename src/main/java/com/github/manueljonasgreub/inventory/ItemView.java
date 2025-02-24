package com.github.manueljonasgreub.inventory;

import com.github.manueljonasgreub.BingoMain;
import com.github.manueljonasgreub.item.BingoItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ItemView {

    public void ShowMenu(Player player) {

        List<BingoItem> items = BingoMain.getInstance().getGame().getBingoItems();
        Inventory inventory = Bukkit.createInventory(player, 54, "Bingo Items");

        int[] slots = {2, 3, 4, 5, 6, 11, 12, 13, 14, 15, 20, 21, 22, 23, 24, 29, 30, 31, 32, 33, 38, 39, 40, 41, 42};

        for (int i = 0; i < items.size(); i++) {
            BingoItem item = items.get(i);
            Material material = Material.matchMaterial(item.getName().toUpperCase());
            ItemStack itemStack;

            if (material != null) {
                itemStack = new ItemStack(material);
            }
            else{
                itemStack = new ItemStack(Material.BARRIER);
                ItemMeta itemMeta = itemStack.getItemMeta();
                if (itemMeta != null) {
                    itemMeta.displayName(Component.text(ChatColor.RED + "Missing Item"));
                    itemMeta.lore(List.of(Component.text(ChatColor.RED + "This item could not be loaded.")));
                    itemStack.setItemMeta(itemMeta);
                }
            }

            if (i < slots.length) {
                inventory.setItem(slots[i], itemStack);
            }
        }

        player.openInventory(inventory);
    }
}
