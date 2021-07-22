package ga.epicpix.network.common;

import com.mongodb.MongoNamespace;
import com.mongodb.client.model.changestream.ChangeStreamDocument;
import org.bson.Document;

@Deprecated(forRemoval = true)
public abstract class MongoWatcher {

    private final MongoNamespace namespace;

    public MongoWatcher(String database, String collection) {
        namespace = new MongoNamespace(database, collection);
    }

    public MongoNamespace getNamespace() {
        return namespace;
    }

    public abstract void run(ChangeStreamDocument<Document> handle);

}
