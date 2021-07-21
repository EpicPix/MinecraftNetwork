package ga.epicpix.network.common.websocket.requests.data;

import com.google.gson.JsonObject;
import ga.epicpix.network.common.Reflection;
import ga.epicpix.network.common.websocket.ClientType;
import ga.epicpix.network.common.websocket.Opcodes;
import ga.epicpix.network.common.websocket.WebSocketConnection;

public class AuthenticateRequestData extends RequestData {

    private final String username;
    private final String password;
    private final ClientType type;

    private AuthenticateRequestData(String username, String password, ClientType type) {
        this.username = username;
        this.password = password;
        this.type = type;
    }

    public static AuthenticateRequestData build(String username, String password, ClientType type) {
        if(!Reflection.getCaller().equals(WebSocketConnection.class.getName())) {
            throw new SecurityException("Cannot build this request data!");
        }
        return new AuthenticateRequestData(username, password, type);
    }

    public int getOpcode() {
        return Opcodes.AUTHENTICATE;
    }

    public JsonObject toJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("username", username);
        obj.addProperty("password", password);
        obj.addProperty("clientType", type.name());
        return obj;
    }

}
