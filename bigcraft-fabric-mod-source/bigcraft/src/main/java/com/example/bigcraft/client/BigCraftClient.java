package com.example.bigcraft.client;

import com.example.bigcraft.BigCraftMod;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screens.MenuScreens;

public class BigCraftClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        MenuScreens.register(BigCraftMod.BIG_CRAFTING_MENU_TYPE, BigCraftingScreen::new);
    }
}
