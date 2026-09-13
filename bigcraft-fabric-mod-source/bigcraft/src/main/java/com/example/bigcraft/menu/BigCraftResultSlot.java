package com.example.bigcraft.menu;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;

/**
 * Behaves like vanilla's crafting result slot: you can only take from it
 * (never place items into it), and taking the result consumes one item
 * from each of the 25 input slots that contributed to the recipe.
 */
public class BigCraftResultSlot extends Slot {

    private final Player player;
    private final Container craftingGrid;
    private final int gridWidth;
    private final int gridHeight;

    public BigCraftResultSlot(Player player, Container craftingGrid, int gridWidth, int gridHeight,
                               Container resultContainer, int index, int x, int y) {
        super(resultContainer, index, x, y);
        this.player = player;
        this.craftingGrid = craftingGrid;
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return false;
    }

    @Override
    public void onTake(Player player, ItemStack stack) {
        // Shrink one from every non-empty ingredient slot, vanilla-style.
        for (int i = 0; i < this.craftingGrid.getContainerSize(); i++) {
            ItemStack ingredient = this.craftingGrid.getItem(i);
            if (!ingredient.isEmpty()) {
                this.craftingGrid.removeItem(i, 1);
            }
        }
        this.craftingGrid.setChanged();
    }

    /** Recomputes the result from the current 5x5 grid using vanilla recipe matching. */
    public void updateResult(RecipeManager recipeManager, net.minecraft.world.level.Level level) {
        java.util.List<ItemStack> items = new java.util.ArrayList<>();
        for (int i = 0; i < this.craftingGrid.getContainerSize(); i++) {
            items.add(this.craftingGrid.getItem(i));
        }
        CraftingInput input = CraftingInput.of(this.gridWidth, this.gridHeight, items);

        ItemStack result = recipeManager
                .getRecipeFor(RecipeType.CRAFTING, input, level)
                .map(holder -> holder.value().assemble(input, level.registryAccess()))
                .orElse(ItemStack.EMPTY);

        this.container.setItem(0, result);
    }
}
