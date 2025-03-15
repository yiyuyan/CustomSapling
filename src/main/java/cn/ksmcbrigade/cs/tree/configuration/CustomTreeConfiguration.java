package cn.ksmcbrigade.cs.tree.configuration;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.FeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.rootplacers.RootPlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;

import java.util.List;
import java.util.Optional;

public class CustomTreeConfiguration extends TreeConfiguration {

    private Block leaves,limbs;

    protected CustomTreeConfiguration(Block leaves,Block limbs,BlockStateProvider p_225457_, TrunkPlacer p_225458_, BlockStateProvider p_225459_, FoliagePlacer p_225460_, Optional<RootPlacer> p_225461_, BlockStateProvider p_225462_, FeatureSize p_225463_, List<TreeDecorator> p_225464_, boolean p_225465_, boolean p_225466_) {
        super(p_225457_, p_225458_, p_225459_, p_225460_, p_225461_, p_225462_, p_225463_, p_225464_, p_225465_, p_225466_);
        this.leaves = leaves;
        this.limbs = limbs;
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
    public String toString() {
        return "CustomTreeConfiguration{" +
                "leaves=" + leaves +
                ", limbs=" + limbs +
                ", trunkProvider=" + trunkProvider +
                ", dirtProvider=" + dirtProvider +
                ", trunkPlacer=" + trunkPlacer +
                ", foliageProvider=" + foliageProvider +
                ", foliagePlacer=" + foliagePlacer +
                ", rootPlacer=" + rootPlacer +
                ", minimumSize=" + minimumSize +
                ", decorators=" + decorators +
                ", ignoreVines=" + ignoreVines +
                ", forceDirt=" + forceDirt +
                '}';
    }
}
