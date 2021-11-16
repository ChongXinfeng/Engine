package engine.graphics.vulkan;

import engine.graphics.backend.ResourceFactory;
import engine.graphics.mesh.InstancedMesh;
import engine.graphics.mesh.MultiBufMesh;
import engine.graphics.mesh.SingleBufMesh;
import engine.graphics.texture.Sampler;
import engine.graphics.texture.Texture2D;
import engine.graphics.texture.TextureCubeMap;
import engine.graphics.vulkan.device.LogicalDevice;

public class VKResourceFactory implements ResourceFactory {

    private final LogicalDevice device;

    public VKResourceFactory(LogicalDevice device) {
        this.device = device;
    }

    @Override
    public Texture2D.Builder createTexture2DBuilder() {
        return null;
    }

    @Override
    public Texture2D getWhiteTexture2D() {
        return null;
    }

    @Override
    public TextureCubeMap.Builder createTextureCubeMapBuilder() {
        return null;
    }

    @Override
    public Sampler.Builder createSamplerBuilder() {
        return null;
    }

    @Override
    public MultiBufMesh.Builder createMultiBufMeshBuilder() {
        return null;
    }

    @Override
    public SingleBufMesh.Builder createSingleBufMeshBuilder() {
        return null;
    }

    @Override
    public <E> InstancedMesh.Builder<E> createInstancedMeshBuilder() {
        return null;
    }
}
