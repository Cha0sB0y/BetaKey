package de.luuuuuis.SQL.MySQL;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class KeyInfo {

    private MySQL MySQL = new MySQL();

    private static ArrayList<String> allowedList = new ArrayList<>();
    private static ArrayList<String> allowedNameList = new ArrayList<>();
    private static ArrayList<String> keyList = new ArrayList<>();

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

    public void addPlayer(String uuid, String name) {
        if (!alreadyAdded(uuid)) {
            MySQL.update("INSERT INTO allowedPlayers (UUID, NAME) VALUES ('" + uuid + "', '" + name + "')");
            allowedList.add(uuid);
        }
    }

    public void removePlayer(String uuid, String name) {
        if (alreadyAdded(uuid)) {
            MySQL.update("DELETE FROM allowedPlayers WHERE NAME='" + name + "'");
            allowedList.remove(uuid);
        }
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


    public void getAllowed() {
        try (ResultSet rs = MySQL.getResult("SELECT * FROM allowedPlayers")) {
            while (rs.next()) {
                allowedList.add(rs.getString("UUID"));
            }
        } catch (SQLException e) {
            System.err.print("BetaKey ERROR >> Probably MySQL is not connected");
            e.printStackTrace();
            System.err.print("BetaKey ERROR >> Probably MySQL is not connected");
        }
    }

    public void getAllowedName() {
        try (ResultSet rs = MySQL.getResult("SELECT * FROM allowedPlayers")) {
            while (rs.next()) {
                allowedNameList.add(rs.getString("NAME"));
            }
        } catch (SQLException e) {
            System.err.print("BetaKey ERROR >> Probably MySQL is not connected");
            e.printStackTrace();
            System.err.print("BetaKey ERROR >> Probably MySQL is not connected");
        }
    }

    public void getKeys() {
        try (ResultSet rs = MySQL.getResult("SELECT * FROM betakeys")) {
            while (rs.next()) {
                keyList.add(rs.getString("betakey"));
            }
        } catch (SQLException e) {
            System.err.print("BetaKey ERROR >> Probably MySQL is not connected");
            e.printStackTrace();
            System.err.print("BetaKey ERROR >> Probably MySQL is not connected");
        }
    }

    public ArrayList<String> getAllowedList() {
        return allowedList;
    }

    public ArrayList<String> getKeyList() {
        keyList.clear();
        getKeys();
        return keyList;
    }

    public ArrayList<String> getAllowedNameList() {
        allowedNameList.clear();
        getAllowedName();
        return allowedNameList;
    }
}
