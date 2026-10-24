package fr.autohold;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class AutoHoldScreen extends Screen {
    private static final int W = 170, H = 20;

    public AutoHoldScreen() {
        super(Component.translatable("screen.autohold.title"));
    }

    @Override
    protected void init() {
        int x = this.width / 2 - W / 2;
        int y = this.height / 2 - 24;

        // Fond du panneau (ajoute en premier = dessine sous les boutons)
        this.addRenderableWidget(new PanelWidget(x - 10, y - 28, W + 20, 118));

        this.addRenderableWidget(Button.builder(walkLabel(), b -> {
            AutoHoldClient.setAutoWalk(Minecraft.getInstance(), !AutoHoldClient.autoWalk);
            b.setMessage(walkLabel());
        }).bounds(x, y, W, H).build());

        this.addRenderableWidget(Button.builder(attackLabel(), b -> {
            AutoHoldClient.setAutoAttack(Minecraft.getInstance(), !AutoHoldClient.autoAttack);
            b.setMessage(attackLabel());
        }).bounds(x, y + 26, W, H).build());

        this.addRenderableWidget(Button.builder(Component.translatable("gui.done"), b -> this.onClose())
                .bounds(x, y + 58, W, H).build());
    }

    private Component walkLabel() {
        return label("option.autohold.walk", AutoHoldClient.autoWalk);
    }

    private Component attackLabel() {
        return label("option.autohold.attack", AutoHoldClient.autoAttack);
    }

    private Component label(String key, boolean on) {
        return Component.translatable(key).append(": ")
                .append(Component.translatable(on ? "option.autohold.on" : "option.autohold.off"));
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
        super.extractRenderState(graphics, mouseX, mouseY, delta);
        String t = this.title.getString();
        graphics.text(this.font, t, this.width / 2 - this.font.width(t) / 2,
                this.height / 2 - 46, 0xFFFFFFFF, true);
    }

    /** Simple rectangle sombre avec bordure, sert de fond au panneau. */
    private static class PanelWidget extends AbstractWidget {
        PanelWidget(int x, int y, int w, int h) {
            super(x, y, w, h, Component.empty());
            this.active = false;
        }

        @Override
        protected void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
            int x1 = getX(), y1 = getY(), x2 = x1 + this.width, y2 = y1 + this.height;
            graphics.fill(x1, y1, x2, y2, 0xCC101010);
            int border = 0xFF808080;
            graphics.fill(x1, y1, x2, y1 + 1, border);
            graphics.fill(x1, y2 - 1, x2, y2, border);
            graphics.fill(x1, y1, x1 + 1, y2, border);
            graphics.fill(x2 - 1, y1, x2, y2, border);
        }

        @Override
        protected void updateWidgetNarration(NarrationElementOutput builder) {
        }
    }
}
