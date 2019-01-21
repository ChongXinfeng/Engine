package unknowndomain.engine.event;

import org.apache.commons.lang3.Validate;
import unknowndomain.engine.Engine;
import unknowndomain.engine.client.rendering.Renderer;
import unknowndomain.engine.client.rendering.texture.TextureManager;
import unknowndomain.engine.client.resource.ResourceManager;
import unknowndomain.engine.registry.Registry;
import unknowndomain.engine.registry.RegistryManager;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Events related to the Engine
 */
public class EngineEvent implements Event {
    /**
     * The Engine
     */
    private Engine engine;

    protected EngineEvent(Engine e) {
        engine = e;
    }

    public Engine getEngine() {
        return engine;
    }

    /* Fired when the Engine starts constructing mods */
    public static class ModConstructionStart extends EngineEvent {
        public ModConstructionStart(Engine e) {
            super(e);
        }
    }

    /*
     * Fired when the Engine finishes constructing mods for the mods to initialize
     * for later stages
     */
    public static class ModInitializationEvent extends EngineEvent {
        public ModInitializationEvent(Engine e) {
            super(e);
        }
    }

    /* Fired when the Engine finishes constructing mods */
    public static class ModConstructionFinish extends EngineEvent {
        public ModConstructionFinish(Engine e) {
            super(e);
        }
    }

    public static class RegistryConstructionEvent extends EngineEvent {
        private final Map<Class<?>, Registry<?>> registries;

        public RegistryConstructionEvent(Engine e, Map<Class<?>, Registry<?>> registries) {
            super(e);
            this.registries = registries;
        }

        public void register(Registry<?> registry) {
            if (registries.containsKey(registry.getEntryType())) {
                throw new IllegalStateException(String.format("Registry<%s> has been registered.", registry.getEntryType().getSimpleName()));
            }
            registries.put(registry.getEntryType(), registry);
        }
    }

    /* Fired when the Engine starts the registration stage */
    public static class RegistrationStart extends EngineEvent {
        private final RegistryManager manager;

        public RegistrationStart(Engine e, RegistryManager registryManager) {
            super(e);
            manager = registryManager;
        }

        public RegistryManager getRegistryManager() {
            return manager;
        }
    }

    /* Fired when the Engine finishes the registration stage */
    public static class RegistrationFinish extends RegistrationStart {
        public RegistrationFinish(Engine e, RegistryManager registryManager) {
            super(e, registryManager);
        }
    }

    /* Fired when the Engine starts constructing resources */
    public static class ResourceConstructionStart extends EngineEvent {
        private final ResourceManager resourceManager;
        private final TextureManager textureManager;
        private final List<Renderer.Factory> renderers;

        public List<Renderer.Factory> getRenderers() {
            return Collections.unmodifiableList(renderers);
        }

        public ResourceConstructionStart(Engine e, ResourceManager resourceManager, TextureManager textureManager, List<Renderer.Factory> renderers) {
            super(e);
            this.resourceManager = resourceManager;
            this.textureManager = textureManager;
            this.renderers = renderers;
        }

        public ResourceManager getResourceManager() {
            return resourceManager;
        }

        public TextureManager getTextureManager() {
            return textureManager;
        }

        public void registerRenderer(Renderer.Factory renderer) {
            renderers.add(Validate.notNull(renderer));
        }
    }

    /* Fired when the Engine finishes constructing resources */
    public static class ResourceConstructionFinish extends EngineEvent {
        private final ResourceManager resourceManager;
        private final TextureManager textureManager;
        private final List<Renderer.Factory> renderers;

        public List<Renderer.Factory> getRenderers() {
            return Collections.unmodifiableList(renderers);
        }

        public ResourceConstructionFinish(Engine e, ResourceManager resourceManager, TextureManager textureManager, List<Renderer.Factory> renderers) {
            super(e);
            this.resourceManager = resourceManager;
            this.textureManager = textureManager;
            this.renderers = renderers;
        }

        public ResourceManager getResourceManager() {
            return resourceManager;
        }

        public TextureManager getTextureManager() {
            return textureManager;
        }

        public void registerRenderer(Renderer.Factory renderer) {
            renderers.add(Validate.notNull(renderer));
        }
    }

    /* Fired when the Engine finishes initializing and is ready to start games */
    public static class InitializationComplete extends EngineEvent {
        public InitializationComplete(Engine e) {
            super(e);
        }
    }
}
