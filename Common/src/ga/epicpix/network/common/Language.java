package ga.epicpix.network.common;

import com.mongodb.client.MongoChangeStreamCursor;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.changestream.ChangeStreamDocument;
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

    public static void startWatcher() {
        final MongoChangeStreamCursor<ChangeStreamDocument<Document>> stream = Mongo.getCollection("data", "languages").watch().cursor();
        Thread t = new Thread(() -> {
            while(true) {
                if(stream.hasNext()) {
                    stream.next();
                    loadLanguages();
                }
            }
        });
        t.setDaemon(true);
        t.start();
    }

    public static class LanguageEntry {
        public String key;
        public String value;

        public LanguageEntry() {}

        public LanguageEntry(String k, String v) {
            key = k;
            value = v;
        }
    }

    public String id;
    public ArrayList<LanguageEntry> entries = new ArrayList<>();

    public Language setId(String id) {
        this.id = id;
        return this;
    }

    public Language addEntry(LanguageEntry entry) {
        entries.add(entry);
        return this;
    }

    public String getTranslation(String key) {
        for(LanguageEntry entry : entries) {
            if(entry.key.equals(key)) {
                return entry.value;
            }
        }
        return key;
    }

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
        return CommonUtils.getDefaultLanguage().getTranslation(key);
    }

    public static Language getLanguage(String language) {
        for(Language lang : loadedLanguages) {
            if(lang.id.equals(language)) {
                return lang;
            }
        }
        return null;
    }

    public static ArrayList<LanguageEntry> getMissingKeys(Language x, Language y) {
        ArrayList<LanguageEntry> missingKeys = new ArrayList<>();
        for (LanguageEntry e : x.entries) {
            boolean found = false;
            for (LanguageEntry f : y.entries) {
                if (e.key.equals(f.key)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                missingKeys.add(e);
            }
        }
        return missingKeys;
    }

    public static Language getLanguageOrDefault(String language, Language def) {
        Language lang = getLanguage(language);
        if(lang==null) {
            loadLanguages();
            lang = getLanguage(language);
            if(lang==null) {
                if(def.id.equals(language)) {
                    Mongo.getCollection("data", "languages").insertOne(CommonUtils.toDocument(def));
                    loadLanguages();
                }
                return def;
            }
        }
        ArrayList<LanguageEntry> missingKeys = getMissingKeys(def, lang);
        if(missingKeys.size()!=0) {
            loadLanguages();
            lang = getLanguage(language);
            if(lang==null) {
                if(def.id.equals(language)) {
                    Mongo.getCollection("data", "languages").insertOne(CommonUtils.toDocument(def));
                    loadLanguages();
                }
                return def;
            }
            missingKeys = getMissingKeys(def, lang);
            if(missingKeys.size()!=0) {
                if(def.id.equals(language)) {
                    lang.entries.addAll(missingKeys);
                    Mongo.getCollection("data", "languages").replaceOne(new Document().append("id", language), CommonUtils.toDocument(lang));
                    loadLanguages();
                    return getLanguage(language);
                }
            }
        }
        return lang;
    }

}
