package engine.graphics.gl.texture;

import engine.graphics.gl.util.GLHelper;
import engine.graphics.image.ReadOnlyImage;
import engine.graphics.texture.ColorFormat;
import engine.graphics.texture.Texture2D;
import org.lwjgl.opengl.GL32C;
import org.lwjgl.opengl.GL45C;

import java.nio.ByteBuffer;

public final class GLTexture2DMultiSample extends GLTexture implements Texture2D, GLFrameBuffer.Attachable {

    private final int width;
    private final int height;

    private final int samples;
    private final boolean fixedSampleLocations;

    public static Builder builder() {
        return new Builder();
    }

    private GLTexture2DMultiSample(GLColorFormat format, int width, int height, int samples, boolean fixedSampleLocations) {
        super(GL32C.GL_TEXTURE_2D_MULTISAMPLE, format);
        this.width = width;
        this.height = height;
        this.samples = samples;
        this.fixedSampleLocations = fixedSampleLocations;
        if (GLHelper.isSupportARBDirectStateAccess()) {
            GL45C.glTexStorage2DMultisample(id, this.samples, format.internalFormat, width, height, this.fixedSampleLocations);
        } else {
            GL32C.glTexImage2DMultisample(GL32C.GL_TEXTURE_2D_MULTISAMPLE, this.samples, format.internalFormat, width, height, this.fixedSampleLocations);
        }
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public void upload(int level, ReadOnlyImage image) {
        throw new UnsupportedOperationException("upload");
    }

    @Override
    public void upload(int level, int offsetX, int offsetY, ReadOnlyImage image) {
        throw new UnsupportedOperationException("upload");
    }

    @Override
    public void upload(int level, int width, int height, ByteBuffer pixels) {
        throw new UnsupportedOperationException("upload");
    }

    @Override
    public void upload(int level, int offsetX, int offsetY, int width, int height, ByteBuffer pixels) {
        throw new UnsupportedOperationException("upload");
    }

    @Override
    public boolean isMultiSample() {
        return true;
    }

    @Override
    public int getSamples() {
        return samples;
    }

    public boolean isFixedSampleLocations() {
        return fixedSampleLocations;
    }

    @Override
    public ColorFormat getFormat() {
        return null;
    }

    public static final class Builder {
        private GLColorFormat format;
        private int samples = 0;
        private boolean fixedSampleLocations = false;

        private Builder() {
        }

        public Builder format(ColorFormat format) {
            this.format = GLColorFormat.valueOf(format);
            return this;
        }

        public Builder samples(int samples) {
            this.samples = samples;
            return this;
        }

        public Builder fixedSampleLocations() {
            this.fixedSampleLocations = true;
            return this;
        }

        public GLTexture2DMultiSample build(int width, int height) {
            return new GLTexture2DMultiSample(format, width, height, samples, fixedSampleLocations);
        }
    }
}
