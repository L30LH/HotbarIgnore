package com.hotbarignore;

import com.hotbarignore.config.HotbarIgnoreConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.input.KeyInput;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.OptionalInt;

public final class HotbarIgnore {
    public static final String MOD_ID = "hotbarignore";
    public static final String MOD_NAME = "HotbarIgnore";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    private HotbarIgnore() {
    }

    public static boolean isEnabled() {
        return HotbarIgnoreConfig.getInstance().enabled;
    }

    /**
     * Determines whether the given key input corresponds to a default hotbar key (1-9).
     *
     * @param keyInput the key pressed
     * @param client the Minecraft client
     * @return 0-8 for hotbar slots 1-9, or -1 if the key is not a default hotbar key
     */
    public static int getHotbarSlot(KeyInput keyInput, MinecraftClient client) {
        if (!isEnabled()) {
            return -1;
        }

        HotbarIgnoreConfig config = HotbarIgnoreConfig.getInstance();

        // Check if keypad is pressed and whether it's allowed
        if (!config.allowKeypad) {
            int key = keyInput.key();
            if (key >= GLFW.GLFW_KEY_KP_1 && key <= GLFW.GLFW_KEY_KP_9) {
                return -1;
            }
        }

        // 1. Direct digit conversion from InputUtil (handles 1-9 on keyboard and keypad)
        OptionalInt digit = InputUtil.fromKeyCode(keyInput).toInt();
        if (digit.isPresent()) {
            int val = digit.getAsInt();
            if (val >= 1 && val <= 9) {
                return val - 1; // 0-based index for hotbar slot
            }
        }

        // 2. Fallback: Check against defaultKey registered in vanilla hotbar KeyBindings
        if (client != null && client.options != null && client.options.hotbarKeys != null) {
            for (int i = 0; i < 9; i++) {
                KeyBinding kb = client.options.hotbarKeys[i];
                if (kb != null) {
                    InputUtil.Key defaultKey = kb.getDefaultKey();
                    if (defaultKey != null && matchesKey(defaultKey, keyInput)) {
                        return i;
                    }
                }
            }
        }

        return -1;
    }

    private static boolean matchesKey(InputUtil.Key key, KeyInput keyInput) {
        if (keyInput.key() == InputUtil.UNKNOWN_KEY.getCode()) {
            return key.getCategory() == InputUtil.Type.SCANCODE && key.getCode() == keyInput.scancode();
        } else {
            return key.getCategory() == InputUtil.Type.KEYSYM && key.getCode() == keyInput.key();
        }
    }
}
