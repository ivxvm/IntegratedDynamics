package org.cyclops.integrateddynamics.core.helper;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Helper methods related to items.
 * @author rubensworks
 */
public final class WrenchHelpers {

    public static final ResourceLocation TAG_WRENCH = new ResourceLocation("forge", "wrench");

    /**
     * Checks if the given player can wrench something.
     * @param player The player.
     * @param heldItem The item the player is holding.
     * @param world The world in which the wrenching is happening.
     * @param pos The position that is being wrenched.
     * @param side The side that is being wrenched.
     * @return If the wrenching can continue with the held item.
     */
    public static boolean isWrench(PlayerEntity player, ItemStack heldItem, World world, BlockPos pos, @Nullable Direction side) {
        return heldItem.getItem().getTags().contains(WrenchHelpers.TAG_WRENCH);
    }

    /**
     * Wrench a given position.
     * Requires the {@link WrenchHelpers#isWrench(PlayerEntity, ItemStack, World, BlockPos, Direction)}
     * to be passed.
     * Takes an extra parameter of any type that is forwarded to the wrench action.
     * @param player The player.
     * @param heldItem The item the player is holding.
     * @param world The world in which the wrenching is happening.
     * @param pos The position that is being wrenched.
     * @param side The side that is being wrenched.
     * @param action The actual wrench action.
     * @param parameter An extra parameter that is forwarded to the action.
     * @param <P> The type of parameter to pass.
     */
    public static <P> void wrench(PlayerEntity player, ItemStack heldItem, World world, BlockPos pos, Direction side, IWrenchAction<P> action, P parameter) {
        if (isWrench(player, heldItem, world, pos, side)) {
            action.onWrench(player, pos, parameter);
        }
    }

    /**
     * Wrench a given position.
     * Requires the {@link WrenchHelpers#isWrench(PlayerEntity, ItemStack, World, BlockPos, Direction)}
     * to be passed.
     * @param player The player.
     * @param heldItem The item the player is holding.
     * @param world The world in which the wrenching is happening.
     * @param pos The position that is being wrenched.
     * @param side The side that is being wrenched.
     * @param action The actual wrench action.
     */
    public static void wrench(PlayerEntity player, ItemStack heldItem, World world, BlockPos pos, Direction side, IWrenchAction<Void> action) {
        wrench(player, heldItem, world, pos, side, action, null);
    }

    /**
     * An action that can serve as wrenching.
     * @param <P> The type of parameter that is passed.
     */
    public static interface IWrenchAction<P> {

        /**
         * Called for the actual wrenching action.
         * @param player The player.
         * @param pos The position that is being wrenched.
         * @param parameter An extra parameter that is used to call this action.
         */
        public void onWrench(PlayerEntity player, BlockPos pos, P parameter);

    }

    /**
     * An action that can serve as wrenching.
     */
    public static abstract class SimpleWrenchAction implements IWrenchAction<Void> {

        /**
         * Called for the actual wrenching action.
         * @param player The player.
         * @param pos The position that is being wrenched.
         * @param parameter An extra parameter that is used to call this action.
         */
        public void onWrench(PlayerEntity player, BlockPos pos, Void parameter) {
            onWrench(player, pos);
        }

        /**
         * Called for the actual wrenching action.
         * @param player The player.
         * @param pos The position that is being wrenched.
         */
        public abstract void onWrench(PlayerEntity player, BlockPos pos);

    }

}
