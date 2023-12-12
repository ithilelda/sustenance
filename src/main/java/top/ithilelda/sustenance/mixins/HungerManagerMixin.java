package top.ithilelda.sustenance.mixins;

import net.minecraft.entity.player.HungerManager;

import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.ithilelda.sustenance.Sustenance;

@Mixin(HungerManager.class)
public class HungerManagerMixin
{
    @Inject(method = "add(IF)V", at = @At("HEAD"), cancellable = true)
    private void add(int food, float saturationModifier, CallbackInfo info)
    {
        HungerManager manager = (HungerManager)(Object)this;
        int curFood = manager.getFoodLevel() + food;
        float curSat = manager.getSaturationLevel() + (float)food * saturationModifier * 2.0f;
        manager.setFoodLevel(Math.min(curFood, 20));
        manager.setSaturationLevel(curSat);
        info.cancel();
    }

    @Redirect(method = "update(Lnet/minecraft/entity/player/PlayerEntity;)V", at = @At(value = "INVOKE", target = "Ljava/lang/Math;min(FF)F"))
    private float getFastHealExhaustionLevel(float v1, float v2)
    {
        return Sustenance.Config.fastHealExhaustionLevel;
    }

    @ModifyConstant(
            method = "update(Lnet/minecraft/entity/player/PlayerEntity;)V",
            constant = @Constant(floatValue = 6.0F),
            slice = @Slice(
                    from = @At(value = "INVOKE", target = "Ljava/lang/Math;min(FF)F"),
                    to = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;heal(F)V")
            ))
    private float fastHealExhaustionRatio(float value)
    {
        return Sustenance.Config.fastHealExhaustionRatio;
    }

    @ModifyConstant(method = "update(Lnet/minecraft/entity/player/PlayerEntity;)V", constant = @Constant(intValue = 10))
    private int fastHealInterval(int value)
    {
        return Sustenance.Config.fastHealInterval;
    }

    @ModifyConstant(method = "update(Lnet/minecraft/entity/player/PlayerEntity;)V", constant = @Constant(intValue = 20))
    private int fastHealThreshold(int value)
    {
        return 0; // as long as you have saturation, you are gonna fast heal regardless of food level.
    }

    @ModifyConstant(
        method = "update(Lnet/minecraft/entity/player/PlayerEntity;)V",
        constant = @Constant(intValue = 80),
        slice = @Slice(
            from = @At(value = "JUMP", opcode = Opcodes.GOTO, ordinal = 1),
            to = @At(value = "JUMP", opcode = Opcodes.GOTO, ordinal = 2)
    ))
    private int slowHealInterval(int value)
    {
        return Sustenance.Config.slowHealInterval;
    }

    @ModifyConstant(method = "update(Lnet/minecraft/entity/player/PlayerEntity;)V", constant = @Constant(intValue = 18))
    private int slowHealThreshold(int value)
    {
        return Sustenance.Config.slowHealThreshold; // food level above this will allow you to slow heal.
    }
}
