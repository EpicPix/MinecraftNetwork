package ga.epicpix.network.common.ranks;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import ga.epicpix.network.common.text.ChatComponent;
import ga.epicpix.network.common.CommonUtils;

import java.util.Arrays;

public class Rank {

    private String id;
    private int priority;
    private ChatComponent[] prefix;
    private ChatComponent[] suffix;
    private String[] permissions;
    private String nameColor;
    private String chatColor;

    public static Rank rankFromJsonObject(JsonObject obj) {
        return new Gson().fromJson(obj, Rank.class);
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

}
