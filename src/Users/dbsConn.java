/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Users;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Admin
 */
public class dbsConn {

    public static Connection getConn() {
        Connection conn = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/users", "root", "1111");
            System.out.println("Database Connected Successfully!!");
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(dbsConn.class.getName()).log(Level.SEVERE, null, ex);
        }

        return conn;
    }

    public static void createUserInfoTable() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS userInfo ("
                + "  UserId INT NOT NULL PRIMARY KEY,"
                + "  lName VARCHAR(50),"
                + "  fName VARCHAR(50),"
                + "  mInitials VARCHAR(3),"
                + "  emailAdd VARCHAR(50),"
                + "  address VARCHAR(255),"
                + "  contactNum VARCHAR(11),"
                + "  HiredDate DATE,"
                + "  `Status` VARCHAR(20)"
                + ");";

        try (Connection conn = dbsConn.getConn(); Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(createTableSQL);
            System.out.println("Table 'userInfo' created successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
