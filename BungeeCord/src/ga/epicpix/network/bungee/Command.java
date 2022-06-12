package ga.epicpix.network.bungee;

import ga.epicpix.network.bungee.commands.BungeeCommandConsole;
import ga.epicpix.network.bungee.commands.BungeeCommandPlayer;
import ga.epicpix.network.common.modules.Module;
import ga.epicpix.network.common.text.ChatColor;
import ga.epicpix.network.common.players.PlayerInfo;
import ga.epicpix.network.common.players.PlayerManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.command.ConsoleCommandSender;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class Command {

    static HashMap<Module, ArrayList<net.md_5.bungee.api.plugin.Command>> moduleToCommands = new HashMap<>();

    public static void registerCommand(Module module, Command cmd) {
        var bungeeCommand = new net.md_5.bungee.api.plugin.Command(cmd.getName()) {
            public void execute(CommandSender sender, String[] args) {
                PlayerInfo info = null;
                if(sender instanceof ProxiedPlayer) {
                    ProxiedPlayer player = (ProxiedPlayer) sender;
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
                            sender.sendMessage(TextComponent.fromLegacyText(ChatColor.convertColorText("/red/You don't have enough permissions!")));
                            return;
                        }
                    }
                }
                cmd.createContext().setData(sender instanceof ProxiedPlayer ? new BungeeCommandPlayer((ProxiedPlayer) sender) : new BungeeCommandConsole((ConsoleCommandSender) sender), info, args).run();
            }
        };

        moduleToCommands.get(module).add(bungeeCommand);
        ProxyServer.getInstance().getPluginManager().registerCommand(Entry.INSTANCE, bungeeCommand);

    }

    public abstract String getName();
    public abstract String getRequiredPermission();
    public abstract CommandContext createContext();

}
