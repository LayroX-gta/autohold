package fr.autohold;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;

public class AutoHoldClient implements ClientModInitializer {
    public static final String MOD_ID = "autohold";

    public static boolean autoWalk = false;
    public static boolean autoAttack = false;
    private static boolean screenOpen = false;

    private static KeyMapping openPanelKey, walkKey, attackKey;

    @Override
    public void onInitializeClient() {
        KeyMapping.Category category = KeyMapping.Category.register(
                Identifier.fromNamespaceAndPath(MOD_ID, "main"));

        openPanelKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.autohold.panel", InputConstants.Type.KEYSYM, InputConstants.KEY_K, category));
        walkKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.autohold.walk", InputConstants.Type.KEYSYM, InputConstants.KEY_G, category));
        attackKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.autohold.attack", InputConstants.Type.KEYSYM, InputConstants.KEY_H, category));

        // Savoir si un ecran (inventaire, menu...) est ouvert
        ScreenEvents.AFTER_INIT.register((client, screen, w, h) -> {
            screenOpen = true;
            ScreenEvents.remove(screen).register(s -> screenOpen = false);
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;

            while (openPanelKey.consumeClick()) {
                if (!screenOpen) client.gui.setScreen(new AutoHoldScreen());
            }
            while (walkKey.consumeClick()) setAutoWalk(client, !autoWalk);
            while (attackKey.consumeClick()) setAutoAttack(client, !autoAttack);

            if (screenOpen) return;
            if (autoWalk) client.options.keyUp.setDown(true);
            if (autoAttack) client.options.keyAttack.setDown(true);
        });

        HudElementRegistry.attachElementBefore(
                VanillaHudElements.CHAT,
                Identifier.fromNamespaceAndPath(MOD_ID, "status"),
                AutoHoldClient::renderStatus);
    }

    private static void renderStatus(GuiGraphicsExtractor graphics, DeltaTracker tickCounter) {
        Minecraft mc = Minecraft.getInstance();
        int y = 4;
        if (autoWalk) {
            graphics.text(mc.font, "> AutoWalk", 4, y, 0xFF55FF55, true);
            y += 10;
        }
        if (autoAttack) {
            graphics.text(mc.font, "> Clic gauche maintenu", 4, y, 0xFFFFAA00, true);
        }
    }

    public static void setAutoWalk(Minecraft client, boolean value) {
        autoWalk = value;
        if (!value) client.options.keyUp.setDown(false);
    }

    public static void setAutoAttack(Minecraft client, boolean value) {
        autoAttack = value;
        if (!value) client.options.keyAttack.setDown(false);
    }
}
