package ga.epicpix.network.common.modules;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class ModuleClassLoader extends ClassLoader {

    private ModuleData data;
    private ModuleFile file;

    private HashMap<String, Class<?>> classes = new HashMap<>();

    public ModuleClassLoader(ModuleFile module, ClassLoader parent) throws IOException {
        super(parent);
        this.file = module;
        data = module.getData();
    }

    public InputStream getResourceAsStream(String name) {
        return null;
    }

    public Class<?> findClass(String name) throws ClassNotFoundException {
        Class<?> clz = classes.get(name);
        if(clz==null) {
            byte[] b = file.getFileData(name.replace('.', '/') + ".class");
            if(b==null) {
                throw new ClassNotFoundException(name);
            }
            clz = defineClass(name, b, 0, b.length);
            classes.put(name, clz);
        }
        return clz;
    }

    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        if(name.startsWith("java.lang.reflect.")) {
            try {
                ModuleLoader.checkModulePermission(ModuleLoader.ModulePermission.REFLECTION);
            }catch(SecurityException e) {
                throw new SecurityException("Cannot access " + name + ": " + e.getMessage());
            }
        }
        return super.loadClass(name, resolve);
    }

    public ModuleData getData() {
        return data;
    }

    protected void destroy() {
        data = null;
        file.clear();
        file = null;
        classes.clear();
        classes = null;
    }

}
