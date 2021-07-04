package ga.epicpix.network.bukkit;

import ga.epicpix.network.common.*;
import org.bukkit.Bukkit;

public class BukkitCommon {

    public static ServerInfo getThisServer() {
        Object MinecraftServer = Reflection.getValueOfField(Bukkit.getServer().getClass(), "console", Bukkit.getServer());
        if(MinecraftServer==null) throw new NullPointerException("Could not get a MinecraftServer instance.");
        Object PropertyManager = Reflection.callMethod(MinecraftServer.getClass(), "getPropertyManager", MinecraftServer);
        //1.14+ fix
        if(PropertyManager==null) PropertyManager = Reflection.callMethod(MinecraftServer.getClass(), "getDedicatedServerProperties", MinecraftServer);
        if(PropertyManager==null) throw new NullPointerException("Could not get a PropertyManager instance.");
        ServerInfo info = new ServerInfo();
        info.id = (String) Reflection.callMethod(PropertyManager.getClass(), "getString", PropertyManager, "server-id", null);
        if(info.id==null) System.err.println("Server Id is not defined.");
        info.type = ServerInfo.ServerType.UNKNOWN;
        info.maxPlayers = Bukkit.getMaxPlayers();
        ServerDetails details = new ServerDetails();
        details.ip = Bukkit.getIp().isEmpty()?CommonUtils.possibleAddress().getHostAddress():Bukkit.getIp();
        details.port = Bukkit.getPort();
        info.details = details;
        info.version = ServerVersion.getVersionByName((String) Reflection.callMethod(MinecraftServer.getClass(), "getVersion", MinecraftServer));
        return info;
    }

}
