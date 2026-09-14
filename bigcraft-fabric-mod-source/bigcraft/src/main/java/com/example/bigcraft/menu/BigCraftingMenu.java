package com.example.bigcraft.menu;

import com.example.bigcraft.BigCraftMod;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class BigCraftingMenu extends AbstractContainerMenu {

    public static final int GRID_WIDTH = 5;
    public static final int GRID_HEIGHT = 5;
    public static final int GRID_SIZE = GRID_WIDTH * GRID_HEIGHT;

    private final SimpleContainer craftingGrid = new SimpleContainer(GRID_SIZE) {
        @Override
        public void setChanged() {
            super.setChanged();
            BigCraftingMenu.this.slotsChangedInternal();
        }
    };
    private final ResultContainer resultContainer = new ResultContainer();
    private final Player player;
    private BigCraftResultSlot resultSlot;

    public BigCraftingMenu(int syncId, Inventory playerInventory) {
        super(BigCraftMod.BIG_CRAFTING_MENU_TYPE, syncId);
        this.player = playerInventory.player;

        int gridOriginX = 8;
        int gridOriginY = 18;

        for (int row = 0; row < GRID_HEIGHT; row++) {
            for (int col = 0; col < GRID_WIDTH; col++) {
                int index = col + row * GRID_WIDTH;
                this.addSlot(new Slot(this.craftingGrid, index,
                        gridOriginX + col * 18, gridOriginY + row * 18));
            }
        }

        this.resultSlot = new BigCraftResultSlot(this.player, this.craftingGrid, GRID_WIDTH, GRID_HEIGHT,
                this.resultContainer, 0, gridOriginX + GRID_WIDTH * 18 + 20, gridOriginY + (GRID_HEIGHT * 18) / 2 - 8);
        this.addSlot(this.resultSlot);

        int inventoryY = gridOriginY + GRID_HEIGHT * 18 + 24;

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9,
                        8 + col * 18, inventoryY + row * 18));
            }
        }
        for (int col = 0; col < 9; col++) {
            this.addSlot(new Slot(playerInventory, col, 8 + col * 18, inventoryY + 58));
        }
    }

    private void slotsChangedInternal() {
        if (this.player != null && !this.player.level().isClientSide()) {
            net.minecraft.server.level.ServerLevel serverLevel =
                    (net.minecraft.server.level.ServerLevel) this.player.level();
            this.resultSlot.updateResult(serverLevel.recipeAccess(), serverLevel);
        }
    }

    @Override
    public void slotsChanged(net.minecraft.world.Container container) {
        super.slotsChanged(container);
        this.slotsChangedInternal();
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot slot = this.slots.get(index);
        if (slot == null || !slot.hasItem()) {
            return ItemStack.EMPTY;
        }

        ItemStack original = slot.getItem();
        ItemStack copy = original.copy();
        int gridAndResultSlots = GRID_SIZE + 1;

        if (index < gridAndResultSlots) {
            if (!this.moveItemStackTo(original, gridAndResultSlots, this.slots.size(), true)) {
                return ItemStack.EMPTY;
            }
        } else {
            if (!this.moveItemStackTo(original, 0, GRID_SIZE, false)) {
                return ItemStack.EMPTY;
            }
        }

        if (original.isEmpty()) {
            slot.set(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }
        return copy;
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        for (int i = 0; i < this.craftingGrid.getContainerSize(); i++) {
            ItemStack stack = this.craftingGrid.removeItemNoUpdate(i);
            if (!stack.isEmpty()) {
                player.getInventory().placeItemBackInInventory(stack);
            }
        }
    }
}
