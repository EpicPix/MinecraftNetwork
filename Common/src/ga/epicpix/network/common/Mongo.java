package ga.epicpix.network.common;

import com.mongodb.MongoNamespace;
import com.mongodb.client.MongoChangeStreamCursor;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.changestream.ChangeStreamDocument;
import org.bson.Document;

import java.util.ArrayList;

public class Mongo {

    private static MongoClient client;
    private static boolean watcherRunning = false;

    public static MongoClient getMongoClient() {
        if(client==null) client = MongoClients.create(MongoCredentials.get().toConnectionString());
        return client;
    }

    private static final ArrayList<MongoWatcher> watchers = new ArrayList<>();

    public static void registerWatcher(MongoWatcher watcher) {
        if(!watcherRunning) {
            startWatcher();
        }
        watchers.add(watcher);
    }

    public static void startWatcher() {
        if(!watcherRunning) {
            final MongoChangeStreamCursor<ChangeStreamDocument<Document>> stream = getMongoClient().watch().cursor();
            Thread t = new Thread(() -> {
                while (true) {
                    try {
                        if (stream.hasNext()) {
                            ChangeStreamDocument<Document> next = stream.next();
                            MongoNamespace namespace = next.getNamespace();
                            for (MongoWatcher watcher : watchers) {
                                if (watcher.getNamespace().equals(namespace)) {
                                    watcher.run(next);
                                }
                            }
                        }
                    }catch(Exception e) {
                        return;
                    }
                }
            });
            t.setDaemon(true);
            t.start();
            watcherRunning = true;
        }
    }

    public static MongoCollection<Document> getCollection(String database, String collection) {
        return getMongoClient().getDatabase(database).getCollection(collection);
    }

    public static void disconnect() {
        getMongoClient().close();
    }
}
