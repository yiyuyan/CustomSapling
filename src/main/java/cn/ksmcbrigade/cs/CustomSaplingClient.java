package cn.ksmcbrigade.cs;

import cn.ksmcbrigade.cs.gui.FixedChatScreen;
import cn.ksmcbrigade.cs.network.GetCommandMessage;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CustomSaplingClient {

    @OnlyIn(Dist.CLIENT)
    public static void handle(GetCommandMessage msg){
        if(!Config.COMMAND_MODE.get()) return;
        try {
            Minecraft.getInstance().setScreen(new FixedChatScreen(msg.result()));
        } catch (Exception e) {
            //nothing
        }
    }
}
