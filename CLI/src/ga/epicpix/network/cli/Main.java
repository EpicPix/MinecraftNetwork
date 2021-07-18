package ga.epicpix.network.cli;

import ga.epicpix.network.common.ChatColor;
import ga.epicpix.network.common.CommonUtils;
import ga.epicpix.network.common.Mongo;
import ga.epicpix.network.common.servers.ServerInfo;
import ga.epicpix.network.common.servers.ServerSignal;
import org.bson.Document;

import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import static ga.epicpix.network.common.CommonUtils.*;

public class Main {

    public static final String DARK_AQUA = "\u001b[36m";
    public static final String AQUA = "\u001b[36;1m";
    public static final String GREEN = "\u001b[32;1m";
    public static final String RED = "\u001b[31;1m";
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

    public static void main(String[] args) throws IOException {
        System.out.println("Network Manager CLI");

        Logger.getLogger("org.mongodb.driver").setLevel(Level.SEVERE);
        Logger.getLogger("org.bson").setLevel(Level.SEVERE);

        System.setOut(new PrintStream(System.out) {
            public void println(String s) {
                super.println(ansi(s, true));
            }
            public void print(String s) {
                super.print(ansi(s, true));
            }
        });

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
                System.out.println("server <id> stop - Stop a server");
                System.out.println("servers - List servers");
            }else if(cmdline.equals("server")) {
                if(getParam(cmd, 1) != null) {
                    ServerInfo server = CommonUtils.getServerInfo(getParam(cmd, 1));
                    if(server==null) {
                        System.out.println("/red/Unknown server! Use \"servers\" to list servers");
                    }else {
                        if(getParam(cmd, 2) != null && getParam(cmd, 2).equals("stop")) {
                            if(server.sendSignal(ServerSignal.STOP)) {
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
                ArrayList<Document> documents = CommonUtils.iteratorToList(Mongo.getCollection("data", "servers").find().iterator());
                ArrayList<ServerInfo> servers = CommonUtils.documentsToObjects(documents, ServerInfo.class);
                showServerListing(servers);
            }else {
                System.out.println("/red/Unknown command. Type \"help\" for help");
            }
        }
    }

    public static void showServerListing(ArrayList<ServerInfo> servers) {
        int id = 2;
        for(ServerInfo server : servers) if(id < server.id.length()) id = server.id.length();

        int type = 4;
        for(ServerInfo server : servers) if(type < server.type.length()) type = server.type.length();

        int players = 7;
        for(ServerInfo server : servers) if(players < (server.onlinePlayers + "/" + server.maxPlayers).length()) players = (server.onlinePlayers + "/" + server.maxPlayers).length();

        int version = 7;
        for(ServerInfo server : servers) if(version < server.version.getName().length()) version = server.version.getName().length();

        int ip = 2;
        for(ServerInfo server : servers) if(ip < (server.details.ip + ":" + server.details.port).length()) ip = (server.details.ip + ":" + server.details.port).length();

        id += 2;
        type += 2;
        players += 2;
        version += 2;
        ip += 2;

        String rep = "║" + repeat(" ", id) + "|" + repeat(" ", type) + "|" + repeat(" ", players) + "|" + repeat(" ", version) + "|" + repeat(" ", ip) + "║";

        if(servers.size()==0) {
            System.out.println("/red/╔" + repeat("═", rep.length()-2) + "╗");
            String x = "║" + repeat(" ", rep.length()-2) + "║";
            System.out.println("/red/" + replaceAt(x, rep.length()/2-8, "No servers found."));
            System.out.println("/red/╚" + repeat("═", rep.length()-2) + "╝");
            return;
        }

        rep = replaceAt(rep, 1+(id-1)/2, "ID");
        rep = replaceAt(rep, 1+id+1+(type-1)/2-1, "TYPE");
        rep = replaceAt(rep, 1+id+1+type+1+(players-1)/2-3, "PLAYERS");
        rep = replaceAt(rep, 1+id+1+type+1+players+1+(version-1)/2-3, "VERSION");
        rep = replaceAt(rep, 1+id+1+type+1+players+1+version+1+(ip-1)/2, "IP");

        System.out.println("/green/" + replaceCharactersSpecial("╔" + repeat("═", rep.length()-2) + "╗", rep, '|', '╤'));
        System.out.println("/green/" + rep.replace('|', '│'));
        System.out.println("/green/" + replaceCharactersSpecial("╟" + repeat("─", rep.length()-2) + "╢", rep, '|', '┼'));

        for(ServerInfo server : servers) {
            String serverOut = ansi("/green/║" + repeat(" ", id+(ansi?AQUA.length()+GREEN.length():0)) + "│" + repeat(" ", type+(ansi?AQUA.length()+GREEN.length():0)) + "│" + repeat(" ", players+(ansi?AQUA.length()+GREEN.length():0)) + "│" + repeat(" ", version+(ansi?AQUA.length()+GREEN.length():0)) + "│" + repeat(" ", ip+(ansi?AQUA.length()+GREEN.length():0)) + "║", true);

            int x = 2+(ansi?GREEN.length():0);
            serverOut = replaceAt(serverOut, x, ansi("/aqua/" + server.id + "/green/", false));
            x += 1+id+(ansi?AQUA.length()+GREEN.length():0);
            serverOut = replaceAt(serverOut, x, ansi("/aqua/" + server.type + "/green/", false));
            x += 1+type+(ansi?AQUA.length()+GREEN.length():0);
            serverOut = replaceAt(serverOut, x, ansi("/aqua/" + server.onlinePlayers + "/" + server.maxPlayers + "/green/", false));
            x += 1+players+(ansi?AQUA.length()+GREEN.length():0);
            serverOut = replaceAt(serverOut, x, ansi("/aqua/" + server.version.getName() + "/green/", false));
            x += 1+version+(ansi?AQUA.length()+GREEN.length():0);
            serverOut = replaceAt(serverOut, x, ansi("/aqua/" + server.details.ip + ":" + server.details.port + "/green/", false));

            System.out.println(serverOut);
        }

        System.out.println("/green/" + replaceCharactersSpecial("╚" + repeat("═", rep.length()-2) + "╝", rep, '|', '╧'));

    }

    public static String getParam(String[] args, int arg) {
        return args.length > arg ? args[arg] : null;
    }

}
