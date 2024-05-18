package me.cominixo.nimipona.mixin;

import me.cominixo.nimipona.ContextualizedTranslatableText;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(Item.class)
public abstract class ItemMixin {

    @Shadow public abstract String getTranslationKey();

    @Shadow public abstract String getTranslationKey(ItemStack stack);


    @Inject(method = "inventoryTick", at = @At("HEAD"))
    private void updateInventoryContext(ItemStack stack, World world, Entity entity, int slot, boolean selected, CallbackInfo ci) {
        if (entity instanceof PlayerEntity player) {

            if (!player.getInventory().equals(ContextualizedTranslatableText.getPlayerInventory())) {
                ContextualizedTranslatableText.clearInventoryContext();
                ContextualizedTranslatableText.addInventoryContext(player.getInventory());
            }

            if (MinecraftClient.getInstance().currentScreen instanceof HandledScreen<?> screen) {
                ContextualizedTranslatableText.addInventoryContext(screen.getScreenHandler().slots.get(0).inventory);
            }

        }
    }

    /**
     * @author cominixo
     *
     * @reason Basically necessary to implement contextualized items
     */
    @Overwrite
    public Text getName(ItemStack stack) {
        return MutableText.of(new ContextualizedTranslatableText(this.getTranslationKey(stack)));
    }

    /**
     * @author cominixo
     *
     * @reason Basically necessary to implement contextualized items
     */
    @Overwrite
    public Text getName() {
        return MutableText.of(new ContextualizedTranslatableText(this.getTranslationKey()));
    }


}
