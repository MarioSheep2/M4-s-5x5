package com.example.bigcraft.network;

import com.example.bigcraft.BigCraftMod;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

/**
 * Empty marker packet: "the passcode was entered correctly, please open
 * my big crafting menu now." The passcode itself is never sent over the
 * network — only the pass/fail result, decided client-side in PasscodeScreen.
 */
public record OpenBigCraftPayload() implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<OpenBigCraftPayload> TYPE =
            new CustomPacketPayload.Type<>(BigCraftMod.id("open_big_craft"));

    public static final StreamCodec<net.minecraft.network.RegistryFriendlyByteBuf, OpenBigCraftPayload> CODEC =
            StreamCodec.unit(new OpenBigCraftPayload());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
