package com.minecolonies.coremod.blocks;

import com.ldtteam.structurize.blocks.interfaces.IAnchorBlock;
import com.minecolonies.api.blocks.AbstractBlockMinecoloniesHorizontal;
import com.minecolonies.api.entity.ai.citizen.builder.IBuilderUndestroyable;
import com.minecolonies.coremod.MineColonies;
import com.minecolonies.coremod.tileentities.TileEntityDecorationController;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

/**
 * Creates a decoration placerholder block.
 */
public class BlockDecorationController extends AbstractBlockMinecoloniesHorizontal<BlockDecorationController> implements IBuilderUndestroyable, IAnchorBlock
{
    /**
     * The hardness this block has.
     */
    private static final float BLOCK_HARDNESS = 5F;

    /**
     * This blocks name.
     */
    private static final String BLOCK_NAME = "decorationcontroller";

    /**
     * The resistance this block has.
     */
    private static final float RESISTANCE = 1F;

    /**
     * If the block is mirrored.
     */
    public static BooleanProperty MIRROR = BooleanProperty.create("mirror");

    /**
     * The bounding boxes.
     */
    protected static final VoxelShape AABB_SOUTH = VoxelShapes.create(0.25D, 0.314D, 0.97D, 0.75D, 0.86D, 1.0D);
    protected static final VoxelShape AABB_NORTH = VoxelShapes.create(0.25D, 0.314D, 0.0D, 0.75D, 0.86D, 0.3D);
    protected static final VoxelShape AABB_EAST  = VoxelShapes.create(0.97D, 0.314D, 0.25D, 1.0D, 0.86D, 0.75D);
    protected static final VoxelShape AABB_WEST  = VoxelShapes.create(0.0D, 0.314D, 0.25D, 0.3D, 0.86D, 0.75D);

    /**
     * Constructor for the placerholder.
     */
    public BlockDecorationController()
    {
        super(Properties.create(Material.WOOD).hardnessAndResistance(BLOCK_HARDNESS, RESISTANCE).doesNotBlockMovement());
        this.setDefaultState(this.getDefaultState().with(HORIZONTAL_FACING, Direction.NORTH).with(MIRROR, false));
        setRegistryName(BLOCK_NAME);
    }

    @Override
    public VoxelShape getShape(final BlockState state, final IBlockReader worldIn, final BlockPos pos, final ISelectionContext context)
    {
        Direction Direction = state.get(HORIZONTAL_FACING);
        switch (Direction)
        {
            case EAST:
                return AABB_EAST;
            case WEST:
                return AABB_WEST;
            case SOUTH:
                return AABB_SOUTH;
            case NORTH:
            default:
                return AABB_NORTH;
        }
    }

    @Override
    public ActionResultType onBlockActivated(
      final BlockState state,
      final World worldIn,
      final BlockPos pos,
      final PlayerEntity player,
      final Hand hand,
      final BlockRayTraceResult ray)
    {
        if (worldIn.isRemote)
        {
            final TileEntity tileEntity = worldIn.getTileEntity(pos);
            if (tileEntity instanceof TileEntityDecorationController)
            {
                MineColonies.proxy.openDecorationControllerWindow(pos);
            }
        }
        return ActionResultType.SUCCESS;
    }

    @Override
    public boolean hasTileEntity(final BlockState state)
    {
        return true;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(HORIZONTAL_FACING, MIRROR);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(final BlockState state, final IBlockReader world)
    {
        return new TileEntityDecorationController();
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(final BlockItemUseContext context)
    {
        return super.getStateForPlacement(context).with(HORIZONTAL_FACING, context.getPlacementHorizontalFacing());
    }

    /**
     * @deprecated
     */
    @NotNull
    @Override
    public BlockState rotate(@NotNull BlockState state, Rotation rot)
    {
        return state.with(HORIZONTAL_FACING, rot.rotate(state.get(HORIZONTAL_FACING)));
    }

    @NotNull
    @Override
    public BlockState mirror(@NotNull BlockState state, Mirror mirrorIn)
    {
        return state.with(MIRROR, mirrorIn != Mirror.NONE);
    }
}
