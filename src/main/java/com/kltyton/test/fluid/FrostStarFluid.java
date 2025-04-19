package com.kltyton.test.fluid;

import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.fluids.ForgeFlowingFluid;

public abstract class FrostStarFluid extends ForgeFlowingFluid {
    protected FrostStarFluid(Properties properties) {
        super(properties);
    }

    public static class Source extends FrostStarFluid {
        public Source(Properties properties) {
            super(properties);
        }
        @Override public boolean isSource(FluidState state) { return true; }
        @Override public int getAmount(FluidState state) { return 8; }
    }

    public static class Flowing extends FrostStarFluid {
        public Flowing(Properties properties) {
            super(properties);
        }
        @Override protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> builder) {
            super.createFluidStateDefinition(builder);
            builder.add(LEVEL);
        }
        @Override public int getAmount(FluidState state) { return state.getValue(LEVEL); }
        @Override public boolean isSource(FluidState state) { return false; }
    }
}
