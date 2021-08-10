package ga.epicpix.network.common.servers;

import com.google.gson.JsonObject;
import ga.epicpix.network.common.CommonUtils;
import ga.epicpix.network.common.SerializableJson;
import ga.epicpix.network.common.annotations.NonNull;

public class ServerDetails implements SerializableJson {

    private final String ip;
    private final int port;

    public ServerDetails(@NonNull String ip, int port) {
        if(ip==null) {
            throw new IllegalArgumentException("IP cannot be null!");
        }
        if(port < 0) {
            throw new IllegalArgumentException("Port must be positive");
        }
        this.ip = ip;
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
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