package ga.epicpix.network.common.modules;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.jar.JarFile;

public class ModuleClassLoader extends URLClassLoader {

    private final ModuleData data;
    private final JarFile file;

    private final HashMap<String, Class<?>> classes = new HashMap<>();

    public ModuleClassLoader(File module, ClassLoader parent) throws IOException {
        super(new URL[] {module.toURI().toURL()}, parent);
        file = new JarFile(module);
        String moduleJsonStr = new String(file.getInputStream(file.getEntry("module.json")).readAllBytes());
        data = ModuleData.fromJson(new Gson().fromJson(moduleJsonStr, JsonObject.class));
    }

    public Class<?> findClass(String name) throws ClassNotFoundException {
        try {
            Class<?> clz = classes.get(name);
            if(clz==null) {
                byte[] b = file.getInputStream(file.getEntry(name.replace('.', '/') + ".class")).readAllBytes();
                classes.put(name, clz = defineClass(name, b, 0, b.length));
            }
            return clz;
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new ClassNotFoundException(name);
    }

    public ModuleData getData() {
        return data;
    }

    protected void destroy() throws IOException {
        file.close();
        classes.clear();
    }

}
