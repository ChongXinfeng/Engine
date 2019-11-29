package nullengine.client.rendering.gl;

public class DirectRenderer {

    private static final DirectRenderer INSTANCE = new DirectRenderer(0x100000);
    private GLBuffer buffer;
    private SingleBufferVAO vao;

    private DirectRenderer(int bufferSize) {
        vao = new SingleBufferVAO(GLBufferUsage.DYNAMIC_DRAW, GLDrawMode.TRIANGLES);
        buffer = GLBuffer.createDirectBuffer(bufferSize);
    }

    public static DirectRenderer getInstance() {
        return INSTANCE;
    }

    public GLBuffer getBuffer() {
        return buffer;
    }

    public void draw() {
        buffer.finish();
        vao.uploadData(buffer);
        vao.bind();
        buffer.getVertexFormat().applyAndEnable();
        vao.setDrawMode(buffer.getDrawMode());
        vao.drawArrays();
        vao.unbind();
    }
}