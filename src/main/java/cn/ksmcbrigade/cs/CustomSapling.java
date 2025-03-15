package cn.ksmcbrigade.cs;

import cn.ksmcbrigade.cs.network.GetCommandMessage;
import cn.ksmcbrigade.cs.tree.CustomSaplingBlock;
import cn.ksmcbrigade.cs.tree.CustomTreeGrower;
import cn.ksmcbrigade.cs.tree.TreeInformation;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.blocks.BlockStateArgument;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.CreativeModeTabRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.registries.*;

import java.util.HashMap;
import java.util.Map;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(CustomSapling.MODID)
public class CustomSapling {

    // Define mod id in a common place for everything to reference
    public static final String MODID = "cs";

    public static final Map<BlockPos,TreeInformation> informationMap = new HashMap<>();

    public static final ResourceKey<ConfiguredFeature<?, ?>> CUSTOM_SAP = FeatureUtils.createKey("cs:custom_sap");
    public static final TreeConfiguration TREE_CONFIGURATION = new TreeInformation(Blocks.OAK_LOG, Blocks.OAK_LEAVES,4,3,2).createTreeConfiguration();
    public static final ConfiguredFeature<?,?> CONFIGURED_FEATURE = new ConfiguredFeature<>(Feature.TREE, CustomSapling.TREE_CONFIGURATION);

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS,MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS,MODID);

    public static final RegistryObject<Block> CUSTOM_SAPLING = BLOCKS.register("custom_sapling",()-> new CustomSaplingBlock(new CustomTreeGrower(),
            BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).noCollission().randomTicks().instabreak().sound(SoundType.GRASS).pushReaction(PushReaction.DESTROY)));

    public static final RegistryObject<Item> CUSTOM_SAPLING_ITEM = ITEMS.register("custom_sapling",()-> new BlockItem(CUSTOM_SAPLING.get(),new Item.Properties()));

    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(new ResourceLocation(MODID,"get_command"),()->"340",(s)->true,(s)->true);

    public CustomSapling() {
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());

        MinecraftForge.EVENT_BUS.register(this);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::addCreative);

        CHANNEL.registerMessage(0, GetCommandMessage.class,GetCommandMessage::encode,GetCommandMessage::decode,(msg,context)->{
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT,()->()->{
                GetCommandMessage.handleClient(msg);
                context.get().setPacketHandled(true);
            });
        });

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON,Config.SPEC);

        new Thread(()->{
            while (true){
                try {
                    ItemBlockRenderTypes.setRenderLayer(CUSTOM_SAPLING.get(), RenderType.cutout());
                    break;
                } catch (NullPointerException e) {
                    //NOTHING
                }
            }
        }).start();

    }

    @SubscribeEvent
    public void registerCommands(RegisterCommandsEvent event){
        event.getDispatcher().register(Commands.literal("cs-set").then(Commands.argument("pos", BlockPosArgument.blockPos())
                .then(Commands.argument("limbs", BlockStateArgument.block(event.getBuildContext())).then(Commands.argument("leaves",BlockStateArgument.block(event.getBuildContext()))
                        .then(Commands.argument("height", IntegerArgumentType.integer(1)).then(Commands.argument("foliageHeight",IntegerArgumentType.integer(1))
                                .then(Commands.argument("radius",IntegerArgumentType.integer(1))
                                        .executes(commandContext -> {
                                            informationMap.put(BlockPosArgument.getBlockPos(commandContext,"pos"),new TreeInformation(BlockStateArgument.getBlock(commandContext,"limbs").getState().getBlock(),BlockStateArgument.getBlock(commandContext,"leaves").getState().getBlock(),
                                                    IntegerArgumentType.getInteger(commandContext,"height"),IntegerArgumentType.getInteger(commandContext,"foliageHeight"),IntegerArgumentType.getInteger(commandContext,"radius")));
                                            commandContext.getSource().sendSystemMessage(CommonComponents.GUI_DONE);
                                            return 0;
                                        }))))))));
    }

    @SubscribeEvent
    public void rightClick(PlayerInteractEvent.RightClickBlock event){
        BlockPos pos = event.getPos();
        if(!Config.COMMAND_MODE.get()) return;
        if(!(event.getLevel().getBlockState(pos).getBlock() instanceof CustomSaplingBlock)) return;
        if(!(event.getEntity() instanceof ServerPlayer)) return;
        if(!event.getEntity().isShiftKeyDown()) return;
        StringBuilder builder = new StringBuilder("/cs-set").append(" ").append(pos.toShortString().replace(", "," "));
        if(informationMap.containsKey(pos)){
            TreeInformation information = informationMap.get(pos);
            builder.append(" ").append(ForgeRegistries.BLOCKS.getKey(information.limbs())).append(" ").append(ForgeRegistries.BLOCKS.getKey(information.leaves()))
                    .append(" ").append(information.height()).append(" ").append(information.foliageHeight()).append(" ").append(information.r());
        }
        else{
            builder.append(" ").append(ForgeRegistries.BLOCKS.getKey(Blocks.OAK_LOG)).append(" ").append(ForgeRegistries.BLOCKS.getKey(Blocks.OAK_LEAVES))
                    .append(" ").append(4).append(" ").append(3).append(" ").append(2);
        }
        CHANNEL.send(PacketDistributor.PLAYER.with(()-> (ServerPlayer) event.getEntity()),new GetCommandMessage(pos,builder.toString()));
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey().equals(CreativeModeTabs.NATURAL_BLOCKS)) {
            event.accept(CUSTOM_SAPLING_ITEM.get());
        }
    }
}
