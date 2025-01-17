package com.github.manueljonasgreub.item;

import org.bukkit.inventory.ItemStack;

public class BingoItem extends ItemStack {

    private boolean isFound = false;

    public BingoItem(ItemStack itemStack) {
        super(itemStack);
    }

    public boolean isFound() {
        return isFound;
    }

    public void setFound(boolean found) {
        isFound = found;
    }
}
