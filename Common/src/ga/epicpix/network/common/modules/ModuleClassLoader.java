package ga.epicpix.network.common.modules;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class ModuleClassLoader extends ClassLoader {

    private ModuleData data;
    private ModuleFile file;

    private HashMap<String, Class<?>> classes = new HashMap<>();

    public ModuleClassLoader(File module, ClassLoader parent) throws IOException {
        super(parent);
        file = new ModuleFile(module);
        data = file.getData();
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
