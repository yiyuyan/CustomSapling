package cn.ksmcbrigade.cs.tree;

import cn.ksmcbrigade.cs.CustomSapling;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CustomTreeGrower extends AbstractTreeGrower {
    @Nullable
    @Override
    protected ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(@NotNull RandomSource randomSource, boolean b) {
        return CustomSapling.CUSTOM_SAP;
    }

    @Override
    public boolean growTree(@NotNull ServerLevel p_222905_, @NotNull ChunkGenerator p_222906_, @NotNull BlockPos p_222907_, @NotNull BlockState p_222908_, @NotNull RandomSource p_222909_) {
        ResourceKey<ConfiguredFeature<?, ?>> resourcekey = this.getConfiguredFeature(p_222909_, this.hasFlowers(p_222905_, p_222907_));
        if (resourcekey == null) {
            return false;
        } else {
            ConfiguredFeature<?, ?> configuredfeature = CustomSapling.CONFIGURED_FEATURE;
            TreeInformation custom = getCustom();
            if(custom!=null) configuredfeature = new ConfiguredFeature<>(Feature.TREE,custom.createTreeConfiguration());
            BlockState blockstate = p_222905_.getFluidState(p_222907_).createLegacyBlock();
            p_222905_.setBlock(p_222907_, blockstate, 4);
            if (configuredfeature.place(p_222905_, p_222906_, p_222909_, p_222907_)) {
                if (p_222905_.getBlockState(p_222907_) == blockstate) {
                    p_222905_.sendBlockUpdated(p_222907_, p_222908_, blockstate, 2);
                }

                return true;
            } else {
                p_222905_.setBlock(p_222907_, p_222908_, 4);
                return false;
            }
        }
    }

    public TreeInformation getCustom(){
        return null;
    }
}
