package ga.epicpix.network.bukkit;

import com.google.gson.JsonObject;
import ga.epicpix.network.bukkit.commands.BukkitCommandPlayer;
import ga.epicpix.network.bukkit.commands.TestCommand;
import ga.epicpix.network.common.Reflection;
import ga.epicpix.network.common.servers.ServerManager;
import ga.epicpix.network.common.servers.ServerVersion;
import ga.epicpix.network.common.text.ChatColor;
import ga.epicpix.network.common.ranks.Rank;
import ga.epicpix.network.common.servers.ServerInfo;
import ga.epicpix.network.common.settings.SettingsManager;
import ga.epicpix.network.common.text.ChatComponent;
import ga.epicpix.network.common.values.ValueType;
import ga.epicpix.network.common.websocket.ClientType;
import ga.epicpix.network.common.websocket.Errorable;
import ga.epicpix.network.common.websocket.WebSocketConnection;
import ga.epicpix.network.common.websocket.requests.data.UpdateServerDataRequest;
import org.bukkit.Bukkit;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Team;
import org.spigotmc.SpigotConfig;
import org.spigotmc.SpigotWorldConfig;

import java.util.Map;

import static ga.epicpix.network.common.servers.ServerInfo.ServerSignal;

public class Entry extends JavaPlugin {

    public static Entry PLUGIN;

    public static final long start = System.currentTimeMillis();

    private static Thread shutdownHook;

    public void onLoad() {

        BukkitCommon.setAllowNether(false);
        BukkitCommon.setAllowEnd(false);
        SpigotConfig.config.set("world-settings.default.verbose", false);

        PLUGIN = this;
        WebSocketConnection.setClientType(ClientType.BUKKIT);

        WebSocketConnection.setSignalHandler((opcode, data, requester) -> {
            ServerSignal signal = ServerSignal.getSignal(data.get("signal").getAsString());
            if (signal == ServerSignal.STOP) {
                System.out.println("Signal to stop the server received");
            }
        });

        WebSocketConnection.setSettingsUpdateHandler((opcode, data, requester) -> {
            if(isEnabled()) {
                JsonObject setting = data.getAsJsonObject("setting");
                if (setting.get("stringVersion").getAsString().equals("BUNGEE_CORD")) {
                    BukkitCommon.setBungeeCord(ValueType.getValueTypeFromJson(setting.getAsJsonObject("value")).getAsBoolean());
                }
            }
        });

        WebSocketConnection.setRankUpdateHandler((opcode, data, requester) -> {
            Rank rank = Rank.rankFromJsonObject(data.getAsJsonObject("rank"));
            Team team = null;
            for(Team iteam : PluginListener.teams) {
                if(iteam.getName().contains(rank.getId())) {
                    team = iteam;
                    break;
                }
            }
            if(team!=null) {
                Team iteam = team;
                Bukkit.getScheduler().runTask(PLUGIN, () -> {
                    iteam.setPrefix(ChatComponent.componentsToString(rank.getPrefix()) + (rank.getPrefix().length==0?"":" ") + ChatColor.convertColorText("/" + rank.getNameColor() + "/"));
                    iteam.setSuffix((rank.getSuffix().length==0?"":ChatColor.convertColorText("/white/ ")) + ChatComponent.componentsToString(rank.getSuffix()));
                });
                for(String p : iteam.getEntries()) {
                    Player player = Bukkit.getPlayer(p);
                    if(player!=null) {
                        player.recalculatePermissions();
                    }
                }
            }
        });

        WebSocketConnection.connect();
        if(ServerVersion.load().hasFailed()) {
            System.out.println("Could not load versions!");
            Bukkit.shutdown();
            return;
        }
        if(BukkitCommon.getVersion()==ServerVersion.UNKNOWN) {
            System.out.println("Server version not found or not supported!");
            Bukkit.shutdown();
            return;
        }

        ServerManager.updateServer(Bukkit.getServerId(), new UpdateServerDataRequest.Data()
                .setType(ServerInfo.ServerType.UNKNOWN)
                .setOnlinePlayers(Bukkit.getOnlinePlayers().size())
                .setMaxPlayers(Bukkit.getMaxPlayers())
                .setVersion(BukkitCommon.getVersion())
                .setDetails(BukkitCommon.getDetails())
                .setBootMillis(start));

        ServerManager.makeWebSocketServerOwner(Bukkit.getServerId());

        Runtime.getRuntime().addShutdownHook(shutdownHook = new Thread(() -> ServerManager.removeServer(BukkitCommon.getServerId())));
        Errorable<ValueType> val = SettingsManager.getSettingOrDefault("BUNGEE_CORD", new ValueType(false));
        if(val.hasFailed()) {
            BukkitCommon.setBungeeCord(false);
        }else {
            BukkitCommon.setBungeeCord(val.getValue().getAsBoolean());
        }
        Command.registerCommand(new TestCommand());
    }

    public void onEnable() {
        PluginListener.publicScoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Bukkit.getServer().getPluginManager().registerEvents(new PluginListener(), this);
        Bukkit.getScheduler().runTask(this, () -> {
            BukkitCommon.removeDefaultPermissions();
            BukkitCommon.removeCommandAliases();
        });
    }

    public void onDisable() {
        shutdownHook.start();
        Runtime.getRuntime().removeShutdownHook(shutdownHook);
    }

}
