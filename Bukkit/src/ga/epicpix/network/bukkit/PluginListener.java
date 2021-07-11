package ga.epicpix.network.bukkit;

import ga.epicpix.network.common.*;
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
        CommonUtils.updateServerInfo(BukkitCommon.getThisServer());

        Player player = e.getPlayer();
        PlayerInfo info = PlayerInfo.getPlayerInfo(player.getUniqueId());
        if(info==null) {
            info = new PlayerInfo().populate(player.getUniqueId(), player.getName(), CommonUtils.getDefaultRank(), CommonUtils.getDefaultLanguage());
        }
        if(!SpigotConfig.bungee) {
            info.lastLogin = System.currentTimeMillis();
            PlayerInfo.updatePlayerInfo(info);
        }
        Rank rank = info.getRank();
        String name = CommonUtils.makeStringLength(Integer.toString(rank.priority), 4, "0") + rank.id;
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
        team.setPrefix(CommonUtils.componentsToString(rank.prefix) + (rank.prefix.length==0?"":" ") + ChatColor.convertColorText("/" + rank.nameColor + "/"));
        team.setSuffix((rank.suffix.length==0?"":ChatColor.convertColorText("/white/ ")) + CommonUtils.componentsToString(rank.suffix));
        team.removePlayer(player);
        team.addPlayer(player);
        player.setScoreboard(publicScoreboard);
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
