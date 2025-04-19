package com.kltyton.test.modifiers;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.armor.EquipmentChangeModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.armor.ModifyDamageModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.behavior.ToolDamageModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.EquipmentChangeContext;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import javax.annotation.Nullable;

public class FirstLifeAlloyModifier extends Modifier implements
        ToolDamageModifierHook,
        EquipmentChangeModifierHook,
        ModifyDamageModifierHook {

    private static final MobEffectInstance REGENERATION = new MobEffectInstance(
            MobEffects.REGENERATION,
            100,  // 5 seconds (20 ticks/second * 5)
            1,     // Amplifier 2 (因为0是等级1)
            true,  // 环境粒子效果
            true   // 显示图标
    );

    // 工具无法被破坏
    @Override
    public int onDamageTool(IToolStackView tool, ModifierEntry modifier, int amount, @Nullable LivingEntity holder) {
        return 0; // 阻止所有耐久损失
    }

    // 装备时给予再生效果
    @Override
    public void onEquip(IToolStackView tool, ModifierEntry modifier, EquipmentChangeContext context) {
        LivingEntity entity = context.getEntity();
        if (entity != null && !tool.isBroken()) {
            entity.addEffect(new MobEffectInstance(REGENERATION));
        }
    }

    // 卸下时移除再生效果
    @Override
    public void onUnequip(IToolStackView tool, ModifierEntry modifier, EquipmentChangeContext context) {
        LivingEntity entity = context.getEntity();
        if (entity != null && entity.hasEffect(MobEffects.REGENERATION)) {
            entity.removeEffect(MobEffects.REGENERATION);
        }
    }

    // 伤害减免30%
    @Override
    public float modifyDamageTaken(IToolStackView tool, ModifierEntry modifier, EquipmentContext context,
                                   EquipmentSlot slotType, DamageSource source, float amount, boolean isDirectDamage) {
        return amount * 0.7f; // 减少30%伤害
    }

    // 注册所需的钩子
// 在 FirstLifeAlloyModifier.java 中修正注册部分
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        // 使用 ModifierHooks 中定义的静态常量
        hookBuilder.addHook(this, ModifierHooks.TOOL_DAMAGE);       // 对应 ToolDamageModifierHook
        hookBuilder.addHook(this, ModifierHooks.EQUIPMENT_CHANGE);  // 对应 EquipmentChangeModifierHook
        hookBuilder.addHook(this, ModifierHooks.MODIFY_DAMAGE);     // 对应 ModifyDamageModifierHook
    }

}