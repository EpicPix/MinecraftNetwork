package ga.epicpix.network.common;

import com.google.gson.Gson;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static java.util.Map.Entry;

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
        Document out = Document.parse(new Gson().toJson(obj));
        if(Types.getType(obj.getClass())!=null) {
            out.append("TYPE", Types.getType(obj.getClass()));
        }
        return out;
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

    public static boolean isPrimitive(Object obj) {
        Class<?> clazz = obj.getClass();
        if(clazz.isPrimitive()) {
            return true;
        }
        return clazz==Boolean.class || clazz==Byte.class || clazz==Short.class || clazz==Integer.class || clazz==Long.class || clazz==Character.class || clazz==Float.class || clazz==Double.class;
    }

    public static Object convertDocument(Document document) {
        Object type = document.get("TYPE");
        if(type==null || type.getClass()!=String.class) return document;

        Class<?> clazz = Types.getType((String) type);
        if(clazz!=null) {
            return documentToObject(document, clazz);
        }else {
            return document;
        }
    }

    public static <K, V> K getValueByKey(V value, Map<K, V> map) {
        for(Entry<K, V> entry : map.entrySet()) {
            if(entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }
        return null;
    }

    public static Language getDefaultLanguage() {
        return Language.getLanguageOrDefault(Settings.getSettingOrDefault("DEFAULT_LANGUAGE", "ENGLISH"), DefaultLanguage.ENGLISH);
    }

    public static Rank getDefaultRank() {
        for(Rank r : Rank.getRanks()) {
            if(r.def) {
                return r;
            }
        }
        throw new IllegalStateException("No default rank found.");
    }

    public static Document set(Document doc) {
        return new Document().append("$set", doc);
    }

    public static String makeStringLength(String string, int length, String c) {
        while(string.length() <= length) {
            string = c + string;
        }
        return string;
    }

    public static String componentsToString(ChatComponent[] components) {
        Boolean bold = null, strikethrough = null, italic = null, obfuscated = null, underlined = null;
        String color = "white";

        String generated = "";
        for(ChatComponent component : components) {
            boolean reupdate = false;
            if(component.bold!=null && component.bold!=bold) {
                bold = component.bold;
                reupdate = true;
            }
            if(component.strikethrough!=null && component.strikethrough!=strikethrough) {
                strikethrough = component.strikethrough;
                reupdate = true;
            }
            if(component.italic!=null && component.italic!=italic) {
                italic = component.italic;
                reupdate = true;
            }
            if(component.obfuscated!=null && component.obfuscated!=obfuscated) {
                obfuscated = component.obfuscated;
                reupdate = true;
            }
            if(component.underlined!=null && component.underlined!=underlined) {
                underlined = component.underlined;
                reupdate = true;
            }
            if(component.color!=null && !component.color.equals(color)) {
                color = component.color;
                reupdate = true;
            }

            if(reupdate) {
                generated += ChatColor.convertColorText("/" + color + "/");
                if(bold) generated += ChatColor.convertColorText("/bold/");
                if(strikethrough) generated += ChatColor.convertColorText("/strikethrough/");
                if(italic) generated += ChatColor.convertColorText("/italic/");
                if(obfuscated) generated += ChatColor.convertColorText("/obfuscated/");
                if(underlined) generated += ChatColor.convertColorText("/underlined/");
            }
            generated += component.text;
        }

        return generated;
    }

    public static boolean hasInternetAccess() {
        try {
            Socket socket = new Socket();
            socket.setSoTimeout(2000);
            socket.connect(new InetSocketAddress("google.com", 443));
            socket.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static final int ENC_SIZE = 32;

    public static String createHash(String data) {
        byte[] bytes = data.getBytes(StandardCharsets.UTF_8);

        int[] ks = new int[ENC_SIZE];
        for(int i = 0; i<ks.length; i++) {
            ks[i] = (char) (data.charAt(i%data.length()) ^ ks[i]);
        }
        for(int i = 0; i<bytes.length; i++) {
            int indx = i%ks.length;
            ks[indx] = bytes[i] - i * 11;
            if(indx!=0) {
                ks[indx] += ks[indx-1] ^ (indx*21-16);
            }
            ks[indx] = ks[indx] & 0xff;
        }
        for(int t = 0; t<0x100000; t++) {
            ks[t%ks.length] ^= ks[(t+3)%ks.length]+t*9;
        }
        String safe = "";
        for(int c : ks) {
            String x = Integer.toHexString(c&0xff);
            while(x.length()<2) {
                x = "0" + x;
            }
            safe += x;
        }
        return safe;
    }

    public static boolean validateHash(String hashed, String data) {
        return hashed.length()==ENC_SIZE*2 && createHash(data).equals(hashed);
    }

    public static String removeColorCodes(String str) {
        StringBuilder generated = new StringBuilder();
        for(int i = 0; i<str.length(); i++) {
            char c = str.charAt(i);
            if(c==ChatColor.COLOR_CHAR) {
                i++;
            }else {
                generated.append(str);
            }
        }
        return generated.toString();
    }

    public static String repeat(String x, int amount) {
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i<amount; i++) {
            builder.append(x);
        }
        return builder.toString();
    }

    public static String replaceAt(String str, int at, String to) {
        StringBuilder n = new StringBuilder(str);
        for(int i = 0; i<to.length(); i++) {
            n.setCharAt(i + at, to.charAt(i));
        }
        return n.toString();
    }

    public static String replaceCharactersSpecial(String in, String check, char what, char with) {
        int amt = Math.min(in.length(), check.length());
        int left = Math.max(0, in.length()-check.length());
        StringBuilder gen = new StringBuilder();
        for(int i = 0; i<amt; i++) {
            if(check.charAt(i)==what) {
                gen.append(with);
            }else {
                gen.append(in.charAt(i));
            }
        }
        gen.append(in.substring(in.length() - left));
        return gen.toString();
    }

}
