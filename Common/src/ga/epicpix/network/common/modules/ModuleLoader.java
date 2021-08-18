package ga.epicpix.network.common.modules;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class ModuleLoader {

    private static final ArrayList<Module> loadedModules = new ArrayList<>();

    public static Module loadModule(File file) throws IOException, ClassNotFoundException {
        ModuleClassLoader loader = new ModuleClassLoader(file, ModuleLoader.class.getClassLoader());
        Class<?> main = Class.forName(loader.getData().getId() + "." + loader.getData().getMain(), true, loader);
        Class<? extends Module> classModule = main.asSubclass(Module.class);
        try {
            Module module = classModule.getDeclaredConstructor().newInstance();
            loadedModules.add(module);
            return module;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList<Module> loadModules(File directory) throws IOException, ClassNotFoundException {
        directory.mkdirs();
        File[] files = directory.listFiles();
        ArrayList<Module> modules = new ArrayList<>();
        if(files!=null) {
            for(File f : files) {
                if(f.getName().endsWith(".module")) {
                    modules.add(loadModule(f));
                }
            }
        }
        return modules;
    }

    public static void enableModules(ArrayList<Module> modules) {
        for(Module m : modules) {
            try {
                m.enable();
            }catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void unloadModules() throws IOException {
        for(Module mod : loadedModules) {
            mod.disable();
            ((ModuleClassLoader) mod.getClass().getClassLoader()).destroy();
        }
        loadedModules.clear();
    }

}
