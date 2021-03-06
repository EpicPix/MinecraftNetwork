package ga.epicpix.network.common;

import ga.epicpix.network.common.annotations.CallerSensitive;
import ga.epicpix.network.common.annotations.ChecksPermission;
import ga.epicpix.network.common.modules.ModuleClassLoader;
import ga.epicpix.network.common.modules.ModuleLoader;
import ga.epicpix.network.common.modules.ModulePermission;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class Reflection {

    @ChecksPermission(ModulePermission.REFLECTION)
    public static Field getField(Class<?> clazz, String fieldName) {
        ModuleLoader.checkReflectionModulePermission();
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

    @ChecksPermission(ModulePermission.REFLECTION)
    public static Object getValueOfField(Class<?> clazz, String fieldName, Object ofObj) {
        ModuleLoader.checkReflectionModulePermission();
        try {
            Field field = getField(clazz, fieldName);
            if(field==null) return null;
            field.setAccessible(true);
            return field.get(ofObj);
        } catch (IllegalAccessException e) {
            return null;
        }
    }

    @ChecksPermission(ModulePermission.REFLECTION)
    public static Method getMethod(Class<?> clazz, String methodName, boolean classes, Object... objs) {
        ModuleLoader.checkReflectionModulePermission();
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

    @ChecksPermission(ModulePermission.REFLECTION)
    public static Object callMethod(Class<?> clazz, String methodName, Object ofObj, Object... objs) {
        ModuleLoader.checkReflectionModulePermission();
        try {
            Method method = getMethod(clazz, methodName, false, objs);
            if(method==null) throw new NoSuchMethodException("Method not found " + clazz.getName() + "." + methodName);
            method.setAccessible(true);
            return method.invoke(ofObj, objs);
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            return null;
        }
    }

    @ChecksPermission(ModulePermission.REFLECTION)
    public static Object callMethodByClasses(Class<?> clazz, String methodName, Object ofObj, Class<?>[] classes, Object... objs) {
        ModuleLoader.checkReflectionModulePermission();
        try {
            Method method = getMethod(clazz, methodName, true, (Object[]) classes);
            if(method==null) throw new NoSuchMethodException("Method not found " + clazz.getName() + "." + methodName);
            method.setAccessible(true);
            return method.invoke(ofObj, objs);
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            return null;
        }
    }

    @ChecksPermission(ModulePermission.REFLECTION)
    public static void setValueOfField(Class<?> clazz, String fieldName, Object ofObj, Object newData) {
        ModuleLoader.checkReflectionModulePermission();
        try {
            Field field = getField(clazz, fieldName);
            if(field==null) throw new NoSuchFieldException("Field not found " + clazz.getName() + "." + fieldName);
            field.setAccessible(true);
            field.set(ofObj, newData);
        } catch (NoSuchFieldException | IllegalAccessException e) {}
    }

    @CallerSensitive
    public static Class<?> getModuleCaller() {
        var o = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE)
            .walk(
                s -> s.map(StackWalker.StackFrame::getDeclaringClass)
                    .dropWhile(clz -> !(clz.getClassLoader() instanceof ModuleClassLoader))
                    .findFirst()
            );
        if(o.isEmpty()) {
            return null;
        }
        return o.get();
    }


    @CallerSensitive
    public static Class<?> getCaller() {
        var o = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE)
            .walk(
                s -> s.map(StackWalker.StackFrame::getDeclaringClass)
                    .skip(2)
                    .findFirst()
            );
        if(o.isEmpty()) {
            return null;
        }
        return o.get();
    }
}
