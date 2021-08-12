package ga.epicpix.network.common.net.websocket.requests;

import com.google.gson.JsonObject;
import ga.epicpix.network.common.net.websocket.ClientType;
import ga.epicpix.network.common.net.websocket.Opcodes;

public class AuthenticateRequest implements WebSocketRequest {

    private final String username;
    private final String password;
    private final ClientType type;
    private final int capabilities;

    private AuthenticateRequest(String username, String password, ClientType type, int capabilities) {
        this.username = username;
        this.password = password;
        this.type = type;
        this.capabilities = capabilities;
    }

    public static AuthenticateRequest build(String username, String password, ClientType type, int capabilities) {
        return new AuthenticateRequest(username, password, type, capabilities);
    }

    public int getOpcode() {
        return Opcodes.AUTHENTICATE;
    }

    public JsonObject toJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("username", username);
        obj.addProperty("password", password);
        obj.addProperty("clientType", type.name());
        obj.addProperty("capabilities", capabilities);
        return obj;
    }

}
