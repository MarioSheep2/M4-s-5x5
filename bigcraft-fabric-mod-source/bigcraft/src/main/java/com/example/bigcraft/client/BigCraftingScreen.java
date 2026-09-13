package com.example.bigcraft.client;

import com.example.bigcraft.menu.BigCraftingMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

/**
 * Purely visual: draws simple panels behind the 25 input slots and the
 * output slot. No custom texture file is required.
 */
public class BigCraftingScreen extends AbstractContainerScreen<BigCraftingMenu> {

    public BigCraftingScreen(BigCraftingMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = 8 + BigCraftingMenu.GRID_WIDTH * 18 + 20 + 18 + 8;
        this.imageHeight = 18 + BigCraftingMenu.GRID_HEIGHT * 18 + 24 + 58 + 14;
        this.inventoryLabelY = this.imageHeight - 96;
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        int x = this.leftPos;
        int y = this.topPos;

        // Backing panel for the whole window.
        graphics.fill(x, y, x + this.imageWidth, y + this.imageHeight, 0xF0C6C6C6);
        graphics.fill(x + 4, y + 4, x + this.imageWidth - 4, y + this.imageHeight - 4, 0xFF8B8B8B);

        // Slot backgrounds for the 25 input slots.
        int gridOriginX = x + 8;
        int gridOriginY = y + 18;
        for (int row = 0; row < BigCraftingMenu.GRID_HEIGHT; row++) {
            for (int col = 0; col < BigCraftingMenu.GRID_WIDTH; col++) {
                int slotX = gridOriginX + col * 18;
                int slotY = gridOriginY + row * 18;
                graphics.fill(slotX - 1, slotY - 1, slotX + 17, slotY + 17, 0xFF373737);
                graphics.fill(slotX, slotY, slotX + 16, slotY + 16, 0xFF8B8B8B);
            }
        }

        // Output slot background.
        int outX = gridOriginX + BigCraftingMenu.GRID_WIDTH * 18 + 20;
        int outY = gridOriginY + (BigCraftingMenu.GRID_HEIGHT * 18) / 2 - 8;
        graphics.fill(outX - 1, outY - 1, outX + 17, outY + 17, 0xFF373737);
        graphics.fill(outX, outY, outX + 16, outY + 16, 0xFFFFD700);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics, mouseX, mouseY, partialTick);
        super.render(graphics, mouseX, mouseY, partialTick);
        this.renderTooltip(graphics, mouseX, mouseY);
    }
}
