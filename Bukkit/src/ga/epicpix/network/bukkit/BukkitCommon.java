package ga.epicpix.network.bukkit;

import ga.epicpix.network.common.*;
import ga.epicpix.network.common.servers.ServerDetails;
import ga.epicpix.network.common.servers.ServerInfo;
import ga.epicpix.network.common.servers.ServerVersion;
import ga.epicpix.network.common.text.ChatComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.entity.Player;
import org.spigotmc.SpigotConfig;

import java.util.Map;

public class BukkitCommon {

    public static String getServerId() {
        Object MinecraftServer = Reflection.getValueOfField(Bukkit.getServer().getClass(), "console", Bukkit.getServer());
        if(MinecraftServer==null) throw new NullPointerException("Could not get a MinecraftServer instance.");
        Object PropertyManager = Reflection.callMethod(MinecraftServer.getClass(), "getPropertyManager", MinecraftServer);
        //1.14+ fix
        if(PropertyManager==null) PropertyManager = Reflection.callMethod(MinecraftServer.getClass(), "getDedicatedServerProperties", MinecraftServer);
        if(PropertyManager==null) throw new NullPointerException("Could not get a PropertyManager instance.");
        return (String) Reflection.callMethod(PropertyManager.getClass(), "getString", PropertyManager, "server-id", null);
    }

    public static Object getMinecraftServer() {
        Object MinecraftServer = Reflection.getValueOfField(Bukkit.getServer().getClass(), "console", Bukkit.getServer());
        if(MinecraftServer==null) throw new NullPointerException("Could not get a MinecraftServer instance.");
        return MinecraftServer;
    }

    public static ServerVersion getVersion() {
        Object MinecraftServer = getMinecraftServer();
        return ServerVersion.getVersionByName((String) Reflection.callMethod(MinecraftServer.getClass(), "getVersion", MinecraftServer));
    }

    public static ServerDetails getDetails() {
        return new ServerDetails(Bukkit.getIp().isEmpty()?CommonUtils.possibleAddress().getHostAddress():Bukkit.getIp(), Bukkit.getPort());
    }

    public static ServerInfo getThisServer() {
        ServerInfo info = new ServerInfo(getServerId());
        if(info.id==null) System.err.println("Server Id is not defined.");
        info.type = ServerInfo.ServerType.UNKNOWN.id();
        info.onlinePlayers = Bukkit.getOnlinePlayers().size();
        info.maxPlayers = Bukkit.getMaxPlayers();
        info.details = getDetails();
        info.version = getVersion();
        info.start = Entry.start;
        return info;
    }

    public static Map<String, Command> getCommandMap() {
        SimpleCommandMap map = (SimpleCommandMap) Reflection.callMethod(Bukkit.getServer().getClass(), "getCommandMap", Bukkit.getServer());
        return (Map<String, Command>) Reflection.getValueOfField(map.getClass(), "knownCommands", map);
    }

    public static TextComponent toTextComponent(ChatComponent comp) {
        TextComponent tc = new TextComponent();
        tc.setText(comp.text);
        tc.setColor(ChatColor.WHITE);
        for(ChatColor c : ChatColor.values()) {
            if(c.getName().equalsIgnoreCase(comp.color)) {
                tc.setColor(c);
                break;
            }
        }
        tc.setBold(comp.bold);
        tc.setStrikethrough(comp.strikethrough);
        tc.setItalic(comp.italic);
        tc.setObfuscated(comp.obfuscated);
        tc.setUnderlined(comp.underlined);
        return tc;
    }

    public static void setBungeeCord(boolean bungee) {
        SpigotConfig.bungee = bungee;
        try {
            Object online = Reflection.getValueOfField(Bukkit.getServer().getClass(), "online", Bukkit.getServer());
            Reflection.setValueOfField(online.getClass(), "value", online, !bungee);
        } catch(Exception e) {}
        Object server = Reflection.getValueOfField(Bukkit.getServer().getClass(), "console", Bukkit.getServer());
        Reflection.setValueOfField(server.getClass(), "onlineMode", server, !bungee);
        System.out.println("BungeeCord mode set to: " + (bungee?"on":"off"));
        if(Entry.PLUGIN.isEnabled()) {
            Bukkit.getScheduler().runTask(Entry.PLUGIN, () -> {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.kickPlayer("An update occurred");
                }
            });
        }
    }

}
