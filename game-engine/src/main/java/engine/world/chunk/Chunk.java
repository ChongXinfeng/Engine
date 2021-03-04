package engine.world.chunk;

import engine.block.state.BlockState;
import engine.event.block.cause.BlockChangeCause;
import engine.math.BlockPos;
import engine.world.World;
import org.joml.Vector3ic;

import javax.annotation.Nonnull;

public interface Chunk {

    ChunkStatus getStatus();

    @Nonnull
    World getWorld();

    ChunkPos getPos();

    int getX();

    int getY();

    int getZ();

    @Nonnull
    Vector3ic getMin();

    @Nonnull
    Vector3ic getMax();

    @Nonnull
    Vector3ic getCenter();

    /**
     * Get block in a specific path
     *
     * @param x x-coordinate of the block related to chunk coordinate system
     * @param y y-coordinate of the block related to chunk coordinate system
     * @param z z-coordinate of the block related to chunk coordinate system
     * @return the block in the specified path
     */
    BlockState getBlock(int x, int y, int z);

    default BlockState getBlock(@Nonnull BlockPos pos) {
        return getBlock(pos.x(), pos.y(), pos.z());
    }

//    int getBlockId(int x, int y, int z);

//    default int getBlockId(@Nonnull BlockPos pos) {
//        return getBlockId(pos.x(), pos.y(), pos.z());
//    }

    BlockState setBlock(@Nonnull BlockPos pos, @Nonnull BlockState block, @Nonnull BlockChangeCause cause);

    boolean isAirChunk();
}
