package ga.epicpix.network.cli;

import com.google.gson.JsonObject;
import ga.epicpix.network.common.modules.ModuleData;
import ga.epicpix.network.common.modules.ModuleManager;
import ga.epicpix.network.common.net.websocket.WebSocketRequester;
import ga.epicpix.network.common.net.websocket.requests.GetModuleRequest;
import ga.epicpix.network.common.ranks.Rank;
import ga.epicpix.network.common.ranks.RankManager;
import ga.epicpix.network.common.servers.ServerManager;
import ga.epicpix.network.common.servers.ServerVersion;
import ga.epicpix.network.common.text.ChatColor;
import ga.epicpix.network.common.servers.ServerInfo;
import ga.epicpix.network.common.settings.SettingsManager;
import ga.epicpix.network.common.text.ChatComponent;
import ga.epicpix.network.common.values.ValueType;
import ga.epicpix.network.common.net.websocket.ClientType;
import ga.epicpix.network.common.net.websocket.Errorable;
import ga.epicpix.network.common.net.websocket.WebSocketConnection;

import java.io.Console;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Scanner;

import static ga.epicpix.network.cli.Ansi.*;
import static ga.epicpix.network.cli.Utils.*;
import static ga.epicpix.network.common.CommonUtils.*;

public class Main {

    private static boolean warningShown = false;

    public static void main(String[] args) {
        System.setOut(new PrintStream(System.out) {
            public void println(String s) {
                super.println(ansi(s, true));
            }
            public void print(String s) {
                super.print(ansi(s, true));
            }
        });
        System.setErr(new PrintStream(System.err) {
            public void println(String s) {
                if(ansi) {
                    System.out.println("/red/" + s);
                }else {
                    super.println(ansi(s, false));
                }
            }
            public void print(String s) {
                if(ansi) {
                    System.out.print("/red/" + s);
                }else {
                    super.print(ansi(s, false));
                }
            }
        });

        System.out.println("Logging in...");

        WebSocketConnection.setClientType(ClientType.CLI);
        WebSocketConnection.addHook("type_auth", () -> {
            System.out.println("/red/Could not authenticate, type the credentials");

            Console console = System.console();
            // console will be probably null if it's run inside a IDE
            String username;
            String password;
            if(console==null) {
                if(!warningShown) {
                    System.out.println("/yellow/Warning! /red/The password will be visible");
                    warningShown = true;
                }
                Scanner sc = new Scanner(System.in);
                System.out.print("/green/Username: ");
                username = sc.nextLine();
                System.out.print("/yellow/Password: ");
                password = sc.nextLine();
            }else {
                if(!warningShown) {
                    System.out.println("/red/(The password will not be visible)");
                    warningShown = true;
                }
                System.out.print("/green/Username: ");
                username = console.readLine();
                System.out.println("yellow/Password: ");
                password = new String(console.readPassword());
            }
            return new String[] {username, password};
        });
        if(!WebSocketConnection.connect()) {
            return;
        }

        System.out.println("Network Manager CLI");
        System.out.println("Type \"help\" for help");
        if(ServerVersion.load().hasFailed()) {
            System.err.println("Versions could not be loaded!");
        }

        Scanner sc = new Scanner(System.in);
        while(true) {
            System.out.print("/dark_aqua/> ");
            String line = sc.nextLine();
            String[] cmd = line.split(" ");
            String cmdline = getParam(cmd, 0);
            if(cmdline.equals("ansi")) {
                ansi = !ansi;
                if(ansi) {
                    System.out.println("/green/Enabled ANSI codes");
                }else {
                    System.out.println("Disabled ANSI codes");
                }
            }else if(cmdline.equals("exit")) {
                System.out.println("Goodbye!");
                System.exit(0);
            }else if(cmdline.equals("help")) {
                System.out.println("ansi - Enables/Disables ANSI Codes");
                System.out.println("exit - Exits this manager");
                System.out.println("help - Show help");
                System.out.println("ranks - List ranks");
                System.out.println("settings - List settings");
                System.out.println("server <id> stop - Stop a server");
                System.out.println("servers - List servers");
            }else if(cmdline.equals("ranks")) {
                Errorable<ArrayList<Rank>> rresp = RankManager.getRanks();
                if(rresp.hasFailed()) {
                    System.out.println("/red/Could not request the rank list!");
                }else {
                    showRanksListing(rresp.getValue());
                }
            }else if(cmdline.equals("settings")) {
                Errorable<HashMap<String, ValueType>> rresp = SettingsManager.getSettings();
                if(rresp.hasFailed()) {
                    System.out.println("/red/Could not request the setting list!");
                }else {
                    showSettingsListing(rresp.getValue());
                }
            }else if(cmdline.equals("server")) {
                if(getParam(cmd, 1) != null) {
                    Errorable<ServerInfo> reqserver = ServerManager.getServerInfo(getParam(cmd, 1));
                    if(reqserver.hasFailed()) {
                        System.out.println("/red/Unknown server! Use \"servers\" to list servers");
                    }else {
                        ServerInfo server = reqserver.getValue();
                        if(getParam(cmd, 2) != null && getParam(cmd, 2).equals("stop")) {
                            if(!ServerManager.sendSignal(server.id, ServerInfo.ServerSignal.STOP).hasFailed()) {
                                System.out.println("/green/Signal sent");
                            }else {
                                System.out.println("/red/Could not send signal");
                            }
                        }else {
                            System.out.println("/red/Usage: server " + server.id + " <stop>");
                        }
                    }
                }else {
                    System.out.println("/red/Wrong usage! Use \"help\" for help");
                }
            }else if(cmdline.equals("servers")) {
                Errorable<ArrayList<ServerInfo>> resp = ServerManager.requestServerList();
                if(resp.hasFailed()) {
                    System.out.println("/red/Could not request server list!");
                }else {
                    showServerListing(resp.getValue());
                }
            }else if(cmdline.equals("modules")) {
                Errorable<ArrayList<ModuleData>> resp = ModuleManager.getModules();
                if(resp.hasFailed()) {
                    System.out.println("/red/Could not request the module list!");
                }else {
                    showModulesListing(resp.getValue());
                }
            }else if(cmdline.equals("module")) {
                if(getParam(cmd, 1) != null) {
                    String id = getParam(cmd, 1);
                    JsonObject data = WebSocketRequester.sendRequest(GetModuleRequest.build(id));
                    System.out.println("Response: " + data);
                }else {
                    System.out.println("/red/Wrong usage! Use \"help\" for help");
                }
            }else {
                System.out.println("/red/Unknown command. Type \"help\" for help");
            }
        }
    }

