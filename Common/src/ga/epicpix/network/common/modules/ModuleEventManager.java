package ga.epicpix.network.common.modules;

import java.util.function.Consumer;

public class ModuleEventManager {

    static Consumer<Module> enableModule;
    static Consumer<Module> disableModule;

    public static void onEnableModule(Consumer<Module> onEnableModule) {
        if(ModuleEventManager.enableModule != null) {
            throw new IllegalArgumentException("onEnableModule is already set");
        }
        ModuleEventManager.enableModule = onEnableModule;
    }

    public static void onDisableModule(Consumer<Module> onDisableModule) {
        if(ModuleEventManager.disableModule != null) {
            throw new IllegalArgumentException("onDisableModule is already set");
        }
        ModuleEventManager.disableModule = onDisableModule;
    }
}
