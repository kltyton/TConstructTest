package com.kltyton.test.modifiers;

import com.kltyton.test.Test;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeHitModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class FrostStarModifier extends Modifier implements MeleeHitModifierHook {

    private static final String FROST_STACKS_KEY = new ResourceLocation(Test.MODID, "frost_stacks").toString();

    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        hookBuilder.addHook(this, ModifierHooks.MELEE_HIT);
    }

    @Override
    public void afterMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damageDealt) {
        LivingEntity target = context.getLivingTarget();
        LivingEntity attacker = context.getAttacker();
        if (target == null || attacker == null || target.level().isClientSide()) return;

        // 施加基础迟缓效果（40 ticks，即2秒）
        target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 40, 0));

        // 处理寒意叠加
        int frostStacks = target.getPersistentData().getInt(FROST_STACKS_KEY) + 1;
        target.getPersistentData().putInt(FROST_STACKS_KEY, frostStacks);

        // 触发冰爆条件
        if (frostStacks >= 10) {
            triggerFrostExplosion(tool, target, attacker);
            target.getPersistentData().putInt(FROST_STACKS_KEY, 0); // 重置层数
        }
    }

    private void triggerFrostExplosion(IToolStackView tool, LivingEntity target, LivingEntity attacker) {
        Level level = target.level();
        float baseDamage = tool.getDamage();
        float explosionDamage = baseDamage * 1.5f;

        // 定义爆炸范围（半径3格）
        AABB explosionArea = new AABB(
                target.getX() - 3, target.getY() - 2, target.getZ() - 3,
                target.getX() + 3, target.getY() + 2, target.getZ() + 3
        );

        // 影响范围内所有实体
        level.getEntitiesOfClass(LivingEntity.class, explosionArea).forEach(entity -> {
            if (entity == attacker) return; // 不伤害攻击者

            // 造成范围伤害
            entity.hurt(level.damageSources().magic(), explosionDamage);

            // 施加高级迟缓（80 ticks即4秒，等级2）和挖掘疲劳模拟冰冻
            entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 80, 2));
            entity.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 80, 1));
        });

        // 客户端特效
        if (level.isClientSide()) {
            spawnExplosionParticles(target);
            playExplosionSound(target);
        }
    }

    // 冰爆粒子效果
    private void spawnExplosionParticles(LivingEntity target) {
        Level level = target.level();
        for (int i = 0; i < 30; i++) {
            double offsetX = (Math.random() - 0.5) * 3;
            double offsetY = Math.random() * 2;
            double offsetZ = (Math.random() - 0.5) * 3;
            level.addParticle(
                    net.minecraft.core.particles.ParticleTypes.SNOWFLAKE,
                    target.getX() + offsetX,
                    target.getY() + offsetY,
                    target.getZ() + offsetZ,
                    0, 0, 0
            );
        }
    }

    // 冰爆音效
    private void playExplosionSound(LivingEntity target) {
        target.level().playLocalSound(
                target.getX(), target.getY(), target.getZ(),
                net.minecraft.sounds.SoundEvents.GLASS_BREAK,
                net.minecraft.sounds.SoundSource.NEUTRAL,
                1.0f, 0.6f, false
        );
    }
}