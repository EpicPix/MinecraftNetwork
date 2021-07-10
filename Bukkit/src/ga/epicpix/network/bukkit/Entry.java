package ga.epicpix.network.bukkit;

import com.mongodb.client.MongoChangeStreamCursor;
import com.mongodb.client.model.changestream.ChangeStreamDocument;
import com.mongodb.client.model.changestream.OperationType;
import ga.epicpix.network.bukkit.commands.TestCommand;
import ga.epicpix.network.common.*;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Entry extends JavaPlugin {

    public static Entry PLUGIN;

    public void onLoad() {
        PLUGIN = this;
        Language.loadLanguages();
        Language.startWatcher();
        BukkitCommon.setBungeeCord(Settings.getSettingOrDefault("BUNGEE_CORD", false));

        Mongo.registerWatcher(new MongoWatcher("data", "settings") {
            public void run(ChangeStreamDocument<Document> handle) {
                if(handle.getOperationType()!=OperationType.DELETE && handle.getOperationType()!=OperationType.DROP) {
                    Document fullDocument = Mongo.getCollection("data", "settings").find(handle.getDocumentKey()).first();
                    if(fullDocument.getString("id").equals("BUNGEE_CORD")) {
                        BukkitCommon.setBungeeCord(fullDocument.getBoolean("value"));
                    }
                }
            }
        });

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
