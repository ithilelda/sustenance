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
    private float saturationHealFix(float v1, float v2)
    {
        return 6.0f;
    }

    @ModifyConstant(method = "update(Lnet/minecraft/entity/player/PlayerEntity;)V", constant = @Constant(intValue = 10))
    private int fastHealInterval(int value)
    {
        return 5;
    }

    @ModifyConstant(method = "update(Lnet/minecraft/entity/player/PlayerEntity;)V", constant = @Constant(intValue = 20))
    private int fastHealThreshold(int value)
    {
        return 20;
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
        return 20;
    }

    @ModifyConstant(method = "update(Lnet/minecraft/entity/player/PlayerEntity;)V", constant = @Constant(intValue = 18))
    private int slowHealThreshold(int value)
    {
        return 18;
    }
}
