package ga.epicpix.network.bukkit;

import org.bukkit.Bukkit;

public class ServerInfo {

    public enum ServerType {
        UNKNOWN
    }

    public String id;
    public ServerType type;
    public int maxPlayers;

    public static ServerInfo getThisServer() {
        Object MinecraftServer = Reflection.getFieldOfClass(Bukkit.getServer().getClass(), "console", Bukkit.getServer());
        if(MinecraftServer==null) throw new NullPointerException("Could not get a MinecraftServer instance.");
        Object PropertyManager = Reflection.callMethod(MinecraftServer.getClass(), "getPropertyManager", MinecraftServer);
        //1.14+ fix
        if(PropertyManager==null) PropertyManager = Reflection.callMethod(MinecraftServer.getClass(), "getDedicatedServerProperties", MinecraftServer);
        if(PropertyManager==null) throw new NullPointerException("Could not get a PropertyManager instance.");
        ServerInfo info = new ServerInfo();
        info.id = (String) Reflection.callMethod(PropertyManager.getClass(), "getString", PropertyManager, "server-id", null);
        if(info.id==null) System.err.println("Server Id is not defined.");
        info.type = ServerType.UNKNOWN;
        info.maxPlayers = Bukkit.getMaxPlayers();
        return info;
    }

    public String toString() {
        return "ServerInfo{id=" + (id==null?"null":("'" + id + "'")) + ", type=" + type + ", maxPlayers=" + maxPlayers + "}";
    }
}
