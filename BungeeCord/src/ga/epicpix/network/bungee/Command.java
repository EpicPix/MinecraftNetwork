package ga.epicpix.network.bungee;

import ga.epicpix.network.bungee.commands.BungeeCommandConsole;
import ga.epicpix.network.bungee.commands.BungeeCommandPlayer;
import ga.epicpix.network.common.CommonUtils;
import ga.epicpix.network.common.Language;
import ga.epicpix.network.common.PlayerInfo;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.command.ConsoleCommandSender;

import java.util.Map;

public abstract class Command {

    public static void registerCommand(Command cmd) {
        ProxyServer.getInstance().getPluginManager().registerCommand(Entry.INSTANCE, new net.md_5.bungee.api.plugin.Command(cmd.getName()) {

            public void execute(CommandSender sender, String[] args) {
                PlayerInfo info = null;
                if(sender instanceof ProxiedPlayer) {
                    ProxiedPlayer player = (ProxiedPlayer) sender;
                    info = PlayerInfo.getPlayerInfo(player.getUniqueId());
                    if(info==null) {
                        info = PlayerInfo.updatePlayerInfo(new PlayerInfo().populate(player.getUniqueId(), player.getName(), CommonUtils.getDefaultRank(), CommonUtils.getDefaultLanguage()));
                    }
                    String req = cmd.getRequiredPermission();
                    if(req!=null) {
                        boolean has = false;
                        for(String permission : info.getRank().permissions) {
                            if(permission.equals(req)) {
                                has = true;
                                break;
                            }
                        }
                        if(!has) {
                            sender.sendMessage(Language.getTranslation("error.no_permissions", info.language));
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
