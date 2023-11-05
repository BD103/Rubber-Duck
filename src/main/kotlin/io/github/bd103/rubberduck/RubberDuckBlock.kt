package io.github.bd103.rubberduck

import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.MapColor
import net.minecraft.block.ShapeContext
import net.minecraft.block.piston.PistonBehavior
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemPlacementContext
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.sound.SoundCategory
import net.minecraft.state.StateManager
import net.minecraft.state.property.Properties
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import net.minecraft.world.World

private val settings = FabricBlockSettings.create()
    .nonOpaque()
    .sounds(BlockSoundGroup.WOOL)
    .strength(0.2F)
    .mapColor(MapColor.YELLOW)
    .pistonBehavior(PistonBehavior.DESTROY)

object RubberDuckBlock : Block(settings) {
    init {
        // Set the default rotation to north
        defaultState = defaultState.with(Properties.HORIZONTAL_FACING, Direction.NORTH)
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>?) {
        // Configure the duck to have a rotation property
        builder?.add(Properties.HORIZONTAL_FACING)
    }

    override fun getPlacementState(ctx: ItemPlacementContext?): BlockState? {
        // Make the duck rotate to face the player when placed
        return super.getPlacementState(ctx)?.with(Properties.HORIZONTAL_FACING, ctx?.horizontalPlayerFacing?.opposite)
    }

    // Return the correct outline depending on rotation
    override fun getOutlineShape(
        state: BlockState?,
        world: BlockView?,
        pos: BlockPos?,
        context: ShapeContext?
    ): VoxelShape {
        val direction = state?.get(Properties.HORIZONTAL_FACING) ?: return VoxelShapes.fullCube()

        // TODO(BD103): More elegant solution than hardcoding duck outlines
        return when (direction) {
            Direction.SOUTH -> createCuboidShape(
                5.0, 0.0, 5.0,
                11.0, 8.0, 15.0
            )

            Direction.EAST -> createCuboidShape(
                5.0, 0.0, 5.0,
                15.0, 8.0, 11.0,
            )

            Direction.WEST -> createCuboidShape(
                1.0, 0.0, 5.0,
                11.0, 8.0, 11.0,
            )

            // North, and every other invalid state
            else -> createCuboidShape(
                5.0, 0.0, 1.0,
                11.0, 8.0, 11.0,
            )
        }
    }

    // Play squeak noise when right-clicked
    override fun onUse(
        state: BlockState?,
        world: World?,
        pos: BlockPos?,
        player: PlayerEntity?,
        hand: Hand?,
        hit: BlockHitResult?
    ): ActionResult {
        // Sound events are played on the server
        if (!world!!.isClient) {
            world.playSound(
                null,
                pos,
                RubberDuck.squeakSound,
                SoundCategory.BLOCKS,
                1.0F,
                1.0F,
            )
        }

        return ActionResult.SUCCESS
    }
}
