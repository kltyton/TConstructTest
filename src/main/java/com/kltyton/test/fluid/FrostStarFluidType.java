package com.kltyton.test.fluid;

import com.kltyton.test.Test;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidType;
import java.util.function.Consumer;

public class FrostStarFluidType extends FluidType {
    public FrostStarFluidType(Properties properties) {
        super(properties);
    }

    @Override
    public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
        consumer.accept(new IClientFluidTypeExtensions() {
            @Override
            public ResourceLocation getStillTexture() {
                return new ResourceLocation(Test.MODID, "block/fluid_still");
            }

            @Override
            public ResourceLocation getFlowingTexture() {
                return new ResourceLocation(Test.MODID, "block/fluid_flow");
            }

            @Override
            public int getTintColor() {
                return 0xFF4AC9FF; // 淡蓝色ARGB颜色
            }
        });
    }
}