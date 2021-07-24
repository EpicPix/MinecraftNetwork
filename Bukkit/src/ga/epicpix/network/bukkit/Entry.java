package ga.epicpix.network.bukkit;

import com.mongodb.client.model.changestream.ChangeStreamDocument;
import com.mongodb.client.model.changestream.OperationType;
import ga.epicpix.network.bukkit.commands.TestCommand;
import ga.epicpix.network.common.*;
import ga.epicpix.network.common.ranks.Rank;
import ga.epicpix.network.common.ranks.RankManager;
import ga.epicpix.network.common.servers.ServerInfo;
import ga.epicpix.network.common.settings.SettingsManager;
import ga.epicpix.network.common.values.ValueType;
import ga.epicpix.network.common.websocket.*;
import ga.epicpix.network.common.websocket.requests.data.UpdateServerDataRequest;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Team;

import static ga.epicpix.network.common.servers.ServerInfo.ServerSignal;

public class Entry extends JavaPlugin {

    public static Entry PLUGIN;

    public static final long start = System.currentTimeMillis();

    public void onLoad() {
        PLUGIN = this;
        WebSocketConnection.setClientType(ClientType.BUKKIT);

        WebSocketConnection.setSignalHandler((opcode, data, requester) -> {
            ServerSignal signal = ServerSignal.getSignal(data.get("signal").getAsString());
            if (signal == ServerSignal.STOP) {
                System.out.println("Signal to stop the server received");
            }
        });

        WebSocketConnection.setSettingsUpdateHandler((opcode, data, requester) -> {
            if(data.get("name").getAsString().equals("BUNGEE_CORD")) {
                BukkitCommon.setBungeeCord(SettingsManager.getSettingOrDefault("BUNGEE_CORD", new ValueType(false)).getAsBoolean());
            }
        });

        WebSocketConnection.connect();

        ServerInfo.updateServer(Bukkit.getServerId(), new UpdateServerDataRequest.Data()
                .setType(ServerInfo.ServerType.UNKNOWN)
                .setOnlinePlayers(Bukkit.getOnlinePlayers().size())
                .setMaxPlayers(Bukkit.getMaxPlayers())
                .setVersion(BukkitCommon.getVersion())
                .setDetails(BukkitCommon.getDetails())
                .setBootMillis(start));

        ServerInfo.makeWebSocketServerOwner(Bukkit.getServerId());

        Runtime.getRuntime().addShutdownHook(new Thread(() -> ServerInfo.removeServer(BukkitCommon.getServerId())));
        BukkitCommon.setBungeeCord(SettingsManager.getSettingOrDefault("BUNGEE_CORD", new ValueType(false)).getAsBoolean());

        Mongo.registerWatcher(new MongoWatcher("data", "ranks") {
            public void run(ChangeStreamDocument<Document> handle) {
                if(handle.getOperationType()!=OperationType.DELETE && handle.getOperationType()!=OperationType.DROP) {
                    Document fullDocument = Mongo.getCollection("data", "ranks").find(handle.getDocumentKey()).first();
                    Rank rank = RankManager.getRank(fullDocument.getString("id"));
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
                            iteam.setPrefix(CommonUtils.componentsToString(rank.getPrefix()) + (rank.getPrefix().length==0?"":" ") + ChatColor.convertColorText("/" + rank.getNameColor() + "/"));
                            iteam.setSuffix((rank.getSuffix().length==0?"":ChatColor.convertColorText("/white/ ")) + CommonUtils.componentsToString(rank.getSuffix()));
                        });
                    }
                }
            }
        });

        Command.registerCommand(new TestCommand());
    }

    public void onEnable() {
        PluginListener.publicScoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Bukkit.getServer().getPluginManager().registerEvents(new PluginListener(), this);
    }

}
