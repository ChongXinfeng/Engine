package engine.world.chunk;

import engine.block.Block;
import engine.event.block.cause.BlockChangeCause;
import engine.game.GameServerFullAsync;
import engine.math.BlockPos;
import engine.registry.Registries;
import engine.server.network.packet.s2c.PacketBlockUpdate;
import engine.world.World;
import org.joml.Vector3i;
import org.joml.Vector3ic;

import javax.annotation.Nonnull;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.lang.ref.WeakReference;

import static engine.world.chunk.ChunkConstants.*;

public class CubicChunk implements Chunk {

    private final WeakReference<World> world;
    private final ChunkPos pos;

    private final Vector3ic min;
    private final Vector3ic max;
    private final Vector3ic center;

    private BlockStorage blockStorage;
    private int nonAirBlockCount = 0;

    public CubicChunk(World world, int chunkX, int chunkY, int chunkZ) {
        this.world = new WeakReference<>(world);
        this.pos = ChunkPos.of(chunkX, chunkY, chunkZ);
        this.min = new Vector3i(chunkX << CHUNK_X_BITS, chunkY << CHUNK_Y_BITS, chunkZ << CHUNK_Z_BITS);
        this.max = min.add(CHUNK_X_SIZE, CHUNK_Y_SIZE, CHUNK_Z_SIZE, new Vector3i());
        this.center = min.add(CHUNK_X_SIZE >> 1, CHUNK_Y_SIZE >> 1, CHUNK_Z_SIZE >> 1, new Vector3i());
    }

    @Nonnull
    @Override
    public World getWorld() {
        return world.get();
    }

    @Override
    public ChunkPos getPos() {
        return pos;
    }

    @Override
    public int getX() {
        return pos.x();
    }

    @Override
    public int getY() {
        return pos.y();
    }

    @Override
    public int getZ() {
        return pos.z();
    }

    @Nonnull
    @Override
    public Vector3ic getMin() {
        return min;
    }

    @Nonnull
    @Override
    public Vector3ic getMax() {
        return max;
    }

    @Nonnull
    @Override
    public Vector3ic getCenter() {
        return center;
    }

    @Override
    public Block getBlock(int x, int y, int z) {
        if (blockStorage == null) {
            return Registries.getBlockRegistry().air();
        }

        return blockStorage.getBlock(x, y, z);
    }

    @Override
    public int getBlockId(int x, int y, int z) {
        if (blockStorage == null) {
            return Registries.getBlockRegistry().getId(Registries.getBlockRegistry().air());
        }

        return blockStorage.getBlockId(x, y, z);
    }

    @Override
    public Block setBlock(@Nonnull BlockPos pos, @Nonnull Block block, @Nonnull BlockChangeCause cause) {
        var block1 = setBlock(pos.x(), pos.y(), pos.z(), block);
        var world1 = getWorld();
        if (!(cause instanceof BlockChangeCause.WorldGenCause))
            if (world1.getGame() instanceof GameServerFullAsync) {
                ((GameServerFullAsync) world1.getGame()).getNetworkServer().sendToAll(new PacketBlockUpdate(world1, pos));
            }
        return block1;
    }

    protected Block setBlock(int x, int y, int z, Block block) {
        if (blockStorage == null) {
            blockStorage = new BlockStorage();
        }

        if (block != Registries.getBlockRegistry().air()) {
            nonAirBlockCount++;
        } else {
            nonAirBlockCount--;
        }

        var block1 = blockStorage.setBlock(x, y, z, block);
        return block1;
    }

    @Override
    public boolean isAirChunk() {
        return nonAirBlockCount == 0;
    }

    public void write(DataOutput output) throws IOException {
        output.writeShort(nonAirBlockCount);

        if (nonAirBlockCount != 0) {
            long[] data = blockStorage.getData().getBackingArray();
            for (int i = 0; i < data.length; i++) {
                output.writeLong(data[i]);
            }
        }
    }

    public void read(DataInput input) throws IOException {
        nonAirBlockCount = input.readShort();

        if (nonAirBlockCount != 0) {
            blockStorage = new BlockStorage();
            long[] data = blockStorage.getData().getBackingArray();
            for (int i = 0; i < data.length; i++) {
                data[i] = input.readLong();
            }
        }
    }
}
