package com.example.bigcraft;

import com.example.bigcraft.menu.BigCraftingMenu;
import com.example.bigcraft.network.OpenBigCraftPayload;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.network.chat.Component;

/**
 * Runs on both the client and the (integrated or dedicated) server.
 * Registers the custom menu type and the packet that the client sends
 * once the correct passcode has been entered.
 */
public class BigCraftMod implements ModInitializer {

    public static final String MOD_ID = "bigcraft";

    // The custom 5x5 crafting menu type. Registered here so it exists on both sides.
    public static MenuType<BigCraftingMenu> BIG_CRAFTING_MENU_TYPE;

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    @Override
    public void onInitialize() {
        BIG_CRAFTING_MENU_TYPE = ScreenHandlerRegistry.registerSimple(
                id("big_crafting"),
                BigCraftingMenu::new
        );

        // Register the C2S payload used to request opening the big crafting menu.
        PayloadTypeRegistry.playC2S().register(OpenBigCraftPayload.TYPE, OpenBigCraftPayload.CODEC);

        // When the server receives it (passcode already validated on the client),
        // open the real, server-synced menu for that player.
        ServerPlayNetworking.registerGlobalReceiver(OpenBigCraftPayload.TYPE, (payload, context) -> {
            context.player().openMenu(new SimpleMenuProvider(
                    (syncId, inventory, player) -> new BigCraftingMenu(syncId, inventory),
                    Component.literal("Passcode Crafting")
            ));
        });
    }
}
