package de.luuuuuis;

import de.luuuuuis.SQL.KeyInfo;
import de.luuuuuis.SQL.MySQL;
import de.luuuuuis.commands.BetaKeyCmd;
import de.luuuuuis.http.HttpHandler;
import de.luuuuuis.listener.JoinListener;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class BetaKey extends Plugin {

    private static BetaKey instance;

    String FilePath;
    static String prefix, kickReason;

    String host, port, database, user, password;

    MySQL MySQL;
    HttpHandler httpHandler;

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public void onEnable() {
        super.onEnable();
        instance = this;

        /*
         *
         * Auto-Updater
         *
         */

        System.out.println("\r\n"
                + " ______     ______     ______   ______     __  __     ______     __  __    \r\n"
                + "/\\  == \\   /\\  ___\\   /\\__  _\\ /\\  __ \\   /\\ \\/ /    /\\  ___\\   /\\ \\_\\ \\   \r\n"
                + "\\ \\  __<   \\ \\  __\\   \\/_/\\ \\/ \\ \\  __ \\  \\ \\  _\"-.  \\ \\  __\\   \\ \\____ \\  \r\n"
                + " \\ \\_____\\  \\ \\_____\\    \\ \\_\\  \\ \\_\\ \\_\\  \\ \\_\\ \\_\\  \\ \\_____\\  \\/\\_____\\ \r\n"
                + "  \\/_____/   \\/_____/     \\/_/   \\/_/\\/_/   \\/_/\\/_/   \\/_____/   \\/_____/ \r\n"
                + "                                                                           ");

        System.out.println("BetaKey >> Enabling BetaKey (" + getDescription().getVersion() + ")");
        System.out.println("BetaKey >> Discord for Support: https://discord.gg/2aSSGcz");

        URL url;
        HttpURLConnection con = null;

        try {
            url = new URL("http://luis.bplaced.net/BetaKey/Auto-Updater/version.html");
            con = (HttpURLConnection) url.openConnection();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {

            System.out.println("BetaKey >> Searching for new builds!");

            String[] I = in.readLine().split("&&");
            if (!(I[0].equalsIgnoreCase(getDescription().getVersion()))) {

                System.out.println("BetaKey >> New build (" + I[0] + ") found!");
                System.out.println("BetaKey >> Download: https://www.spigotmc.org/resources/58818/");

                Thread th = new Thread(() -> {

                    URL dURL = null;
                    try {
                        dURL = new URL(I[1]);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }

                    try (InputStream input = dURL.openStream();
                         FileOutputStream output = new FileOutputStream(getFile())) {
                        byte[] buffer = new byte[4096];
                        int n;
                        while (-1 != (n = input.read(buffer))) {
                            output.write(buffer, 0, n);
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }

                    System.out.println("BetaKey >> Update downloaded! Restart as soon as possible.");

                });
                th.start();
            } else {
                con.disconnect();
                System.out.println("BetaKey >> You are running on the latest build ;)");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        FilePath = getDataFolder().getPath();

        URL dowURL = null;
        File file;

        file = new File(getDataFolder().getPath(), "config.json");

        if (!file.exists()) {
            try {
                dowURL = new URL("http://luis.bplaced.net/BetaKey/Auto-Updater/config.json");
                if (!getDataFolder().exists()) {
                    getDataFolder().mkdir();
                }
                file.createNewFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            try (InputStream input = dowURL.openStream();
                 FileOutputStream output = new FileOutputStream(file)) {

                byte[] buffer = new byte[4096];
                int n;
                while (-1 != (n = input.read(buffer))) {
                    output.write(buffer, 0, n);
                }

                input.close();
                output.close();

                System.out.println("BetaKey >> New config.json downloaded");

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        HashMap<String, Object> MySQLCredentials = new HashMap<>();

        Object obj;
        try {
            obj = new JSONParser().parse(new FileReader(getDataFolder().getPath() + "/" + "config.json"));
            JSONObject JObj = (JSONObject) obj;

            prefix = JObj.get("Prefix").toString().replace("§", "&") + "&7";
            kickReason = JObj.get("KickReason").toString().replace("§", "&");

            Map mysqlJSON = (Map) JObj.get("MySQL");

            Iterator<Map.Entry> itr3 = mysqlJSON.entrySet().iterator();
            while (itr3.hasNext()) {
                Map.Entry pair = itr3.next();
                MySQLCredentials.put(pair.getKey().toString(), pair.getValue());
            }

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        host = MySQLCredentials.get("Host").toString();
        port = MySQLCredentials.get("Port").toString();
        database = MySQLCredentials.get("Database").toString();
        user = MySQLCredentials.get("User").toString();
        password = MySQLCredentials.get("Password").toString();

        MySQL = new MySQL();
        MySQL.connect(host, port, database, user, password);
        new KeyInfo().getAllowed();

        getProxy().registerChannel("BungeeCord");

        PluginManager pm = ProxyServer.getInstance().getPluginManager();

        pm.registerListener(this, new JoinListener());
        pm.registerCommand(this, new BetaKeyCmd("betakey"));

        httpHandler = new HttpHandler();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        MySQL.close();
        httpHandler.stop();
    }

    /**
     * by https://stackoverflow.com/a/13678355/10011954
     *
     * @param length
     * @return
     */
    public static String getRandomKey(int length) {
        String randomStr = UUID.randomUUID().toString();
        while (randomStr.length() < length) {
            randomStr += UUID.randomUUID().toString();
        }
        return randomStr.substring(0, length);
    }

    public static String getRandomPermaKey(int length) {
        String randomStr = UUID.randomUUID().toString();
        while (randomStr.length() < length) {
            randomStr += UUID.randomUUID().toString();
        }
        return randomStr.substring(0, length);
    }

    public static BetaKey getInstance() {
        return instance;
    }

    public static String getPrefix() {
        String back = ChatColor.translateAlternateColorCodes('&', prefix);
        return back;
    }

    public static String getKickReason() {
        String back = ChatColor.translateAlternateColorCodes('&', kickReason);
        return back;
    }
}
