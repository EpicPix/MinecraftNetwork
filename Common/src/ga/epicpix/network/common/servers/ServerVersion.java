package ga.epicpix.network.common.servers;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import ga.epicpix.network.common.SerializableJson;
import ga.epicpix.network.common.http.websocket.Errorable;
import ga.epicpix.network.common.http.websocket.Opcodes;
import ga.epicpix.network.common.http.websocket.requests.Request;
import ga.epicpix.network.common.http.websocket.requests.data.GetVersionsRequest;

import java.util.*;

public class ServerVersion implements SerializableJson {

    private static final ArrayList<ServerVersion> VERSIONS = new ArrayList<>();

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
    public static Errorable<Boolean> load() {
        JsonObject data = Request.sendRequest(Request.createRequest(Opcodes.GET_VERSIONS, GetVersionsRequest.build()));
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
