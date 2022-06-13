package ga.epicpix.network.common.modules;

public class Module {

    private boolean isEnabled;

    protected void enable() {}
    protected void disable() {}

    final void setEnabled(boolean enable) {
        if(enable) {
            ModuleLoader.checkModulePermission(ModulePermission.ENABLE_MODULE);
            if(!isEnabled) {
                if(ModuleEventManager.enableModule != null) ModuleEventManager.enableModule.accept(this);
                enable();
                isEnabled = true;
            }
        }else {
            ModuleLoader.checkModulePermission(ModulePermission.DISABLE_MODULE);
            if(isEnabled) {
                disable();
                if(ModuleEventManager.disableModule != null) ModuleEventManager.disableModule.accept(this);
                isEnabled = false;
            }
        }
    }

    public final boolean isEnabled() {
        return isEnabled;
    }

    public final ModuleData getData() {
        return ((ModuleClassLoader) getClass().getClassLoader()).getData();
    }

}
