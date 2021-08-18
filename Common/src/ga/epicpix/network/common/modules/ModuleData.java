package ga.epicpix.network.common.modules;

import com.google.gson.JsonObject;

public class ModuleData {

    private final String name;
    private final String id;
    private final String main;
    private final ModuleLibrary library;

    private ModuleData(String name, String id, String main, ModuleLibrary library) {
        this.name = name;
        this.id = id;
        this.main = main;
        this.library = library;
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

    protected static ModuleData fromJson(JsonObject json) {
        return new ModuleData(
                json.get("name").getAsString(),
                json.get("id").getAsString(),
                json.get("main").getAsString(),
                ModuleLibrary.valueOf(json.get("library").getAsString().toUpperCase())
        );
    }

}
