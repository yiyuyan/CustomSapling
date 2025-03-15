package cn.ksmcbrigade.cs.network;

import cn.ksmcbrigade.cs.CustomSaplingClient;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;

public record GetCommandMessage(BlockPos blockPos,String result) {
    public static void encode(GetCommandMessage message,FriendlyByteBuf buf){
        buf.writeBlockPos(message.blockPos);
        buf.writeUtf(message.result);
    }

    public static GetCommandMessage decode(FriendlyByteBuf buf){
        return new GetCommandMessage(buf.readBlockPos(),buf.readUtf());
    }

    public static void handleClient(GetCommandMessage msg){
        CustomSaplingClient.handle(msg);
    }
}
