package ga.epicpix.network.common.net.websocket.requests.data;

import com.google.gson.JsonObject;
import ga.epicpix.network.common.net.websocket.Opcodes;

import static ga.epicpix.network.common.servers.ServerInfo.ServerSignal;

public class SendSignalRequest extends RequestData {

    private final String serverName;
    private final ServerSignal signal;

    private SendSignalRequest(String serverName, ServerSignal signal) {
        this.serverName = serverName;
        this.signal = signal;
    }

    public static SendSignalRequest build(String serverName, ServerSignal signal) {
        return new SendSignalRequest(serverName, signal);
    }

    public int getOpcode() {
        return Opcodes.SEND_SIGNAL;
    }

    public JsonObject toJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("server", serverName);
        obj.addProperty("signal", signal.name());
        return obj;
    }

}
