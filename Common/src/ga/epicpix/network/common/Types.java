package ga.epicpix.network.common;

import ga.epicpix.network.common.servers.*;

import java.util.HashMap;

public class Types {

    private static final HashMap<String, Class<?>> types = new HashMap<>();

    static {
        register(Language.class, PlayerInfo.class, Rank.class);
        register(ServerDetails.class, ServerInfo.class, ServerVersion.class);
    }

    public static void register(Class<?>... clazz) {
        for(Class<?> c : clazz) {
            register(c);
        }
    }

    public static void register(Class<?> clazz) {
        if(clazz.getAnnotation(TypeClass.class)!=null) {
            registerType(clazz.getAnnotation(TypeClass.class).value(), clazz);
        }else {
            System.err.println("Tried to register a type with no @TypeClass annotation: " + clazz.getName());
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
