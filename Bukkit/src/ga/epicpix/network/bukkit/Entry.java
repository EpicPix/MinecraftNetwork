package ga.epicpix.network.bukkit;

import com.mongodb.client.MongoChangeStreamCursor;
import com.mongodb.client.model.changestream.ChangeStreamDocument;
import com.mongodb.client.model.changestream.OperationType;
import ga.epicpix.network.bukkit.commands.TestCommand;
import ga.epicpix.network.common.CommonUtils;
import ga.epicpix.network.common.Language;
import ga.epicpix.network.common.Mongo;
import ga.epicpix.network.common.Settings;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import org.bson.BsonDocument;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.plugin.java.JavaPlugin;
import org.spigotmc.SpigotConfig;

public class Entry extends JavaPlugin {

    public static Entry PLUGIN;

    public void onLoad() {
        PLUGIN = this;
        Language.loadLanguages();
        Language.startWatcher();
        BukkitCommon.setBungeeCord(Settings.getSettingOrDefault("BUNGEE_CORD", false));

        final MongoChangeStreamCursor<ChangeStreamDocument<Document>> stream = Mongo.getCollection("data", "settings").watch().cursor();
        Thread t = new Thread(() -> {
            while(true) {
                if(stream.hasNext()) {
                    ChangeStreamDocument<Document> next = stream.next();
                    if(next.getOperationType()!=OperationType.DELETE && next.getOperationType()!=OperationType.DROP) {
                        Document fullDocument = Mongo.getCollection("data", "settings").find(next.getDocumentKey()).first();
                        if(fullDocument.getString("id").equals("BUNGEE_CORD")) {
                            BukkitCommon.setBungeeCord(fullDocument.getBoolean("value"));
                        }
                    }
                }
            }
        });
        t.setDaemon(true);
        t.start();

        System.out.println("Server Info: " + BukkitCommon.getThisServer());
        System.out.println("Database Server Info: " + CommonUtils.getServerInfo(BukkitCommon.getServerId()));
        System.out.println("Updating DB");
        CommonUtils.updateServerInfo(BukkitCommon.getThisServer());
        System.out.println("Database Server Info: " + CommonUtils.getServerInfo(BukkitCommon.getServerId()));
        Runtime.getRuntime().addShutdownHook(new Thread(() -> CommonUtils.removeServerInfo(BukkitCommon.getServerId())));
        Command.registerCommand(new TestCommand());
    }

    public void onEnable() {
        Bukkit.getServer().getPluginManager().registerEvents(new PluginListener(), this);
    }

}
