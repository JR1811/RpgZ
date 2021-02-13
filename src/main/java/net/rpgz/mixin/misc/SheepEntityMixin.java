package net.rpgz.mixin.misc;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.rpgz.access.AddingInventoryItems;

@Mixin(SheepEntity.class)
public abstract class SheepEntityMixin extends AnimalEntity {
    public SheepEntityMixin(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public void dropLoot(DamageSource source, boolean causedByPlayer) {
        super.dropLoot(source, causedByPlayer);
        if ((Object) this instanceof SheepEntity) {
            SheepEntity sheepEntity = (SheepEntity) (Object) this;
            ((AddingInventoryItems) this)
                    .addingInventoryItems(new ItemStack(sheepEntity.DROPS.get(sheepEntity.getColor())));
        }

    }
}
