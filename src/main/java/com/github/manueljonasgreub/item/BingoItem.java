package com.github.manueljonasgreub.item;

import org.bukkit.inventory.ItemStack;

public class BingoItem {
    private final ItemStack itemStack;
    private boolean found;

    public BingoItem(ItemStack itemStack) {
        this.itemStack = itemStack;
        this.found = false;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public boolean isFound() {
        return found;
    }

    public void setFound(boolean found) {
        this.found = found;
    }
}