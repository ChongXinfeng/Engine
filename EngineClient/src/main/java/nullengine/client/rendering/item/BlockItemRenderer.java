package nullengine.client.rendering.item;

import nullengine.block.Block;
import nullengine.client.rendering.block.BlockRenderManager;
import nullengine.client.rendering.gl.DirectRenderer;
import nullengine.client.rendering.gl.GLBuffer;
import nullengine.client.rendering.gl.GLDrawMode;
import nullengine.client.rendering.gl.vertex.GLVertexFormats;
import nullengine.item.BlockItem;
import nullengine.item.ItemStack;

public class BlockItemRenderer implements ItemRenderer {

    @Override
    public void init() {
    }

    @Override
    public void render(ItemStack itemStack, float partial) {
        Block block = ((BlockItem) itemStack.getItem()).getBlock();
        DirectRenderer directRenderer = DirectRenderer.getInstance();
        GLBuffer buffer = directRenderer.getBuffer();
        buffer.begin(GLDrawMode.TRIANGLES, GLVertexFormats.POSITION_COLOR_ALPHA_TEXTURE_NORMAL);
        BlockRenderManager.instance().generateMesh(block, buffer);
        directRenderer.draw();
    }

    @Override
    public void dispose() {

    }
}
