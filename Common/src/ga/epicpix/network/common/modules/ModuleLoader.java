package ga.epicpix.network.common.modules;

import com.google.gson.annotations.SerializedName;
import ga.epicpix.network.common.Reflection;
import ga.epicpix.network.common.annotations.ChecksPermission;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public final class ModuleLoader {

    private static final List<Module> loadedModules = new ArrayList<>();

    @ChecksPermission(ModulePermission.LOAD_MODULE)
    public static Module loadModule(ModuleFile moduleFile) {
        checkModulePermission(ModulePermission.LOAD_MODULE);
        try {
            ModuleClassLoader loader = new ModuleClassLoader(moduleFile, ModuleLoader.class.getClassLoader());
            Class<?> main = Class.forName(loader.getData().getId() + "." + loader.getData().getMain(), true, loader);
            Class<? extends Module> classModule = main.asSubclass(Module.class);
            Module module = classModule.getDeclaredConstructor().newInstance();
            loadedModules.add(module);
            return module;
        } catch (IOException | ReflectiveOperationException e) {
            e.printStackTrace();
            return null;
        }
    }

    @ChecksPermission(ModulePermission.LOAD_MODULE)
    public static Module loadModule(File module) throws IOException {
        return loadModule(new ModuleFile(Files.readAllBytes(module.toPath())));
    }

    @ChecksPermission(ModulePermission.LOAD_MODULE)
    public static ArrayList<Module> loadModules(File directory) throws IOException {
        checkModulePermission(ModulePermission.LOAD_MODULE);
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

    @ChecksPermission(ModulePermission.ENABLE_MODULE)
    public static void enableModule(Module module) {
        try {
            module.setEnabled(true);
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    @ChecksPermission(ModulePermission.ENABLE_MODULE)
    public static void enableModules(List<? extends Module> modules) {
        checkModulePermission(ModulePermission.ENABLE_MODULE);
        for(Module m : modules) {
            enableModule(m);
        }
    }

    @ChecksPermission(ModulePermission.DISABLE_MODULE)
    public static void disableModule(Module module) {
        try {
            module.setEnabled(false);
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    @ChecksPermission({ModulePermission.UNLOAD_MODULE, ModulePermission.DISABLE_MODULE})
    public static void unloadModule(Module module) {
        checkModulePermission(ModulePermission.UNLOAD_MODULE);
        disableModule(module);
        ((ModuleClassLoader) module.getClass().getClassLoader()).destroy();
        loadedModules.remove(module);
    }

    @ChecksPermission({ModulePermission.UNLOAD_MODULE, ModulePermission.DISABLE_MODULE})
    public static void unloadModules() throws IOException {
        checkModulePermission(ModulePermission.UNLOAD_MODULE);
        while(loadedModules.size() != 0) {
            unloadModule(loadedModules.get(0));
        }
    }

    @ChecksPermission(ModulePermission.LIST_MODULES)
    public static Module[] getLoadedModules() {
        checkModulePermission(ModulePermission.LIST_MODULES);
        return loadedModules.toArray(new Module[0]);
    }

    public static void checkModulePermission(ModulePermission permission) {
        var moduleClass = Reflection.getModuleCaller();
        if(moduleClass != null) {
            ModuleClassLoader mcl = (ModuleClassLoader) moduleClass.getClassLoader();
            for(var perm : mcl.getData().getPermissions()) {
                if(perm == permission) {
                    return;
                }
            }
            throw new SecurityException("Module '" + mcl.getData().getName() + "' does not have " + permission.getName() + " permission");
        }
    }

    public static void checkReflectionModulePermission() {
        var moduleClass = Reflection.getCaller();
        if(moduleClass != null && moduleClass.getClassLoader() instanceof ModuleClassLoader) {
            ModuleClassLoader mcl = (ModuleClassLoader) moduleClass.getClassLoader();
            for(var perm : mcl.getData().getPermissions()) {
                if(perm == ModulePermission.REFLECTION) {
                    return;
                }
            }
            throw new SecurityException("Module '" + mcl.getData().getName() + "' does not have " + ModulePermission.REFLECTION.getName() + " permission");
        }
    }

}
