package cn.ksmcbrigade.cs.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public class ScreenMixin {
    @Inject(method = "wrapScreenError",at = @At("HEAD"),cancellable = true)
    private static void error(Runnable p_96580_, String p_96581_, String p_96582_, CallbackInfo ci){
        if(Minecraft.getInstance().screen instanceof ChatScreen){
            try {
                p_96580_.run();
            } catch (Throwable e) {
                //nothing
            }
            ci.cancel();
        }
    }
}
