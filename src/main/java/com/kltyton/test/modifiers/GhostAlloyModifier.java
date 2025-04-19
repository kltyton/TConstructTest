package com.kltyton.test.modifiers;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.damagesource.DamageSource;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.behavior.AttributesModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.build.ToolStatsModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.combat.DamageDealtModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeDamageModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

import java.util.UUID;
import java.util.function.BiConsumer;

public class GhostAlloyModifier extends Modifier implements
        AttributesModifierHook,
        ToolStatsModifierHook,
        MeleeDamageModifierHook,
        DamageDealtModifierHook {

    // 属性修饰符UUID（确保唯一性）
    private static final UUID ARMOR_UUID = UUID.fromString("a1b2c3d4-e5f6-4789-9a10-b11c12d13e15");
    private static final UUID TOUGHNESS_UUID = UUID.fromString("a1b2c3d4-e5f6-4789-9a10-b11c12d13e16");

    // 吸血比例
    private static final float LIFE_STEAL_RATIO = 0.6f;

    // 增加60%护甲和韧性（属性修改）
    @Override
    public void addAttributes(IToolStackView tool, ModifierEntry entry,
                              EquipmentSlot slot, BiConsumer<Attribute, AttributeModifier> consumer) {
        if (slot.getType() == EquipmentSlot.Type.ARMOR) {
            // 护甲值增加60%（乘法叠加）
            consumer.accept(Attributes.ARMOR, new AttributeModifier(
                    ARMOR_UUID,
                    "tconstruct.ghost_alloy.armor",
                    0.6f,
                    AttributeModifier.Operation.MULTIPLY_TOTAL
            ));

            // 韧性增加60%
            consumer.accept(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(
                    TOUGHNESS_UUID,
                    "tconstruct.ghost_alloy.toughness",
                    0.6f,
                    AttributeModifier.Operation.MULTIPLY_TOTAL
            ));
        }
    }

    // 增加基础攻击力60%（工具属性修改）
    @Override
    public void addToolStats(IToolContext context, ModifierEntry entry, ModifierStatsBuilder builder) {
        // 正确方式：直接添加60%攻击伤害加成（乘法叠加）
        ToolStats.ATTACK_DAMAGE.add(builder, 0.6f); // 参数为乘数 1.0=100%
    }

    @Override
    public float getMeleeDamage(IToolStackView tool, ModifierEntry entry,
                                ToolAttackContext context, float baseDamage, float damage) {
        return damage * 1.6f; // 最终伤害再增加60%
    }

    // 吸血逻辑
    @Override
    public void onDamageDealt(IToolStackView tool, ModifierEntry entry, EquipmentContext context,
                              EquipmentSlot slot, LivingEntity target, DamageSource source,
                              float amount, boolean isDirectDamage) {
        if (isDirectDamage && amount > 0) {
            LivingEntity attacker = context.getEntity();
            if (attacker != null) {
                // 计算吸血量（实际造成伤害的60%）
                float healAmount = amount * LIFE_STEAL_RATIO;
                attacker.heal(healAmount);
            }
        }
    }

    // 注册钩子
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        hookBuilder.addHook(this, ModifierHooks.ATTRIBUTES)
                .addHook(this, ModifierHooks.TOOL_STATS)
                .addHook(this, ModifierHooks.MELEE_DAMAGE)
                .addHook(this, ModifierHooks.DAMAGE_DEALT);
    }
}