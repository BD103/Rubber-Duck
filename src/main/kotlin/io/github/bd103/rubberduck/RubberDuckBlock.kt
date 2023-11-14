package io.github.bd103.rubberduck

import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.*
import net.minecraft.block.piston.PistonBehavior
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.fluid.FluidState
import net.minecraft.fluid.Fluids
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
import net.minecraft.world.WorldAccess

private val settings = FabricBlockSettings.create()
    .nonOpaque()
    .sounds(BlockSoundGroup.WOOL)
    .strength(0.2F)
    .mapColor(MapColor.YELLOW)
    .pistonBehavior(PistonBehavior.DESTROY)

object RubberDuckBlock : Block(settings), Waterloggable {
    init {
        // Set the default rotation to north
        defaultState = defaultState
            .with(Properties.HORIZONTAL_FACING, Direction.NORTH)
            .with(Properties.WATERLOGGED, false)
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        // Configure the duck to have a rotation property
        builder.add(Properties.HORIZONTAL_FACING, Properties.WATERLOGGED)
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState {
        // Make the duck rotate to face the player when placed
        return super.getPlacementState(ctx)!!
            .with(Properties.HORIZONTAL_FACING, ctx.horizontalPlayerFacing?.opposite)
            .with(Properties.WATERLOGGED, ctx.world.getFluidState(ctx.blockPos).isOf(Fluids.WATER))
    }

    // Render water overtop of duck when waterlogged
    override fun getFluidState(state: BlockState): FluidState = if (state.get(Properties.WATERLOGGED)) {
        Fluids.WATER.getStill(false)
    } else {
        super.getFluidState(state)
    }

    // Make water flow when neighbor blocks are broken
    override fun getStateForNeighborUpdate(
        state: BlockState,
        direction: Direction,
        neighborState: BlockState,
        world: WorldAccess,
        pos: BlockPos,
        neighborPos: BlockPos,
    ): BlockState {
        if (state.get(Properties.WATERLOGGED)) {
            world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world))
        }

        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos)
    }

    // Return the correct outline depending on rotation
    override fun getOutlineShape(
        state: BlockState,
        world: BlockView,
        pos: BlockPos,
        context: ShapeContext,
    ): VoxelShape {
        val direction = state.get(Properties.HORIZONTAL_FACING) ?: return VoxelShapes.fullCube()

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
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hand: Hand,
        hit: BlockHitResult,
    ): ActionResult {
        // Sound events are played on the server
        if (!world.isClient) {
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