    public static int itemEntryLength(int len) {
        return len+ansiLength(AQUA + GREEN);
    }

    public static String showChatRankExample(Rank rank) {
        StringBuilder builder = new StringBuilder();
        String prefix = ChatColor.convertColorText(ChatComponent.componentsToString(rank.getPrefix()));
        if(prefix.length()!=0) builder.append(prefix).append(" ");
        builder.append("%username%");
        String suffix = ChatColor.convertColorText(ChatComponent.componentsToString(rank.getSuffix()));
        if(suffix.length()!=0) builder.append(" ").append(suffix);
        return builder.toString().replace(ChatColor.COLOR_CHAR, '&');
    }

    public static void showRanksListing(ArrayList<Rank> ranks) {
        if(ranks.size()==0) {
            showErrorMessage("No ranks found.");
            return;
        }

        int idLen = getLongestFromCompute(2, Rank::getId, ranks) + 2;
        int priorityLen = getLongestFromCompute(8, Rank::getPriority, ranks) + 2;
        int chatLen = getLongestFromCompute(4, Main::showChatRankExample, ranks) + 2;

        String rep = "║" + " ".repeat(idLen) + "|" + " ".repeat(priorityLen) + "|" + " ".repeat(chatLen) + "║";

        rep = setStrings(rep, new StringInfo(idLen, "ID"), new StringInfo(priorityLen, "PRIORITY"), new StringInfo(chatLen, "CHAT"));

        printlnGreen(replaceCharactersSpecial("╔" + "═".repeat(rep.length()-2) + "╗", rep, '|', '╤'));
        printlnGreen(rep.replace('|', '│'));
        printlnGreen(replaceCharactersSpecial("╟" + "─".repeat(rep.length()-2) + "╢", rep, '|', '┼'));

        for(Rank rank : ranks) {
            String settingOut = ansi("/green/║" + " ".repeat(itemEntryLength(idLen))
                    + "│" + " ".repeat(itemEntryLength(priorityLen))
                    + "│" + " ".repeat(itemEntryLength(chatLen)) + "║", true);

            int x = 2 + ansiLength(GREEN);
            settingOut = replaceAt(settingOut, x, ansi("/aqua/" + rank.getId() + "/green/", false));
            x += 1 + itemEntryLength(idLen);
            settingOut = replaceAt(settingOut, x, ansi("/aqua/" + rank.getPriority() + "/green/", false));
            x += 1 + itemEntryLength(priorityLen);
            settingOut = replaceAt(settingOut, x, ansi("/aqua/" + showChatRankExample(rank) + "/green/", false));

            System.out.println(settingOut);

        }

        printlnGreen(replaceCharactersSpecial("╚" + "═".repeat( rep.length()-2) + "╝", rep, '|', '╧'));

    }

