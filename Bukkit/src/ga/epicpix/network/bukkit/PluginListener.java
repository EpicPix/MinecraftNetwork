package ga.epicpix.network.bukkit;

import ga.epicpix.network.common.CommonUtils;
import ga.epicpix.network.common.Reflection;
import ga.epicpix.network.common.players.PlayerInfo;
import ga.epicpix.network.common.players.PlayerManager;
import ga.epicpix.network.common.ranks.Rank;
import ga.epicpix.network.common.servers.ServerManager;
import ga.epicpix.network.common.settings.SettingsManager;
import ga.epicpix.network.common.text.ChatColor;
import ga.epicpix.network.common.text.ChatComponent;
import ga.epicpix.network.common.values.ValueType;
import ga.epicpix.network.common.http.websocket.Errorable;
import ga.epicpix.network.common.http.websocket.requests.data.UpdatePlayerRequest;
import ga.epicpix.network.common.http.websocket.requests.data.UpdateServerDataRequest;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.permissions.PermissibleBase;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.spigotmc.SpigotConfig;

import java.util.*;

public class PluginListener implements Listener {

    public static final ArrayList<Team> teams = new ArrayList<>();
    public static Scoreboard publicScoreboard;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        ServerManager.updateServer(BukkitCommon.getServerId(), new UpdateServerDataRequest.Data().setOnlinePlayers(Bukkit.getOnlinePlayers().size()));

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
        PermissibleBase base = (PermissibleBase) Reflection.getValueOfField(player.getClass(), "perm", player);
        PermissionAttachment attachment = new PermissionAttachment(Entry.PLUGIN, player) {
            public Map<String, Boolean> getPermissions() {
                base.clearPermissions();
                Map<String, PermissionAttachmentInfo> permissions = (Map<String, PermissionAttachmentInfo>) Reflection.getValueOfField(base.getClass(), "permissions", base);
                for(String perm : rank.getPermissions()) {
                    permissions.put(perm, new PermissionAttachmentInfo(player, perm, this, true));
                }
                return new HashMap<>();
            }
        };
        ((List<PermissionAttachment>) Reflection.getValueOfField(base.getClass(), "attachments", base)).add(attachment);
        player.recalculatePermissions();
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e) {
        ServerManager.updateServer(BukkitCommon.getServerId(), new UpdateServerDataRequest.Data().setOnlinePlayers(Bukkit.getOnlinePlayers().size()));
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();
        PlayerInfo info = PlayerManager.getPlayerOrCreate(player.getUniqueId(), player.getName()).getValue();
        Errorable<ValueType> rshowColon = SettingsManager.getSettingOrDefault("SHOW_COLON_CHAT", new ValueType(true));
        boolean showColon = true;
        if(!rshowColon.hasFailed()) {
            showColon = rshowColon.getValue().getAsBoolean();
        }
        Rank rank = info.getRank();
        e.setCancelled(true);
        ArrayList<ChatComponent> ccomponents = new ArrayList<>();
        Collections.addAll(ccomponents, rank.getPrefix());
        if(rank.getPrefix().length!=0) ccomponents.add(new ChatComponent().setText(" "));

        ccomponents.add(new ChatComponent().setText(e.getPlayer().getName()).setColor(rank.getNameColor()));

        if(rank.getSuffix().length!=0) ccomponents.add(new ChatComponent().setText(" "));
        ccomponents.addAll(Arrays.asList(rank.getSuffix()));

        if(showColon) ccomponents.add(new ChatComponent().setText(": ").setColor("white"));
        ccomponents.add(new ChatComponent().setText(e.getMessage()).setColor(rank.getChatColor()));


        ArrayList<TextComponent> components = new ArrayList<>();
        ccomponents.forEach((c) -> components.add(BukkitCommon.toTextComponent(c)));
        TextComponent[] acomponents = components.toArray(new TextComponent[0]);
        for(Player p : e.getRecipients()) {
            p.spigot().sendMessage(acomponents);
        }
        System.out.println(ChatColor.removeColorCodes(ChatColor.convertColorText(ChatComponent.componentsToString(ccomponents.toArray(new ChatComponent[0])))));
    }

}
