package com.hotbarignore.mixin;

import com.hotbarignore.HotbarIgnore;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.input.KeyInput;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HandledScreen.class)
public abstract class HandledScreenMixin<T extends ScreenHandler> extends Screen {
    protected HandledScreenMixin(Text title) {
        super(title);
    }

    @Shadow
    @Nullable
    protected Slot focusedSlot;

    @Shadow
    @Final
    protected T handler;

    @Shadow
    protected abstract void onMouseClick(Slot slot, int slotId, int button, SlotActionType actionType);

    @Inject(method = "handleHotbarKeyPressed", at = @At("HEAD"), cancellable = true)
    private void hotbarignore$handleHotbarKeyPressed(KeyInput keyInput, CallbackInfoReturnable<Boolean> cir) {
        if (!HotbarIgnore.isEnabled()) {
            return;
        }

        if (this.handler.getCursorStack().isEmpty() && this.focusedSlot != null) {
            // Preserve offhand swap key (swapHandsKey) functionality
            if (this.client.options.swapHandsKey.matchesKey(keyInput)) {
                this.onMouseClick(this.focusedSlot, this.focusedSlot.id, 40, SlotActionType.SWAP);
                cir.setReturnValue(true);
                return;
            }

            // Check if key is a default hotbar key (1-9)
            int slot = HotbarIgnore.getHotbarSlot(keyInput, this.client);
            if (slot >= 0 && slot < 9) {
                this.onMouseClick(this.focusedSlot, this.focusedSlot.id, slot, SlotActionType.SWAP);
                cir.setReturnValue(true);
                return;
            }

            // If it is neither swap hands nor a default hotbar key, ignore custom keybinds
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "onMouseClick(Lnet/minecraft/client/gui/Click;)V", at = @At("HEAD"), cancellable = true)
    private void hotbarignore$onMouseClick(Click click, CallbackInfo ci) {
        if (!HotbarIgnore.isEnabled()) {
            return;
        }

        if (this.focusedSlot != null && this.handler.getCursorStack().isEmpty()) {
            // Allow swap hands if assigned to a mouse button
            if (this.client.options.swapHandsKey.matchesMouse(click)) {
                this.onMouseClick(this.focusedSlot, this.focusedSlot.id, 40, SlotActionType.SWAP);
            }
            // Custom mouse hotbar keybinds (e.g. Mouse 4/5) are ignored
            ci.cancel();
        }
    }
}
