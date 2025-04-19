package com.kltyton.test.modifiers;

import com.kltyton.test.Test;
import slimeknights.tconstruct.library.modifiers.util.ModifierDeferredRegister;
import slimeknights.tconstruct.library.modifiers.util.StaticModifier;


public class TestModifier {
    public static ModifierDeferredRegister MODIFIERS = ModifierDeferredRegister.create(Test.MODID);
    public static final StaticModifier<FrostStarModifier> frostStar = MODIFIERS.register("frost_stacks", FrostStarModifier::new);
    public static final StaticModifier<AntihemostaticTabletModifier> antihemostaticTablet = MODIFIERS.register("antihemostatic_stacks", AntihemostaticTabletModifier::new);
    public static final StaticModifier<FirstLifeAlloyModifier> firstLifeAlloy = MODIFIERS.register("first_life_alloy_stacks", FirstLifeAlloyModifier::new);
    public static final StaticModifier<GhostAlloyModifier> ghostAlloy = MODIFIERS.register("ghost_alloy_stacks", GhostAlloyModifier::new);
}
