package io.github.bd103.rubberduck

import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.ShapeContext
import net.minecraft.item.ItemPlacementContext
import net.minecraft.state.StateManager
import net.minecraft.state.property.Properties
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView

private val settings = FabricBlockSettings.create().nonOpaque()

object RubberDuckBlock : Block(settings) {
    init {
        defaultState = defaultState.with(Properties.HORIZONTAL_FACING, Direction.NORTH)
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>?) {
        builder?.add(Properties.HORIZONTAL_FACING)
    }

    override fun getPlacementState(ctx: ItemPlacementContext?): BlockState? {
        return super.getPlacementState(ctx)?.with(Properties.HORIZONTAL_FACING, ctx?.horizontalPlayerFacing?.opposite)
    }

    override fun getOutlineShape(
        state: BlockState?,
        world: BlockView?,
        pos: BlockPos?,
        context: ShapeContext?
    ): VoxelShape {
        val direction = state?.get(Properties.HORIZONTAL_FACING) ?: return VoxelShapes.fullCube()

        // TODO(BD103): More elegant solution than hardcoding duck outlines
        return when (direction) {
            Direction.SOUTH -> VoxelShapes.cuboid(
                5.0 / 16.0, 0.0 / 16.0, 5.0 / 16.0,
                11.0 / 16.0, 8.0 / 16.0, 15.0 / 16.0,
            )

            Direction.EAST -> VoxelShapes.cuboid(
                5.0 / 16.0, 0.0 / 16.0, 5.0 / 16.0,
                15.0 / 16.0, 8.0 / 16.0, 11.0 / 16.0,
            )

            Direction.WEST -> VoxelShapes.cuboid(
                1.0 / 16.0, 0.0 / 16.0, 5.0 / 16.0,
                11.0 / 16.0, 8.0 / 16.0, 11.0 / 16.0,
            )

            // North, and every other invalid state
            else -> VoxelShapes.cuboid(
                5.0 / 16.0, 0.0 / 16.0, 1.0 / 16.0,
                11.0 / 16.0, 8.0 / 16.0, 11.0 / 16.0,
            )
        }
    }
}