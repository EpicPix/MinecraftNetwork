package ga.epicpix.network.common;

import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.util.ArrayList;

public class Language {

    public static final String TYPE = "LANGUAGE";

    public static final ArrayList<Language> loadedLanguages = new ArrayList<>();

    public static void loadLanguages() {
        loadedLanguages.clear();
        MongoCollection<Document> dlanguages = Mongo.getCollection("data", "languages");
        loadedLanguages.addAll(CommonUtils.documentsToObjects(CommonUtils.iteratorToList(dlanguages.find().iterator()), Language.class));
    }

    public static class LanguageEntry {
        public String key;
        public String value;
    }

    public String id;
    public LanguageEntry[] entries;

    public static String getTranslation(String key, String language) {
        for(Language lang : loadedLanguages) {
            if(lang.id.equalsIgnoreCase(language)) {
                for(LanguageEntry entry : lang.entries) {
                    if(entry.key.equals(key)) {
                        return entry.value;
                    }
                }
                break;
            }
        }
        return key;
    }

}
