package ga.epicpix.network.bukkit;

import ga.epicpix.network.common.*;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;

public class PluginListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        CommonUtils.updateServerInfo(BukkitCommon.getThisServer());

        Player player = e.getPlayer();
        PlayerInfo info = PlayerInfo.getPlayerInfo(player.getUniqueId());
        if(info==null) {
            info = PlayerInfo.updatePlayerInfo(new PlayerInfo().populate(player.getUniqueId(), player.getName(), CommonUtils.getDefaultRank(), CommonUtils.getDefaultLanguage()));
        }
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e) {
        CommonUtils.updateServerInfo(BukkitCommon.getThisServer());
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();
        PlayerInfo info = PlayerInfo.getPlayerInfo(player.getUniqueId());
        if(info==null) {
            info = PlayerInfo.updatePlayerInfo(new PlayerInfo().populate(player.getUniqueId(), player.getName(), CommonUtils.getDefaultRank(), CommonUtils.getDefaultLanguage()));
        }
        boolean showColon = Settings.getSettingOrDefault("SHOW_COLON_CHAT", true);
        Rank rank = info.getRank();
        e.setCancelled(true);
        ArrayList<TextComponent> components = new ArrayList<>();
        for(ChatComponent c : rank.prefix) components.add(BukkitCommon.toTextComponent(c));
        if(rank.prefix.length!=0) components.add(BukkitCommon.toTextComponent(new ChatComponent().setText(" ")));
        components.add(BukkitCommon.toTextComponent(new ChatComponent().setText(e.getPlayer().getName()).setColor(rank.nameColor)));
        if(rank.suffix.length!=0) components.add(BukkitCommon.toTextComponent(new ChatComponent().setText(" ")));
        for(ChatComponent c : rank.suffix) components.add(BukkitCommon.toTextComponent(c));
        if(showColon) components.add(BukkitCommon.toTextComponent(new ChatComponent().setText(": ").setColor("white")));
        components.add(BukkitCommon.toTextComponent(new ChatComponent().setText(e.getMessage()).setColor(rank.chatColor)));
        TextComponent[] acomponents = components.toArray(new TextComponent[0]);
        for(Player p : e.getRecipients()) {
            p.spigot().sendMessage(acomponents);
        }
    }

}
