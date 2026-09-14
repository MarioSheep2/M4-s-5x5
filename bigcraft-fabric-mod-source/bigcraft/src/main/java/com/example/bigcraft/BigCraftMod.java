package com.example.bigcraft;

import com.example.bigcraft.menu.BigCraftingMenu;
import com.example.bigcraft.network.OpenBigCraftPayload;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.network.chat.Component;

public class BigCraftMod implements ModInitializer {

    public static final String MOD_ID = "bigcraft";

    public static final MenuType<BigCraftingMenu> BIG_CRAFTING_MENU_TYPE = Registry.register(
            BuiltInRegistries.MENU,
            id("big_crafting"),
            new MenuType<>(BigCraftingMenu::new, FeatureFlags.VANILLA_SET)
    );

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }

    @Override
    public void onInitialize() {
        PayloadTypeRegistry.playC2S().register(OpenBigCraftPayload.TYPE, OpenBigCraftPayload.CODEC);

        ServerPlayNetworking.registerGlobalReceiver(OpenBigCraftPayload.TYPE, (payload, context) -> {
            context.player().openMenu(new SimpleMenuProvider(
                    (syncId, inventory, player) -> new BigCraftingMenu(syncId, inventory),
                    Component.literal("Passcode Crafting")
            ));
        });
    }
}
