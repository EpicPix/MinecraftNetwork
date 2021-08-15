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
import org.bukkit.permissions.Permissible;
import org.bukkit.permissions.Permission;
import org.spigotmc.SpigotConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class BukkitCommon {

    public static Object getPropertyManager() {
        Object MinecraftServer = getMinecraftServer();
        Object PropertyManager = Reflection.getValueOfField(MinecraftServer.getClass(), "propertyManager", MinecraftServer);
        if(PropertyManager==null) throw new NullPointerException("Could not get a PropertyManager instance.");
        if(PropertyManager.getClass().getSimpleName().endsWith("DedicatedServerSettings")) {
            PropertyManager = Reflection.getValueOfField(PropertyManager.getClass(), "properties", PropertyManager);
        }
        return PropertyManager;
    }

    public static String getServerId() {
        Object PropertyManager = getPropertyManager();
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
        info.type = ServerInfo.ServerType.UNKNOWN.getId();
        info.onlinePlayers = Bukkit.getOnlinePlayers().size();
        info.maxPlayers = Bukkit.getMaxPlayers();
        info.details = getDetails();
        info.version = getVersion();
        info.start = Entry.start;
        return info;
    }

    @SuppressWarnings("unchecked")
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
        if(SpigotConfig.bungee==bungee) {
            return;
        }
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

    public static String hyphenToCamelCase(String val) {
        StringBuilder n = new StringBuilder();
        boolean upper = false;
        for(char c : val.toCharArray()) {
            if(c=='-') {
                upper = true;
            }else {
                if(upper) {
                    c = Character.toUpperCase(c);
                    upper = false;
                }
                n.append(c);
            }
        }
        return n.toString();
    }

    public static void setPropertiesValue(String key, Object value) {
        Object PropertyManager = getPropertyManager();
        if(!PropertyManager.getClass().getSimpleName().endsWith("DedicatedServerProperties")) {
            Reflection.callMethodByClasses(PropertyManager.getClass(), "setProperty", PropertyManager, new Class<?>[] {String.class, Object.class}, key, value);
        }else {
            key = hyphenToCamelCase(key);
            Reflection.setValueOfField(PropertyManager.getClass(), key, PropertyManager, value);
        }
    }

    public static void setAllowNether(boolean allow) {
        setPropertiesValue("allow-nether", allow);
    }

    public static void setAllowEnd(boolean allow) {
        Object configuration = Reflection.getValueOfField(Bukkit.getServer().getClass(), "configuration", Bukkit.getServer());
        Reflection.callMethodByClasses(configuration.getClass(), "set", configuration, new Class<?>[] {String.class, Object.class}, "settings.allow-end", allow);
    }

    @SuppressWarnings("unchecked")
    public static void removeDefaultPermissions() {
        Object SimplePluginManager = Bukkit.getServer().getPluginManager();
        Map<Boolean, Set<Permission>> defaultPerms = (Map<Boolean, Set<Permission>>) Reflection.getValueOfField(SimplePluginManager.getClass(), "defaultPerms", SimplePluginManager);
        defaultPerms.get(false).clear();
        defaultPerms.get(true).clear();

        Map<String, Permission> permissions = (Map<String, Permission>) Reflection.getValueOfField(SimplePluginManager.getClass(), "permissions", SimplePluginManager);
        permissions.clear();

        Map<String, Map<Permissible, Boolean>> permSubs = (Map<String, Map<Permissible, Boolean>>) Reflection.getValueOfField(SimplePluginManager.getClass(), "permSubs", SimplePluginManager);
        permSubs.clear();
    }

    public static void removeCommandAliases() {
        ArrayList<String> remove = new ArrayList<>();
        Map<String, Command> map = getCommandMap();
        map.keySet().forEach((a) -> {
            if(a.contains(":")) remove.add(a);
        });
        remove.forEach(map::remove);
    }
}
