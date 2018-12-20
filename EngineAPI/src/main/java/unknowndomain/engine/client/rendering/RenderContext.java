package unknowndomain.engine.client.rendering;

import unknowndomain.engine.client.rendering.camera.Camera;
import unknowndomain.engine.client.rendering.display.GameWindow;
import unknowndomain.engine.client.rendering.texture.TextureManager;

public interface RenderContext {

    Camera getCamera();

    void setCamera(Camera camera);

    GameWindow getWindow();

    TextureManager getTextureManager();

    double partialTick();
}