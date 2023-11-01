package net.rpgz.access;

import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;

public interface InventoryAccess {

    public SimpleInventory rpgz$getInventory();

    public void rpgz$addingInventoryItems(ItemStack stack);
}
