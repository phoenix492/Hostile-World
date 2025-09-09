package net.phoenix492.block;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.MapCodec;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.phoenix492.util.TagKeys;

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
    public static final int MAX_AGE = BlockStateProperties.MAX_AGE_15;
    public static final IntegerProperty AGE = BlockStateProperties.AGE_15;
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
                .setValue(AGE, 0)
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
                .filter(state -> state.getValue(AGE) == 0)
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
    protected MapCodec<? extends BaseFireBlock> codec() {
        return CODEC;
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        return this.canSurvive(state, level, currentPos)
            ? this.getStateWithAge(level, currentPos, state.getValue(AGE))
            : Blocks.AIR.defaultBlockState();
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return level.getBlockState(pos.below()).is(TagKeys.Blocks.MUSHROOM_FIRE_BURNS);
    }

    /**
     * Adapted from Vanilla's fire spread method, cleaned up and lightly rewritten so it makes sense to me instead of
     * the psychopaths at mojang and the java decompiler.
     */
    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        // Schedules this fire block to be ticked between 30 and 40 ticks into the future.
        level.scheduleTick(pos, this, 30 + random.nextInt(10));

        // If fire tick is disabled, bailout.
        if (!level.getGameRules().getBoolean(GameRules.RULE_DOFIRETICK)) {
            return;
        }

        // Remove this block and bailout if it's in an invalid place.
        if (!state.canSurvive(level, pos)) {
            level.removeBlock(pos, false);
            return;
        }

        BlockPos blockPosBelow = pos.below();
        BlockState blockstateBelow = level.getBlockState(blockPosBelow);
        boolean onInfiniburnSource = blockstateBelow.isFireSource(level, blockPosBelow, Direction.UP);
        int age = state.getValue(AGE);

        /*
         If not on a fire source, and it's raining in the level, and this fire block is in the rain, and we pass a
         random check based on age, remove the block and bailout.
        */
        if (!onInfiniburnSource && level.isRaining() && this.isNearRain(level, pos) && random.nextFloat() < 0.2F + (float)age * 0.03F) {
            level.removeBlock(pos, false);
            return;
        }

        // Possibly increase this fire blocks age between 0 and 1, weighted towards zero.
        int newAge = Math.min(MAX_AGE, age + random.nextInt(3) / 2);
        if (age != newAge) {
            state = state.setValue(AGE, newAge);
            level.setBlock(pos, state, 4);
        }

        if (!onInfiniburnSource) {
            // Ordinarily invalid locations can be valid if they're burning on a sturdy block (?), but only for a short period.
            if (!this.isValidFireLocation(level, pos)) {
                if (!blockstateBelow.isFaceSturdy(level, blockPosBelow, Direction.UP) || age > 3) {
                    level.removeBlock(pos, false);
                }
                return;
            }

            // Once the fire hits max age, it's got a 1/4 chance of burning out if it's on a block that can't catch fire.
            if (age == MAX_AGE && random.nextInt(4) == 0 && !this.canCatchFire(level, pos.below(), Direction.UP)) {
                level.removeBlock(pos, false);
                return;
            }
        }

        /*
         Once we've made it past the "destroy myself" checks, we can check to see if we're destroying blocks around us.
         I'm not a huge fan of the repeated code here, but we check differently for below/above so I don't see a cleaner way
         Unless you want to include an if/else in a forEach Direction, which is probably a little less immediately clear anyway.
        */
        boolean increasedFireBurnout = level.getBiome(pos).is(BiomeTags.INCREASED_FIRE_BURNOUT);
        int increasedFireBurnoutModifier = increasedFireBurnout ? -50 : 0;
        this.checkBurnOut(level, pos.east(), 300 + increasedFireBurnoutModifier, random, age, Direction.WEST);
        this.checkBurnOut(level, pos.west(), 300 + increasedFireBurnoutModifier, random, age, Direction.EAST);
        this.checkBurnOut(level, pos.north(), 300 + increasedFireBurnoutModifier, random, age, Direction.SOUTH);
        this.checkBurnOut(level, pos.south(), 300 + increasedFireBurnoutModifier, random, age, Direction.NORTH);
        this.checkBurnOut(level, pos.below(), 250 + increasedFireBurnoutModifier, random, age, Direction.UP);
        this.checkBurnOut(level, pos.above(), 250 + increasedFireBurnoutModifier, random, age, Direction.DOWN);

        // Finally, it's time to check fire spread.
        BlockPos.MutableBlockPos checkedBlock = new BlockPos.MutableBlockPos();
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                for (int y = -1; y <= 4; y++) {

                    // Stops us from checking the fire block itself.
                    if (x == 0 && y == 0 && z == 0) {
                        continue;
                    }

                    checkedBlock.setWithOffset(pos, x, y, z);
                    int checkedBlockIgniteOdds = this.getIgniteOdds(level, checkedBlock);
                    if (checkedBlockIgniteOdds <= 0) {
                        continue;
                    }

                    // Adjust ignite odds based on fire age and world difficulty.
                    checkedBlockIgniteOdds = (checkedBlockIgniteOdds + 40 + level.getDifficulty().getId() * 7) / (age + 30);

                    // Biomes tagged as #minecraft:increased_fire_burnout cut spread in half.
                    if (increasedFireBurnout) {
                        checkedBlockIgniteOdds /= 2;
                    }

                    // Reduce spread chance for each block above the fire.
                    int verticalSpreadChanceFilter = 100;
                    if (y > 1) {
                        verticalSpreadChanceFilter += (y - 1) * 100;
                    }

                    // Fire can't propagate in rain.
                    if (level.isRaining() && this.isNearRain(level, checkedBlock)) {
                        continue;
                    }

                    // Finally spread the fire.
                    if (checkedBlockIgniteOdds > 0 && random.nextInt(verticalSpreadChanceFilter) <= checkedBlockIgniteOdds) {
                        int newFireAge = Math.min(15, age + random.nextInt(5) / 4);
                        level.setBlockAndUpdate(checkedBlock, this.getStateWithAge(level, checkedBlock, newFireAge));
                    }
                }
            }
        }
    }

    private void checkBurnOut(ServerLevel level, BlockPos pos, int chance, RandomSource random, int age, Direction direction) {
        if (level.getBlockState(pos).is(TagKeys.Blocks.MUSHROOM_FIRE_BURNS)) {

        }
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return this.shapesCache.get(state.setValue(AGE, 0));
    }

    @Override
    protected boolean canBurn(BlockState state) {
        return true;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Level level = context.getLevel();
        BlockPos clickedPos = context.getClickedPos();
        BlockPos posBelow = context.getClickedPos().below();
        BlockState stateBelow = context.getLevel().getBlockState(posBelow);

        if (this.canCatchFire(level, posBelow, Direction.UP) || stateBelow.isFaceSturdy(level, posBelow, Direction.UP)) {
            return this.defaultBlockState();
        }

        BlockState fireState = this.defaultBlockState();
        for (Direction direction : Direction.values()) {
            BooleanProperty directionProp = PROPERTY_BY_DIRECTION.get(direction);
            fireState = fireState.setValue(directionProp, this.canCatchFire(level, clickedPos.relative(direction), direction.getOpposite()));
        }
        return fireState;

    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE, NORTH, EAST, SOUTH, WEST, UP);
    }


    private boolean canCatchFire(BlockGetter world, BlockPos pos, Direction face) {
        return world.getBlockState(pos).isFlammable(world, pos, face);
    }

    private boolean isValidFireLocation(BlockGetter level, BlockPos pos) {
        for (Direction direction : Direction.values()) {
            if (this.canCatchFire(level, pos.relative(direction), direction.getOpposite())) {
                return true;
            }
        }

        return false;
    }

    private boolean isNearRain(Level level, BlockPos pos) {
        return level.isRainingAt(pos)
            || level.isRainingAt(pos.west())
            || level.isRainingAt(pos.east())
            || level.isRainingAt(pos.north())
            || level.isRainingAt(pos.south());
    }


    private int getIgniteOdds(BlockState state) {
        return state.hasProperty(BlockStateProperties.WATERLOGGED) && state.getValue(BlockStateProperties.WATERLOGGED)
            ? 0
            : this.igniteOdds.getInt(state.getBlock());
    }

    private int getIgniteOdds(LevelReader level, BlockPos pos) {
        if (!level.isEmptyBlock(pos)) {
            return 0;
        } else {
            int i = 0;

            for (Direction direction : Direction.values()) {
                BlockState blockstate = level.getBlockState(pos.relative(direction));
                i = Math.max(blockstate.getFireSpreadSpeed(level, pos.relative(direction), direction.getOpposite()), i);
            }

            return i;
        }
    }


    private BlockState getStateWithAge(LevelAccessor level, BlockPos pos, int age) {
        BlockState blockstate = getState(level, pos);
        return blockstate.is(Blocks.FIRE) ? blockstate.setValue(AGE, Integer.valueOf(age)) : blockstate;
    }


    public void setFlammable(Block block, int encouragement, int flammability) {
        if (block == Blocks.AIR) throw new IllegalArgumentException("Tried to set air on fire... This is bad.");
        this.igniteOdds.put(block, encouragement);
        this.burnOdds.put(block, flammability);
    }
}
