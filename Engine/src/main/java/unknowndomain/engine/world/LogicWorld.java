package unknowndomain.engine.world;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import io.netty.util.collection.LongObjectHashMap;
import io.netty.util.collection.LongObjectMap;
import org.joml.AABBd;
import org.joml.Vector2d;
import org.joml.Vector3f;
import org.joml.Vector3i;
import unknowndomain.engine.Entity;
import unknowndomain.engine.GameContext;
import unknowndomain.engine.block.Block;
import unknowndomain.engine.block.BlockPrototype;
import unknowndomain.engine.event.Event;
import unknowndomain.engine.math.BlockPos;
import unknowndomain.engine.math.ChunkPos;
import unknowndomain.engine.util.Facing;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class LogicWorld implements World {
    private LongObjectMap<Chunk> chunks = new LongObjectHashMap<>();
    private ChunkProvider chunkProvider = new ChunkProviderDummy();
    private List<Entity> entityList = new ArrayList<>();
    private GameContext context;

    public LogicWorld(GameContext context) {
        this.context = context;
    }

    public void addEntity(Entity entity) {
        entityList.add(entity);
    }

    public BlockPrototype.Hit rayHit(Vector3f from, Vector3f dir, int distance) {
        return rayHit(from, dir, distance, Sets.newHashSet(context.getBlockRegistry().getValue(0)));
    }

    public BlockPrototype.Hit rayHit(Vector3f from, Vector3f dir, int distance, Set<Block> ignore) {
        Vector3f rayOffset = dir.normalize(new Vector3f()).mul(distance);
        Vector3f dist = rayOffset.add(from, new Vector3f());

        BlockPos fromPos = new BlockPos((int) from.x, (int) from.y, (int) from.z);
        BlockPos toPos = new BlockPos((int) dist.x, (int) dist.y, (int) dist.z);

        List<BlockPos> all = Lists.newArrayList();
        int fromX = Math.min(fromPos.getX(), toPos.getX()) - 1,
                toX = Math.max(fromPos.getX(), toPos.getX()) + 1,
                fromY = Math.min(fromPos.getY(), toPos.getY()) - 1,
                toY = Math.max(fromPos.getY(), toPos.getY()) + 1,
                fromZ = Math.min(fromPos.getZ(), toPos.getZ()) - 1,
                toZ = Math.max(fromPos.getZ(), toPos.getZ()) + 1;
        for (int i = fromX; i <= toX; i++) {
            for (int j = fromY; j <= toY; j++) {
                for (int k = fromZ; k <= toZ; k++) {
                    all.add(new BlockPos(i, j, k));
                }
            }
        }
        all.sort(Comparator.comparingInt(a -> a.sqDistanceBetween(fromPos)));

        for (BlockPos pos : all) {
            Block object = getBlock(pos);
            if (ignore.contains(object)) continue;
            Vector3f local = from.sub(pos.getX(), pos.getY(), pos.getZ(), new Vector3f());
            AABBd box = object.getBoundingBox();
            Vector2d result = new Vector2d();
            boolean hit = box.intersectRay(local.x, local.y, local.z, rayOffset.x, rayOffset.y, rayOffset.z, result);
            if (hit) {
//                    System.out.println(pos);
                Vector3f hitPoint = local.add(rayOffset.mul((float) result.x, new Vector3f()));
                Facing facing = Facing.NORTH;
                if (hitPoint.x == 0f) {
                    facing = Facing.WEST;
                } else if (hitPoint.x == 1f) {
                    facing = Facing.EAST;
                } else if (hitPoint.y == 0f) {
                    facing = Facing.BOTTOM;
                } else if (hitPoint.y == 1f) {
                    facing = Facing.TOP;
                } else if (hitPoint.z == 0f) {
                    facing = Facing.SOUTH;
                } else if (hitPoint.z == 1f) {
                    facing = Facing.NORTH;
                }
                return new BlockPrototype.Hit(pos, object, hitPoint, facing);
            }
        }
        return null;
    }

    public void tick() {
        for (Entity entity : entityList) {
            Vector3f motion = entity.getMotion();

//            if (motion.y > 0) motion.y -= 0.01f;
//            else if (motion.y < 0) motion.y += 0.01f;
//            if (Math.abs(motion.y) <= 0.01f) motion.y = 0; // physics update
        }
        chunks.forEach(this::tickChunk);

        for (Entity entity : entityList) {
            entity.tick(); // state machine update
        }
    }

    private void tickChunk(long pos, Chunk chunk) {
        Collection<Block> blocks = chunk.getRuntimeBlock();
        if (blocks.size() != 0) {
            for (Block object : blocks) {
                BlockPrototype.TickBehavior behavior = object.getBehavior(BlockPrototype.TickBehavior.class);
                if (behavior != null) {
                    behavior.tick(object);
                }
            }
        }
    }

    @Override
    public Chunk getChunk(int x, int z) {
        long pos = (long) x << 32 | z;
        return chunks.get(pos);
    }

    public Block getBlock(BlockPos pos) {
        ChunkPos chunkPos = pos.toChunk();
        long cp = (long) chunkPos.getChunkX() << 32 | chunkPos.getChunkZ();
        Chunk chunk = this.chunks.get(cp);
        if (chunk != null)
            return chunk.getBlock(pos);
        else {
            Chunk nchunk = chunkProvider.provideChunk(this.context, pos); // TODO async load
            chunks.put(cp, nchunk);
            return nchunk.getBlock(pos);
        }
    }

    @Override
    public Block setBlock(BlockPos pos, Block block) {
        ChunkPos chunkPos = pos.toChunk();
        long cp = (long) chunkPos.getChunkX() << 32 | chunkPos.getChunkZ();
        Chunk chunk = this.chunks.get(cp);
        if (chunk != null)
            chunk.setBlock(pos, block);
        else {
            Chunk nchunk = chunkProvider.provideChunk(this.context, pos); // TODO async load
            this.chunks.put(cp, nchunk);
            nchunk.setBlock(pos, block);
        }
        return null;
    }

    @Nullable
    @Override
    public <T> T getComponent(@Nonnull String name) {
        return null;
    }

    @Nullable
    @Override
    public <T> T getComponent(@Nonnull Class<T> type) {
        return null;
    }

    @Nullable
    @Override
    public <T> T getBehavior(Class<T> type) {
        return null;
    }

    public static class ChunkLoad implements Event {
        public final ChunkPos pos;
        public final int[][] blocks;

        public ChunkLoad(ChunkPos pos, int[][] blocks) {
            this.pos = pos;
            this.blocks = blocks;
        }
    }

    public static class ChunkUnload implements Event {
        public final Vector3i pos;

        public ChunkUnload(Vector3i pos) {
            this.pos = pos;
        }
    }
}