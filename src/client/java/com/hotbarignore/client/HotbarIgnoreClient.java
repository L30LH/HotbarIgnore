package com.hotbarignore.client;

import com.hotbarignore.HotbarIgnore;
import com.hotbarignore.config.HotbarIgnoreConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class HotbarIgnoreClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HotbarIgnoreConfig.load();
        HotbarIgnore.LOGGER.info("HotbarIgnore initialized! Custom hotbar keybinds will be ignored in inventory screens.");

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager.literal("hotbarignore")
                .then(ClientCommandManager.literal("toggle")
                    .executes(context -> {
                        HotbarIgnoreConfig config = HotbarIgnoreConfig.getInstance();
                        config.enabled = !config.enabled;
                        HotbarIgnoreConfig.save();
                        context.getSource().sendFeedback(Text.literal("[HotbarIgnore] Mod is now ")
                            .formatted(Formatting.GOLD)
                            .append(Text.literal(config.enabled ? "ENABLED" : "DISABLED")
                                .formatted(config.enabled ? Formatting.GREEN : Formatting.RED)));
                        return 1;
                    }))
                .then(ClientCommandManager.literal("reload")
                    .executes(context -> {
                        HotbarIgnoreConfig.load();
                        context.getSource().sendFeedback(Text.literal("[HotbarIgnore] Configuration reloaded.")
                            .formatted(Formatting.GREEN));
                        return 1;
                    }))
                .executes(context -> {
                    HotbarIgnoreConfig config = HotbarIgnoreConfig.getInstance();
                    context.getSource().sendFeedback(Text.literal("[HotbarIgnore] Status: ")
                        .formatted(Formatting.GOLD)
                        .append(Text.literal(config.enabled ? "ENABLED" : "DISABLED")
                            .formatted(config.enabled ? Formatting.GREEN : Formatting.RED))
                        .append(Text.literal(" (Type /hotbarignore toggle to toggle)").formatted(Formatting.GRAY)));
                    return 1;
                }));
        });
    }
}
