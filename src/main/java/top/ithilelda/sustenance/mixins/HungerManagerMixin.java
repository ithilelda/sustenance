package top.ithilelda.sustenance.mixins;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.player.HungerManager;

import net.minecraft.server.network.ServerPlayerEntity;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(HungerManager.class)
public class HungerManagerMixin
{
    // set the upper limit of saturation to almost infinity, regardless of the food level.
    @Redirect(method = "addInternal(IF)V", at = @At(value = "FIELD", target="net/minecraft/entity/player/HungerManager.foodLevel:I", opcode = Opcodes.GETFIELD, ordinal = 1))
    private int addWithoutSaturationLimit(HungerManager instance)
    {
        return Integer.MAX_VALUE;
    }
    // Can always eat food if your saturation level is lower than 20.
    @ModifyReturnValue(method = "isNotFull", at = @At("RETURN"))
    private boolean isNotSaturated(boolean original)
    {
        HungerManager hm = (HungerManager) (Object) this;
        return hm.getSaturationLevel() < 20.0F;
    }

    // as long as you have saturation, you are gonna fast heal regardless of food level.
    @ModifyConstant(method = "update(Lnet/minecraft/server/network/ServerPlayerEntity;)V", constant = @Constant(intValue = 20))
    private int fastHealThreshold(int value) { return 0; }

    // increase fast heal interval to each tick.
    @ModifyConstant(method = "update(Lnet/minecraft/server/network/ServerPlayerEntity;)V", constant = @Constant(intValue = 10))
    private int fastHealInterval(int value)
    {
        return 1;
    }

    // when you have saturation, each fast heal will always add 4 exhaustion, resulting in 1 saturation decrease.
    // meaning that 1 saturation = 1 point of health restored.
    @ModifyExpressionValue(method = "update(Lnet/minecraft/server/network/ServerPlayerEntity;)V", at = @At(value = "INVOKE", target = "Ljava/lang/Math;min(FF)F"))
    private float fastHealExhaustionLevel(float original) { return 4.0F; }

    // player will always heal 1 point when fast healing.
    @WrapOperation(method = "update(Lnet/minecraft/server/network/ServerPlayerEntity;)V", at = @At(value = "INVOKE", target = "net/minecraft/server/network/ServerPlayerEntity.heal(F)V"))
    private void fastHealPlayer(ServerPlayerEntity instance, float v, Operation<Void> original)
    {
        original.call(instance, 1.0F);
    }

    // slow heal will occur when food level is higher than 10.
    @ModifyConstant(method = "update(Lnet/minecraft/server/network/ServerPlayerEntity;)V", constant = @Constant(intValue = 18))
    private int slowHealThreshold(int value) { return 10; }
}
