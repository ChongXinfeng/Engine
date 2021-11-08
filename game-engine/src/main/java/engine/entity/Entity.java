package engine.entity;

import engine.component.GameObject;
import engine.logic.Tickable;
import engine.world.World;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.joml.primitives.AABBd;

import javax.annotation.Nonnull;
import java.util.UUID;

public interface Entity extends GameObject<Entity>, Tickable {

    @Nonnull
    EntityProvider getProvider();

    @Nonnull
    UUID getUniqueId();

    int getId();

    @Nonnull
    World getWorld();

    // TODO: Don't use Vector3d, it isn't observable
    Vector3d getPosition();

    // TODO: Don't use Vector3f, it isn't observable
    Vector3f getRotation();

    // TODO: Don't use Vector3f, it isn't observable
    Vector3f getMotion();

    AABBd getBoundingBox();

    boolean hasCollision();

    void destroy();

    boolean isDestroyed();
}
