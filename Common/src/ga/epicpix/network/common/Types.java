package ga.epicpix.network.common;

import ga.epicpix.network.common.servers.*;

import java.util.HashMap;

@Deprecated(forRemoval = true)
public class Types {

    private static final HashMap<String, Class<?>> types = new HashMap<>();

    static {
        registerType("LANGUAGE", Language.class);
        registerType("PLAYER_INFO", PlayerInfo.class);
        registerType("RANK", Rank.class);

        registerType("SERVER_DETAILS", ServerDetails.class);
        registerType("SERVER_INFO", ServerInfo.class);
        registerType("SERVER_VERSION", ServerVersion.class);
    }

    public static void registerType(String type, Class<?> clazz) {
        types.put(type, clazz);
    }

    public static Class<?> getType(String type) {
        if(!types.containsKey(type)) {
            System.err.println("Undefined type: " + type);
        }
        return types.get(type);
    }

    public static String getType(Class<?> clazz) {
        return CommonUtils.getValueByKey(clazz, types);
    }

}
