package ga.epicpix.network.common;

import java.util.HashMap;

public class Types {

    private static final HashMap<String, Class<?>> types = new HashMap<>();

    static {
        register(ChatComponent.class);
        register(Language.class);
        register(PlayerInfo.class);
        register(Rank.class);
        register(ServerDetails.class);
        register(ServerInfo.class);
        register(ServerVersion.class);
    }

    public static void register(Class<?> clazz) {
        if(Reflection.getField(clazz, "TYPE")!=null) {
            registerType((String) Reflection.getValueOfField(clazz, "TYPE", null), clazz);
        }else {
            System.err.println("Tried to register a type with no TYPE field: " + clazz.getName());
        }
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
