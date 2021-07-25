package ga.epicpix.network.bungee;

import ga.epicpix.network.bungee.commands.BungeeCommandConsole;
import ga.epicpix.network.bungee.commands.BungeeCommandPlayer;
import ga.epicpix.network.common.ChatColor;
import ga.epicpix.network.common.players.PlayerInfo;
import ga.epicpix.network.common.players.PlayerManager;
import ga.epicpix.network.common.ranks.RankManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.command.ConsoleCommandSender;

public abstract class Command {

    public static void registerCommand(Command cmd) {
        ProxyServer.getInstance().getPluginManager().registerCommand(Entry.INSTANCE, new net.md_5.bungee.api.plugin.Command(cmd.getName()) {

            public void execute(CommandSender sender, String[] args) {
                PlayerInfo info = null;
                if(sender instanceof ProxiedPlayer player) {
                    info = PlayerManager.getPlayer(player.getUniqueId());
                    if(info==null) {
                        info = PlayerManager.createPlayer(player.getUniqueId(), player.getName());
                    }
                    String req = cmd.getRequiredPermission();
                    if(req!=null) {
                        boolean has = false;
                        for(String permission : info.getRank().getPermissions()) {
                            if(permission.equals(req)) {
                                has = true;
                                break;
                            }
                        }
                        if(!has) {
                            sender.sendMessage(ChatColor.convertColorText("/red/You don't have enough permissions!"));
                            return;
                        }
                    }
                }
                cmd.createContext().setData(sender instanceof ProxiedPlayer ? new BungeeCommandPlayer((ProxiedPlayer) sender) : new BungeeCommandConsole((ConsoleCommandSender) sender), info, args).run();
            }
        });

    }

    public abstract String getName();
    public abstract String getRequiredPermission();
    public abstract CommandContext createContext();

}
