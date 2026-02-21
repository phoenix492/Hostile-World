package net.phoenix492.hostileworld.block;

import net.phoenix492.hostileworld.config.HostileWorldConfig;
import net.phoenix492.hostileworld.data.map.MycofireFlammabilityData;
import net.phoenix492.hostileworld.registration.ModBlocks;
import net.phoenix492.hostileworld.registration.ModDataAttachments;
import net.phoenix492.hostileworld.registration.ModDataMaps;
import net.phoenix492.hostileworld.util.ModTagKeys;

import com.mojang.serialization.MapCodec;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import com.google.common.collect.ImmutableMap;

public class MycofireBlock extends BaseFireBlock {
    /**
     * DO. NOT. CHECK. THESE. VALUES. BEFORE. CONFIG REGISTRATION.
     * <br>
     * It <i>will</i> crash the game.
     */
    public enum MycofireStrength implements StringRepresentable {
        WEAK(
            "weak",
            HostileWorldConfig.SERVER_CONFIG.weakMaxAge,
            HostileWorldConfig.SERVER_CONFIG.weakTickCooldownBase,
            HostileWorldConfig.SERVER_CONFIG.weakTickCooldownRange,
            HostileWorldConfig.SERVER_CONFIG.weakBurnoutChanceMult,
            HostileWorldConfig.SERVER_CONFIG.weakSpreadChanceMult
        ),
        NORMAL(
            "normal",
            HostileWorldConfig.SERVER_CONFIG.normalMaxAge,
            HostileWorldConfig.SERVER_CONFIG.normalTickCooldownBase,
            HostileWorldConfig.SERVER_CONFIG.normalTickCooldownRange,
            HostileWorldConfig.SERVER_CONFIG.normalBurnoutChanceMult,
            HostileWorldConfig.SERVER_CONFIG.normalSpreadChanceMult
        ),
        STRONG(
            "strong",
            HostileWorldConfig.SERVER_CONFIG.strongMaxAge,
            HostileWorldConfig.SERVER_CONFIG.strongTickCooldownBase,
            HostileWorldConfig.SERVER_CONFIG.strongTickCooldownRange,
            HostileWorldConfig.SERVER_CONFIG.strongBurnoutChanceMult,
            HostileWorldConfig.SERVER_CONFIG.strongSpreadChanceMult
        ),
        APOCALYPTIC(
            "apocalyptic",
            HostileWorldConfig.SERVER_CONFIG.apocalypticMaxAge,
            HostileWorldConfig.SERVER_CONFIG.apocalypticTickCooldownBase,
            HostileWorldConfig.SERVER_CONFIG.apocalypticTickCooldownRange,
            HostileWorldConfig.SERVER_CONFIG.apocalypticBurnoutChanceMult,
            HostileWorldConfig.SERVER_CONFIG.apocalypticSpreadChanceMult
        );

        private final String name;
        private final ModConfigSpec.ConfigValue<Integer> maxAge;
        private final ModConfigSpec.ConfigValue<Integer> tickCooldownBase;
        private final ModConfigSpec.ConfigValue<Integer> tickCooldownRange;
        private final ModConfigSpec.ConfigValue<Double> burnoutChanceMult;
        private final ModConfigSpec.ConfigValue<Double> spreadChanceMult;

        MycofireStrength(String name,
                         ModConfigSpec.ConfigValue<Integer> maxAge,
                         ModConfigSpec.ConfigValue<Integer> tickCooldownBase,
                         ModConfigSpec.ConfigValue<Integer> tickCooldownRange,
                         ModConfigSpec.ConfigValue<Double> burnoutChanceMult,
                         ModConfigSpec.ConfigValue<Double> spreadChanceMult) {
            this.name = name;
            this.maxAge = maxAge;
            this.tickCooldownBase = tickCooldownBase;
            this.tickCooldownRange = tickCooldownRange;
            this.burnoutChanceMult = burnoutChanceMult;
            this.spreadChanceMult = spreadChanceMult;
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }

        public double getBurnoutChanceMult() {
            return burnoutChanceMult.get();
        }

        public int getTickCooldownRange() {
            return tickCooldownRange.get();
        }

        public int getTickCooldownBase() {
            return tickCooldownBase.get();
        }

        public int getMaxAge() {
            return maxAge.get();
        }

        public double getSpreadChanceMult() {
            return spreadChanceMult.get();
        }
    }


    public static final MapCodec<MycofireBlock> CODEC = simpleCodec(MycofireBlock::new);
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
    public static final EnumProperty<MycofireStrength> STRENGTH = EnumProperty.create("strength", MycofireStrength.class);
    public static final IntegerProperty AGE = IntegerProperty.create("age", 0, 30);
    private final Map<BlockState, VoxelShape> shapesCache;
    private static final Map<Direction, BooleanProperty> PROPERTY_BY_DIRECTION = PipeBlock.PROPERTY_BY_DIRECTION
        .entrySet()
        .stream()
        .filter(entry -> entry.getKey() != Direction.DOWN)
        .collect(Util.toMap());
    private static final int BASE_BURNOUT_CHANCE = 250;

