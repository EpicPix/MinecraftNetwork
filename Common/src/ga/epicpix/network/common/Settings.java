package ga.epicpix.network.common;

import com.mongodb.client.MongoCollection;
import org.bson.Document;

public class Settings {

    public static Object getSetting(String setting) {
        MongoCollection<Document> settings = Mongo.getCollection("data", "settings");
        Document doc = settings.find(new Document().append("id", setting)).first();
        if(doc==null) {
            return null;
        }else {
            Object value = doc.get("value");
            if(value instanceof Document) {
                return CommonUtils.convertDocument((Document) value);
            }
            return value;
        }
    }

    public static <T> T getSettingOrDefault(String settingid, T def) {
        Object setting = getSetting(settingid);
        if(setting==null) {
            setSetting(settingid, def);
            return def;
        }
        return (T) setting;
    }

    public static void setSetting(String setting, Object value) {
        if(!CommonUtils.isPrimitive(value) && !String.class.isAssignableFrom(value.getClass()) && !Document.class.isAssignableFrom(value.getClass())) {
            value = CommonUtils.toDocument(value);
        }
        MongoCollection<Document> settings = Mongo.getCollection("data", "settings");
        Document filter = new Document().append("id", setting);
        Document doc = settings.find(filter).first();
        if(doc==null) {
            settings.insertOne(new Document().append("id", setting).append("value", value));
        }else {
            settings.replaceOne(filter, new Document().append("id", setting).append("value", value));
        }
    }

}
