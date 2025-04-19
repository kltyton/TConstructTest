package com.kltyton.test.modifiers;

import com.kltyton.test.Test;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeHitModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.UUID;

public class AntihemostaticTabletModifier extends Modifier implements MeleeHitModifierHook {
    // 静血层数的持久化存储键
    private static final ResourceLocation ANTIHEMOSTATIC_STACKS_KEY =
            new ResourceLocation(Test.MODID, "antihemostatic_stacks");
    // 生命上限修饰器的唯一UUID（根据你的mod修改）
    private static final UUID HEALTH_REDUCTION_UUID =
            UUID.fromString("a1b2c3d4-e5f6-4789-9a10-b11c12d13e14");

    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        hookBuilder.addHook(this, ModifierHooks.MELEE_HIT); // 注册近战攻击钩子
    }

    @Override
    public void afterMeleeHit(
            IToolStackView tool,
            ModifierEntry modifier,
            ToolAttackContext context,
            float damageDealt
    ) {
        LivingEntity target = context.getLivingTarget();
        LivingEntity attacker = context.getAttacker();
        if (target == null || attacker == null || target.level().isClientSide()) return;

        // --- 静血层数处理 ---
        int currentStacks = target.getPersistentData().getInt(ANTIHEMOSTATIC_STACKS_KEY.toString());
        if (currentStacks < 4) {
            currentStacks++; // 叠加层数（最多4层）
            target.getPersistentData().putInt(ANTIHEMOSTATIC_STACKS_KEY.toString(), currentStacks);
            applyHealthModifier(target, currentStacks); // 更新生命上限
        }

        // --- 给予攻击者再生效果（2秒，等级II）---
        attacker.addEffect(new MobEffectInstance(
                MobEffects.REGENERATION,
                40,   // 40 ticks = 2秒
                1     // 等级II（原版中1对应II级）
        ));
    }

    // 应用生命上限减少修饰器
    private void applyHealthModifier(LivingEntity target, int stacks) {
        AttributeInstance maxHealthAttr = target.getAttribute(Attributes.MAX_HEALTH);
        if (maxHealthAttr == null) return;

        // 移除旧修饰器（避免重复叠加）
        maxHealthAttr.removeModifier(HEALTH_REDUCTION_UUID);

        // 计算减少比例（每层-10%，乘法叠加）
        double reduction = -0.10 * stacks; // 4层时减少40%
        AttributeModifier modifier = new AttributeModifier(
                HEALTH_REDUCTION_UUID,
                "antihemostatic_health_reduction",
                reduction,
                AttributeModifier.Operation.MULTIPLY_TOTAL // 总生命值乘以 (1 + reduction)
        );

        // 应用新修饰器
        maxHealthAttr.addPermanentModifier(modifier);

        // 确保生命值不超过新上限
        if (target.getHealth() > target.getMaxHealth()) {
            target.setHealth(target.getMaxHealth());
        }
    }
}
