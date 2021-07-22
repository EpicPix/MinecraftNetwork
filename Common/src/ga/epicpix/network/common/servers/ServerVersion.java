package ga.epicpix.network.common.servers;

import com.google.gson.JsonObject;
import ga.epicpix.network.common.SerializableJson;

import java.util.*;

public record ServerVersion(int protocol, String name) implements SerializableJson {

    public static final ArrayList<ServerVersion> VERSIONS = new ArrayList<>();

    public static final ServerVersion UNKNOWN;

    static {
        VERSIONS.add(UNKNOWN = new ServerVersion(-1, "???"));

        VERSIONS.add(new ServerVersion(47, "1.8"));
        VERSIONS.add(new ServerVersion(47, "1.8.1"));
        VERSIONS.add(new ServerVersion(47, "1.8.2"));
        VERSIONS.add(new ServerVersion(47, "1.8.3"));
        VERSIONS.add(new ServerVersion(47, "1.8.4"));
        VERSIONS.add(new ServerVersion(47, "1.8.5"));
        VERSIONS.add(new ServerVersion(47, "1.8.6"));
        VERSIONS.add(new ServerVersion(47, "1.8.7"));
        VERSIONS.add(new ServerVersion(47, "1.8.8"));
        VERSIONS.add(new ServerVersion(107, "1.9"));
        VERSIONS.add(new ServerVersion(108, "1.9.1"));
        VERSIONS.add(new ServerVersion(109, "1.9.2"));
        VERSIONS.add(new ServerVersion(110, "1.9.3"));
        VERSIONS.add(new ServerVersion(110, "1.9.4"));
        VERSIONS.add(new ServerVersion(210, "1.10"));
        VERSIONS.add(new ServerVersion(210, "1.10.1"));
        VERSIONS.add(new ServerVersion(210, "1.10.2"));
        VERSIONS.add(new ServerVersion(315, "1.11"));
        VERSIONS.add(new ServerVersion(316, "1.11.1"));
        VERSIONS.add(new ServerVersion(316, "1.11.2"));
        VERSIONS.add(new ServerVersion(335, "1.12"));
        VERSIONS.add(new ServerVersion(338, "1.12.1"));
        VERSIONS.add(new ServerVersion(340, "1.12.2"));
        VERSIONS.add(new ServerVersion(393, "1.13"));
        VERSIONS.add(new ServerVersion(401, "1.13.1"));
        VERSIONS.add(new ServerVersion(404, "1.13.2"));
        VERSIONS.add(new ServerVersion(477, "1.14"));
        VERSIONS.add(new ServerVersion(480, "1.14.1"));
        VERSIONS.add(new ServerVersion(485, "1.14.2"));
        VERSIONS.add(new ServerVersion(490, "1.14.3"));
        VERSIONS.add(new ServerVersion(498, "1.14.4"));
        VERSIONS.add(new ServerVersion(573, "1.15"));
        VERSIONS.add(new ServerVersion(575, "1.15.1"));
        VERSIONS.add(new ServerVersion(578, "1.15.2"));
        VERSIONS.add(new ServerVersion(735, "1.16"));
        VERSIONS.add(new ServerVersion(736, "1.16.1"));
        VERSIONS.add(new ServerVersion(751, "1.16.2"));
        VERSIONS.add(new ServerVersion(753, "1.16.3"));
        VERSIONS.add(new ServerVersion(754, "1.16.4"));
        VERSIONS.add(new ServerVersion(754, "1.16.5"));
        VERSIONS.add(new ServerVersion(755, "1.17"));
    }

    public String toString() {
        return name();
    }

    public static ServerVersion getVersionByName(String name) {
        for (ServerVersion version : VERSIONS) {
            if (version.name().equals(name)) {
                return version;
            }
        }
        return UNKNOWN;
    }

    public static ServerVersion getVersionByProtocol(int protocol) {
        for (ServerVersion version : VERSIONS) {
            if (version.protocol() == protocol) {
                return version;
            }
        }
        return UNKNOWN;
    }

    public JsonObject toJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("name", name);
        obj.addProperty("protocol", protocol);
        return obj;
    }
}
