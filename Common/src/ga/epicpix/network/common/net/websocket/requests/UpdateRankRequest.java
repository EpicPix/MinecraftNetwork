package ga.epicpix.network.common.net.websocket.requests;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import ga.epicpix.network.common.SerializableJson;
import ga.epicpix.network.common.annotations.ChecksPermission;
import ga.epicpix.network.common.modules.ModuleLoader;
import ga.epicpix.network.common.modules.ModulePermission;
import ga.epicpix.network.common.text.ChatComponent;
import ga.epicpix.network.common.net.websocket.Opcodes;

public class UpdateRankRequest implements WebSocketRequest {

    private final String rank;
    private final Data data;

    private UpdateRankRequest(String rank, Data data) {
        this.rank = rank;
        this.data = data;
    }

    public static final class Data implements SerializableJson {

        private Integer priority;
        private ChatComponent[] prefix;
        private ChatComponent[] suffix;
        private String[] permissions;
        private String nameColor;
        private String chatColor;

        public void setPriority(Integer priority) {
            this.priority = priority;
        }

        public void setPrefix(ChatComponent[] prefix) {
            this.prefix = prefix;
        }

        public void setSuffix(ChatComponent[] suffix) {
            this.suffix = suffix;
        }

        public void setPermissions(String[] permissions) {
            this.permissions = permissions;
        }

        public void setNameColor(String nameColor) {
            this.nameColor = nameColor;
        }

        public void setChatColor(String chatColor) {
            this.chatColor = chatColor;
        }

        public JsonObject toJson() {
            JsonObject obj = new JsonObject();
            if(priority!=null) obj.addProperty("priority", priority);
            if(prefix!=null) obj.add("prefix", new Gson().toJsonTree(prefix));
            if(suffix!=null) obj.add("suffix", new Gson().toJsonTree(prefix));
            if(permissions!=null) obj.add("permissions", new Gson().toJsonTree(permissions));
            if(nameColor!=null) obj.addProperty("nameColor", nameColor);
            if(chatColor!=null) obj.addProperty("chatColor", chatColor);
            return obj;
        }
    }

    @ChecksPermission(ModulePermission.UPDATE_RANK)
    public static UpdateRankRequest build(String serverName, Data data) {
        ModuleLoader.checkModulePermission(ModulePermission.UPDATE_RANK);
        return new UpdateRankRequest(serverName, data);
    }

    public int getOpcode() {
        return Opcodes.UPDATE_RANK;
    }

    public JsonObject toJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("rank", rank);
        obj.add("data", data.toJson());
        return obj;
    }

}
