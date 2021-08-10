package ga.epicpix.network.common.websocket.requests.data;

import com.google.gson.JsonObject;
import ga.epicpix.network.common.Reflection;
import ga.epicpix.network.common.annotations.CallerSensitive;
import ga.epicpix.network.common.websocket.Opcodes;
import ga.epicpix.network.common.websocket.requests.RequestPolicies;

import static ga.epicpix.network.common.servers.ServerInfo.ServerSignal;

public class SendSignalRequest extends RequestData {

    private final String serverName;
    private final ServerSignal signal;

    private SendSignalRequest(String serverName, ServerSignal signal) {
        this.serverName = serverName;
        this.signal = signal;
    }

    @CallerSensitive
    public static SendSignalRequest build(String serverName, ServerSignal signal) {
        if(!RequestPolicies.isAllowed(Opcodes.SEND_SIGNAL, Reflection.getCaller())) {
            throw new SecurityException("Cannot build this request data!");
        }
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
