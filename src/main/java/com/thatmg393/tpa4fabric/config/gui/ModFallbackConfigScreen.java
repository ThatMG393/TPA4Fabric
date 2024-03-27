package com.thatmg393.usefulhuds.config.gui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;

public class ModFallbackConfigScreen extends Screen {
    protected final Screen parent;

    public ModFallbackConfigScreen(Screen parent) {
        super(Text.translatable("usefulhuds.config.fallback.title"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        ButtonWidget doneButton = ButtonWidget.builder(
            ScreenTexts.DONE,
            b -> this.close()
        ).width(200)
        .position(this.width / 2 - 100, this.height - 27)
        .build();
        
        this.addDrawableChild(doneButton);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 20, 0xFFFFFF);
        context.drawCenteredTextWithShadow(this.textRenderer, Text.translatable("usefulhuds.config.fallback.why"), this.width / 2, this.height / 2, 0xFFFFFF);
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public void close() {
        this.client.setScreen(this.parent);
    }
}
