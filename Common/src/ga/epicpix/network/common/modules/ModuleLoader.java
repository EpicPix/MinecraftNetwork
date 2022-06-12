package ga.epicpix.network.common.modules;

import com.google.gson.annotations.SerializedName;
import ga.epicpix.network.common.Reflection;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public final class ModuleLoader {

    private static final List<Module> loadedModules = new ArrayList<>();

    public enum ModulePermission {

        // Managers
        @SerializedName("ModuleManager.getModules") GET_MODULES("Get Modules"),
        @SerializedName("ModuleManager.getModule") GET_MODULE("Get Module"),
        @SerializedName("ModuleManager.addModule") ADD_MODULE("Add Module"),
        @SerializedName("PlayerManager.getPlayer") GET_PLAYER("Get Player"),
        @SerializedName("PlayerManager.updatePlayer") UPDATE_PLAYER("Update Player"),
        @SerializedName("RankManager.getRanks") GET_RANKS("Get Ranks"),
        @SerializedName("RankManager.getRank") GET_RANK("Get Rank"),
        @SerializedName("RankManager.getDefaultRank") GET_DEFAULT_RANK("Get Default Rank"),
        @SerializedName("RankManager.updateRank") UPDATE_RANK("Update Rank"),
        @SerializedName("RankManager.createRank") CREATE_RANK("Create Rank"),
        @SerializedName("RankManager.deleteRank") DELETE_RANK("Delete Rank"),
        @SerializedName("ServerManager.updateServer") UPDATE_SERVER("Update Server"),
        @SerializedName("ServerManager.removeServer") REMOVE_SERVER("Remove Server"),
        @SerializedName("ServerManager.sendSignal") SEND_SIGNAL("Send Signal"),
        @SerializedName("ServerManager.listServers") LIST_SERVERS("List Servers"),
        @SerializedName("ServerManager.getServerInfo") GET_SERVER_INFO("Get Server Info"),
        @SerializedName("SettingsManager.getSettings") GET_SETTINGS("Get Settings"),
        @SerializedName("SettingsManager.getSetting") GET_SETTING("Get Setting"),
        @SerializedName("SettingsManager.setSetting") SET_SETTING("Set Setting"),

        // Other websocket requests
        @SerializedName("WebSocketRequests.getVersions") GET_VERSIONS("Get Versions"),

        // Reflection
        @SerializedName("Reflection") REFLECTION("Reflection"),

        // Modules
        @SerializedName("ModuleLoader.load") LOAD_MODULE("Load Module"),
        @SerializedName("ModuleLoader.unload") UNLOAD_MODULE("Unload Module"),
        @SerializedName("ModuleLoader.enable") ENABLE_MODULE("Enable Module"),
        @SerializedName("ModuleLoader.disable") DISABLE_MODULE("Disable Module"),
        ;

        private final String name;

        ModulePermission(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public static Module loadModule(ModuleFile moduleFile) throws IOException, ClassNotFoundException {
        checkModulePermission(ModulePermission.LOAD_MODULE);
        ModuleClassLoader loader = new ModuleClassLoader(moduleFile, ModuleLoader.class.getClassLoader());
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

    public static Module loadModule(File module) throws IOException, ClassNotFoundException {
        return loadModule(new ModuleFile(Files.readAllBytes(module.toPath())));
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

    public static void enableModule(Module module) {
        checkModulePermission(ModulePermission.ENABLE_MODULE);
        try {
            /* pre-enable */ if(ModuleEventManager.enableModule != null) ModuleEventManager.enableModule.accept(module);
            module.enable();
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void enableModules(List<? extends Module> modules) {
        checkModulePermission(ModulePermission.ENABLE_MODULE);
        for(Module m : modules) {
            enableModule(m);
        }
    }

    public static void disableModule(Module module) {
        checkModulePermission(ModulePermission.DISABLE_MODULE);
        if(ModuleEventManager.disableModule != null) ModuleEventManager.disableModule.accept(module);
        module.disable();
    }

    public static void unloadModule(Module module) {
        checkModulePermission(ModulePermission.UNLOAD_MODULE);
        disableModule(module);
        ((ModuleClassLoader) module.getClass().getClassLoader()).destroy();

    }

    public static void unloadModules() throws IOException {
        checkModulePermission(ModulePermission.UNLOAD_MODULE);
        for(Module mod : loadedModules) {
            unloadModule(mod);
        }
        loadedModules.clear();
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
