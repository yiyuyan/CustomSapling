package cn.ksmcbrigade.cs.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class FixedChatScreen extends ChatScreen {
    public FixedChatScreen(String p_95579_) {
        super(p_95579_);
        while (this.minecraft==null){
            this.minecraft = Minecraft.getInstance();
        }
    }

    @Override
    protected void init() {
        if(this.minecraft==null){
            this.onClose();
            return;
        }
        super.init();
    }

    @Override
    public void render(@NotNull GuiGraphics p_282470_, int p_282674_, int p_282014_, float p_283132_) {
        if(this.input==null || this.font==null || this.minecraft==null || this.commandSuggestions==null) return;
        super.render(p_282470_, p_282674_, p_282014_, p_283132_);
    }
}
