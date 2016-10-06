/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dijalmasilva.q2.node1;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dijalma Silva <dijalmacz@gmail.com>
 */
public class DataBase {

    private static final String USER = "postgres";
    private static final String PASSWORD = "123456";
    private static final String URL = "jdbc:postgresql://localhost:5432/q2node1";
    private static Connection conn;

    public static void main(String[] args) throws IOException {

        while (true) {

            ServerSocket ss = new ServerSocket(8000);
            Socket socket = ss.accept();
            InputStream in = socket.getInputStream();
            byte[] b = new byte[in.available()];
            in.read(b);
            System.out.println(new String(b).trim());
            String result = executeSQL(new String(b).trim());
            socket.getOutputStream().write(result.getBytes());
        }
    }

    static void createConnection() throws SQLException {
        conn = DriverManager.getConnection(URL, USER, PASSWORD);
    }

    static void closeConnection() throws SQLException {
        conn.close();
    }

    static String executeSQL(String sql) {
        String result = "";

        try {
            Statement s = conn.createStatement();
            if (sql.contains("insert")) {
                s.executeUpdate(sql);
                result = "Usuário cadastrado";
            } else {
                ResultSet rs = s.executeQuery(sql);

                while (rs.next()) {
                    result += rs + ";";
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;
    }

    static void createTableUser() throws SQLException {
        try (Statement s = conn.createStatement()) {
            s.executeUpdate("create table user ( id serial primary key, nome varchar(120) )");
        }
    }
}
