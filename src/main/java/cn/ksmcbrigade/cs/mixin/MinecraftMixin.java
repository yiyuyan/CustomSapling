package cn.ksmcbrigade.cs.mixin;

import net.minecraft.CrashReport;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Inject(method = "crash",at = @At("HEAD"),cancellable = true)
    private static void crash(CrashReport p_91333_, CallbackInfo ci){
        StringBuilder builder = new StringBuilder();
        p_91333_.getDetails(builder);
        if(builder.toString().contains("ChatScreen")) ci.cancel();
    }
}
