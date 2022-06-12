package ga.epicpix.network.common.modules;

public class Module {

    protected void enable() {}
    protected void disable() {}

    public final ModuleData getData() {
        return ((ModuleClassLoader) getClass().getClassLoader()).getData();
    }

}
