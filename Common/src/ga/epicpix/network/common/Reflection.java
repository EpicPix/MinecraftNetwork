package ga.epicpix.network.common;

import ga.epicpix.network.common.annotations.CallerSensitive;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public final class Reflection {

    public static Field getField(Class<?> clazz, String fieldName) {
        List<Field> fields = new ArrayList<>();
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
            if(field==null) return null;
            field.setAccessible(true);
            return field.get(ofObj);
        } catch (IllegalAccessException e) {
            return null;
        }
    }

    public static Method getMethod(Class<?> clazz, String methodName, boolean classes, Object... objs) {
        List<Method> methods = new ArrayList<>();
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
            method.setAccessible(true);
            return method.invoke(ofObj, objs);
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            return null;
        }
    }

    public static Object callMethodByClasses(Class<?> clazz, String methodName, Object ofObj, Class<?>[] classes, Object... objs) {
        try {
            Method method = getMethod(clazz, methodName, true, (Object[]) classes);
            if(method==null) throw new NoSuchMethodException("Method not found " + clazz.getName() + "." + methodName);
            method.setAccessible(true);
            return method.invoke(ofObj, objs);
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            return null;
        }
    }

    public static void setValueOfField(Class<?> clazz, String fieldName, Object ofObj, Object newData) {
        try {
            Field field = getField(clazz, fieldName);
            if(field==null) throw new NoSuchFieldException("Field not found " + clazz.getName() + "." + fieldName);
            field.setAccessible(true);
            field.set(ofObj, newData);
        } catch (NoSuchFieldException | IllegalAccessException e) {}
    }

    @CallerSensitive
    public static Class<?> getCaller() {
        var o = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE)
            .walk(
                s -> s.map(StackWalker.StackFrame::getDeclaringClass)
                    .skip(2)
                    .findFirst()
            );
        if(o.isEmpty()) {{
            return null;
        }}
        return o.get();
    }
}
