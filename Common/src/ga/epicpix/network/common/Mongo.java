package ga.epicpix.network.common;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

@Deprecated(forRemoval = true)
public class Mongo {

    private static MongoClient client;

    public static void registerWatcher(MongoWatcher watcher) {}

    public static MongoCollection<Document> getCollection(String database, String collection) {
        if(client==null) client = MongoClients.create(MongoCredentials.get().toConnectionString());
        return client.getDatabase(database).getCollection(collection);
    }
}
