package ga.epicpix.network.common.ranks;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import ga.epicpix.network.common.ChatComponent;
import ga.epicpix.network.common.CommonUtils;

import java.util.Arrays;

public class Rank {

    @SerializedName("default") boolean isDefault;
    String id;
    int priority;
    ChatComponent[] prefix;
    ChatComponent[] suffix;
    String[] permissions;
    String nameColor;
    String chatColor;

    static Rank rankFromJsonObject(JsonObject obj) {
        return new Gson().fromJson(obj, Rank.class);
    }

    public boolean isDefault() {
        return isDefault;
    }

    public String getId() {
        return id;
    }

    public int getPriority() {
        return priority;
    }

    public ChatComponent[] getPrefix() {
        return prefix;
    }

    public ChatComponent[] getSuffix() {
        return suffix;
    }

    public String[] getPermissions() {
        return permissions;
    }

    public String getNameColor() {
        return nameColor;
    }

    public String getChatColor() {
        return chatColor;
    }

    public String toString() {
        return "Rank{default=" + isDefault + ", id=" + CommonUtils.toString(id) + ", priority=" + priority + ", prefix=" + Arrays.toString(prefix) + ", suffix=" + Arrays.toString(suffix) + ", chatColor=" + chatColor + "}";
    }
}
