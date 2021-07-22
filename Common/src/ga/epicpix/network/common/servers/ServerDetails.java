package ga.epicpix.network.common.servers;

import com.google.gson.JsonObject;
import ga.epicpix.network.common.CommonUtils;
import ga.epicpix.network.common.SerializableJson;

public record ServerDetails(String ip, int port) implements SerializableJson {

    public ServerDetails {
        if(ip==null) {
            throw new IllegalArgumentException("IP cannot be null!");
        }
        if(port < 0) {
            throw new IllegalArgumentException("Port must be positive");
        }
    }

    public String toString() {
        return "ServerDetails{" +
                "ip=" + CommonUtils.toString(ip) +
                ", port=" + port +
                '}';
    }

    public JsonObject toJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("ip", ip);
        obj.addProperty("port", port);
        return obj;
    }
}