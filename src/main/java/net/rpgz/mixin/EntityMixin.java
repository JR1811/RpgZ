package net.rpgz.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.text.LiteralText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Nameable;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.entity.EntityLike;
import net.rpgz.access.InventoryAccess;
import net.rpgz.ui.LivingEntityScreenHandler;
import net.shirojr.nemuelch.util.NeMuelchTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin implements Nameable, EntityLike, CommandOutput, InventoryAccess {
    @Unique
    public SimpleInventory inventory = new SimpleInventory(9);
    @Shadow
    public World world;

    /**
     * Added additional ItemStack check from  to help out with the body drag feature from
     * the <a href="https://github.com/JR1811/NeMuelch-1.18">NeMuelch-1.18</a> Fabric Mod.<br><br>
     * For a more specific location of the interaction between <b><i>NeMuelch-1.18</i></b> and <b><i>RpgZ</i></b> compare
     * this mixin method with the
     * <a href="https://github.com/JR1811/NeMuelch-1.18/blob/17d92d16372d93b63856113589a8acc5a69af972/src/main/java/net/shirojr/nemuelch/mixin/ItemMixin.java#L31-L34">corresponding NeMuelch feature</a>.
     */
    @Inject(method = "interactAt", at = @At(value = "HEAD"), cancellable = true)
    private void rpgz$interactAt(PlayerEntity player, Vec3d hitPos, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        if (!((Entity) (Object) this instanceof LivingEntity targetEntity)) return;

        if (targetEntity.deathTime <= 20) return;
        if (this.world.isClient) cir.setReturnValue(ActionResult.SUCCESS);

        if (!this.inventory.isEmpty() || !player.getStackInHand(hand).isIn(NeMuelchTags.Items.PULL_BODY_TOOLS)) {
            //if (!this.inventory.isEmpty() || !(player.getStackInHand(hand).getItem() instanceof SnowballItem)) {
            if (player.isSneaking()) {
                for (int i = 0; i < this.inventory.size(); i++) {
                    ItemStack stack = this.inventory.getStack(i);
                    player.getInventory().offerOrDrop(stack);
                }
                this.inventory.clear();
            } else
                player.openHandledScreen(new SimpleNamedScreenHandlerFactory((syncId, inv, p) ->
                        new LivingEntityScreenHandler(syncId, p.getInventory(), this.inventory),
                        new LiteralText("")));
            cir.setReturnValue(ActionResult.SUCCESS);
        } else if (targetEntity instanceof PlayerEntity) return;
    }

    @Override
    public void rpgz$addingInventoryItems(ItemStack stack) {
        if (!this.world.isClient && !stack.isEmpty())
            this.inventory.addStack(stack);
    }

    @Override
    public SimpleInventory rpgz$getInventory() {
        return this.inventory;
    }
}
