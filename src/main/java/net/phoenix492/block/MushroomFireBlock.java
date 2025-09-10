package net.phoenix492.block;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.MapCodec;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.phoenix492.hostileworld.HostileWorld;
import net.phoenix492.registration.ModBlocks;
import net.phoenix492.util.TagKeys;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MushroomFireBlock extends BaseFireBlock {
    public static final MapCodec<MushroomFireBlock> CODEC = simpleCodec(MushroomFireBlock::new);
    private final Object2IntMap<Block> igniteOdds = new Object2IntOpenHashMap<>();
    private final Object2IntMap<Block> burnOdds = new Object2IntOpenHashMap<>();
    private static final VoxelShape UP_AABB = Block.box(0.0, 15.0, 0.0, 16.0, 16.0, 16.0);
    private static final VoxelShape WEST_AABB = Block.box(0.0, 0.0, 0.0, 1.0, 16.0, 16.0);
    private static final VoxelShape EAST_AABB = Block.box(15.0, 0.0, 0.0, 16.0, 16.0, 16.0);
    private static final VoxelShape NORTH_AABB = Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 1.0);
    private static final VoxelShape SOUTH_AABB = Block.box(0.0, 0.0, 15.0, 16.0, 16.0, 16.0);
    public static final BooleanProperty NORTH = PipeBlock.NORTH;
    public static final BooleanProperty EAST = PipeBlock.EAST;
    public static final BooleanProperty SOUTH = PipeBlock.SOUTH;
    public static final BooleanProperty WEST = PipeBlock.WEST;
    public static final BooleanProperty UP = PipeBlock.UP;
    private final Map<BlockState, VoxelShape> shapesCache;
    private static final Map<Direction, BooleanProperty> PROPERTY_BY_DIRECTION = PipeBlock.PROPERTY_BY_DIRECTION
        .entrySet()
        .stream()
        .filter(entry -> entry.getKey() != Direction.DOWN)
        .collect(Util.toMap());

    public MushroomFireBlock(Properties properties) {
        super(properties, 4f);
        this.registerDefaultState(
            this.defaultBlockState()
                .setValue(NORTH, false)
                .setValue(EAST, false)
                .setValue(SOUTH, false)
                .setValue(WEST, false)
                .setValue(UP, false)
        );
        this.shapesCache = ImmutableMap.copyOf(
            this.stateDefinition
                .getPossibleStates()
                .stream()
                .collect(Collectors.toMap(Function.identity(), MushroomFireBlock::calculateShape))
        );
    }

    private static VoxelShape calculateShape(BlockState state) {
        VoxelShape voxelshape = Shapes.empty();
        if (state.getValue(UP)) {
            voxelshape = UP_AABB;
        }

        if (state.getValue(NORTH)) {
            voxelshape = Shapes.or(voxelshape, NORTH_AABB);
        }

        if (state.getValue(SOUTH)) {
            voxelshape = Shapes.or(voxelshape, SOUTH_AABB);
        }

        if (state.getValue(EAST)) {
            voxelshape = Shapes.or(voxelshape, EAST_AABB);
        }

        if (state.getValue(WEST)) {
            voxelshape = Shapes.or(voxelshape, WEST_AABB);
        }

        return voxelshape.isEmpty() ? DOWN_AABB : voxelshape;
    }

    @Override
    protected @NotNull MapCodec<? extends BaseFireBlock> codec() {
        return CODEC;
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        return this.canSurvive(state, level, currentPos)
            ? getStateForPlacement(level, currentPos)
            : Blocks.AIR.defaultBlockState();
    }

    // This one is the method for determining if fire is even allowed to be placed in a location.
    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return level.getBlockState(pos.below()).isFaceSturdy(level, pos.below(), Direction.UP) || this.isIgnitableLocation(level, pos);
    }

    /**
     * Adapted from Vanilla's fire spread method, cleaned up and lightly rewritten so it makes sense to me instead of
     * the psychopaths at mojang and the java decompiler, and then heavily rewritten to fit my fire's behavior.
     */
    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        /*
         Schedules this fire block to be ticked between 20 and 30 ticks into the future.
         This is about 50% faster than regular fire.
        */
        level.scheduleTick(pos, this, 20 + random.nextInt(11));

        // If fire tick is disabled, bailout.
        if (!level.getGameRules().getBoolean(GameRules.RULE_DOFIRETICK)) {
            return;
        }

        // Remove this block if it's in an invalid place, but give it one last chance to propagate.
        if (!state.canSurvive(level, pos)) {
            level.removeBlock(pos, false);
        }

        BlockPos blockPosBelow = pos.below();
        BlockState blockstateBelow = level.getBlockState(blockPosBelow);
        boolean onInfiniburnSource = blockstateBelow.is(level.dimensionType().infiniburn());


        if (!onInfiniburnSource) {
            if (!this.isIgnitableLocation(level, pos)) {
                // if we're on a non-flammable block, 1 in 4 chance to extinguish.
                if (!blockstateBelow.isFaceSturdy(level, blockPosBelow, Direction.UP) || random.nextInt(3) == 0) {
                    level.removeBlock(pos, false);
                    return;
                }
                return;
            }
        }

        /*
         Once we've made it past the "destroy myself" checks, we can check to see if we're destroying blocks around us.
        */
        boolean biomeFireResistant = level.getBiome(pos).is(TagKeys.Biomes.MUSHROOM_FIRE_RESISTANT);
        int fireResistantBiomeModifier = biomeFireResistant ? -50 : 0;
        for (Direction d : Direction.values()) {
            if (d == Direction.DOWN || d == Direction.UP) {
                this.checkBurnOut(level, pos.relative(d), 250 + fireResistantBiomeModifier, random);
            }
            else {
                this.checkBurnOut(level, pos.relative(d), 300 + fireResistantBiomeModifier, random);
            }
        }

        // Finally, it's time to check fire spread.
        BlockPos.MutableBlockPos checkedBlock = new BlockPos.MutableBlockPos();
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                for (int y = -1; y <= 4; y++) {

                    // Stops us from checking the fire block itself.
                    if (x == 0 && z == 0 && y == 0 ) {
                        continue;
                    }

                    checkedBlock.setWithOffset(pos, x, y, z);
                    int checkedBlockIgniteOdds = this.checkIgnition(level, checkedBlock);
                    if (checkedBlockIgniteOdds <= 0) {
                        continue;
                    }

                    // Biomes tagged as #hostileworld:mushroom_fire_resistant cut spread in half.
                    if (biomeFireResistant) {
                        checkedBlockIgniteOdds /= 2;
                    }

                    // Reduce spread chance for each block above the fire.
                    int verticalSpreadChanceFilter = 100;
                    if (y > 1) {
                        verticalSpreadChanceFilter += (y - 1) * 100;
                    }

                    // Finally spread the fire.
                    if (checkedBlockIgniteOdds > 0 && random.nextInt(verticalSpreadChanceFilter) <= checkedBlockIgniteOdds) {
                        level.setBlockAndUpdate(checkedBlock, getStateForPlacement(level, checkedBlock));
                    }
                }
            }
        }
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return this.shapesCache.get(state);
    }

    @Override
    protected boolean canBurn(BlockState state) {
        return true;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(NORTH, EAST, SOUTH, WEST, UP);
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (!oldState.is(state.getBlock()) && !state.canSurvive(level, pos)) {
            level.removeBlock(pos, false);
        }
        level.scheduleTick(pos, this, 30 + level.random.nextInt(10));
    }

    public static boolean canBePlacedAt(Level level, BlockPos pos) {
        BlockState blockstate = level.getBlockState(pos);
        return blockstate.isAir() && getState(level, pos).canSurvive(level, pos);
    }

    public static BlockState getState(BlockGetter reader, BlockPos pos) {
        return ModBlocks.MUSHROOM_FIRE.get().getStateForPlacement(reader, pos);
    }

    public void setFlammable(Block block, int encouragement, int flammability) {
        /*
         This check is way too funny to leave out. It also serves as idiot proofing.
         ... Although unlike Mojang I actually check for void air and cave air. Nice job guys.
        */
        if (block instanceof AirBlock) throw new IllegalArgumentException("Tried to set air on fire... This is bad.");
        this.igniteOdds.put(block, encouragement);
        this.burnOdds.put(block, flammability);
    }

    private void checkBurnOut(ServerLevel level, BlockPos pos, int chance, RandomSource random) {
        BlockState burnoutTarget = level.getBlockState(pos);
        if (!burnoutTarget.is(TagKeys.Blocks.MUSHROOM_FIRE_BURNS)) {
            return;
        }

        if (random.nextInt(chance) > getBurnOdds(burnoutTarget)) {
            return;
        }

        if (burnoutTarget.getBlock() instanceof SnowyDirtBlock) {
            level.setBlockAndUpdate(pos, Blocks.DIRT.defaultBlockState());
        }

        else {
            level.setBlockAndUpdate(pos, this.defaultBlockState());
        }
    }

    private BlockState getStateForPlacement(BlockGetter level, BlockPos pos) {
        BlockPos below = pos.below();
        BlockState blockstate = level.getBlockState(below);
        BlockState toReturn = this.defaultBlockState();

        if (!this.canCatchFire(level, below, Direction.UP) && !blockstate.isFaceSturdy(level, below, Direction.UP)) {
            for (Direction direction : Direction.values()) {
                BooleanProperty booleanproperty = PROPERTY_BY_DIRECTION.get(direction);
                if (booleanproperty != null) {
                    toReturn = toReturn.setValue(booleanproperty, this.canCatchFire(level, pos.relative(direction), direction.getOpposite()));
                }
            }
        }

        return toReturn;
    }

    private boolean canCatchFire(BlockGetter world, BlockPos pos, Direction face) {
        return world.getBlockState(pos).is(TagKeys.Blocks.MUSHROOM_FIRE_BURNS);
    }

    private boolean isIgnitableLocation(BlockGetter level, BlockPos pos) {
        for (Direction direction : Direction.values()) {
            if (this.canCatchFire(level, pos.relative(direction), direction.getOpposite())) {
                return true;
            }
        }
        return false;
    }

    private int checkIgnition(LevelReader level, BlockPos pos) {
        /*
         Non-empty blocks do not suddenly turn into fire via this method.
         That is reserved for checkBurnout.
        */
        if (!level.isEmptyBlock(pos)) {
            return 0;
        }

        // Get the maximum ignition odds among all adjacent blocks
        int maxIgnitionOdds = 0;
        for (Direction direction : Direction.values()) {
            Block adjacentBlock = level.getBlockState(pos.relative(direction)).getBlock();
            maxIgnitionOdds = Math.max(this.igniteOdds.getInt(adjacentBlock), maxIgnitionOdds);
        }

        return maxIgnitionOdds;
    }

    private int getBurnOdds(BlockState state) {
        return this.burnOdds.getInt(state.getBlock());
    }

    private void log(String s) {
        HostileWorld.LOGGER.debug(s);
    }

}
