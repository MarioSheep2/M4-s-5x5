package com.example.bigcraft.mixin;

import com.example.bigcraft.client.PasscodeScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CraftingScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.CraftingMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Adds a small "Passcode Panel" button below the vanilla 3x3 crafting grid.
 */
@Mixin(CraftingScreen.class)
public abstract class CraftingScreenMixin extends AbstractContainerScreen<CraftingMenu> {

    public CraftingScreenMixin(CraftingMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void bigcraft$addPasscodeButton(CallbackInfo ci) {
        int buttonWidth = 20;
        int buttonHeight = 20;
        int x = this.leftPos + (this.imageWidth - buttonWidth) / 2;
        int y = this.topPos + this.imageHeight + 4;

        this.addRenderableWidget(Button.builder(Component.literal("\uD83D\uDD12"), btn -> {
                    Minecraft client = Minecraft.getInstance();
                    client.setScreen(new PasscodeScreen(this));
                })
                .bounds(x, y, buttonWidth, buttonHeight)
                .tooltip(net.minecraft.client.gui.components.Tooltip.create(Component.literal("Enter passcode")))
                .build());
    }
}
