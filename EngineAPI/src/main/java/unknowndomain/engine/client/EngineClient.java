package unknowndomain.engine.client;

import unknowndomain.engine.Engine;
import unknowndomain.engine.client.asset.AssetManager;
import unknowndomain.engine.client.asset.source.AssetSource;
import unknowndomain.engine.client.asset.source.AssetSourceManager;
import unknowndomain.engine.client.game.GameClient;
import unknowndomain.engine.client.rendering.display.GameWindow;
import unknowndomain.engine.client.rendering.texture.TextureManager;

public interface EngineClient extends Engine {

    GameWindow getWindow();

    AssetManager getAssetManager();

    AssetSourceManager getAssetSourceManager();

    TextureManager getTextureManager();

    AssetSource getEngineAssetSource();

    @Override
    GameClient getCurrentGame();
}