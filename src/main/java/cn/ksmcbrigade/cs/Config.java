package cn.ksmcbrigade.cs;

import net.minecraftforge.common.ForgeConfigSpec;

public class Config {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec.BooleanValue COMMAND_MODE = BUILDER.define("command_mode",false);
    public static final ForgeConfigSpec SPEC = BUILDER.build();
}