    public static void showSettingsListing(HashMap<String, ValueType> settings) {
        if(settings.size()==0) {
            showErrorMessage("No settings found.");
            return;
        }

        int nameLen = getLongestFromCompute(4, str -> str, settings.keySet()) + 2;
        int typeLen = getLongestFromCompute(4, ValueType::convertValueTypeToString, settings.values()) + 2;
        int valueLen = getLongestFromCompute(5, ValueType::toString, settings.values()) + 2;

        String rep = "║" + " ".repeat(nameLen) + "|" + " ".repeat(typeLen) + "|" + " ".repeat(valueLen) + "║";

        rep = setStrings(rep, new StringInfo(nameLen, "NAME"), new StringInfo(typeLen, "TYPE"), new StringInfo(valueLen, "VALUE"));

        printlnGreen(replaceCharactersSpecial("╔" + "═".repeat(rep.length()-2) + "╗", rep, '|', '╤'));
        printlnGreen(rep.replace('|', '│'));
        printlnGreen(replaceCharactersSpecial("╟" + "─".repeat(rep.length()-2) + "╢", rep, '|', '┼'));

        for(Entry<String, ValueType> setting : settings.entrySet()) {
            String settingOut = ansi("/green/║" + " ".repeat(itemEntryLength(nameLen))
                    + "│" + " ".repeat(itemEntryLength(typeLen))
                    + "│" + " ".repeat(itemEntryLength(valueLen)) + "║", true);

            int x = 2 + ansiLength(GREEN);
            settingOut = replaceAt(settingOut, x, ansi("/aqua/" + setting.getKey() + "/green/", false));
            x += 1 + itemEntryLength(nameLen);
            settingOut = replaceAt(settingOut, x, ansi("/aqua/" + ValueType.convertValueTypeToString(setting.getValue()) + "/green/", false));
            x += 1 + itemEntryLength(typeLen);
            settingOut = replaceAt(settingOut, x, ansi("/aqua/" + setting.getValue().toString() + "/green/", false));

            System.out.println(settingOut);

        }

        printlnGreen(replaceCharactersSpecial("╚" + "═".repeat( rep.length()-2) + "╝", rep, '|', '╧'));

    }

    public static void showServerListing(ArrayList<ServerInfo> servers) {

        if(servers.size()==0) {
            showErrorMessage("No servers found.");
            return;
        }

        final long time = System.currentTimeMillis();
        int idLen = getLongestFromCompute(2, (serv) -> serv.id, servers) + 2;
        int typeLen = getLongestFromCompute(4, (serv) -> serv.type, servers) + 2;
        int playersLen = getLongestFromCompute(7, (serv) -> serv.onlinePlayers + "/" + serv.maxPlayers, servers) + 2;
        int versionLen = getLongestFromCompute(7, (serv) -> serv.version.getStringVersion(), servers) + 2;
        int ipLen = getLongestFromCompute(2, (serv) -> serv.details.getIp() + ":" + serv.details.getPort(), servers) + 2;
        int uptimeLen = getLongestFromCompute(6, (serv) -> convertTime(time-serv.start), servers) + 2;

        String rep = "║" + " ".repeat(idLen) + "|" + " ".repeat(typeLen) + "|" + " ".repeat(playersLen) + "|" + " ".repeat(versionLen) + "|" + " ".repeat(ipLen) + "|" + " ".repeat(uptimeLen) + "║";

        rep = setStrings(rep, new StringInfo(idLen, "ID"), new StringInfo(typeLen, "TYPE"), new StringInfo(playersLen, "PLAYERS"), new StringInfo(versionLen, "VERSION"), new StringInfo(ipLen, "IP"), new StringInfo(uptimeLen, "UPTIME"));

        printlnGreen(replaceCharactersSpecial("╔" + "═".repeat(rep.length()-2) + "╗", rep, '|', '╤'));
        printlnGreen(rep.replace('|', '│'));
        printlnGreen(replaceCharactersSpecial("╟" + "─".repeat(rep.length()-2) + "╢", rep, '|', '┼'));

        for(ServerInfo server : servers) {
            String serverOut = ansi("/green/║" + " ".repeat(itemEntryLength(idLen))
                    + "│" + " ".repeat(itemEntryLength(typeLen))
                    + "│" + " ".repeat(itemEntryLength(playersLen))
                    + "│" + " ".repeat(itemEntryLength(versionLen))
                    + "│" + " ".repeat(itemEntryLength(ipLen))
                    + "│" + " ".repeat(itemEntryLength(uptimeLen)) + "║", true);

            int x = 2+ansiLength(GREEN);
            serverOut = replaceAt(serverOut, x, ansi("/aqua/" + server.id + "/green/", false));
            x += 1+itemEntryLength(idLen);
            serverOut = replaceAt(serverOut, x, ansi("/aqua/" + server.type + "/green/", false));
            x += 1+itemEntryLength(typeLen);
            serverOut = replaceAt(serverOut, x, ansi("/aqua/" + server.onlinePlayers + "/" + server.maxPlayers + "/green/", false));
            x += 1+itemEntryLength(playersLen);
            serverOut = replaceAt(serverOut, x, ansi("/aqua/" + server.version.getStringVersion() + "/green/", false));
            x += 1+itemEntryLength(versionLen);
            serverOut = replaceAt(serverOut, x, ansi("/aqua/" + server.details.getIp() + ":" + server.details.getPort() + "/green/", false));
            x += 1+itemEntryLength(ipLen);
            serverOut = replaceAt(serverOut, x, ansi("/aqua/" + convertTime(time-server.start) + "/green/", false));

            System.out.println(serverOut);
        }

        printlnGreen(replaceCharactersSpecial("╚" + "═".repeat( rep.length()-2) + "╝", rep, '|', '╧'));

    }

