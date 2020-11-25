package engine.graphics;

import engine.graphics.material.Material;
import engine.graphics.math.BoundingVolume;
import engine.graphics.mesh.Mesh;
import engine.graphics.queue.RenderQueue;
import engine.graphics.queue.RenderType;
import engine.graphics.texture.Texture;
import engine.graphics.texture.Texture2D;

public class Geometry extends Node3D {

    private boolean visible = true;

    private RenderType renderType;

    private Mesh mesh;
    private Texture texture = Texture2D.white();
    private Material material;

    private BoundingVolume boundingVolume = new BoundingVolume();

    public Geometry() {
        this(RenderType.OPAQUE);
    }

    public Geometry(RenderType renderType) {
        this.renderType = renderType;
        scene().addChangeListener((observable, oldValue, newValue) -> {
            if (visible) {
                if (oldValue != null) oldValue.getRenderQueue().remove(this, this.renderType);
                if (newValue != null) newValue.getRenderQueue().add(this, this.renderType);
            }
        });
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        if (this.visible == visible) return;
        this.visible = visible;
        if (visible) scene.ifPresent(scene -> scene.getRenderQueue().add(this, renderType));
        else scene.ifPresent(scene -> scene.getRenderQueue().remove(this, renderType));
    }

    public RenderType getRenderType() {
        return renderType;
    }

    public void setRenderType(RenderType renderType) {
        if (visible) {
            scene.ifPresent(scene -> {
                RenderQueue renderQueue = scene.getRenderQueue();
                renderQueue.remove(this, this.renderType);
                renderQueue.add(this, renderType);
            });
        }
        this.renderType = renderType;
    }

    public Mesh getMesh() {
        return mesh;
    }

    public void setMesh(Mesh mesh) {
        this.mesh = mesh;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public BoundingVolume getBoundingVolume() {
        return boundingVolume;
    }
}
