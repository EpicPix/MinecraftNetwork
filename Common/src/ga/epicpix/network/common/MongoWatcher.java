package ga.epicpix.network.common;

import com.mongodb.client.model.changestream.ChangeStreamDocument;
import org.bson.Document;

@Deprecated(forRemoval = true)
public abstract class MongoWatcher {

    public MongoWatcher(String database, String collection) {}

    public abstract void run(ChangeStreamDocument<Document> handle);

}