    public static void showModulesListing(ArrayList<ModuleData> moduleData) {

        if(moduleData.size()==0) {
            showErrorMessage("No modules found.");
            return;
        }

        final long time = System.currentTimeMillis();
        int idLen = getLongestFromCompute(2, ModuleData::getId, moduleData) + 2;
        int nameLen = getLongestFromCompute(4, ModuleData::getName, moduleData) + 2;
        int libraryLen = getLongestFromCompute(7, ModuleData::getLibrary, moduleData) + 2;
        int versionLen = getLongestFromCompute(7, ModuleData::getVersion, moduleData) + 2;

        String rep = "║" + " ".repeat(idLen) + "|" + " ".repeat(nameLen) + "|" + " ".repeat(libraryLen) + "|" + " ".repeat(versionLen) + "║";

        rep = setStrings(rep, new StringInfo(idLen, "Id"), new StringInfo(nameLen, "Name"), new StringInfo(libraryLen, "Library"), new StringInfo(versionLen, "Version"));

        printlnGreen(replaceCharactersSpecial("╔" + "═".repeat(rep.length()-2) + "╗", rep, '|', '╤'));
        printlnGreen(rep.replace('|', '│'));
        printlnGreen(replaceCharactersSpecial("╟" + "─".repeat(rep.length()-2) + "╢", rep, '|', '┼'));

        for(ModuleData server : moduleData) {
            String serverOut = ansi("/green/║" + " ".repeat(itemEntryLength(idLen))
                + "│" + " ".repeat(itemEntryLength(nameLen))
                + "│" + " ".repeat(itemEntryLength(libraryLen))
                + "│" + " ".repeat(itemEntryLength(versionLen)) + "║", true);

            int x = 2+ansiLength(GREEN);
            serverOut = replaceAt(serverOut, x, ansi("/aqua/" + server.getId() + "/green/", false));
            x += 1+itemEntryLength(idLen);
            serverOut = replaceAt(serverOut, x, ansi("/aqua/" + server.getName() + "/green/", false));
            x += 1+itemEntryLength(nameLen);
            serverOut = replaceAt(serverOut, x, ansi("/aqua/" + server.getLibrary() + "/green/", false));
            x += 1+itemEntryLength(libraryLen);
            serverOut = replaceAt(serverOut, x, ansi("/aqua/" + server.getVersion() + "/green/", false));
            x += 1+itemEntryLength(versionLen);
            System.out.println(serverOut);
        }

        printlnGreen(replaceCharactersSpecial("╚" + "═".repeat( rep.length()-2) + "╝", rep, '|', '╧'));

    }

    public static void showErrorMessage(String errmsg) {
        StringBuilder builder = new StringBuilder();
        //Setup string
        builder.append("/red/");
        builder.append("╔══════════════════════════════╗").append("\n");
        builder.append("║                              ║").append("\n");
        builder.append("╚══════════════════════════════╝");
        //Replace part of the string to contain error message
        int at = builder.indexOf("║") / 2 + builder.lastIndexOf("║") / 2 - (errmsg.length()-1) / 2;
        for(int i = 0; i<errmsg.length(); i++) builder.setCharAt(at+i, errmsg.charAt(i));
        System.out.println(builder);
    }

}
