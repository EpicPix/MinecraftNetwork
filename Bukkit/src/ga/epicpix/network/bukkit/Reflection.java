package ga.epicpix.network.bukkit;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Reflection {

    public static Object getFieldOfClass(Class<?> clazz, String fieldName, Object ofObj) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            boolean access = field.isAccessible();
            field.setAccessible(true);
            Object obj = field.get(ofObj);
            field.setAccessible(access);
            return obj;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return null;
        }
    }

    public static Object callMethod(Class<?> clazz, String methodName, Object ofObj, Object... objs) {
        try {
            Class<?>[] classes = new Class<?>[objs.length];
            for(int i = 0; i<objs.length; i++) classes[i] = objs[i].getClass();
            Method method = clazz.getDeclaredMethod(methodName, classes);
            boolean access = method.isAccessible();
            method.setAccessible(true);
            Object obj = method.invoke(ofObj, objs);
            method.setAccessible(access);
            return obj;
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            return null;
        }
    }
}
