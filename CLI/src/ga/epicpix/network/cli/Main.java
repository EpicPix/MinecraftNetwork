package ga.epicpix.network.cli;

import ga.epicpix.network.common.ranks.Rank;
import ga.epicpix.network.common.ranks.RankManager;
import ga.epicpix.network.common.servers.ServerManager;
import ga.epicpix.network.common.text.ChatColor;
import ga.epicpix.network.common.servers.ServerInfo;
import ga.epicpix.network.common.settings.SettingsManager;
import ga.epicpix.network.common.text.ChatComponent;
import ga.epicpix.network.common.values.ValueType;
import ga.epicpix.network.common.websocket.ClientType;
import ga.epicpix.network.common.websocket.Errorable;
import ga.epicpix.network.common.websocket.WebSocketConnection;

import java.io.Console;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Scanner;

import static ga.epicpix.network.cli.Utils.*;
import static ga.epicpix.network.common.CommonUtils.*;

public class Main {

    public static final String DARK_AQUA = "\u001b[36m";
    public static final String AQUA = "\u001b[36;1m";
    public static final String GREEN = "\u001b[32;1m";
    public static final String RED = "\u001b[31;1m";
    public static final String YELLOW = "\u001b[33m";
    public static final String WHITE = "\u001b[37;1m";
    public static final String GRAY = "\u001b[30;1m";
    public static final String LIGHT_GRAY = "\u001b[37m";
    public static final String RESET = "\u001b[0m";

    public static boolean ansi = System.getenv().get("TERM")!=null;

    public static String ansi(String in, boolean reset) {
        StringBuilder rebuilt = new StringBuilder();
        in = ChatColor.convertColorText(in);
        if(ansi) {
            for(int i = 0; i<in.length(); i++) {
                if(in.charAt(i)==ChatColor.COLOR_CHAR) {
                    i++;
                    char c = in.charAt(i);
                    if(c=='3') {
                        rebuilt.append(DARK_AQUA);
                    }else if(c=='7') {
                        rebuilt.append(LIGHT_GRAY);
                    }else if(c=='8') {
                        rebuilt.append(GRAY);
                    }else if(c=='a') {
                        rebuilt.append(GREEN);
                    }else if(c=='b') {
                        rebuilt.append(AQUA);
                    }else if(c=='c') {
                        rebuilt.append(RED);
                    }else if(c=='e') {
                        rebuilt.append(YELLOW);
                    }else if(c=='f') {
                        rebuilt.append(WHITE);
                    }else if(c=='r') {
                        rebuilt.append(RESET);
                    }else {
                        rebuilt.append(ChatColor.COLOR_CHAR + c);
                    }
                }else {
                    rebuilt.append(in.charAt(i));
                }
            }
            if(reset) rebuilt.append(RESET);
        }else {
            for(int i = 0; i<in.length(); i++) {
                if(in.charAt(i)==ChatColor.COLOR_CHAR) {
                    i++;
                }else {
                    rebuilt.append(in.charAt(i));
                }
            }
        }
        return rebuilt.toString();
    }

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
        WebSocketConnection.connect();

