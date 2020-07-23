package engine.server;

import configuration.Config;
import configuration.io.ConfigIOUtils;

import java.nio.file.Path;

public class ServerConfig {

    private final Path configPath;
    private String serverIp;
    private int serverPort;
    private String game;

    public ServerConfig(){
        this(Path.of("server.json"));
    }

    public ServerConfig(Path path){
        configPath = path;
        resetToDefault();
    }

    public void resetToDefault(){
        serverIp = "";
        serverPort = 18104;
        game = "default";
    }

    public void load(){
        var config = ConfigIOUtils.load(configPath.toAbsolutePath());
        serverIp = config.getString("server-ip", "");
        serverPort = config.getInt("server-port", 18104);
        game = config.getString("game", "default");
    }

    public void save(){
        var config = new Config();
        config.set("server-ip", serverIp);
        config.set("server-port", serverPort);
        config.set("game", game);
        config.save(configPath.toAbsolutePath());
    }

    public String getServerIp() {
        return serverIp;
    }

    public int getServerPort() {
        return serverPort;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }
}
