package engine.gui.graphics;

import engine.graphics.item.ItemRenderManager;
import engine.graphics.util.DrawMode;
import engine.graphics.vertex.VertexDataBuffer;
import engine.graphics.vertex.VertexFormat;
import engine.graphics.voxel.VoxelGraphics;
import engine.gui.control.ItemView;
import engine.item.BlockItem;
import engine.item.ItemStack;
import org.joml.Matrix4f;

public class ItemViewRenderer extends NodeRenderer<ItemView> {

    public static final ItemViewRenderer INSTANCE = new ItemViewRenderer();

    @Override
    public void render(ItemView node, Graphics graphics) {
        ItemStack itemStack = node.getItemStack();
        if (itemStack.isNotEmpty()) {
            VertexDataBuffer buffer = VertexDataBuffer.currentThreadBuffer();
            buffer.begin(VertexFormat.POSITION_COLOR_ALPHA_TEX_COORD_NORMAL);
            ItemRenderManager.instance().generateMesh(buffer, itemStack, 0);
            buffer.finish();
            float size = node.size().get();
            Matrix4f oldMatrix = graphics.getTransform(new Matrix4f());
            Matrix4f modelMatrix = graphics.getTransform(new Matrix4f());
            if (itemStack.getItem() instanceof BlockItem) {
                modelMatrix.translate(size * 0.5f, size * 0.5f, 0)
                        .rotate((float) -Math.PI / 4f, 0, 1, 0)
                        .rotate((float) -Math.PI / 6f, 1, 0, -1)
                        .scale(size * 0.55f, -size * 0.55f, size * 0.55f);
            } else {
                modelMatrix.translate(size * 0.5f, size * 0.5f, 0)
                        .rotate((float) Math.PI, 0, 1, 0)
                        .scale(size, -size, 1);
            }
            graphics.setTransform(modelMatrix);
            graphics.drawStreamedMesh(DrawMode.TRIANGLES, buffer, VoxelGraphics.getVoxelTextureAtlas().getTexture());
            graphics.setTransform(oldMatrix);
        }
    }
}
