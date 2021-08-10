package ga.epicpix.network.bukkit.map;

import ga.epicpix.network.bukkit.Entry;
import ga.epicpix.network.common.Reflection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldInitEvent;

public class MapListener implements Listener {

    private boolean done;

    @EventHandler
    public void onInit(WorldInitEvent e) {
        e.getWorld().setKeepSpawnInMemory(false);
        if(!done) {
            Reflection.setValueOfField(Entry.PLUGIN.getClass(), "isEnabled", Entry.PLUGIN, false); // Make sure to disable the plugin so the POSTWORLD step is still gonna run
            done = true;
        }
    }

}
