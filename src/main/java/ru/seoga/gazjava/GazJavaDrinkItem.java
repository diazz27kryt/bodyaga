package ru.seoga.gazjava;

import java.util.List;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.UseAction;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;

public class GazJavaDrinkItem extends Item {
    private static final List<String> POSITIVE_EFFECTS = List.of(
        "speed", "haste", "strength", "health_boost", "regeneration",
        "resistance", "fire_resistance", "water_breathing", "invisibility", "night_vision",
        "jump_boost", "absorption", "slow_falling", "conduit_power",
        "dolphins_grace", "hero_of_the_village"
    );
    private final int durationSeconds;

    public GazJavaDrinkItem(Settings settings, int durationSeconds) {
        super(settings);
        this.durationSeconds = durationSeconds;
    }

    @Override public UseAction getUseAction(ItemStack stack) { return UseAction.DRINK; }
    @Override public int getMaxUseTime(ItemStack stack, LivingEntity user) { return 32; }

    @Override public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        user.setCurrentHand(hand);
        return TypedActionResult.consume(user.getStackInHand(hand));
    }

    @Override public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (!world.isClient) {
            int ticks = durationSeconds * 20;
            for (String effectId : POSITIVE_EFFECTS) {
                Registries.STATUS_EFFECT.getEntry(Identifier.ofVanilla(effectId)).ifPresent(effect ->
                    user.addStatusEffect(new StatusEffectInstance(effect, ticks, 0, false, true, true))
                );
            }
            user.heal(8.0F);
            if (user instanceof PlayerEntity player) player.getHungerManager().add(20, 1.0F);
        }
        if (user instanceof PlayerEntity player && !player.getAbilities().creativeMode) {
            stack.decrement(1);
            ItemStack bottle = new ItemStack(Items.GLASS_BOTTLE);
            if (stack.isEmpty()) return bottle;
            if (!player.getInventory().insertStack(bottle)) player.dropItem(bottle, false);
        }
        return stack;
    }
}
