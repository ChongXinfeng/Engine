package unknowndomain.engine.api.mod;

import unknowndomain.engine.api.util.versioning.ComparableVersion;
//TODO: collect mod's class loader, instance of mod main class, mod config, mod looger, config dir.
public class ModContainer {
	
    private final String modid;
    private final ComparableVersion version;
    
    private String name;
    
    private Object instance;

    public ModContainer(String modid, String version){
        this.modid = modid;
        this.version = new ComparableVersion(version);
    }

	public String getModid() {
		return modid;
	}

	public ComparableVersion getVersion() {
		return version;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Object getInstance() {
		return instance;
	}
}