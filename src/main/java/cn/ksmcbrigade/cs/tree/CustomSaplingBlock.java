package cn.ksmcbrigade.cs.tree;

import cn.ksmcbrigade.cs.Config;
import cn.ksmcbrigade.cs.CustomSapling;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class CustomSaplingBlock extends SaplingBlock {
    public CustomSaplingBlock(AbstractTreeGrower p_55978_, Properties p_55979_) {
        super(p_55978_, p_55979_);
    }

    public AbstractTreeGrower getGrower(Level level, BlockPos pos){
        if(Config.COMMAND_MODE.get()){
            if(CustomSapling.informationMap.containsKey(pos)) return CustomSapling.informationMap.get(pos).createGrower();
        }
        else{
            Block limbs = level.getBlockState(pos.below()).getBlock(),leaves = level.getBlockState(pos.below(2)).getBlock();
            return new TreeInformation(limbs,leaves,6,3,2).createGrower();
        }
        return this.treeGrower;
    }

    @Override
    public void advanceTree(@NotNull ServerLevel p_222001_, @NotNull BlockPos p_222002_, BlockState p_222003_, @NotNull RandomSource p_222004_) {
        if (p_222003_.getValue(STAGE) == 0) {
            p_222001_.setBlock(p_222002_, (BlockState)p_222003_.cycle(STAGE), 4);
        } else {
            if(this.getGrower(p_222001_,p_222002_).growTree(p_222001_, p_222001_.getChunkSource().getGenerator(), p_222002_, p_222003_, p_222004_)){
                CustomSapling.informationMap.remove(p_222002_);
            }
        }
    }

    @Override
    protected boolean mayPlaceOn(@NotNull BlockState p_51042_, @NotNull BlockGetter p_51043_, @NotNull BlockPos p_51044_) {
        return true;
    }
}
