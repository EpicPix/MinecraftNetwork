package ga.epicpix.network.common.modules;

import com.google.gson.annotations.SerializedName;

public enum ModulePermission {

    // Managers
    @SerializedName("ModuleManager.getModules") GET_MODULES("Get Modules"),
    @SerializedName("ModuleManager.getModule") GET_MODULE("Get Module"),
    @SerializedName("ModuleManager.addModule") ADD_MODULE("Add Module"),
    @SerializedName("PlayerManager.getPlayer") GET_PLAYER("Get Player"),
    @SerializedName("PlayerManager.updatePlayer") UPDATE_PLAYER("Update Player"),
    @SerializedName("RankManager.getRanks") GET_RANKS("Get Ranks"),
    @SerializedName("RankManager.getRank") GET_RANK("Get Rank"),
    @SerializedName("RankManager.getDefaultRank") GET_DEFAULT_RANK("Get Default Rank"),
    @SerializedName("RankManager.updateRank") UPDATE_RANK("Update Rank"),
    @SerializedName("RankManager.createRank") CREATE_RANK("Create Rank"),
    @SerializedName("RankManager.deleteRank") DELETE_RANK("Delete Rank"),
    @SerializedName("ServerManager.updateServer") UPDATE_SERVER("Update Server"),
    @SerializedName("ServerManager.removeServer") REMOVE_SERVER("Remove Server"),
    @SerializedName("ServerManager.sendSignal") SEND_SIGNAL("Send Signal"),
    @SerializedName("ServerManager.listServers") LIST_SERVERS("List Servers"),
    @SerializedName("ServerManager.getServerInfo") GET_SERVER_INFO("Get Server Info"),
    @SerializedName("SettingsManager.getSettings") GET_SETTINGS("Get Settings"),
    @SerializedName("SettingsManager.getSetting") GET_SETTING("Get Setting"),
    @SerializedName("SettingsManager.setSetting") SET_SETTING("Set Setting"),

    // Other websocket requests
    @SerializedName("WebSocketRequests.getVersions") GET_VERSIONS("Get Versions"),

    // Reflection
    @SerializedName("Reflection") REFLECTION("Reflection"),

    // Modules
    @SerializedName("ModuleLoader.load") LOAD_MODULE("Load Module"),
    @SerializedName("ModuleLoader.unload") UNLOAD_MODULE("Unload Module"),
    @SerializedName("ModuleLoader.enable") ENABLE_MODULE("Enable Module"),
    @SerializedName("ModuleLoader.disable") DISABLE_MODULE("Disable Module"),
    @SerializedName("ModuleLoader.listModules") LIST_MODULES("List Modules"),
    ;

    private final String name;

    ModulePermission(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
