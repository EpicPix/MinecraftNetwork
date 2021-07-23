package ga.epicpix.network.common;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

@Deprecated(forRemoval = true)
public class Mongo {

    private static MongoClient client;

    public static MongoClient getMongoClient() {
        if(client==null) client = MongoClients.create(MongoCredentials.get().toConnectionString());
        return client;
    }

    public static void registerWatcher(MongoWatcher watcher) {}

    public static MongoCollection<Document> getCollection(String database, String collection) {
        return getMongoClient().getDatabase(database).getCollection(collection);
    }
}
