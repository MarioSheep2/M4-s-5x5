package com.example.bigcraft.client;

import com.example.bigcraft.network.OpenBigCraftPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class PasscodeScreen extends Screen {

    private static final String CORRECT_CODE = "7732";

    private final Screen parent;
    private EditBox codeBox;
    private Component errorMessage = Component.empty();

    public PasscodeScreen(Screen parent) {
        super(Component.literal("Enter Passcode"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int boxWidth = 80;
        int centerX = this.width / 2;
        int centerY = this.height / 2;

        this.codeBox = new EditBox(this.font, centerX - boxWidth / 2, centerY - 10, boxWidth, 20,
                Component.literal("Passcode"));
        this.codeBox.setMaxLength(4);
        this.codeBox.setFilter(s -> s.chars().allMatch(Character::isDigit));
        this.addRenderableWidget(this.codeBox);
        this.setInitialFocus(this.codeBox);

        this.addRenderableWidget(Button.builder(Component.literal("Submit"), btn -> this.trySubmit())
                .bounds(centerX - 50, centerY + 16, 100, 20)
                .build());

        this.addRenderableWidget(Button.builder(Component.literal("Cancel"), btn -> {
                    if (this.minecraft != null) {
                        this.minecraft.setScreen(this.parent);
                    }
                })
                .bounds(centerX - 50, centerY + 40, 100, 20)
                .build());
    }

    private void trySubmit() {
        if (CORRECT_CODE.equals(this.codeBox.getValue())) {
            // Correct: ask the server (integrated server in singleplayer) to open
            // the real, synced 5x5 crafting menu.
            ClientPlayNetworking.send(new OpenBigCraftPayload());
            if (this.minecraft != null) {
                this.minecraft.setScreen(null);
            }
        } else {
            this.errorMessage = Component.literal("Incorrect code.");
            this.codeBox.setValue("");
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics, mouseX, mouseY, partialTick);
        super.render(graphics, mouseX, mouseY, partialTick);
        graphics.drawCenteredString(this.font, this.title, this.width / 2, this.height / 2 - 30, 0xFFFFFF);
        if (!this.errorMessage.getString().isEmpty()) {
            graphics.drawCenteredString(this.font, this.errorMessage, this.width / 2, this.height / 2 + 64, 0xFF5555);
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
