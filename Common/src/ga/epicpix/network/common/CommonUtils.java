package ga.epicpix.network.common;

import com.google.gson.Gson;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Iterator;

public class CommonUtils {

    public static String urlEncode(String str) {
        if(Reflection.getMethod(URLEncoder.class, "encode", true, String.class, String.class) != null) {
            return (String) Reflection.callMethod(URLEncoder.class, "encode", false, str, StandardCharsets.UTF_8.toString());
        }else {
            return (String) Reflection.callMethod(URLEncoder.class, "encode", false, str, StandardCharsets.UTF_8);
        }
    }

    public static String toString(String string) {
        return string==null?"null":("'" + string + "'");
    }

    public static ArrayList<InetAddress> possibleAddresses() {
        try {
            Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();
            ArrayList<InetAddress> addresses = new ArrayList<>();
            while (e.hasMoreElements()) {
                NetworkInterface n = e.nextElement();
                Enumeration<InetAddress> ee = n.getInetAddresses();
                while (ee.hasMoreElements()) {
                    InetAddress i = ee.nextElement();
                    if (i instanceof Inet4Address) {
                        if (!i.isLoopbackAddress()) {
                            addresses.add(i);
                        }
                    }
                }
            }
            return addresses;
        } catch (SocketException e) {
            System.err.println("Failed to get possible addresses");
        }
        return new ArrayList<>();
    }

    public static InetAddress possibleAddress() {
        ArrayList<InetAddress> addresses = possibleAddresses();
        if(addresses.size()!=0) {
            return addresses.get(0);
        }
        return InetAddress.getLoopbackAddress();
    }

    public static <T> ArrayList<T> toList(T[] array) {
        return new ArrayList<>(Arrays.asList(array));
    }

    public static <T> ArrayList<T> iteratorToList(Iterator<T> iterator) {
        ArrayList<T> list = new ArrayList<>();
        while(iterator.hasNext()) {
            list.add(iterator.next());
        }
        return list;
    }

    public static Document toDocument(Object obj) {
        if(obj==null) return null;
        return Document.parse(new Gson().toJson(obj));
    }

    public static <T> T documentToObject(Document doc, Class<T> clazz) {
        if(doc==null) return null;
        try {
            return new Gson().fromJson(doc.toJson(), clazz);
        }catch(Exception e) {
            return null;
        }
    }

    public static <T> ArrayList<T> documentsToObjects(ArrayList<Document> documents, Class<T> clazz) {
        ArrayList<T> converted = new ArrayList<>();
        for(Document doc : documents) converted.add(documentToObject(doc, clazz));
        return converted;
    }

    public static ServerInfo getServerInfo(String server) {
        MongoCollection<Document> servers = Mongo.getCollection("data", "servers");
        return documentToObject(servers.find(Filters.eq("id", server)).first(), ServerInfo.class);
    }

    public static void updateServerInfo(ServerInfo info) {
        MongoCollection<Document> servers = Mongo.getCollection("data", "servers");
        Bson filter = Filters.eq("id", info.id);
        long amt = servers.countDocuments(filter);
        if(amt==0) {
            servers.insertOne(toDocument(info));
        }else {
            if(amt!=1) {
                servers.deleteMany(filter);
                servers.insertOne(toDocument(info));
            }else {
                servers.replaceOne(filter, toDocument(info));
            }
        }
    }

    public static void removeServerInfo(String server) {
        MongoCollection<Document> servers = Mongo.getCollection("data", "servers");
        servers.deleteMany(Filters.eq("id", server));
    }

}
