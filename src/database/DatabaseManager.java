/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package database;

/**
 *
 * @author Sonny
 */
import Objects.Category;
import Objects.Product;
import Objects.User;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import static openingScreen.opening.categories;
import static openingScreen.opening.products;

public class DatabaseManager {

    Connection conn;
    PreparedStatement pstmt;
    Statement stmt;
    ResultSet rs;
    Product details;
    Category cat;
    ResultSetMetaData rsmd;
    int colCount;
    String query;

    public Connection connectDatabase() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            //conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Project", "root", "CaptainAbby_22");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Project", "root", "1111");
            System.out.println("Connected to the database.");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return conn;
    }

    public void createProductTable() {
        String query = "CREATE TABLE IF NOT EXISTS Product (ProductId INT NOT NULL PRIMARY KEY, ProductName VARCHAR(50),  Price DOUBLE, CategoryId int, ProductImage longblob)";
        try {
            stmt = conn.createStatement();
            int rowsAffected = stmt.executeUpdate(query);
            if (rowsAffected == 0) {
                System.out.println("Products table was created.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createCategoryTable() {
        String query = "CREATE TABLE IF NOT EXISTS Category (CategoryId INT NOT NULL PRIMARY KEY, CategoryName varchar(50))";
        try {
            stmt = conn.createStatement();
            int rowsAffected = stmt.executeUpdate(query);
            if (rowsAffected == 0) {
                System.out.println("Category table was created.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createSizeTable() {
        String query = "CREATE TABLE IF NOT EXISTS Sizing (sizeId INT NOT NULL PRIMARY KEY, sizeName varchar(20), sizeOz varchar(10), sizeRate decimal(4,2))";
        try {
            stmt = conn.createStatement();
            int rowsAffected = stmt.executeUpdate(query);
            if (rowsAffected == 0) {
                System.out.println("Sizes table was created.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createUserInfoTable() {
        String query = "CREATE TABLE IF NOT EXISTS userInfo (UserId INT NOT NULL PRIMARY KEY,lName VARCHAR(50),fName VARCHAR(50), mInitials VARCHAR(3),emailAdd VARCHAR(50), address VARCHAR(255),contactNum VARCHAR(11),HiredDate DATE,`Status` VARCHAR(20))";
        try {
            stmt = conn.createStatement();
            int rowsAffected = stmt.executeUpdate(query);
            if (rowsAffected == 0) {
                System.out.println("User table was created.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addProduct(int productId, String productName, double price, int categoryId, byte[] productImage) {

        query = "INSERT INTO Product (ProductId, ProductName, Price, CategoryId, ProductImage) VALUES (?, ?, ?, ?, ?)";
        try {
            pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, productId);
            pstmt.setString(2, productName);

            pstmt.setDouble(3, price);
            pstmt.setInt(4, categoryId);
            pstmt.setBytes(5, productImage);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected == 1) {
                System.out.println("productadded");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void addCategory(int categoryId, String categoryName) {
        String query = "INSERT INTO Category (CategoryId, CategoryName) VALUES (?, ?)";
        try {
            pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, categoryId);
            pstmt.setString(2, categoryName);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected == 1) {
                System.out.println("catadded");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addSizes(int id, String name, String oz, double rate) {
        String query = "INSERT INTO Sizing (sizeId, sizeName, sizeOz, sizeRate) VALUES (?,?,?,?)";
        try {
            pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, id);
            pstmt.setString(2, name);
            pstmt.setString(3, oz);
            pstmt.setDouble(4, rate);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected == 1) {
                System.out.println("ssizeadded");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteAllProducts() {
        try {
            String query = "DELETE FROM Product";
            pstmt = conn.prepareStatement(query);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected == 0) {
                System.out.println("Cleared product table successfully!");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void deleteAllCategories() {
        try {
            String query = "DELETE FROM Category";
            pstmt = conn.prepareStatement(query);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected == 0) {
                System.out.println("Cleared category table successfully!");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void deleteAllSizes() {
        try {
            String query = "DELETE FROM Sizing";
            pstmt = conn.prepareStatement(query);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected == 0) {
                System.out.println("Cleared sizes table successfully!");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void retrieveAllProducts(JPanel panel) {
        products.clear();
        try {
            String query = "select * from Product";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);

            while (rs.next()) {

                details = (new Product(rs.getInt("ProductId"),
                        rs.getString("ProductName"), rs.getDouble("Price"),
                        rs.getInt("CategoryId"), rs.getBytes("ProductImage")));
                products.add(details);
            }

            for (Product p : products) {
                panel.add(p);
                System.out.println("Product Displayed");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void retrieveAllProducts(JTable productsTbl) {
        DefaultTableModel model = (DefaultTableModel) productsTbl.getModel();
        model.setRowCount(0); // Clear table
        products.clear();

        try {
            query = "SELECT * FROM Product";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);

            rsmd = rs.getMetaData();
            colCount = rsmd.getColumnCount();

            while (rs.next()) {
                details = new Product(
                        rs.getInt("ProductId"),
                        rs.getString("ProductName"),
                        rs.getDouble("Price"),
                        rs.getInt("CategoryId"),
                        rs.getBytes("ProductImage")
                );
                products.add(details);
            }

            for (Product p : products) {
                Object[] row = new Object[colCount];

                row[0] = p.getProductId();
                row[1] = p.getProductName();
                row[2] = p.getProductPrice();
                row[3] = p.getProductCategory();
                row[4] = p.getProductImage(); // optional if JTable not showing images
                model.addRow(row);
                System.out.println("Row displayed: " + p.getProductName());
            }

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void retrieveAllCategories(JPanel panel) {
        categories.clear();
        try {
            query = "SELECT * FROM Category";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);

            while (rs.next()) {
                cat = new Category(rs.getInt("CategoryId"), rs.getString("CategoryName"));
                categories.add(cat);
            }

            // Add 'All' option at the top (ID -1 to identify it's a placeholder)
            categories.add(0, new Category(-1, "All"));

            panel.removeAll();
            for (Category c : categories) {
                panel.add(c); // Ensure you're adding a visual component
            }

            panel.revalidate();
            panel.repaint();

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void retrieveAllCategories(JComboBox<String> categoryCB) {
        categories.clear();
        categoryCB.removeAllItems(); // Clear existing items

        try {
            String query = "SELECT * FROM Category";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);

            while (rs.next()) {
                cat = new Category(
                        rs.getInt("CategoryId"),
                        rs.getString("CategoryName")
                );
                categories.add(cat);
                categoryCB.addItem(cat.getCategoryName()); // Populate combo box
                System.out.println("Category Displayed: " + cat.getCategoryName());
            }

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void retrieveAllCategories(JTable catTbl) {
        DefaultTableModel model = (DefaultTableModel) catTbl.getModel();
        model.setRowCount(0); // Clear table
        try {
            categories.clear();
            query = "SELECT * FROM Category";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);
            rsmd = rs.getMetaData();
            colCount = rsmd.getColumnCount();

            while (rs.next()) {
                Category c = new Category(rs.getInt("CategoryId"),
                        rs.getString("CategoryName"));
                categories.add(c);
            }

            for (Category cat : categories) {
                Object[] row = new Object[colCount];

                row[0] = cat.getCategoryId();
                row[1] = cat.getCategoryName();

                model.addRow(row);
                System.out.println("Row displayed: " + cat.getCategoryName());
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ArrayList<String> getAllCategories() { //for CB
        ArrayList<String> cat = new ArrayList();
        cat.clear();
        query = "SELECT CategoryName FROM Category ";

        try {
            pstmt = conn.prepareStatement(query);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                cat.add(rs.getString("CategoryName"));
            }

        } catch (SQLException e) {
            System.err.println("Failed to fetch categories: " + e.getMessage());
        }

        return cat;
    }

    public void retrieveProductperCat(String category, JPanel panel) {
        products.clear();

        String getCategoryIdQuery = "SELECT CategoryId FROM Category WHERE CategoryName = ?";
        String getProductsQuery = "SELECT * FROM Product WHERE CategoryId = ?";

        try (
                PreparedStatement getCategoryStmt = conn.prepareStatement(getCategoryIdQuery); PreparedStatement getProductsStmt = conn.prepareStatement(getProductsQuery)) {
            getCategoryStmt.setString(1, category);
            try (ResultSet categoryRs = getCategoryStmt.executeQuery()) {
                if (!categoryRs.next()) {
                    System.err.println("Category not found: " + category);
                    return;
                }
                int categoryId = categoryRs.getInt("CategoryId");

                getProductsStmt.setInt(1, categoryId);
                try (ResultSet productRs = getProductsStmt.executeQuery()) {
                    while (productRs.next()) {
                        Product details = new Product(
                                productRs.getInt("ProductId"),
                                productRs.getString("ProductName"),
                                productRs.getDouble("Price"),
                                productRs.getInt("CategoryId"),
                                productRs.getBytes("ProductImage")
                        );
                        products.add(details);
                    }
                }
            }

            panel.removeAll(); // Optional: clear panel before adding
            for (Product p : products) {
                panel.add(p);
                System.out.println("Product from category " + category + " displayed");
            }

            panel.revalidate();
            panel.repaint();

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void retrieveProductperCat(String category, JTable productsTbl) {
        products.clear();

        DefaultTableModel model = (DefaultTableModel) productsTbl.getModel();
        model.setRowCount(0); // Clear table

        try {
            if (category.equalsIgnoreCase("All")) {
                // Show all products
                String getAllProductsQuery = "SELECT * FROM Product";
                try (PreparedStatement getAllStmt = conn.prepareStatement(getAllProductsQuery); ResultSet rs = getAllStmt.executeQuery()) {

                    while (rs.next()) {
                        details = new Product(
                                rs.getInt("ProductId"),
                                rs.getString("ProductName"),
                                rs.getDouble("Price"),
                                rs.getInt("CategoryId"),
                                rs.getBytes("ProductImage")
                        );
                        products.add(details);
                    }
                }
            } else {
                // Filtered by category
                String getCategoryIdQuery = "SELECT CategoryId FROM Category WHERE CategoryName = ?";
                String getProductsQuery = "SELECT * FROM Product WHERE CategoryId = ?";

                try (
                        PreparedStatement getCategoryStmt = conn.prepareStatement(getCategoryIdQuery); PreparedStatement getProductsStmt = conn.prepareStatement(getProductsQuery)) {
                    getCategoryStmt.setString(1, category);
                    try (ResultSet categoryRs = getCategoryStmt.executeQuery()) {
                        if (!categoryRs.next()) {
                            System.err.println("Category not found: " + category);
                            return;
                        }

                        int categoryId = categoryRs.getInt("CategoryId");
                        getProductsStmt.setInt(1, categoryId);

                        try (ResultSet productRs = getProductsStmt.executeQuery()) {
                            while (productRs.next()) {
                                details = new Product(
                                        productRs.getInt("ProductId"),
                                        productRs.getString("ProductName"),
                                        productRs.getDouble("Price"),
                                        productRs.getInt("CategoryId"),
                                        productRs.getBytes("ProductImage")
                                );
                                products.add(details);
                            }
                        }
                    }
                }
            }

            // Load products into JTable
            for (Product p : products) {
                Object[] row = new Object[5];
                row[0] = p.getProductId();
                row[1] = p.getProductName();
                row[2] = p.getProductPrice();
                row[3] = p.getProductCategory();
                row[4] = p.getProductImage(); // optional
                model.addRow(row);
                System.out.println("Product from category " + category + " displayed");
            }

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int getCategoryIdByName(String categoryName) {
        query = "SELECT CategoryId FROM Category WHERE CategoryName = ?";
        try {
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, categoryName);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("CategoryId");
            } else {
                JOptionPane.showMessageDialog(null, "Category not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database error: " + e.getMessage());
        }
        return -1; // Invalid ID
    }

    public String getCategoryNameByProductId(int productId) {
        String categoryName = null;
        query = "SELECT c.CategoryName "
                + "FROM Product p "
                + "JOIN Category c ON p.CategoryId = c.CategoryId "
                + "WHERE p.ProductId = ?";

        try {
            pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, productId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                categoryName = rs.getString("CategoryName");
            } else {
                JOptionPane.showMessageDialog(null, "No category found for product ID: " + productId);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        return categoryName;
    }

    public int generateNextProductId() {
        int nextId = 1000;
        query = "SELECT MAX(ProductId) AS MaxId FROM Product";

        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);

            if (rs.next()) {
                int maxId = rs.getInt("MaxId");
                if (!rs.wasNull()) {
                    nextId = maxId + 1;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return nextId;
    }

    public void updateProduct(int productId, String productName, double price, int categoryId, byte[] productImage) {
        query = "UPDATE Product set ProductName=?, Price=?, CategoryId=?, ProductImage=? WHERE ProductId=?";
        try {
            pstmt = conn.prepareStatement(query);

            pstmt.setString(1, productName);

            pstmt.setDouble(2, price);
            pstmt.setInt(3, categoryId);
            pstmt.setBytes(4, productImage);
            pstmt.setInt(5, productId);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected == 1) {
                System.out.println("productupdated");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void deleteProduct(int productId) {
        try {
            query = "DELETE FROM Product WHERE ProductId=?";
            pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, productId);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected == 0) {
                System.out.println("productdeleted");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
        public void retrieveAllProducts(DefaultTableModel model) {

        try {
            query = "SELECT * FROM Product";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);

            rsmd = rs.getMetaData();
            colCount = rsmd.getColumnCount();

            while (rs.next()) {
                details = new Product(
                        rs.getInt("ProductId"),
                        rs.getString("ProductName"),
                        rs.getDouble("Price"),
                        rs.getInt("CategoryId"),
                        rs.getBytes("ProductImage")
                );
                products.add(details);
            }

            for (Product p : products) {
                Object[] row = new Object[colCount];

                row[0] = p.getProductId();
                row[1] = p.getProductName();
                row[2] = p.getProductPrice();
                row[3] = p.getProductCategory();
                row[4] = p.getProductImage(); // optional if JTable not showing images
                model.addRow(row);
                System.out.println("Row displayed: " + p.getProductName());
            }

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    public void retrieveUsers(DefaultTableModel model) {
        ArrayList<User> data = new ArrayList<>();

        try {
            Statement stmt = conn.createStatement();
            String query = "SELECT * FROM userInfo";
            ResultSet rs = stmt.executeQuery(query);
            ResultSetMetaData rsmd = rs.getMetaData();

            int colCount = rsmd.getColumnCount();
            User info;

            while (rs.next()) {
                info = new User(rs.getInt("UserId"), rs.getString("lName"), rs.getString("fName"), rs.getString("mInitials"),
                        rs.getString("address"), rs.getString("emailAdd"), rs.getString("contactNum"), rs.getDate("HiredDate"), rs.getString("Status"));
                data.add(info);
            }
            Object[] row = new Object[colCount];
            model.setRowCount(0);

            for (User datalist : data) {
                row[0] = datalist.getId();
                row[1] = datalist.getfullName();
                row[2] = datalist.getAdd();
                row[3] = datalist.getEAdd();
                row[4] = datalist.getContactNum();
                row[5] = datalist.getHiredDate();
                row[6] = datalist.getStatus();
                model.addRow(row);
            }

        } catch (SQLException e) {
        }
    }

}
