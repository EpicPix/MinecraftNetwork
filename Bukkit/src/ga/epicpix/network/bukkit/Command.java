package ga.epicpix.network.bukkit;

import ga.epicpix.network.bukkit.commands.BukkitCommandConsole;
import ga.epicpix.network.bukkit.commands.BukkitCommandPlayer;
import ga.epicpix.network.common.modules.Module;
import ga.epicpix.network.common.text.ChatColor;
import ga.epicpix.network.common.players.PlayerInfo;
import ga.epicpix.network.common.players.PlayerManager;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class Command {

    static HashMap<Module, ArrayList<org.bukkit.command.Command>> moduleToCommands = new HashMap<>();

    public static void registerCommand(Module module, Command cmd) {
        Map<String, org.bukkit.command.Command> map = BukkitCommon.getCommandMap();
        var bukkitCommand = new org.bukkit.command.Command(cmd.getName()) {

            public boolean execute(CommandSender sender, String usedCommandName, String[] args) {
                PlayerInfo info = null;
                if(sender instanceof Player) {
                    Player player = (Player) sender;
                    info = PlayerManager.getPlayerOrCreate(player.getUniqueId(), player.getName()).getValue();
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
        };
        moduleToCommands.get(module).add(bukkitCommand);
        map.put(cmd.getName(), bukkitCommand);
    }

    public abstract String getName();
    public abstract String getRequiredPermission();
    public abstract CommandContext createContext();

}
