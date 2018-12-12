package de.luuuuuis.SQL;

import de.luuuuuis.BetaKey;
import net.md_5.bungee.api.ProxyServer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class KeyInfo {

    private MySQL MySQL = new MySQL();

    private static ArrayList<String> allowedList = new ArrayList<>();

    public KeyInfo() {

    }

    /**
     * Key exists = valid Key is missing = false
     *
     * @param key
     * @return boolean
     */

    public boolean keyIsValid(String key) {
        try (ResultSet rs = MySQL.getResult("SELECT * FROM betakeys WHERE betakey='" + key + "'");) {
            if (rs.next()) {
                return rs.getString("betakey") != null;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * delets the key from betakeys
     *
     * @param key
     */

    public void deleteKey(String key) {
        if (keyIsValid(key)) {
            MySQL.update("DELETE FROM betakeys WHERE betakey='" + key + "'");
        }
    }

    /**
     * creates a key -> Use BetaKey.getRandomString(36);
     *
     * @param key
     */

    public void createKey(String key) {
        MySQL.update("INSERT INTO betakeys (betakey) VALUES ('" + key + "')");
    }

    public void addPlayer(String uuid) {
        MySQL.update("INSERT INTO allowedPlayers (UUID) VALUES ('" + uuid + "')");
        allowedList.add(uuid);
    }

    public void deletePlayer(String uuid) {
        MySQL.update("DELETE FROM allowedPlayers WHERE UUID='" + uuid + "'");
        allowedList.remove(uuid);
    }

    public boolean alreadyAdded(String uuid) {
        try (ResultSet rs = MySQL.getResult("SELECT * FROM allowedPlayers WHERE UUID='" + uuid + "'")) {
            if (rs.next()) {
                return rs.getString("UUID") != null;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public void updateList() {
        ProxyServer.getInstance().getScheduler().schedule(BetaKey.getInstance(), () -> {

                try (ResultSet rs = MySQL.getResult("SELECT * FROM allowedPlayers")) {
                    while (rs.next()) {
                        try {
                            allowedList.add(rs.getString("UUID"));
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

        }, 0, 3, TimeUnit.MINUTES);
    }

    public ArrayList<String> getAllowedList() {
        return allowedList;
    }
}
