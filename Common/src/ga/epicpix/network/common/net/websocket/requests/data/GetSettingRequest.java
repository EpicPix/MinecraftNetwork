package ga.epicpix.network.common.net.websocket.requests.data;

import com.google.gson.JsonObject;
import ga.epicpix.network.common.net.websocket.Opcodes;

public class GetSettingRequest implements WebSocketRequest {

    private final String setting;

    private GetSettingRequest(String setting) {
        this.setting = setting;
    }

    public static GetSettingRequest build(String setting) {
        return new GetSettingRequest(setting);
    }

    public int getOpcode() {
        return Opcodes.GET_SETTING;
    }

    public JsonObject toJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("setting", setting);
        return obj;
    }

}
