package me.cominixo.nimipona;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.text.TranslatableTextContent;
import java.util.HashSet;
import java.util.Set;

public class ContextualizedTranslatableText extends TranslatableTextContent {

    private static PlayerInventory playerInventory;

    private static Set<String> inventoryContext;

    public ContextualizedTranslatableText(String key) {
        super(key, "", null);

    }

    public static void addInventoryContext(Inventory inventory) {
        if (inventory instanceof PlayerInventory playerInventory) {
            ContextualizedTranslatableText.playerInventory = playerInventory;
        }
        for (int i = 0; i < inventory.size(); ++i) {
            inventoryContext.add(inventory.getStack(i).getItem().getTranslationKey());
        }

    }

    public static Set<String> getInventoryContext() {
        return inventoryContext;
    }

    public static void clearInventoryContext() {

        inventoryContext = new HashSet<>();
    }

    public static PlayerInventory getPlayerInventory() {
        return playerInventory;
    }



}
