package ga.epicpix.network.common;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;

public class Reflection {

    public static Field getField(Class<?> clazz, String fieldName) {
        ArrayList<Field> fields = new ArrayList<>();
        while(clazz!=Object.class) {
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
            clazz = clazz.getSuperclass();
        }
        for(Field f : fields) {
            if(f.getName().equals(fieldName)) {
                return f;
            }
        }
        return null;
    }

    public static Object getValueOfField(Class<?> clazz, String fieldName, Object ofObj) {
        try {
            Field field = getField(clazz, fieldName);
            if(field==null) throw new NoSuchFieldException("Field not found " + clazz.getName() + "." + fieldName);
            boolean access = field.isAccessible();
            field.setAccessible(true);
            Object obj = field.get(ofObj);
            field.setAccessible(access);
            return obj;
        } catch (IllegalAccessException e) {
            return null;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Method getMethod(Class<?> clazz, String methodName, boolean classes, Object... objs) {
        ArrayList<Method> methods = new ArrayList<>();
        while(clazz!=Object.class) {
            methods.addAll(Arrays.asList(clazz.getDeclaredMethods()));
            clazz = clazz.getSuperclass();
        }
        for(Method m : methods) {
            if(m.getName().equals(methodName)) {
                boolean possible = m.getParameterCount()==objs.length;
                if(possible) {
                    for (int i = 0; i < objs.length; i++) {
                        if (objs[i] != null) {
                            if (classes) {
                                if (!((Class<?>) objs[i]).isAssignableFrom(m.getParameterTypes()[i])) {
                                    possible = false;
                                    break;
                                }
                            } else {
                                if (!objs[i].getClass().isAssignableFrom(m.getParameterTypes()[i])) {
                                    possible = false;
                                    break;
                                }
                            }
                        }
                    }
                }
                if(possible) {
                    return m;
                }
            }
        }
        return null;
    }

    public static Object callMethod(Class<?> clazz, String methodName, Object ofObj, Object... objs) {
        try {
            Method method = getMethod(clazz, methodName, false, objs);
            if(method==null) throw new NoSuchMethodException("Method not found " + clazz.getName() + "." + methodName);
            boolean access = method.isAccessible();
            method.setAccessible(true);
            Object obj = method.invoke(ofObj, objs);
            method.setAccessible(access);
            return obj;
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            return null;
        }
    }

    public static void setValueOfField(Class<?> clazz, String fieldName, Object ofObj, Object newData) {
        try {
            Field field = getField(clazz, fieldName);
            if(field==null) throw new NoSuchFieldException("Field not found " + clazz.getName() + "." + fieldName);
            boolean access = field.isAccessible();
            field.setAccessible(true);
            field.set(ofObj, newData);
            field.setAccessible(access);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
