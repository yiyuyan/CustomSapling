package cn.ksmcbrigade.cs.tree.configuration;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.FeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import org.jetbrains.annotations.NotNull;

public class CustomTreeConfigurationBuilder extends TreeConfiguration.TreeConfigurationBuilder {

    private Block leaves,limbs;

    public CustomTreeConfigurationBuilder(Block limbs, TrunkPlacer p_191360_, Block leaves, FoliagePlacer p_191362_, FeatureSize p_191363_) {
        super(BlockStateProvider.simple(limbs), p_191360_, BlockStateProvider.simple(leaves), p_191362_, p_191363_);
        this.limbs = limbs;
        this.leaves = leaves;
    }

    public Block getLeaves() {
        return leaves;
    }

    public Block getLimbs() {
        return limbs;
    }

    public void setLeaves(Block leaves) {
        this.leaves = leaves;
        this.trunkProvider = BlockStateProvider.simple(leaves);
    }

    public void setLimbs(Block limbs) {
        this.limbs = limbs;
        this.foliageProvider = BlockStateProvider.simple(this.limbs);
    }

    @Override
    public @NotNull TreeConfiguration build() {
        return new CustomTreeConfiguration(this.leaves,this.limbs,this.trunkProvider, this.trunkPlacer, this.foliageProvider, this.foliagePlacer, this.rootPlacer, this.dirtProvider, this.minimumSize, this.decorators, this.ignoreVines, this.forceDirt);
    }

    @Override
    public String toString() {
        return "CustomTreeConfigurationBuilder{" +
                "leaves=" + leaves +
                ", limbs=" + limbs +
                ", trunkProvider=" + trunkProvider +
                ", trunkPlacer=" + trunkPlacer +
                ", foliageProvider=" + foliageProvider +
                ", foliagePlacer=" + foliagePlacer +
                ", rootPlacer=" + rootPlacer +
                ", dirtProvider=" + dirtProvider +
                ", minimumSize=" + minimumSize +
                ", decorators=" + decorators +
                ", ignoreVines=" + ignoreVines +
                ", forceDirt=" + forceDirt +
                '}';
    }
}
