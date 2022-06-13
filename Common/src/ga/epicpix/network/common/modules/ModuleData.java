package ga.epicpix.network.common.modules;

import java.util.Arrays;

public class ModuleData {

    private final String name;
    private final String id;
    private final String main;
    private final ModuleLibrary library;
    private final String version;
    private final ModulePermission[] permissions;

    private ModuleData(String name, String id, String main, ModuleLibrary library, String version, ModulePermission[] permissions) {
        this.name = name;
        this.id = id;
        this.main = main;
        this.library = library;
        this.version = version;
        this.permissions = permissions;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getMain() {
        return main;
    }

    public ModuleLibrary getLibrary() {
        return library;
    }

    public String getVersion() {
        return version;
    }

    public String toString() {
        return "ModuleData{name='" + name + "', id='" + id + "', main='" + main + "', library=" + library + ", version='" + version + "', permissions=" + Arrays.toString(permissions) + "}";
    }

    public ModulePermission[] getPermissions() {
        return permissions.clone();
    }
}
