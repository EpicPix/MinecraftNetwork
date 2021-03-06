package ga.epicpix.network.common.net.websocket.requests;

import com.google.gson.JsonObject;
import ga.epicpix.network.common.annotations.ChecksPermission;
import ga.epicpix.network.common.modules.ModuleLoader;
import ga.epicpix.network.common.modules.ModulePermission;
import ga.epicpix.network.common.net.websocket.Opcodes;

import static ga.epicpix.network.common.servers.ServerInfo.ServerSignal;

public class SendSignalRequest implements WebSocketRequest {

    private final String serverName;
    private final ServerSignal signal;

    private SendSignalRequest(String serverName, ServerSignal signal) {
        this.serverName = serverName;
        this.signal = signal;
    }

    @ChecksPermission(ModulePermission.SEND_SIGNAL)
    public static SendSignalRequest build(String serverName, ServerSignal signal) {
        ModuleLoader.checkModulePermission(ModulePermission.SEND_SIGNAL);
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
