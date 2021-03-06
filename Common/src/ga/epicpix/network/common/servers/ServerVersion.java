package ga.epicpix.network.common.servers;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import ga.epicpix.network.common.SerializableJson;
import ga.epicpix.network.common.annotations.ChecksPermission;
import ga.epicpix.network.common.modules.ModulePermission;
import ga.epicpix.network.common.net.websocket.Errorable;
import ga.epicpix.network.common.net.websocket.WebSocketRequester;
import ga.epicpix.network.common.net.websocket.requests.GetVersionsRequest;

import java.util.*;

public class ServerVersion implements SerializableJson {

    private static final List<ServerVersion> VERSIONS = new ArrayList<>();

    //Use this like it's null for this type
    public static final ServerVersion UNKNOWN = new ServerVersion(-1, "???");

    private int protocolVersion;
    private String stringVersion;

    public ServerVersion(int protocolVersion, String stringVersion) {
        this.protocolVersion = protocolVersion;
        this.stringVersion = stringVersion;
    }

    public int getProtocolVersion() {
        return protocolVersion;
    }

    public String getStringVersion() {
        return stringVersion;
    }

    public String toString() {
        return getStringVersion();
    }

    public static ServerVersion getVersionByName(String name) {
        for (ServerVersion version : VERSIONS) {
            if (version.getStringVersion().equals(name)) {
                return version;
            }
        }
        return UNKNOWN;
    }

    public static ServerVersion getVersionByProtocol(int protocol) {
        for (ServerVersion version : VERSIONS) {
            if (version.getProtocolVersion() == protocol) {
                return version;
            }
        }
        return UNKNOWN;
    }

    // This is required to be called if you want to use any version methods
    @ChecksPermission(ModulePermission.GET_VERSIONS)
    public static Errorable<Boolean> load() {
        JsonObject data = WebSocketRequester.sendRequest(GetVersionsRequest.build());
        if(!data.get("ok").getAsBoolean()) {
            return new Errorable<>(data.get("errno").getAsInt());
        }
        VERSIONS.clear();
        for(JsonElement e : data.getAsJsonArray("versions")) {
            VERSIONS.add(new Gson().fromJson(e, ServerVersion.class));
        }
        return new Errorable<>(true);
    }

    public JsonObject toJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("stringVersion", stringVersion);
        obj.addProperty("protocolVersion", protocolVersion);
        return obj;
    }
}