    public MycofireBlock(Properties properties) {
        super(properties, 4f);
        this.registerDefaultState(
            this.defaultBlockState()
                .setValue(STRENGTH, MycofireStrength.NORMAL)
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
                .collect(Collectors.toMap(Function.identity(), MycofireBlock::calculateShape))
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
            ? getStateForPlacement(level, currentPos).setValue(STRENGTH, state.getValue(STRENGTH)).setValue(AGE, state.getValue(AGE))
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
        // Collect the various properties we need to check through the following code.
        BlockPos blockPosBelow = pos.below();
        BlockState blockstateBelow = level.getBlockState(blockPosBelow);
        boolean onInfiniburnSource = blockstateBelow.is(level.dimensionType().infiniburn());
        MycofireStrength strength = state.getValue(STRENGTH);
        int age = state.getValue(AGE);
        boolean biomeFireResistant = level.getBiome(pos).is(ModTagKeys.Biomes.MYCOFIRE_RESISTANT);
        int fireResistantBiomeModifier = biomeFireResistant ? -50 : 0;
        int burnoutChance = (int) ((BASE_BURNOUT_CHANCE + fireResistantBiomeModifier) * strength.getBurnoutChanceMult());

        // Schedule another tick in the future according to this fire's strength.
        level.scheduleTick(pos, this, strength.getTickCooldownBase() + random.nextInt(strength.getTickCooldownRange()));

        /*
         If fire tick is disabled, bailout. We do this AFTER scheduling a tick otherwise fire that was ever ticked
         while fire spread was disabled will never tick again.
        */
        if (!level.getGameRules().getBoolean(GameRules.RULE_DOFIRETICK)) {
            return;
        }

        // Remove this block if it's in an invalid place, but give it one last chance to propagate.
        if (!state.canSurvive(level, pos)) {
            level.removeBlock(pos, false);
        }

        if (!onInfiniburnSource) {
            if (!this.isIgnitableLocation(level, pos)) {
                /*
                 If we're in a nonignitable location on a block that can't physically support the fire, extinguish.
                 Otherwise, 1 in (MAX_AGE - Current Age) + 1 chance to extinguish.
                */
                if (!blockstateBelow.isFaceSturdy(level, blockPosBelow, Direction.UP) || random.nextInt((strength.getMaxAge() - age) + 1) == 0) {
                    level.removeBlock(pos, false);
                    return;
                }
                // Increase the age of fire in non-ignitable locations.
                level.setBlockAndUpdate(pos, increaseFireAge(state));
            }
        } else {
            // Infiniburning mycofire should become weak to prevent setting up unreasonably effective beacons of spread prevention.
            level.setBlockAndUpdate(pos, state.setValue(MycofireBlock.STRENGTH, MycofireStrength.WEAK));
        }

         // After getting past all the "destroy myself" checks, it's time to see if we're destroying something else.
        for (Direction d : Direction.values()) {
            if (d == Direction.DOWN || d == Direction.UP) {
                this.checkBurnOut(level, pos.relative(d), burnoutChance - 50, random, state);
            }
            else {
                this.checkBurnOut(level, pos.relative(d), burnoutChance, random, state);
            }
        }

        // Finally, time to check fire spread.
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

                    // Biomes tagged as #hostileworld:mycofire_resistant cut spread in half.
                    if (biomeFireResistant) {
                        checkedBlockIgniteOdds /= 2;
                    }

                    // Mult. spread based on fire strength
                    checkedBlockIgniteOdds = (int) (checkedBlockIgniteOdds * strength.getSpreadChanceMult());

                    // Reduce spread chance for each block above the fire.
                    int verticalSpreadChanceFilter = 100;
                    if (y > 1) {
                        verticalSpreadChanceFilter += (y - 1) * 100;
                    }

                    // Finally spread the fire. 33% chance to increase age of newly placed fire by 1.
                    if (checkedBlockIgniteOdds > 0 && random.nextInt(verticalSpreadChanceFilter) < checkedBlockIgniteOdds) {
                        level.setBlockAndUpdate(checkedBlock, increaseFireAgeZeroBiased(getStateForPropagation(level, checkedBlock, state), random));
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
        builder.add(AGE, STRENGTH, NORTH, EAST, SOUTH, WEST, UP);
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (!oldState.is(state.getBlock()) && !state.canSurvive(level, pos)) {
            level.removeBlock(pos, false);
        }
        level.scheduleTick(pos, this, 10);
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (entity.hasData(ModDataAttachments.FUNGAL_INFECTION)) {
            entity.getData(ModDataAttachments.FUNGAL_INFECTION).reduceInfectionLevel(1);
        }
        super.entityInside(state, level, pos, entity);
    }

    public static boolean canBePlacedAt(Level level, BlockPos pos) {
        BlockState blockstate = level.getBlockState(pos);
        return blockstate.isAir() && getState(level, pos).canSurvive(level, pos);
    }

    public static BlockState getState(BlockGetter reader, BlockPos pos) {
        return ModBlocks.MYCOFIRE.get().getStateForPlacement(reader, pos);
    }

    private void checkBurnOut(ServerLevel level, BlockPos pos, int chance, RandomSource random, BlockState source) {
        BlockState burnoutTarget = level.getBlockState(pos);
        MycofireFlammabilityData flammabilityData = burnoutTarget.getBlockHolder().getData(ModDataMaps.MYCOFIRE_FLAMMABILITY_DATA);

        /*
         This is a check so blocks like grass and flowers that would otherwise block mushroom fire from
         purging mycelium beneath them instead have the block below checked, making it less effective at doing its
         job and leaving too many mycelium pockets.
        */
        if (flammabilityData == null) {
            if (!(level.getBlockState(pos).getBlock() instanceof AirBlock)) {
                if (level.getBlockState(pos.below()).getBlockHolder().getData(ModDataMaps.MYCOFIRE_FLAMMABILITY_DATA) != null) {
                    checkBurnOut(level, pos.below(), chance, random, source);
                }
            }
            return;
        }

        if (random.nextInt(chance) < flammabilityData.flammability()) {
            // Different handling for blocks that burn away and blocks that are purified
            if (flammabilityData.burnoutTarget() instanceof AirBlock) {
                // Apply chance to age the newly spread fire.
                int newFireAge = Math.min(source.getValue(AGE) + random.nextInt(5) / 4, source.getValue(STRENGTH).getMaxAge());
                level.setBlock(pos, this.getStateForPropagation(level, pos, source).setValue(AGE, newFireAge), 3);
            }
            else {
                level.setBlock(pos, flammabilityData.burnoutTarget().defaultBlockState(), 3);
            }
        }


    }

    private BlockState getStateForPropagation(BlockGetter level, BlockPos pos, BlockState source) {
        BlockState toReturn = getStateForPlacement(level, pos);
        return toReturn.setValue(STRENGTH, source.getValue(STRENGTH)).setValue(AGE, source.getValue(AGE));
    }

    private BlockState getStateForPlacement(BlockGetter level, BlockPos pos) {
        BlockPos below = pos.below();
        BlockState blockstate = level.getBlockState(below);
        BlockState toReturn = this.defaultBlockState();

        if (!this.canCatchFire(level, below) && !blockstate.isFaceSturdy(level, below, Direction.UP)) {
            for (Direction direction : Direction.values()) {
                BooleanProperty booleanproperty = PROPERTY_BY_DIRECTION.get(direction);
                if (booleanproperty != null) {
                    toReturn = toReturn.setValue(booleanproperty, this.canCatchFire(level, pos.relative(direction)));
                }
            }
        }

        return toReturn;
    }

    private boolean canCatchFire(BlockGetter world, BlockPos pos) {
        return (world.getBlockState(pos).getBlockHolder().getData(ModDataMaps.MYCOFIRE_FLAMMABILITY_DATA) instanceof MycofireFlammabilityData);
    }

    private boolean isIgnitableLocation(BlockGetter level, BlockPos pos) {
        for (Direction direction : Direction.values()) {
            if (this.canCatchFire(level, pos.relative(direction))) {
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

            if (level.getBlockState(pos.relative(direction)).getBlockHolder().getData(ModDataMaps.MYCOFIRE_FLAMMABILITY_DATA) instanceof MycofireFlammabilityData flammabilityData) {
                maxIgnitionOdds = Math.max(flammabilityData.encouragement(), maxIgnitionOdds);
            }
        }

        return maxIgnitionOdds;
    }


    private BlockState increaseFireAge(BlockState fireState) {
        MycofireStrength strength = fireState.getValue(STRENGTH);
        int currentAge = fireState.getValue(AGE);
        int maxAge = strength.getMaxAge();

        if (currentAge < maxAge) {
            return fireState.setValue(AGE, currentAge + 1);
        } else {
            return fireState.setValue(AGE, maxAge);
        }
    }

    private BlockState increaseFireAgeZeroBiased(BlockState fireState, RandomSource random) {
        if (random.nextInt(2) == 0) {
            return increaseFireAge(fireState);
        }
        else {
            return fireState;
        }
    }

}
