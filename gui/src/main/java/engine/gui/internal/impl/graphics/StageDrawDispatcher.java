package engine.gui.internal.impl.graphics;

import engine.graphics.graph.DrawDispatcher;
import engine.graphics.graph.Frame;
import engine.graphics.graph.Renderer;
import engine.graphics.shader.ShaderResource;
import engine.gui.Parent;
import engine.gui.Scene;
import engine.gui.stage.Stage;

public final class StageDrawDispatcher implements DrawDispatcher {
    private final Stage stage;

    private GraphicsImpl graphics;

    public StageDrawDispatcher(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void init(ShaderResource resource) {
        graphics = new GraphicsImpl(resource);
    }

    @Override
    public void draw(Frame frame, ShaderResource resource, Renderer renderer) {
        Scene scene = stage.getScene();
        if (scene == null) return;
        scene.update();

        Parent root = scene.getRoot();
        if (!root.isVisible()) {
            return; // Invisible root, don't need render it.
        }

        graphics.setup(renderer, frame.getWidth(), frame.getHeight(), stage.getScaleX(), stage.getScaleY());
        root.getRenderer().render(root, graphics);
    }
}
