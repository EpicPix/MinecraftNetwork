package ga.epicpix.network.bungee;

import ga.epicpix.network.common.Language;
import net.md_5.bungee.api.plugin.Plugin;

public class Entry extends Plugin {

    public void onLoad() {
        Language.loadLanguages();
    }

}
