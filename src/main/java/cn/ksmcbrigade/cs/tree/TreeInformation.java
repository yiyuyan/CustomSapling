package cn.ksmcbrigade.cs.tree;

import cn.ksmcbrigade.cs.tree.configuration.CustomTreeConfigurationBuilder;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;

public record TreeInformation(Block limbs, Block leaves, int height, int foliageHeight, int r) {
    public TreeConfiguration createTreeConfiguration() {
        int h = 32,h_offset = 0;
        if(height<=32) h = height;
        if(height>32) h_offset = height-32;
        return new CustomTreeConfigurationBuilder(limbs, new StraightTrunkPlacer(h, h_offset,h_offset), leaves, new BlobFoliagePlacer(ConstantInt.of(r), ConstantInt.of(0), foliageHeight), new TwoLayersFeatureSize(1, 0, 1)).ignoreVines().build();
    }

    public CustomTreeGrower createGrower(){
        return new CustomTreeGrower(){
            @Override
            public TreeInformation getCustom() {
                return TreeInformation.this;
            }
        };
    }
}
