package ga.epicpix.network.common.websocket.requests.data;

import com.google.gson.JsonObject;
import ga.epicpix.network.common.Reflection;
import ga.epicpix.network.common.websocket.ClientType;
import ga.epicpix.network.common.websocket.Opcodes;
import ga.epicpix.network.common.websocket.requests.RequestPolicies;

public class AuthenticateRequest extends RequestData {

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
        if(!RequestPolicies.isAllowed(Opcodes.AUTHENTICATE, Reflection.getCaller())) {
            throw new SecurityException("Cannot build this request data!");
        }
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
