package ga.epicpix.network.bukkit;

import ga.epicpix.network.bukkit.commands.BukkitCommandConsole;
import ga.epicpix.network.bukkit.commands.BukkitCommandPlayer;
import ga.epicpix.network.common.ChatColor;
import ga.epicpix.network.common.players.PlayerInfo;
import ga.epicpix.network.common.players.PlayerManager;
import ga.epicpix.network.common.ranks.RankManager;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

public abstract class Command {

    public static void registerCommand(Command cmd) {
        Map<String, org.bukkit.command.Command> map = BukkitCommon.getCommandMap();
        map.put(cmd.getName(), new org.bukkit.command.Command(cmd.getName()) {

            public boolean execute(CommandSender sender, String usedCommandName, String[] args) {
                PlayerInfo info = null;
                if(sender instanceof Player player) {
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
                            return false;
                        }
                    }
                }
                cmd.createContext().setData(sender instanceof Player ? new BukkitCommandPlayer((Player) sender) : new BukkitCommandConsole((ConsoleCommandSender) sender), info, args).run();
                return true;
            }
        });
    }

    public abstract String getName();
    public abstract String getRequiredPermission();
    public abstract CommandContext createContext();

}
