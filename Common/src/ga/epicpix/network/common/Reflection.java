package ga.epicpix.network.common;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;

public class Reflection {

    public static Object getFieldOfClass(Class<?> clazz, String fieldName, Object ofObj) {
        try {
            Class<?> sclazz = clazz;
            ArrayList<Field> fields = new ArrayList<>();
            while(clazz!=Object.class) {
                fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
                clazz = clazz.getSuperclass();
            }
            Field field = null;
            for(Field f : fields) {
                if(f.getName().equals(fieldName)) {
                    field = f;
                    break;
                }
            }
            if(field==null) throw new NoSuchFieldException("Field not found " + sclazz + "." + fieldName);
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

    public static Object callMethod(Class<?> clazz, String methodName, Object ofObj, Object... objs) {
        try {
            ArrayList<Method> methods = new ArrayList<>();
            Class<?> sclazz = clazz;
            while(clazz!=Object.class) {
                methods.addAll(Arrays.asList(clazz.getDeclaredMethods()));
                clazz = clazz.getSuperclass();
            }
            Method method = null;
            for(Method m : methods) {
                if(m.getName().equals(methodName)) {
                    boolean possible = m.getParameterCount()==objs.length;
                    for(int i = 0; i < objs.length; i++) {
                        if(objs[i]!=null) {
                            if(!objs[i].getClass().isAssignableFrom(m.getParameterTypes()[i])) {
                                possible = false;
                                break;
                            }
                        }
                    }
                    if(possible) {
                        method = m;
                        break;
                    }
                }
            }
            if(method==null) throw new NoSuchMethodException("Method not found " + sclazz.getName() + "." + methodName);
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
