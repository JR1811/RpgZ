package net.rpgz.mixin;

import net.minecraft.world.LightType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

import net.fabricmc.api.Environment;
import net.fabricmc.api.EnvType;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

@Environment(EnvType.CLIENT)
@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin<T extends Entity> {

    @Inject(method = "getLight", at = @At("TAIL"), cancellable = true)
    public final void getLightMixin(T entity, float tickDelta, CallbackInfoReturnable<Integer> info) {
        if (entity.isLiving() && entity instanceof MobEntity && ((MobEntity) entity).isDead()) {
            Box box = entity.getBoundingBox();
            BlockPos blockPos = new BlockPos(box.getCenter().getX(), box.maxY, box.getCenter().getZ());
            info.setReturnValue(LightmapTextureManager.pack(this.getBlockLight(entity, blockPos), this.getSkyLight(entity, blockPos)));
        }
    }

    @Shadow
    protected int getBlockLight(T entity, BlockPos blockPos) {
        return 0;
    }

    @Shadow
    protected int getSkyLight(T entity, BlockPos pos) {
        return entity.world.getLightLevel(LightType.SKY, pos);
    }

}
