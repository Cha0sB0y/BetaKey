package de.luuuuuis.SQL;

import java.sql.*;

public class MySQL {

    public MySQL() {
        // idk
    }

    public static Connection con;

    public void connect(String host, String port, String database, String user, String password) {
        if (!isConnected()) {
            try {
                con = DriverManager.getConnection(
                        "jdbc:mysql://" + host + ":" + port
                                + "/" + database + "?autoReconnect=true&useUnicode=yes",
                        user, password);
                System.out.println("BetaKey >> MySQL Connected");
                createTable();
            } catch (SQLException ex) {
                System.err.println("BetaKey ERROR >> Can not connect to MySQL");
                ex.printStackTrace();
                System.err.println("BetaKey ERROR >> Can not connect to MySQL");
            }
        }
    }

    public void createTable() {
        if (isConnected()) {
            try {
                con.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS betakeys(betakey VARCHAR(36))");
                con.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS allowedPlayers(UUID VARCHAR(36), NAME VARCHAR(16))");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void close() {
        if (isConnected()) {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isConnected() {
        return con != null;
    }

    public void update(String qry) {
        if (isConnected()) {
            try {
                con.createStatement().executeUpdate(qry);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public ResultSet getResult(String qry) {
        if (isConnected()) {
            ResultSet rs = null;
            try {
                Statement st = con.createStatement();
                rs = st.executeQuery(qry);
                return rs;
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }
}
