package io.github.redstoneparadox.creeperfall.game.map;

import io.github.redstoneparadox.creeperfall.game.config.CreeperfallMapConfig;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import xyz.nucleoid.map_templates.MapTemplate;

import java.util.ArrayList;
import java.util.List;

public class CreeperfallMapGenerator {
    private final CreeperfallMapConfig config;

    public CreeperfallMapGenerator(CreeperfallMapConfig config) {
        this.config = config;
    }

    public CreeperfallMap build() {
        MapTemplate template = MapTemplate.createEmpty();
        CreeperfallMap map = new CreeperfallMap(template, this.config);

        this.buildMainPlatform(template);
        this.buildPerimeter(template);
        map.spawn = new BlockPos(0,65,0);

        return map;
    }

    private void buildMainPlatform(MapTemplate builder) {
        int radius = config.size/2;
        int adjustmentConst = 0;

        if (config.size % 2 == 1) adjustmentConst = 1;

        int positiveBound = radius - 1;
        int negativeBound = -radius - adjustmentConst;

        BlockPos min = new BlockPos(negativeBound, 64, negativeBound);
        BlockPos max = new BlockPos(positiveBound, 64, positiveBound);

        for (BlockPos pos: BlockPos.iterate(min, max)) {
            int remainderX = Math.abs(pos.getX()) % 2;
            int remainderZ = Math.abs(pos.getZ()) % 2;

            if ((remainderX == 0 && remainderZ == 0) || (remainderX == 1 && remainderZ == 1)) {
                builder.setBlockState(pos, Blocks.BLACK_STAINED_GLASS.getDefaultState());
            }
            else {
                builder.setBlockState(pos, Blocks.LIGHT_GRAY_STAINED_GLASS.getDefaultState());
            }
        }
    }

    private void buildPerimeter(MapTemplate builder) {
        int radius = (config.size + 2)/2;
        int adjustmentConst = 0;

        if (config.size % 2 == 1) adjustmentConst = 1;

        int positiveBound = radius - 1;
        int negativeBound = -radius - adjustmentConst;
        int topY = 68;

        BlockPos northMin = new BlockPos(negativeBound, 64, negativeBound);
        BlockPos northMax = new BlockPos(positiveBound, topY, negativeBound);

        BlockPos southMin = new BlockPos(negativeBound, 64, positiveBound);
        BlockPos southMax = new BlockPos(positiveBound, topY, positiveBound);

        BlockPos eastMin = new BlockPos(positiveBound, 64, negativeBound);
        BlockPos eastMax = new BlockPos(positiveBound, topY, positiveBound);

        BlockPos westMin = new BlockPos(negativeBound, 64, negativeBound);
        BlockPos westMax = new BlockPos(negativeBound, topY, positiveBound);

        List<Iterable<BlockPos>> iterables = new ArrayList<>();

        iterables.add(BlockPos.iterate(northMin, northMax));
        iterables.add(BlockPos.iterate(southMin, southMax));
        iterables.add(BlockPos.iterate(eastMin, eastMax));
        iterables.add(BlockPos.iterate(westMin, westMax));

        for (Iterable<BlockPos> iterable: iterables) {
            for (BlockPos pos: iterable) {
                switch (pos.getY()) {
                    case 64:
                        builder.setBlockState(pos, Blocks.BLACKSTONE.getDefaultState());
                        break;
                    case 65:
                    case 66:
                    case 67:
                        builder.setBlockState(pos, Blocks.BRICK_WALL.getDefaultState());
                        break;
                    case 68:
                        builder.setBlockState(pos, Blocks.MOSSY_STONE_BRICK_SLAB.getDefaultState());
                        break;
                    default:
                        break;
                }
            }
        }
    }
}