        System.out.println("Network Manager CLI");
        System.out.println("Type \"help\" for help");

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
            }else {
                System.out.println("/red/Unknown command. Type \"help\" for help");
            }
        }
    }

    public static String convertTime(long timems) {
        boolean neg = timems<0;
        timems = Math.abs(timems);
        long sec = (timems/1000)%60;
        long min = (timems/60000)%60;
        long hr = (timems/3600000)%24;
        long d = timems/86400000;
        if(d!=0) return (neg?"-":"") + d + "d " + hr + "h " + min + "m " + sec + "s";
        else if(hr!=0) return (neg?"-":"") + hr + "h " + min + "m " + sec + "s";
        else if(min!=0) return (neg?"-":"") + min + "m " + sec + "s";
        else return (neg?"-":"") + sec + "s";
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
            showNothingFound("No ranks found.");
            return;
        }

        int id = getLongestFromCompute(2, Rank::getId, ranks) + 2;
        int priority = getLongestFromCompute(8, Rank::getPriority, ranks) + 2;
        int chat = getLongestFromCompute(4, Main::showChatRankExample, ranks) + 2;

        String rep = "║" + " ".repeat(id) + "|" + " ".repeat(priority) + "|" + " ".repeat(chat) + "║";

        rep = setStrings(rep, new StringInfo(id, "ID"), new StringInfo(priority, "PRIORITY"), new StringInfo(chat, "CHAT"));

        System.out.println("/green/" + replaceCharactersSpecial("╔" + "═".repeat(rep.length()-2) + "╗", rep, '|', '╤'));
        System.out.println("/green/" + rep.replace('|', '│'));
        System.out.println("/green/" + replaceCharactersSpecial("╟" + "─".repeat(rep.length()-2) + "╢", rep, '|', '┼'));

        for(Rank rank : ranks) {
            String settingOut = ansi("/green/║" + " ".repeat(id + (ansi ? AQUA.length() + GREEN.length() : 0))
                    + "│" + " ".repeat(priority + (ansi ? AQUA.length() + GREEN.length() : 0))
                    + "│" + " ".repeat(chat + (ansi ? AQUA.length() + GREEN.length() : 0)) + "║", true);

            int x = 2 + (ansi ? GREEN.length() : 0);
            settingOut = replaceAt(settingOut, x, ansi("/aqua/" + rank.getId() + "/green/", false));
            x += 1 + id + (ansi ? AQUA.length() + GREEN.length() : 0);
            settingOut = replaceAt(settingOut, x, ansi("/aqua/" + rank.getPriority() + "/green/", false));
            x += 1 + priority + (ansi ? AQUA.length() + GREEN.length() : 0);
            settingOut = replaceAt(settingOut, x, ansi("/aqua/" + showChatRankExample(rank) + "/green/", false));

            System.out.println(settingOut);

        }

        System.out.println("/green/" + replaceCharactersSpecial("╚" + "═".repeat( rep.length()-2) + "╝", rep, '|', '╧'));

    }

    public static void showSettingsListing(HashMap<String, ValueType> settings) {
        if(settings.size()==0) {
            showNothingFound("No settings found.");
            return;
        }

        int name = getLongestFromCompute(4, str -> str, settings.keySet()) + 2;
        int type = getLongestFromCompute(4, ValueType::convertValueTypeToString, settings.values()) + 2;
        int value = getLongestFromCompute(5, ValueType::toString, settings.values()) + 2;

        String rep = "║" + " ".repeat(name) + "|" + " ".repeat(type) + "|" + " ".repeat(value) + "║";

        rep = setStrings(rep, new StringInfo(name, "NAME"), new StringInfo(type, "TYPE"), new StringInfo(value, "VALUE"));

        System.out.println("/green/" + replaceCharactersSpecial("╔" + "═".repeat(rep.length()-2) + "╗", rep, '|', '╤'));
        System.out.println("/green/" + rep.replace('|', '│'));
        System.out.println("/green/" + replaceCharactersSpecial("╟" + "─".repeat(rep.length()-2) + "╢", rep, '|', '┼'));

        for(Entry<String, ValueType> setting : settings.entrySet()) {
            String settingOut = ansi("/green/║" + " ".repeat(name + (ansi ? AQUA.length() + GREEN.length() : 0))
                    + "│" + " ".repeat(type + (ansi ? AQUA.length() + GREEN.length() : 0))
                    + "│" + " ".repeat(value + (ansi ? AQUA.length() + GREEN.length() : 0)) + "║", true);

            int x = 2 + (ansi ? GREEN.length() : 0);
            settingOut = replaceAt(settingOut, x, ansi("/aqua/" + setting.getKey() + "/green/", false));
            x += 1 + name + (ansi ? AQUA.length() + GREEN.length() : 0);
            settingOut = replaceAt(settingOut, x, ansi("/aqua/" + ValueType.convertValueTypeToString(setting.getValue()) + "/green/", false));
            x += 1 + type + (ansi ? AQUA.length() + GREEN.length() : 0);
            settingOut = replaceAt(settingOut, x, ansi("/aqua/" + setting.getValue().toString() + "/green/", false));

            System.out.println(settingOut);

        }

        System.out.println("/green/" + replaceCharactersSpecial("╚" + "═".repeat( rep.length()-2) + "╝", rep, '|', '╧'));

    }

    public static void showServerListing(ArrayList<ServerInfo> servers) {

        if(servers.size()==0) {
            showNothingFound("No servers found.");
            return;
        }

        final long time = System.currentTimeMillis();
        int id = getLongestFromCompute(2, (serv) -> serv.id, servers) + 2;
        int type = getLongestFromCompute(4, (serv) -> serv.type, servers) + 2;
        int players = getLongestFromCompute(7, (serv) -> serv.onlinePlayers + "/" + serv.maxPlayers, servers) + 2;
        int version = getLongestFromCompute(7, (serv) -> serv.version.name(), servers) + 2;
        int ip = getLongestFromCompute(2, (serv) -> serv.details.ip() + ":" + serv.details.port(), servers) + 2;
        int uptime = getLongestFromCompute(6, (serv) -> convertTime(time-serv.start), servers) + 2;

        String rep = "║" + " ".repeat(id) + "|" + " ".repeat(type) + "|" + " ".repeat(players) + "|" + " ".repeat(version) + "|" + " ".repeat(ip) + "|" + " ".repeat(uptime) + "║";

        rep = setStrings(rep, new StringInfo(id, "ID"), new StringInfo(type, "TYPE"), new StringInfo(players, "PLAYERS"), new StringInfo(version, "VERSION"), new StringInfo(ip, "IP"), new StringInfo(uptime, "UPTIME"));

        System.out.println("/green/" + replaceCharactersSpecial("╔" + "═".repeat(rep.length()-2) + "╗", rep, '|', '╤'));
        System.out.println("/green/" + rep.replace('|', '│'));
        System.out.println("/green/" + replaceCharactersSpecial("╟" + "─".repeat(rep.length()-2) + "╢", rep, '|', '┼'));

        for(ServerInfo server : servers) {
            String serverOut = ansi("/green/║" + " ".repeat(id+(ansi?AQUA.length()+GREEN.length():0))
                    + "│" + " ".repeat( type+(ansi?AQUA.length()+GREEN.length():0))
                    + "│" + " ".repeat( players+(ansi?AQUA.length()+GREEN.length():0))
                    + "│" + " ".repeat( version+(ansi?AQUA.length()+GREEN.length():0))
                    + "│" + " ".repeat( ip+(ansi?AQUA.length()+GREEN.length():0))
                    + "│" + " ".repeat( uptime+(ansi?AQUA.length()+GREEN.length():0)) + "║", true);

            int x = 2+(ansi?GREEN.length():0);
            serverOut = replaceAt(serverOut, x, ansi("/aqua/" + server.id + "/green/", false));
            x += 1+id+(ansi?AQUA.length()+GREEN.length():0);
            serverOut = replaceAt(serverOut, x, ansi("/aqua/" + server.type + "/green/", false));
            x += 1+type+(ansi?AQUA.length()+GREEN.length():0);
            serverOut = replaceAt(serverOut, x, ansi("/aqua/" + server.onlinePlayers + "/" + server.maxPlayers + "/green/", false));
            x += 1+players+(ansi?AQUA.length()+GREEN.length():0);
            serverOut = replaceAt(serverOut, x, ansi("/aqua/" + server.version.name() + "/green/", false));
            x += 1+version+(ansi?AQUA.length()+GREEN.length():0);
            serverOut = replaceAt(serverOut, x, ansi("/aqua/" + server.details.ip() + ":" + server.details.port() + "/green/", false));
            x += 1+ip+(ansi?AQUA.length()+GREEN.length():0);
            serverOut = replaceAt(serverOut, x, ansi("/aqua/" + convertTime(time-server.start) + "/green/", false));

            System.out.println(serverOut);
        }

        System.out.println("/green/" + replaceCharactersSpecial("╚" + "═".repeat( rep.length()-2) + "╝", rep, '|', '╧'));

    }

    public static void showNothingFound(String str) {
        int amt = 30;
        System.out.println("/red/╔" + "═".repeat(amt) + "╗");
        String x = "║" + " ".repeat(amt) + "║";
        System.out.println("/red/" + replaceAt(x, amt/2-(str.length()-1)/2, str));
        System.out.println("/red/╚" + "═".repeat(amt) + "╝");
    }

}
