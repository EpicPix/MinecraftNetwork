package ga.epicpix.network.bukkit;

import ga.epicpix.network.common.*;
import ga.epicpix.network.common.players.PlayerInfo;
import ga.epicpix.network.common.players.PlayerManager;
import ga.epicpix.network.common.ranks.Rank;
import ga.epicpix.network.common.servers.ServerInfo;
import ga.epicpix.network.common.settings.SettingsManager;
import ga.epicpix.network.common.text.ChatColor;
import ga.epicpix.network.common.text.ChatComponent;
import ga.epicpix.network.common.values.ValueType;
import ga.epicpix.network.common.websocket.requests.data.UpdatePlayerRequest;
import ga.epicpix.network.common.websocket.requests.data.UpdateServerDataRequest;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.spigotmc.SpigotConfig;

import java.util.ArrayList;

public class PluginListener implements Listener {

    public static final ArrayList<Team> teams = new ArrayList<>();
    public static Scoreboard publicScoreboard;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        ServerInfo.updateServer(BukkitCommon.getServerId(), new UpdateServerDataRequest.Data().setOnlinePlayers(Bukkit.getOnlinePlayers().size()));

        Player player = e.getPlayer();
        PlayerInfo info = PlayerManager.getPlayerOrCreate(player.getUniqueId(), player.getName()).getValue();
        if(!SpigotConfig.bungee) {
            PlayerManager.updatePlayer(player.getUniqueId(), player.getName(), new UpdatePlayerRequest.Data().setLastLogin(System.currentTimeMillis()));
        }
        Rank rank = info.getRank();
        String name = CommonUtils.makeStringLengthPrepend(Integer.toString(rank.getPriority()), 4, "0") + rank.getId();
        Team team = null;
        for(Team iteam : teams) {
            if(name.equals(iteam.getName())) {
                team = iteam;
                break;
            }
        }
        if(team==null) {
            team = publicScoreboard.registerNewTeam(name);
            teams.add(team);
        }
        team.setPrefix(ChatComponent.componentsToString(rank.getPrefix()) + (rank.getPrefix().length==0?"":" ") + ChatColor.convertColorText("/" + rank.getNameColor() + "/"));
        team.setSuffix((rank.getSuffix().length==0?"":ChatColor.convertColorText("/white/ ")) + ChatComponent.componentsToString(rank.getSuffix()));
        team.removeEntry(player.getName());
        team.addEntry(player.getName());
        player.setScoreboard(publicScoreboard);
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e) {
        ServerInfo.updateServer(BukkitCommon.getServerId(), new UpdateServerDataRequest.Data().setOnlinePlayers(Bukkit.getOnlinePlayers().size()));
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();
        PlayerInfo info = PlayerManager.getPlayerOrCreate(player.getUniqueId(), player.getName()).getValue();
        boolean showColon = SettingsManager.getSettingOrDefault("SHOW_COLON_CHAT", new ValueType(true)).getAsBoolean();
        Rank rank = info.getRank();
        e.setCancelled(true);
        ArrayList<TextComponent> components = new ArrayList<>();
        for(ChatComponent c : rank.getPrefix()) components.add(BukkitCommon.toTextComponent(c));
        if(rank.getPrefix().length!=0) components.add(BukkitCommon.toTextComponent(new ChatComponent().setText(" ")));
        components.add(BukkitCommon.toTextComponent(new ChatComponent().setText(e.getPlayer().getName()).setColor(rank.getNameColor())));
        if(rank.getSuffix().length!=0) components.add(BukkitCommon.toTextComponent(new ChatComponent().setText(" ")));
        for(ChatComponent c : rank.getSuffix()) components.add(BukkitCommon.toTextComponent(c));
        if(showColon) components.add(BukkitCommon.toTextComponent(new ChatComponent().setText(": ").setColor("white")));
        components.add(BukkitCommon.toTextComponent(new ChatComponent().setText(e.getMessage()).setColor(rank.getChatColor())));
        TextComponent[] acomponents = components.toArray(new TextComponent[0]);
        for(Player p : e.getRecipients()) {
            p.spigot().sendMessage(acomponents);
        }
    }

}
