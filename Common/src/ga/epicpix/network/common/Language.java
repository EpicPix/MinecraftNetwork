package ga.epicpix.network.common;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.changestream.ChangeStreamDocument;
import org.bson.Document;

import java.util.ArrayList;

import static ga.epicpix.network.common.ChatColor.convertColorText;

public class Language {

    public static final ArrayList<Language> loadedLanguages = new ArrayList<>();

    public static void loadLanguages() {
        loadedLanguages.clear();
        MongoCollection<Document> dlanguages = Mongo.getCollection("data", "languages");
        loadedLanguages.addAll(CommonUtils.documentsToObjects(CommonUtils.iteratorToList(dlanguages.find().iterator()), Language.class));
    }

    public static record LanguageEntry(String key, String value) {}

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
                return convertColorText(entry.value);
            }
        }
        return convertColorText(key);
    }

    public static String getTranslation(String key, String language) {
        for(Language lang : loadedLanguages) {
            if(lang.id.equalsIgnoreCase(language)) {
                for(LanguageEntry entry : lang.entries) {
                    if(entry.key.equals(key)) {
                        return convertColorText(entry.value);
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
