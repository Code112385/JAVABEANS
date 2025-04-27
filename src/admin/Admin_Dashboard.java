/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package admin;

import Objects.Category;
import Objects.Product;
import database.DatabaseManager;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.swing.*;
import java.util.*;

// DBS
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import static openingScreen.opening.categories;
import static openingScreen.opening.dbManager;

/**
 *
 * @author Jealian Abby Sulit
 */
public class Admin_Dashboard extends javax.swing.JFrame {

    //COLORS
    Color wheat = new Color(245, 222, 179);
    Color wood = new Color(190, 156, 106);

    // Database
    Connection conn;
    PreparedStatement pstmt;
    Statement stmt;
    ResultSet rs;
    Product details;
    Category cat;
    ResultSetMetaData rsmd;
    int colCount;
    String query;
    Product p;
    String imgpath;
    private byte[] imageBytes;

    //User table model
    DefaultTableModel modelUser, modelProduct;

    public Admin_Dashboard() {
        initComponents();

        Image icon = Toolkit.getDefaultToolkit().getImage("C:\\Users\\Jealian Abby Sulit\\Documents\\NetBeansProjects\\JavaBeans_POS\\src\\Images\\logo.png");
        this.setIconImage(icon);

        //table model
        modelUser = (DefaultTableModel) userTable.getModel();
        modelProduct = (DefaultTableModel) productsTbl.getModel();
        
        // Database Connections and Objects
        conn = dbManager.connectDatabase();
        dbManager.retrieveUsers(modelUser);

        //for banner
        ImageIcon imageIcon = new ImageIcon("C:\\Users\\Jealian Abby Sulit\\Documents\\NetBeansProjects\\JavaBeans_POS\\src\\Images\\header.png");
        Image img = imageIcon.getImage();
        Image img2 = img.getScaledInstance(headerLbl.getWidth(), headerLbl.getHeight(), Image.SCALE_SMOOTH);
        imageIcon = new ImageIcon(img2);
        headerLbl.setIcon(imageIcon);

        //for icon buttons
        //home icon
        ImageIcon homeIcon = new ImageIcon("C:\\Users\\Jealian Abby Sulit\\Documents\\NetBeansProjects\\JavaBeans_POS\\src\\Images\\home.png");
        Image home = homeIcon.getImage();
        Image home2 = home.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        homeIcon = new ImageIcon(home2);
        homeLbl.setIcon(homeIcon);

        //pos icon
        ImageIcon posIcon = new ImageIcon("C:\\Users\\Jealian Abby Sulit\\Documents\\NetBeansProjects\\JavaBeans_POS\\src\\Images\\pos.png");
        Image pos = posIcon.getImage();
        Image pos2 = pos.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        posIcon = new ImageIcon(pos2);
        posLbl.setIcon(posIcon);

        //acc icon
        ImageIcon accIcon = new ImageIcon("C:\\Users\\Jealian Abby Sulit\\Documents\\NetBeansProjects\\JavaBeans_POS\\src\\Images\\accounts.png");
        Image acc = accIcon.getImage();
        Image acc2 = acc.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        accIcon = new ImageIcon(acc2);
        accLbl.setIcon(accIcon);

        //menu icon
        ImageIcon menuIcon = new ImageIcon("C:\\Users\\Jealian Abby Sulit\\Documents\\NetBeansProjects\\JavaBeans_POS\\src\\Images\\products.png");
        Image menu = menuIcon.getImage();
        Image menu2 = menu.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        menuIcon = new ImageIcon(menu2);
        menuLbl.setIcon(menuIcon);

        //sales icon
        ImageIcon salesIcon = new ImageIcon("C:\\Users\\Jealian Abby Sulit\\Documents\\NetBeansProjects\\JavaBeans_POS\\src\\Images\\sales.png");
        Image sales = salesIcon.getImage();
        Image sales2 = sales.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        salesIcon = new ImageIcon(sales2);
        salesLbl.setIcon(salesIcon);

        //transaction icon
        ImageIcon transactionIcon = new ImageIcon("C:\\Users\\Jealian Abby Sulit\\Documents\\NetBeansProjects\\JavaBeans_POS\\src\\Images\\transaction.png");
        Image transaction = transactionIcon.getImage();
        Image transaction2 = transaction.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        transactionIcon = new ImageIcon(transaction2);
        transactionLbl.setIcon(transactionIcon);

        //logout icon
        ImageIcon logoutIcon = new ImageIcon("C:\\Users\\Jealian Abby Sulit\\Documents\\NetBeansProjects\\JavaBeans_POS\\src\\Images\\logout.png");
        Image logout = logoutIcon.getImage();
        Image logout2 = logout.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        logoutIcon = new ImageIcon(logout2);
        logoutLbl.setIcon(logoutIcon);

        //buttons
        /*
        ImageIcon iconImage = new ImageIcon("C:\\Users\\Jealian Abby Sulit\\Documents\\NetBeansProjects\\EDP_Sulit_JA\\src\\Images\\edit.png");
            Image imgedit = iconImage.getImage();
            Image img2edit = imgedit.getScaledInstance(25, 25, Image.SCALE_SMOOTH);
            editBtn.setIcon(new ImageIcon(img2edit));
         */
        displayProducts();
        displayCategories();
    }

    public static byte[] convertImagePathToBytes(String path) throws IOException {
        try (FileInputStream fis = new FileInputStream(path); ByteArrayOutputStream bos = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[1024];
            int read;
            while ((read = fis.read(buffer)) != -1) {
                bos.write(buffer, 0, read);
            }
            return bos.toByteArray();
        }
    }

    public void uploadPhoto() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("NetbeansProjects/JavaBeans_POS/src/Images/products"));

        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Image Files", "jpg", "jpeg", "png", "gif");
        fileChooser.setFileFilter(filter);

        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            imgpath = selectedFile.getAbsolutePath();

            try {
                imageBytes = convertImagePathToBytes(imgpath);

                ImageIcon imageIcon = new ImageIcon(imgpath);
                Image scaledImage = imageIcon.getImage().getScaledInstance(
                        productImg.getWidth(), productImg.getHeight(), Image.SCALE_SMOOTH);
                productImg.setIcon(new ImageIcon(scaledImage));
                productImg.setText(null);

                System.out.println("Image Path: " + imgpath);
                System.out.println("Image Byte Size: " + imageBytes.length);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Error loading image: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    public void displayProducts() {
        dbManager.retrieveAllProducts(panelMenu);
    }

    public void displayCategories() {
        ArrayList<String> cat = dbManager.getAllCategories();

        // Panel update
        dbManager.retrieveAllCategories(categoryPnl);

        // ComboBoxes
        updateComboBox(searchCatCB, cat, "All");
        updateComboBox(categoryCB, cat, "Select Category");
    }

    private void updateComboBox(JComboBox<String> comboBox, ArrayList<String> categories, String defaultItem) {
        comboBox.removeAllItems();
        comboBox.addItem(defaultItem);
        for (String category : categories) {
            comboBox.addItem(category);
        }
    }

    public void clear() {
        productsTbl.clearSelection();
        nameTxtF.setText("Enter Product Name");
        priceTxtF.setText("Enter Price");
        categoryCB.setSelectedIndex(0);
        prodIdLbl.setText("");
        productImg.setText("");
        productImg.setIcon(null);
        imageBytes = null;
        imgpath = null;
    }

    public void validateProductForm(String productName, String priceText, int selectedCategoryName, byte[] imageBytes) {
        boolean hasError = false;

        // Flags for each field
        boolean missingName = (productName == null || productName.trim().isEmpty());
        boolean missingPrice = (priceText == null || priceText.trim().isEmpty() || priceText.equalsIgnoreCase("Enter price"));
        boolean missingCategory = (selectedCategoryName == 0);
        boolean missingImage = (imageBytes == null);

        // Check for all missing
        if (missingName || missingPrice || missingCategory || missingImage) {
            JOptionPane.showMessageDialog(null, "Please fill in all required fields and upload an image.", "Missing Information", JOptionPane.WARNING_MESSAGE);
            throw new IllegalArgumentException();
        }

        // Show individual messages if needed
        if (missingName) {
            JOptionPane.showMessageDialog(null, "Please enter the product name.", "Missing Field", JOptionPane.WARNING_MESSAGE);
            hasError = true;
        }
        if (missingPrice) {
            JOptionPane.showMessageDialog(null, "Please enter the product price.", "Missing Field", JOptionPane.WARNING_MESSAGE);
            hasError = true;
        }
        if (missingCategory) {
            JOptionPane.showMessageDialog(null, "Please select a category.", "Missing Field", JOptionPane.WARNING_MESSAGE);
            hasError = true;
        }
        if (missingImage) {
            JOptionPane.showMessageDialog(null, "Please upload a product image.", "Missing Field", JOptionPane.WARNING_MESSAGE);
            hasError = true;
        }

        if (hasError) {
            throw new IllegalArgumentException();
        }

        // Validate price format
        try {
            double price = Double.parseDouble(priceText);
            if (price < 0) {
                JOptionPane.showMessageDialog(null, "Price must be a positive number.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                throw new IllegalArgumentException();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Price must be a valid number.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            throw new IllegalArgumentException();
        }
    }

    private void search(String text) {
        TableRowSorter<TableModel> rowSorter = new TableRowSorter<>(modelUser);
        userTable.setRowSorter(rowSorter);

        int selectedColumn; //= filterCol.getSelectedIndex();
        selectedColumn = switch (filterCol.getSelectedIndex()) {
            case 1 ->
                0;
            case 2 ->
                1;
            case 3 ->
                2;
            case 4 ->
                5;
            default ->
                -1;
        };
        String placeholder = "Search";
        if (text.trim().isEmpty() || text.equals(placeholder)) {
            rowSorter.setRowFilter(null); // Don't filter
        } else {
            if (selectedColumn == -1) {
                //Filter in all columns
                rowSorter.setRowFilter(RowFilter.regexFilter(text));
            } else {
                // Filter only in the selected column
                rowSorter.setRowFilter(RowFilter.regexFilter(text, selectedColumn));
            }
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bannerPnl = new javax.swing.JPanel();
        headerTitle = new javax.swing.JLabel();
        headerLbl = new javax.swing.JLabel();
        bodyPnl = new javax.swing.JPanel();
        layeredPane = new javax.swing.JLayeredPane();
        homeBody = new javax.swing.JPanel();
        averageTPnl = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        ordersTPnl = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        slideshowPnl = new javax.swing.JPanel();
        salesTPnl = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        menuBody = new javax.swing.JPanel();
        searchTxtF = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        productImg = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        nameTxtF = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        priceTxtF = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        categoryCB = new javax.swing.JComboBox<>();
        addBtn = new javax.swing.JButton();
        jLabel20 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        priceTxtF1 = new javax.swing.JTextField();
        updateBtn = new javax.swing.JButton();
        clrBtn = new javax.swing.JButton();
        prodIdLbl = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        productsTbl = new javax.swing.JTable();
        jButton2 = new javax.swing.JButton();
        sortProd = new javax.swing.JComboBox<>();
        searchCatCB = new javax.swing.JComboBox<>();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        deleteBtn = new javax.swing.JButton();
        posBody = new javax.swing.JPanel();
        scrollPane1 = new javax.swing.JScrollPane();
        panelMenu = new javax.swing.JPanel();
        ordersBdy = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        subTValue = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        totalValue = new javax.swing.JLabel();
        taxValue = new javax.swing.JLabel();
        clearOrdersBtn = new javax.swing.JButton();
        orderScroll = new javax.swing.JScrollPane();
        jPanel2 = new javax.swing.JPanel();
        categoryScrll = new javax.swing.JScrollPane();
        categoryPnl = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        searchF = new javax.swing.JTextField();
        accBody = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        userTable = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        searchUserTxtF = new javax.swing.JTextField();
        lnameTxtF = new javax.swing.JTextField();
        fnameTxtF = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        miTxtF = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        addTxtF = new javax.swing.JTextField();
        eaddTxtF = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        contTxtF = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        dateChooser = new com.toedter.calendar.JDateChooser();
        statuTxtF = new javax.swing.JLabel();
        addUser = new javax.swing.JButton();
        updateUser = new javax.swing.JButton();
        deleteUser = new javax.swing.JButton();
        clrUser = new javax.swing.JButton();
        userIdTxtF = new javax.swing.JTextField();
        jButton9 = new javax.swing.JButton();
        blockBtn = new javax.swing.JButton();
        sortUsers = new javax.swing.JComboBox<>();
        jPanel8 = new javax.swing.JPanel();
        filterCol = new javax.swing.JComboBox<>();
        salesBody = new javax.swing.JPanel();
        navigationPnl1 = new javax.swing.JPanel();
        logoutPnl = new javax.swing.JPanel();
        logoutLbl = new javax.swing.JLabel();
        transactionPnl = new javax.swing.JPanel();
        transactionLbl = new javax.swing.JLabel();
        salesPnl = new javax.swing.JPanel();
        salesLbl = new javax.swing.JLabel();
        menuPnl = new javax.swing.JPanel();
        menuLbl = new javax.swing.JLabel();
        accPnl = new javax.swing.JPanel();
        accLbl = new javax.swing.JLabel();
        posPnl = new javax.swing.JPanel();
        posLbl = new javax.swing.JLabel();
        homePnl = new javax.swing.JPanel();
        homeLbl = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Java Beans Admin");
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        bannerPnl.setBackground(new java.awt.Color(104, 70, 3));
        bannerPnl.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        headerTitle.setFont(new java.awt.Font("STLiti", 1, 48)); // NOI18N
        headerTitle.setForeground(new java.awt.Color(255, 255, 255));
        headerTitle.setText("Java Beans");
        bannerPnl.add(headerTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 0, 300, 90));
        bannerPnl.add(headerLbl, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1310, 90));

        getContentPane().add(bannerPnl, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1310, 90));

        bodyPnl.setBackground(new java.awt.Color(227, 227, 227));
        bodyPnl.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        layeredPane.setBackground(new java.awt.Color(204, 204, 0));
        layeredPane.setLayout(new java.awt.CardLayout());

        homeBody.setBackground(new java.awt.Color(255, 255, 255));
        homeBody.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        averageTPnl.setBackground(new java.awt.Color(164, 130, 6));
        averageTPnl.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        averageTPnl.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("STSong", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("AVERAGE SALE");
        averageTPnl.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 150, 40));

        homeBody.add(averageTPnl, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 390, 290, 130));

        ordersTPnl.setBackground(new java.awt.Color(164, 130, 6));
        ordersTPnl.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        ordersTPnl.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("STSong", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("TOTAL ORDERS");
        ordersTPnl.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 150, 40));

        homeBody.add(ordersTPnl, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 390, 290, 130));

        slideshowPnl.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));

        javax.swing.GroupLayout slideshowPnlLayout = new javax.swing.GroupLayout(slideshowPnl);
        slideshowPnl.setLayout(slideshowPnlLayout);
        slideshowPnlLayout.setHorizontalGroup(
            slideshowPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1006, Short.MAX_VALUE)
        );
        slideshowPnlLayout.setVerticalGroup(
            slideshowPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 306, Short.MAX_VALUE)
        );

        homeBody.add(slideshowPnl, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 30, 1010, 310));

        salesTPnl.setBackground(new java.awt.Color(164, 130, 6));
        salesTPnl.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        salesTPnl.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setFont(new java.awt.Font("STSong", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("TOTAL SALES");
        salesTPnl.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 150, 40));

        homeBody.add(salesTPnl, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 390, 290, 130));

        layeredPane.add(homeBody, "card2");

        menuBody.setBackground(new java.awt.Color(255, 255, 255));
        menuBody.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        searchTxtF.setFont(new java.awt.Font("Serif", 2, 14)); // NOI18N
        searchTxtF.setForeground(new java.awt.Color(102, 102, 102));
        searchTxtF.setText("Search Product Name");
        searchTxtF.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        searchTxtF.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                searchTxtFFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                searchTxtFFocusLost(evt);
            }
        });
        menuBody.add(searchTxtF, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 20, 490, 40));

        jLabel14.setFont(new java.awt.Font("STSong", 0, 48)); // NOI18N
        jLabel14.setText("MENU");
        menuBody.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, 180, 40));

        jPanel6.setBackground(new java.awt.Color(204, 153, 0));
        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        productImg.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel6.add(productImg, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 140, 190, 170));

        jPanel7.setBackground(new java.awt.Color(124, 101, 42));
        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel16.setFont(new java.awt.Font("STSong", 1, 24)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText("MENU ITEM DETAILS");
        jPanel7.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 30, 280, 30));

        jPanel6.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 350, 90));

        jLabel17.setFont(new java.awt.Font("STSong", 1, 18)); // NOI18N
        jLabel17.setText("PRODUCT ID #");
        jPanel6.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 110, -1, -1));

        nameTxtF.setFont(new java.awt.Font("Serif", 0, 14)); // NOI18N
        nameTxtF.setForeground(new java.awt.Color(102, 102, 102));
        nameTxtF.setText("Enter Product Name");
        nameTxtF.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                nameTxtFFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                nameTxtFFocusLost(evt);
            }
        });
        jPanel6.add(nameTxtF, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 360, 160, 30));

        jLabel18.setFont(new java.awt.Font("STSong", 1, 14)); // NOI18N
        jLabel18.setText("PRICE:");
        jPanel6.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 410, -1, -1));

        priceTxtF.setFont(new java.awt.Font("Serif", 0, 14)); // NOI18N
        priceTxtF.setForeground(new java.awt.Color(102, 102, 102));
        priceTxtF.setText("Enter Price");
        priceTxtF.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                priceTxtFFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                priceTxtFFocusLost(evt);
            }
        });
        jPanel6.add(priceTxtF, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 400, 160, 30));

        jLabel19.setFont(new java.awt.Font("STSong", 1, 14)); // NOI18N
        jLabel19.setText("CATEGORY:");
        jPanel6.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 450, -1, -1));

        categoryCB.setFont(new java.awt.Font("Serif", 0, 14)); // NOI18N
        categoryCB.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        categoryCB.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel6.add(categoryCB, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 440, 160, 30));

        addBtn.setBackground(new java.awt.Color(0, 102, 51));
        addBtn.setFont(new java.awt.Font("STSong", 1, 14)); // NOI18N
        addBtn.setForeground(new java.awt.Color(255, 255, 255));
        addBtn.setText("ADD");
        addBtn.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        addBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addBtnActionPerformed(evt);
            }
        });
        jPanel6.add(addBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 550, 100, 40));

        jLabel20.setFont(new java.awt.Font("STSong", 1, 14)); // NOI18N
        jLabel20.setText("PRODUCT NAME:");
        jPanel6.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 360, -1, -1));

        jLabel22.setFont(new java.awt.Font("STSong", 1, 14)); // NOI18N
        jLabel22.setText("STOCKS:");
        jPanel6.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 490, -1, -1));

        priceTxtF1.setFont(new java.awt.Font("Serif", 0, 14)); // NOI18N
        jPanel6.add(priceTxtF1, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 480, 160, 30));

        updateBtn.setBackground(new java.awt.Color(51, 0, 102));
        updateBtn.setFont(new java.awt.Font("STSong", 1, 14)); // NOI18N
        updateBtn.setForeground(new java.awt.Color(255, 255, 255));
        updateBtn.setText("UPDATE");
        updateBtn.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        updateBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateBtnActionPerformed(evt);
            }
        });
        jPanel6.add(updateBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 550, 100, 40));

        clrBtn.setBackground(new java.awt.Color(153, 153, 153));
        clrBtn.setFont(new java.awt.Font("STSong", 1, 14)); // NOI18N
        clrBtn.setForeground(new java.awt.Color(255, 255, 255));
        clrBtn.setText("CLEAR");
        clrBtn.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        clrBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clrBtnActionPerformed(evt);
            }
        });
        jPanel6.add(clrBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 550, 100, 40));

        prodIdLbl.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        jPanel6.add(prodIdLbl, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 100, 150, 40));

        jButton3.setBackground(new java.awt.Color(204, 102, 0));
        jButton3.setFont(new java.awt.Font("STSong", 1, 14)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("UPLOAD");
        jButton3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel6.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 310, 190, 30));

        menuBody.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 20, 350, 610));

        jPanel5.setBackground(new java.awt.Color(124, 101, 42));
        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel21.setFont(new java.awt.Font("STSong", 1, 40)); // NOI18N
        jLabel21.setText("JAVA BEANS PRODUCTS");
        jPanel5.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 20, 500, 40));

        menuBody.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, 700, 80));

        jPanel4.setBackground(new java.awt.Color(153, 102, 0));
        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jScrollPane2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(102, 51, 0), 2, true));

        productsTbl.setFont(new java.awt.Font("Serif", 1, 14)); // NOI18N
        productsTbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Product ID", "Product Name", "Price", "Category ID", "Product Image"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        productsTbl.setRowHeight(25);
        productsTbl.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        productsTbl.setShowVerticalLines(true);
        productsTbl.getTableHeader().setReorderingAllowed(false);
        productsTbl.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                productsTblMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(productsTbl);
        if (productsTbl.getColumnModel().getColumnCount() > 0) {
            productsTbl.getColumnModel().getColumn(0).setResizable(false);
            productsTbl.getColumnModel().getColumn(0).setPreferredWidth(70);
            productsTbl.getColumnModel().getColumn(1).setResizable(false);
            productsTbl.getColumnModel().getColumn(1).setPreferredWidth(150);
            productsTbl.getColumnModel().getColumn(2).setResizable(false);
            productsTbl.getColumnModel().getColumn(3).setResizable(false);
            productsTbl.getColumnModel().getColumn(4).setResizable(false);
        }

        jPanel4.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, 660, 320));

        jButton2.setBackground(new java.awt.Color(0, 0, 102));
        jButton2.setFont(new java.awt.Font("STSong", 1, 14)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("PRINT LIST");
        jButton2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel4.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 410, 130, 40));

        sortProd.setFont(new java.awt.Font("Serif", 0, 14)); // NOI18N
        sortProd.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select", "Product ID", "Product Name", "Price" }));
        sortProd.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        sortProd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sortProdActionPerformed(evt);
            }
        });
        jPanel4.add(sortProd, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 40, 180, 30));

        searchCatCB.setFont(new java.awt.Font("Serif", 0, 14)); // NOI18N
        searchCatCB.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        searchCatCB.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        searchCatCB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchCatCBActionPerformed(evt);
            }
        });
        jPanel4.add(searchCatCB, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, 180, 30));

        jLabel24.setFont(new java.awt.Font("STSong", 1, 18)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(255, 255, 255));
        jLabel24.setText("SORT BY:");
        jPanel4.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 10, 170, 20));

        jLabel25.setFont(new java.awt.Font("STSong", 1, 18)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(255, 255, 255));
        jLabel25.setText("SELECT CATEGORY:");
        jPanel4.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 220, 20));

        jButton4.setBackground(new java.awt.Color(0, 102, 102));
        jButton4.setFont(new java.awt.Font("STSong", 1, 14)); // NOI18N
        jButton4.setForeground(new java.awt.Color(255, 255, 255));
        jButton4.setText("MODIFY CATEGORY");
        jButton4.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel4.add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 410, 170, 40));

        deleteBtn.setBackground(new java.awt.Color(102, 0, 0));
        deleteBtn.setFont(new java.awt.Font("STSong", 1, 14)); // NOI18N
        deleteBtn.setForeground(new java.awt.Color(255, 255, 255));
        deleteBtn.setText("DELETE");
        deleteBtn.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        deleteBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteBtnActionPerformed(evt);
            }
        });
        jPanel4.add(deleteBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 410, 120, 40));

        menuBody.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 170, 700, 460));

        layeredPane.add(menuBody, "card5");

        posBody.setBackground(new java.awt.Color(255, 255, 255));
        posBody.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        scrollPane1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        scrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        panelMenu.setBackground(new java.awt.Color(83, 71, 0));
        panelMenu.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        panelMenu.setLayout(new java.awt.GridLayout(0, 3, 30, 20));
        scrollPane1.setViewportView(panelMenu);

        posBody.add(scrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 130, 700, 500));

        ordersBdy.setBackground(new java.awt.Color(204, 153, 0));
        ordersBdy.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(153, 102, 0));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel5.setFont(new java.awt.Font("STSong", 1, 24)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Current Order");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 0, 170, 50));

        jLabel8.setFont(new java.awt.Font("STSong", 1, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("SUBTOTAL: ");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, 130, 30));

        subTValue.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        subTValue.setText("0");
        jPanel1.add(subTValue, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 50, 130, 30));

        jLabel23.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        jLabel23.setText("PHP");
        jPanel1.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 50, 60, 30));

        ordersBdy.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 370, 90));

        jPanel3.setBackground(new java.awt.Color(153, 102, 0));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel7.setBackground(new java.awt.Color(255, 255, 255));
        jLabel7.setFont(new java.awt.Font("STSong", 1, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("TOTAL: ");
        jPanel3.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 110, 30));

        jButton1.setBackground(new java.awt.Color(0, 204, 0));
        jButton1.setFont(new java.awt.Font("Serif", 1, 14)); // NOI18N
        jButton1.setText("PLACE ORDER");
        jButton1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 90, 160, 40));

        jLabel9.setBackground(new java.awt.Color(255, 255, 255));
        jLabel9.setFont(new java.awt.Font("STSong", 1, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("TAX:");
        jPanel3.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, 60, 30));

        totalValue.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        totalValue.setText("0");
        jPanel3.add(totalValue, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 10, 160, 30));

        taxValue.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        taxValue.setText("0");
        jPanel3.add(taxValue, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 50, 170, 30));

        clearOrdersBtn.setBackground(new java.awt.Color(204, 204, 204));
        clearOrdersBtn.setFont(new java.awt.Font("Serif", 1, 14)); // NOI18N
        clearOrdersBtn.setText("RESET");
        clearOrdersBtn.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel3.add(clearOrdersBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, 160, 40));

        ordersBdy.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 500, 370, 150));

        orderScroll.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        jPanel2.setBackground(new java.awt.Color(204, 153, 0));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 368, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 428, Short.MAX_VALUE)
        );

        orderScroll.setViewportView(jPanel2);

        ordersBdy.add(orderScroll, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 90, 370, 410));

        posBody.add(ordersBdy, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 0, 370, 650));

        categoryScrll.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        categoryPnl.setBackground(new java.awt.Color(153, 102, 0));
        categoryPnl.setBorder(new javax.swing.border.MatteBorder(null));
        categoryScrll.setViewportView(categoryPnl);

        posBody.add(categoryScrll, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, 700, 60));

        jLabel15.setFont(new java.awt.Font("STSong", 1, 45)); // NOI18N
        jLabel15.setText("POINT OF SALES");
        posBody.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 400, -1));

        searchF.setFont(new java.awt.Font("Serif", 2, 14)); // NOI18N
        searchF.setForeground(new java.awt.Color(102, 102, 102));
        searchF.setText("Search Product/Category");
        searchF.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        searchF.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                searchFFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                searchFFocusLost(evt);
            }
        });
        searchF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchFActionPerformed(evt);
            }
        });
        posBody.add(searchF, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 10, 290, 40));

        layeredPane.add(posBody, "card3");

        accBody.setBackground(new java.awt.Color(255, 255, 255));
        accBody.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        userTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Employee ID", "Employee Name", "Address", "Email Address", "Contact #", "Hired Date", "Status"
            }
        ));
        userTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                userTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(userTable);

        accBody.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 417, 1010, 190));

        jLabel4.setFont(new java.awt.Font("STSong", 1, 45)); // NOI18N
        jLabel4.setText("MANAGE ACCOUNTS");
        accBody.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 20, 540, 50));

        jLabel6.setFont(new java.awt.Font("STSong", 1, 16)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("USER ID:");
        accBody.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 110, 90, 20));

        jLabel10.setFont(new java.awt.Font("STSong", 1, 16)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("LAST NAME:");
        accBody.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 170, -1, -1));

        jLabel12.setFont(new java.awt.Font("STSong", 1, 16)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("STATUS:");
        accBody.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(910, 290, -1, -1));

        searchUserTxtF.setFont(new java.awt.Font("Serif", 2, 14)); // NOI18N
        searchUserTxtF.setForeground(new java.awt.Color(102, 102, 102));
        searchUserTxtF.setText("Search");
        searchUserTxtF.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        searchUserTxtF.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                searchUserTxtFFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                searchUserTxtFFocusLost(evt);
            }
        });
        searchUserTxtF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchUserTxtFActionPerformed(evt);
            }
        });
        searchUserTxtF.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                searchUserTxtFKeyPressed(evt);
            }
        });
        accBody.add(searchUserTxtF, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 20, 340, 40));

        lnameTxtF.setFont(new java.awt.Font("Serif", 0, 14)); // NOI18N
        lnameTxtF.setForeground(new java.awt.Color(102, 102, 102));
        lnameTxtF.setText("Last Name");
        lnameTxtF.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lnameTxtF.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                lnameTxtFFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                lnameTxtFFocusLost(evt);
            }
        });
        accBody.add(lnameTxtF, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 160, 280, 40));

        fnameTxtF.setFont(new java.awt.Font("Serif", 0, 14)); // NOI18N
        fnameTxtF.setForeground(new java.awt.Color(102, 102, 102));
        fnameTxtF.setText("First Name");
        fnameTxtF.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        fnameTxtF.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                fnameTxtFFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                fnameTxtFFocusLost(evt);
            }
        });
        accBody.add(fnameTxtF, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 160, 220, 40));

        jLabel26.setFont(new java.awt.Font("STSong", 1, 16)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(255, 255, 255));
        jLabel26.setText("FIRST NAME:");
        accBody.add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 170, -1, -1));

        miTxtF.setFont(new java.awt.Font("Serif", 0, 14)); // NOI18N
        miTxtF.setForeground(new java.awt.Color(102, 102, 102));
        miTxtF.setText("M.I");
        miTxtF.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        miTxtF.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                miTxtFFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                miTxtFFocusLost(evt);
            }
        });
        accBody.add(miTxtF, new org.netbeans.lib.awtextra.AbsoluteConstraints(930, 160, 140, 40));

        jLabel27.setFont(new java.awt.Font("STSong", 1, 16)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(255, 255, 255));
        jLabel27.setText("MIDDLE INITIAL:");
        accBody.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 170, -1, -1));

        jLabel28.setFont(new java.awt.Font("STSong", 1, 16)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(255, 255, 255));
        jLabel28.setText("ADDRESS:");
        accBody.add(jLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 230, -1, -1));

        addTxtF.setFont(new java.awt.Font("Serif", 0, 14)); // NOI18N
        addTxtF.setForeground(new java.awt.Color(102, 102, 102));
        addTxtF.setText("Address");
        addTxtF.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        addTxtF.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                addTxtFFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                addTxtFFocusLost(evt);
            }
        });
        accBody.add(addTxtF, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 220, 440, 40));

        eaddTxtF.setFont(new java.awt.Font("Serif", 0, 14)); // NOI18N
        eaddTxtF.setForeground(new java.awt.Color(102, 102, 102));
        eaddTxtF.setText("Email-Address");
        eaddTxtF.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        eaddTxtF.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                eaddTxtFFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                eaddTxtFFocusLost(evt);
            }
        });
        accBody.add(eaddTxtF, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 220, 330, 40));

        jLabel29.setFont(new java.awt.Font("STSong", 1, 16)); // NOI18N
        jLabel29.setForeground(new java.awt.Color(255, 255, 255));
        jLabel29.setText("EMAIL-ADDRESS:");
        accBody.add(jLabel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 230, -1, -1));

        contTxtF.setFont(new java.awt.Font("Serif", 0, 14)); // NOI18N
        contTxtF.setForeground(new java.awt.Color(102, 102, 102));
        contTxtF.setText("Contact Number");
        contTxtF.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        contTxtF.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                contTxtFFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                contTxtFFocusLost(evt);
            }
        });
        accBody.add(contTxtF, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 280, 310, 40));

        jLabel30.setFont(new java.awt.Font("STSong", 1, 16)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(255, 255, 255));
        jLabel30.setText("CONTACT NO:");
        accBody.add(jLabel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 290, -1, -1));

        jLabel31.setFont(new java.awt.Font("STSong", 1, 16)); // NOI18N
        jLabel31.setForeground(new java.awt.Color(255, 255, 255));
        jLabel31.setText("HIRED DATE:");
        accBody.add(jLabel31, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 290, -1, -1));
        accBody.add(dateChooser, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 280, 260, 40));

        statuTxtF.setFont(new java.awt.Font("Serif", 0, 14)); // NOI18N
        statuTxtF.setForeground(new java.awt.Color(102, 102, 102));
        statuTxtF.setText("BLOCKED?");
        accBody.add(statuTxtF, new org.netbeans.lib.awtextra.AbsoluteConstraints(990, 290, -1, -1));

        addUser.setBackground(new java.awt.Color(0, 102, 0));
        addUser.setFont(new java.awt.Font("STSong", 1, 16)); // NOI18N
        addUser.setForeground(new java.awt.Color(255, 255, 255));
        addUser.setText("ADD");
        addUser.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        addUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addUserActionPerformed(evt);
            }
        });
        accBody.add(addUser, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 350, 150, 40));

        updateUser.setBackground(new java.awt.Color(0, 0, 102));
        updateUser.setFont(new java.awt.Font("STSong", 1, 16)); // NOI18N
        updateUser.setForeground(new java.awt.Color(255, 255, 255));
        updateUser.setText("MODIFY");
        updateUser.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        updateUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateUserActionPerformed(evt);
            }
        });
        accBody.add(updateUser, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 350, 150, 40));

        deleteUser.setBackground(new java.awt.Color(153, 51, 0));
        deleteUser.setFont(new java.awt.Font("STSong", 1, 16)); // NOI18N
        deleteUser.setForeground(new java.awt.Color(255, 255, 255));
        deleteUser.setText("DELETE");
        deleteUser.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        deleteUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteUserActionPerformed(evt);
            }
        });
        accBody.add(deleteUser, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 350, 140, 40));

        clrUser.setBackground(new java.awt.Color(153, 153, 153));
        clrUser.setFont(new java.awt.Font("STSong", 1, 16)); // NOI18N
        clrUser.setForeground(new java.awt.Color(255, 255, 255));
        clrUser.setText("CLEAR");
        clrUser.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        clrUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clrUserActionPerformed(evt);
            }
        });
        accBody.add(clrUser, new org.netbeans.lib.awtextra.AbsoluteConstraints(940, 350, 120, 40));

        userIdTxtF.setFont(new java.awt.Font("Serif", 0, 14)); // NOI18N
        userIdTxtF.setForeground(new java.awt.Color(102, 102, 102));
        userIdTxtF.setText("User ID");
        userIdTxtF.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        userIdTxtF.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                userIdTxtFFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                userIdTxtFFocusLost(evt);
            }
        });
        accBody.add(userIdTxtF, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 100, 440, 40));

        jButton9.setBackground(new java.awt.Color(0, 102, 102));
        jButton9.setFont(new java.awt.Font("Serif", 1, 16)); // NOI18N
        jButton9.setForeground(new java.awt.Color(255, 255, 255));
        jButton9.setText("PRINT USER LIST");
        jButton9.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        accBody.add(jButton9, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 90, 180, 40));

        blockBtn.setBackground(new java.awt.Color(102, 51, 0));
        blockBtn.setFont(new java.awt.Font("STSong", 1, 16)); // NOI18N
        blockBtn.setForeground(new java.awt.Color(255, 255, 255));
        blockBtn.setText("BLOCK/UBLOCK");
        blockBtn.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        blockBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                blockBtnActionPerformed(evt);
            }
        });
        accBody.add(blockBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 350, 160, 40));

        sortUsers.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Sort By", "Employee Id", "Name", "Hired Date", "Status" }));
        sortUsers.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sortUsersActionPerformed(evt);
            }
        });
        accBody.add(sortUsers, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 350, 180, 30));

        jPanel8.setBackground(new java.awt.Color(153, 102, 0));
        jPanel8.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1056, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 546, Short.MAX_VALUE)
        );

        accBody.add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, 1060, 550));

        filterCol.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Filteer by", "Employee Id", "Name", "Address", "Hired Date" }));
        accBody.add(filterCol, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 20, 190, 40));

        layeredPane.add(accBody, "card4");

        javax.swing.GroupLayout salesBodyLayout = new javax.swing.GroupLayout(salesBody);
        salesBody.setLayout(salesBodyLayout);
        salesBodyLayout.setHorizontalGroup(
            salesBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1110, Short.MAX_VALUE)
        );
        salesBodyLayout.setVerticalGroup(
            salesBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 650, Short.MAX_VALUE)
        );

        layeredPane.add(salesBody, "card6");

        bodyPnl.add(layeredPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 0, 1110, 650));

        navigationPnl1.setBackground(new java.awt.Color(190, 156, 106));
        navigationPnl1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        logoutPnl.setBackground(new java.awt.Color(190, 156, 106));
        logoutPnl.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                logoutPnlMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                logoutPnlMouseExited(evt);
            }
        });
        logoutPnl.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        logoutLbl.setBackground(new java.awt.Color(0, 204, 255));
        logoutLbl.setFont(new java.awt.Font("Serif", 0, 16)); // NOI18N
        logoutLbl.setForeground(new java.awt.Color(75, 49, 8));
        logoutLbl.setText("LOGOUT");
        logoutPnl.add(logoutLbl, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 0, 150, 40));

        navigationPnl1.add(logoutPnl, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 580, 180, 40));

        transactionPnl.setBackground(new java.awt.Color(190, 156, 106));
        transactionPnl.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                transactionPnlMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                transactionPnlMouseExited(evt);
            }
        });
        transactionPnl.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        transactionLbl.setBackground(new java.awt.Color(0, 204, 255));
        transactionLbl.setFont(new java.awt.Font("Serif", 0, 16)); // NOI18N
        transactionLbl.setForeground(new java.awt.Color(75, 49, 8));
        transactionLbl.setText(" TRANSACTION");
        transactionPnl.add(transactionLbl, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 0, 150, 40));

        navigationPnl1.add(transactionPnl, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 330, 180, 40));

        salesPnl.setBackground(new java.awt.Color(190, 156, 106));
        salesPnl.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                salesPnlMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                salesPnlMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                salesPnlMouseExited(evt);
            }
        });
        salesPnl.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        salesLbl.setBackground(new java.awt.Color(0, 204, 255));
        salesLbl.setFont(new java.awt.Font("Serif", 0, 16)); // NOI18N
        salesLbl.setForeground(new java.awt.Color(75, 49, 8));
        salesLbl.setText(" SALES");
        salesPnl.add(salesLbl, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 0, 150, 40));

        navigationPnl1.add(salesPnl, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 270, 180, 40));

        menuPnl.setBackground(new java.awt.Color(190, 156, 106));
        menuPnl.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                menuPnlMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                menuPnlMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                menuPnlMouseExited(evt);
            }
        });
        menuPnl.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        menuLbl.setBackground(new java.awt.Color(0, 204, 255));
        menuLbl.setFont(new java.awt.Font("Serif", 0, 16)); // NOI18N
        menuLbl.setForeground(new java.awt.Color(75, 49, 8));
        menuLbl.setText(" MENU");
        menuPnl.add(menuLbl, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 0, 150, 40));

        navigationPnl1.add(menuPnl, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 210, 180, 40));

        accPnl.setBackground(new java.awt.Color(190, 156, 106));
        accPnl.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                accPnlMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                accPnlMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                accPnlMouseExited(evt);
            }
        });
        accPnl.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        accLbl.setBackground(new java.awt.Color(0, 204, 255));
        accLbl.setFont(new java.awt.Font("Serif", 0, 16)); // NOI18N
        accLbl.setForeground(new java.awt.Color(75, 49, 8));
        accLbl.setText(" ACCOUNTS");
        accPnl.add(accLbl, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 0, 150, 40));

        navigationPnl1.add(accPnl, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 150, 180, 40));

        posPnl.setBackground(new java.awt.Color(190, 156, 106));
        posPnl.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                posPnlMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                posPnlMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                posPnlMouseExited(evt);
            }
        });
        posPnl.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        posLbl.setBackground(new java.awt.Color(0, 204, 255));
        posLbl.setFont(new java.awt.Font("Serif", 0, 16)); // NOI18N
        posLbl.setForeground(new java.awt.Color(75, 49, 8));
        posLbl.setText("  POS");
        posPnl.add(posLbl, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 0, 150, 40));

        navigationPnl1.add(posPnl, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, 180, 40));

        homePnl.setBackground(new java.awt.Color(190, 156, 106));
        homePnl.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                homePnlMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                homePnlMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                homePnlMouseExited(evt);
            }
        });
        homePnl.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        homeLbl.setFont(new java.awt.Font("Serif", 0, 16)); // NOI18N
        homeLbl.setForeground(new java.awt.Color(75, 49, 8));
        homeLbl.setText("  HOME");
        homePnl.add(homeLbl, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 0, 150, 40));

        navigationPnl1.add(homePnl, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 180, 40));

        bodyPnl.add(navigationPnl1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 200, 650));

        getContentPane().add(bodyPnl, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 90, 1310, 650));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void homePnlMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_homePnlMouseEntered
        homePnl.setBackground(wheat);
    }//GEN-LAST:event_homePnlMouseEntered

    private void homePnlMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_homePnlMouseExited
        homePnl.setBackground(wood);
    }//GEN-LAST:event_homePnlMouseExited

    private void posPnlMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_posPnlMouseEntered
        posPnl.setBackground(wheat);
    }//GEN-LAST:event_posPnlMouseEntered

    private void posPnlMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_posPnlMouseExited
        posPnl.setBackground(wood);
    }//GEN-LAST:event_posPnlMouseExited

    private void accPnlMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_accPnlMouseEntered
        accPnl.setBackground(wheat);
    }//GEN-LAST:event_accPnlMouseEntered

    private void accPnlMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_accPnlMouseExited
        accPnl.setBackground(wood);
    }//GEN-LAST:event_accPnlMouseExited

    private void menuPnlMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuPnlMouseExited
        menuPnl.setBackground(wood);
    }//GEN-LAST:event_menuPnlMouseExited

    private void menuPnlMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuPnlMouseEntered
        menuPnl.setBackground(wheat);
    }//GEN-LAST:event_menuPnlMouseEntered

    private void salesPnlMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_salesPnlMouseEntered
        salesPnl.setBackground(wheat);
    }//GEN-LAST:event_salesPnlMouseEntered

    private void salesPnlMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_salesPnlMouseExited
        salesPnl.setBackground(wood);
    }//GEN-LAST:event_salesPnlMouseExited

    private void transactionPnlMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_transactionPnlMouseEntered
        transactionPnl.setBackground(wheat);
    }//GEN-LAST:event_transactionPnlMouseEntered

    private void transactionPnlMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_transactionPnlMouseExited
        transactionPnl.setBackground(wood);
    }//GEN-LAST:event_transactionPnlMouseExited

    private void logoutPnlMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logoutPnlMouseEntered
        logoutPnl.setBackground(wheat);
    }//GEN-LAST:event_logoutPnlMouseEntered

    private void logoutPnlMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logoutPnlMouseExited
        logoutPnl.setBackground(wood);
    }//GEN-LAST:event_logoutPnlMouseExited

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void posPnlMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_posPnlMouseClicked
        posBody.setVisible(true);
        accBody.setVisible(false);
        homeBody.setVisible(false);
        menuBody.setVisible(false);
        salesBody.setVisible(false);

    }//GEN-LAST:event_posPnlMouseClicked

    private void searchFFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_searchFFocusGained
        if (String.valueOf(searchF.getText()).equals("Search Product/Category")) {
            searchF.setText("");
            searchF.setForeground(Color.BLACK);
        }
    }//GEN-LAST:event_searchFFocusGained

    private void searchFFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_searchFFocusLost
        if (String.valueOf(searchF.getText()).isEmpty()) {
            searchF.setText("Search Product Name");
            searchF.setForeground(Color.GRAY);
        }
    }//GEN-LAST:event_searchFFocusLost

    private void searchTxtFFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_searchTxtFFocusGained
        if (String.valueOf(searchTxtF.getText()).equals("Search Product Name")) {
            searchTxtF.setText("");
            searchTxtF.setForeground(Color.BLACK);
        }
    }//GEN-LAST:event_searchTxtFFocusGained

    private void searchTxtFFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_searchTxtFFocusLost
        if (String.valueOf(searchTxtF.getText()).isEmpty()) {
            searchTxtF.setText("Search Product/Category");
            searchTxtF.setForeground(Color.GRAY);
        }
    }//GEN-LAST:event_searchTxtFFocusLost

    private void nameTxtFFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_nameTxtFFocusGained
        if (String.valueOf(nameTxtF.getText()).equals("Enter Product Name")) {
            nameTxtF.setText("");
            nameTxtF.setForeground(Color.BLACK);
        }
    }//GEN-LAST:event_nameTxtFFocusGained

    private void nameTxtFFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_nameTxtFFocusLost
        if (String.valueOf(nameTxtF.getText()).isEmpty()) {
            nameTxtF.setText("Enter Product Name");
            nameTxtF.setForeground(Color.GRAY);
        }
    }//GEN-LAST:event_nameTxtFFocusLost

    private void priceTxtFFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_priceTxtFFocusGained
        if (String.valueOf(priceTxtF.getText()).equals("Enter Price")) {
            priceTxtF.setText("");
            priceTxtF.setForeground(Color.BLACK);
        }
    }//GEN-LAST:event_priceTxtFFocusGained

    private void priceTxtFFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_priceTxtFFocusLost
        if (String.valueOf(priceTxtF.getText()).isEmpty()) {
            priceTxtF.setText("Enter Price");
            priceTxtF.setForeground(Color.GRAY);
        }
    }//GEN-LAST:event_priceTxtFFocusLost

    private void homePnlMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_homePnlMouseClicked
        posBody.setVisible(false);
        accBody.setVisible(false);
        homeBody.setVisible(true);
        menuBody.setVisible(false);
        salesBody.setVisible(false);
    }//GEN-LAST:event_homePnlMouseClicked

    private void accPnlMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_accPnlMouseClicked
        posBody.setVisible(false);
        accBody.setVisible(true);
        homeBody.setVisible(false);
        menuBody.setVisible(false);
        salesBody.setVisible(false);
    }//GEN-LAST:event_accPnlMouseClicked

    private void menuPnlMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuPnlMouseClicked
        posBody.setVisible(false);
        accBody.setVisible(false);
        homeBody.setVisible(false);
        menuBody.setVisible(true);
        salesBody.setVisible(false);

        dbManager.retrieveAllProducts(productsTbl);
        JTableHeader header = productsTbl.getTableHeader();
        header.setFont(new Font("Serif", Font.BOLD, 15));
        header.setBackground(wood); // Coffee brown
        header.setForeground(Color.BLACK);
        ((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);


    }//GEN-LAST:event_menuPnlMouseClicked

    private void salesPnlMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_salesPnlMouseClicked
        posBody.setVisible(false);
        accBody.setVisible(false);
        homeBody.setVisible(false);
        menuBody.setVisible(false);
        salesBody.setVisible(true);
    }//GEN-LAST:event_salesPnlMouseClicked

    private void searchFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchFActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchFActionPerformed

    private void productsTblMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_productsTblMouseClicked
        try {
            int i = productsTbl.getSelectedRow();
            if (i == -1) {
                JOptionPane.showMessageDialog(null, "Please select a product first!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String value = String.valueOf(productsTbl.getValueAt(i, 0));
            int id = Integer.parseInt(value);

            stmt = conn.createStatement();
            query = "SELECT * FROM product WHERE ProductId = '" + id + "'";
            rs = stmt.executeQuery(query);

            if (rs.next()) {
                // Retrieve product details
                int ProductId = rs.getInt("ProductId");
                String ProductName = rs.getString("ProductName");
                double Price = rs.getDouble("Price");
                int CategoryId = rs.getInt("CategoryId");
                byte[] ProductImage = rs.getBytes("ProductImage"); // Retrieve image as byte array

                // Create Product object
                p = new Product(ProductId, ProductName, Price, CategoryId, ProductImage);

                // Set text fields
                prodIdLbl.setText(String.valueOf(p.getProductId()));
                nameTxtF.setText(p.getProductName());
                priceTxtF.setText(String.valueOf(p.getProductPrice()));

                // Get Category Name using CategoryId
                PreparedStatement categoryStmt = conn.prepareStatement("SELECT CategoryName FROM Category WHERE CategoryId = ?");
                categoryStmt.setInt(1, CategoryId);
                ResultSet catRs = categoryStmt.executeQuery();

                if (catRs.next()) {
                    String categoryName = catRs.getString("CategoryName");

                    // Loop through combo box items to find match
                    for (int j = 0; j < categoryCB.getItemCount(); j++) {
                        if (categoryCB.getItemAt(j).equalsIgnoreCase(categoryName)) {
                            categoryCB.setSelectedIndex(j);
                            break;
                        }
                    }
                }

                // Convert byte array (BLOB) to Image and display it
                if (ProductImage != null) {
                    imageBytes = ProductImage; // ✅ Store the image byte array for later use

                    ImageIcon icon = new ImageIcon(ProductImage);
                    Image img = icon.getImage().getScaledInstance(productImg.getWidth(), productImg.getHeight(), Image.SCALE_SMOOTH);
                    productImg.setIcon(new ImageIcon(img));
                } else {
                    imageBytes = null; // Clear previous image if none exists
                    productImg.setIcon(null);
                    JOptionPane.showMessageDialog(null, "No image found for this product.", "Image", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(Admin_Dashboard.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid product ID!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_productsTblMouseClicked

    private void sortProdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sortProdActionPerformed
        // TODO add your handling code here:
        String selected = sortProd.getSelectedItem().toString();
        loadProductsSorted(selected);
    }//GEN-LAST:event_sortProdActionPerformed

    private void searchCatCBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchCatCBActionPerformed
        String selectedCategory = (String) searchCatCB.getSelectedItem();
        if (selectedCategory != null) {
            dbManager.retrieveProductperCat(selectedCategory, productsTbl);
        }
    }//GEN-LAST:event_searchCatCBActionPerformed

    private void clrBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clrBtnActionPerformed
        clear();
    }//GEN-LAST:event_clrBtnActionPerformed

    private void addBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addBtnActionPerformed
        int i = productsTbl.getSelectedRow();

        if (i != -1) {
            JOptionPane.showMessageDialog(null, "Product cannot be duplicated", "Error Message", JOptionPane.ERROR_MESSAGE);
            clear();
        } else {
            try {
                String productName = nameTxtF.getText().trim();
                String priceText = priceTxtF.getText().trim();
                int selectedCategory = categoryCB.getSelectedIndex();
                String selectedCategoryName = (String) categoryCB.getSelectedItem();
                validateProductForm(productName, priceText, selectedCategory, imageBytes);

                // Safe to continue
                double price = Double.parseDouble(priceText);
                int categoryId = dbManager.getCategoryIdByName(selectedCategoryName);
                if (categoryId == -1) {
                    return;
                }

                dbManager.addProduct(dbManager.generateNextProductId(), productName, price, categoryId, imageBytes);
                JOptionPane.showMessageDialog(null, "Product added successfully!", "Information Message", JOptionPane.INFORMATION_MESSAGE);
                dbManager.retrieveProductperCat(searchCatCB.getSelectedItem().toString(), productsTbl);
                clear();
            } catch (IllegalArgumentException ignored) {
                // Handled already by validation
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Unexpected error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }


    }//GEN-LAST:event_addBtnActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        uploadPhoto();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void updateBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateBtnActionPerformed
        int i = productsTbl.getSelectedRow();

        if (i == -1) {
            JOptionPane.showMessageDialog(null, "Please select a product to update.", "Error Message", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            String productName = nameTxtF.getText().trim();
            String priceText = priceTxtF.getText().trim();
            String selectedCategoryName = (String) categoryCB.getSelectedItem();
            int selectedCat = categoryCB.getSelectedIndex();
            validateProductForm(productName, priceText, selectedCat, imageBytes);

            // Safe to continue
            double price = Double.parseDouble(priceText);
            int categoryId = dbManager.getCategoryIdByName(selectedCategoryName);
            if (categoryId == -1) {
                return;
            }

            int id = Integer.parseInt(prodIdLbl.getText());
            dbManager.updateProduct(id, productName, price, categoryId, imageBytes);
            JOptionPane.showMessageDialog(null, "Product updated successfully!", "Information Message", JOptionPane.INFORMATION_MESSAGE);
            dbManager.retrieveProductperCat(searchCatCB.getSelectedItem().toString(), productsTbl);
            clear();
        } catch (IllegalArgumentException ignored) {
            // Handled already by validation
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Unexpected error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_updateBtnActionPerformed

    private void deleteBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteBtnActionPerformed
        int i = productsTbl.getSelectedRow();

        if (i == -1) {
            JOptionPane.showMessageDialog(null, "Please select a product to delete.", "Error Message", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int c = JOptionPane.showConfirmDialog(
                null,
                "Are you sure you want to delete this product?",
                "Confirmation Message",
                JOptionPane.YES_NO_OPTION
        );
        int productId = Integer.parseInt(prodIdLbl.getText());
        if (c == 0) {
            dbManager.deleteProduct(productId);
            JOptionPane.showMessageDialog(null, "Product deleted successfully!", "Information Message", JOptionPane.INFORMATION_MESSAGE);
            dbManager.retrieveProductperCat(searchCatCB.getSelectedItem().toString(), productsTbl);
            clear();
        }
    }//GEN-LAST:event_deleteBtnActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton4ActionPerformed

    private void searchUserTxtFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchUserTxtFActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchUserTxtFActionPerformed

    private void userIdTxtFFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_userIdTxtFFocusGained
        if (String.valueOf(userIdTxtF.getText()).equals("User ID")) {
            userIdTxtF.setText("");
            userIdTxtF.setForeground(Color.BLACK);
        }
    }//GEN-LAST:event_userIdTxtFFocusGained

    private void userIdTxtFFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_userIdTxtFFocusLost
        if (String.valueOf(userIdTxtF.getText()).isEmpty()) {
            userIdTxtF.setText("User ID");
            userIdTxtF.setForeground(Color.GRAY);
        }
    }//GEN-LAST:event_userIdTxtFFocusLost

    private void lnameTxtFFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_lnameTxtFFocusGained
        if (String.valueOf(lnameTxtF.getText()).equals("Last Name")) {
            lnameTxtF.setText("");
            lnameTxtF.setForeground(Color.BLACK);
        }
    }//GEN-LAST:event_lnameTxtFFocusGained

    private void lnameTxtFFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_lnameTxtFFocusLost
        if (String.valueOf(lnameTxtF.getText()).isEmpty()) {
            lnameTxtF.setText("Last Name");
            lnameTxtF.setForeground(Color.GRAY);
        }
    }//GEN-LAST:event_lnameTxtFFocusLost

    private void addTxtFFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_addTxtFFocusGained
        if (String.valueOf(addTxtF.getText()).equals("Address")) {
            addTxtF.setText("");
            addTxtF.setForeground(Color.BLACK);
        }
    }//GEN-LAST:event_addTxtFFocusGained

    private void addTxtFFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_addTxtFFocusLost
        if (String.valueOf(addTxtF.getText()).isEmpty()) {
            addTxtF.setText("Address");
            addTxtF.setForeground(Color.GRAY);
        }
    }//GEN-LAST:event_addTxtFFocusLost

    private void contTxtFFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_contTxtFFocusGained
        if (String.valueOf(contTxtF.getText()).equals("Contact Number")) {
            contTxtF.setText("");
            contTxtF.setForeground(Color.BLACK);
        }
    }//GEN-LAST:event_contTxtFFocusGained

    private void contTxtFFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_contTxtFFocusLost
        if (String.valueOf(contTxtF.getText()).isEmpty()) {
            contTxtF.setText("Contact Number");
            contTxtF.setForeground(Color.GRAY);
        }
    }//GEN-LAST:event_contTxtFFocusLost

    private void fnameTxtFFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fnameTxtFFocusGained
        if (String.valueOf(fnameTxtF.getText()).equals("First Name")) {
            fnameTxtF.setText("");
            fnameTxtF.setForeground(Color.BLACK);
        }
    }//GEN-LAST:event_fnameTxtFFocusGained

    private void fnameTxtFFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fnameTxtFFocusLost
        if (String.valueOf(fnameTxtF.getText()).isEmpty()) {
            fnameTxtF.setText("First Name");
            fnameTxtF.setForeground(Color.GRAY);
        }
    }//GEN-LAST:event_fnameTxtFFocusLost

    private void eaddTxtFFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_eaddTxtFFocusGained
        if (String.valueOf(eaddTxtF.getText()).equals("Email-Address")) {
            eaddTxtF.setText("");
            eaddTxtF.setForeground(Color.BLACK);
        }
    }//GEN-LAST:event_eaddTxtFFocusGained

    private void miTxtFFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_miTxtFFocusGained
        if (String.valueOf(miTxtF.getText()).equals("M.I")) {
            miTxtF.setText("");
            miTxtF.setForeground(Color.BLACK);
        }
    }//GEN-LAST:event_miTxtFFocusGained

    private void eaddTxtFFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_eaddTxtFFocusLost
        if (String.valueOf(eaddTxtF.getText()).isEmpty()) {
            eaddTxtF.setText("Email-Address");
            eaddTxtF.setForeground(Color.GRAY);
        }
    }//GEN-LAST:event_eaddTxtFFocusLost

    private void miTxtFFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_miTxtFFocusLost
        if (String.valueOf(miTxtF.getText()).isEmpty()) {
            miTxtF.setText("M.I");
            miTxtF.setForeground(Color.GRAY);
        }
    }//GEN-LAST:event_miTxtFFocusLost

    private void searchUserTxtFFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_searchUserTxtFFocusGained
        if (String.valueOf(searchUserTxtF.getText()).equals("Search")) {
            searchUserTxtF.setText("");
            searchUserTxtF.setForeground(Color.BLACK);
        }
    }//GEN-LAST:event_searchUserTxtFFocusGained

    private void searchUserTxtFFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_searchUserTxtFFocusLost
        if (String.valueOf(searchUserTxtF.getText()).isEmpty()) {
            searchUserTxtF.setText("Search");
            searchUserTxtF.setForeground(Color.GRAY);
        }
    }//GEN-LAST:event_searchUserTxtFFocusLost

    private void addUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addUserActionPerformed
        // TODO add your handling code here:
        // LocalDate currentDate = LocalDate.now();
        //java.sql.Date date = java.sql.Date.valueOf(currentDate);
        String lname = lnameTxtF.getText();
        String fname = fnameTxtF.getText();
        String midI = miTxtF.getText();
        String add = addTxtF.getText();
        String eAdd = eaddTxtF.getText();
        String stats = "Active";
        String fullName = lname + ", " + fname + " " + midI + ".";
        int id;
        // int con;

        if (!validateAllFields()) {
            return;
        }
        java.sql.Date date = java.sql.Date.valueOf(new SimpleDateFormat("yyyy-MM-dd").format(dateChooser.getDate()));

        try {
            id = Integer.parseInt(userIdTxtF.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid Id!! Please enter a number", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String contactNumber = contTxtF.getText().trim();

        if (!contactNumber.matches("\\d{11}")) {
            JOptionPane.showMessageDialog(this, "Invalid Contact Number!! It must be exactly 11 digits.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!isValidEmail(eAdd)) {
            JOptionPane.showMessageDialog(this, "Invalid Email!! Please enter a valid email address", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO userInfo(userId, lName, fName, mInitials, emailAdd, address, contactNum, HiredDate, Status) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");

            ps.setInt(1, id);
            ps.setString(2, lname);
            ps.setString(3, fname);
            ps.setString(4, midI);
            ps.setString(5, eAdd);
            ps.setString(6, add);
            ps.setString(7, contactNumber);
            ps.setDate(8, date);
            ps.setString(9, stats);
            ps.executeUpdate();
            modelUser.addRow(new Object[]{id, fullName, add, eAdd, contactNumber, date, stats});
            clearUser();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error inserting data: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_addUserActionPerformed

    private void userTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_userTableMouseClicked
        // TODO add your handling code here:
        int row = userTable.getSelectedRow();
        int id = (int) modelUser.getValueAt(row, 0);
        String query = "SELECT * FROM userInfo WHERE UserId = '" + id + "'";
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a user first.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            if (rs.next()) {

                String strgId = String.valueOf(rs.getInt("UserId"));
                String lname = rs.getString("lName");
                String fname = rs.getString(3);
                String midI = rs.getString(4);
                String eAdd = rs.getString(5);
                String add = rs.getString(6);
                String con = rs.getString(7);
                Date date = rs.getDate(8);
                String stat = rs.getString(9);

                // Set data to fields
                userIdTxtF.setText(strgId);
                lnameTxtF.setText(lname);
                fnameTxtF.setText(fname);
                miTxtF.setText(midI);
                eaddTxtF.setText(eAdd);
                addTxtF.setText(add);
                contTxtF.setText(con);
                dateChooser.setDate(date);
                statuTxtF.setText(stat);

                // Disable selected fields
                userIdTxtF.setEnabled(false);
                lnameTxtF.setEnabled(false);
                fnameTxtF.setEnabled(false);
                miTxtF.setEnabled(false);
                dateChooser.setEnabled(false);

                userIdTxtF.setForeground(Color.BLACK);
                lnameTxtF.setForeground(Color.BLACK);
                fnameTxtF.setForeground(Color.BLACK);
                miTxtF.setForeground(Color.BLACK);
                eaddTxtF.setForeground(Color.BLACK);
                addTxtF.setForeground(Color.BLACK);
                contTxtF.setForeground(Color.BLACK);
                dateChooser.setForeground(Color.BLACK);
                if (statuTxtF.getText().equals("Active")) {
                    statuTxtF.setForeground(Color.GREEN);
                } else {
                    statuTxtF.setForeground(Color.RED);
                }
                if (stat.equalsIgnoreCase("Active")) {
                    blockBtn.setText("BLOCK");
                } else if (stat.equalsIgnoreCase("Blocked")) {
                    blockBtn.setText("UNBLOCK");
                }

            }

        } catch (SQLException e) {

        }
    }//GEN-LAST:event_userTableMouseClicked

    private void updateUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateUserActionPerformed
        // TODO add your handling code here:
        int row = userTable.getSelectedRow();
        int id = (int) modelUser.getValueAt(row, 0);
        String strID = String.valueOf(id);
        String add = addTxtF.getText();
        String eAdd = eaddTxtF.getText();

        String contactNumber = contTxtF.getText().trim();

        if (!validateAllFields()) {
            return;
        }

        if (!contactNumber.matches("\\d{11}")) {
            JOptionPane.showMessageDialog(this, "Invalid Contact Number!! It must be exactly 11 digits.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!isValidEmail(eAdd)) {
            JOptionPane.showMessageDialog(this, "Invalid Email!! Please enter a valid email address", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            PreparedStatement ps = conn.prepareStatement("UPDATE userInfo SET emailAdd = ?, address = ?, contactNum = ? WHERE UserId = ?");
            ps.setString(1, eAdd);
            ps.setString(2, add);
            ps.setString(3, contactNumber);
            ps.setInt(4, id);

            ps.executeUpdate();

            modelUser.setValueAt(strID, row, 0);
            modelUser.setValueAt(eAdd, row, 3);
            modelUser.setValueAt(add, row, 2);
            modelUser.setValueAt(contactNumber, row, 2);

            JOptionPane.showMessageDialog(null, "Update Successfully", "Message", JOptionPane.INFORMATION_MESSAGE);
            clearUser();
        } catch (SQLException ex) {
            Logger.getLogger(Admin_Dashboard.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_updateUserActionPerformed

    private void deleteUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteUserActionPerformed
        // TODO add your handling code here:
        try {
            int row = userTable.getSelectedRow();
            int id = (int) modelUser.getValueAt(row, 0);
            int message = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete?", "Message", JOptionPane.YES_NO_OPTION);

            if (message == 0) {
                PreparedStatement ps = conn.prepareStatement("DELETE FROM userInfo WHERE UserId = ?");

                ps.setInt(1, id);

                ps.executeUpdate();
                modelUser.removeRow(row);
                JOptionPane.showMessageDialog(null, "Deleted Successfully", "Message", JOptionPane.INFORMATION_MESSAGE);
                clearUser();
            }
        } catch (SQLException ex) {
            Logger.getLogger(Admin_Dashboard.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_deleteUserActionPerformed

    private void blockBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_blockBtnActionPerformed
        // TODO add your handling code here:

        int row = userTable.getSelectedRow();
        if (row != -1) {
            String status = modelUser.getValueAt(row, 6).toString();
            if (status.equalsIgnoreCase("Active")) {
                blockBtn.setText("BLOCK");
            } else if (status.equalsIgnoreCase("Blocked")) {
                blockBtn.setText("UNBLOCK");
            }
        }

        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a user first.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int userId = (int) modelUser.getValueAt(row, 0);
        String currentStatus = modelUser.getValueAt(row, 6).toString();
        String newStatus = currentStatus.equalsIgnoreCase("Active") ? "Blocked" : "Active";

        // Confirmation dialog
        String action = newStatus.equals("Blocked") ? "block" : "unblock";
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to " + action + " this user?",
                "Confirm " + action.toUpperCase(),
                JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            PreparedStatement ps = conn.prepareStatement("UPDATE userInfo SET `Status` = ? WHERE UserId = ?");
            ps.setString(1, newStatus);
            ps.setInt(2, userId);
            ps.executeUpdate();

            String message = newStatus.equals("Blocked") ? "User has been BLOCKED." : "User has been UNBLOCKED.";
            JOptionPane.showMessageDialog(this, message, "Status Changed", JOptionPane.INFORMATION_MESSAGE);
            clearUser();
            dbManager.retrieveUsers(modelUser);
            blockBtn.setText("BLOCK/UNBLOCK");

        } catch (SQLException ex) {
            Logger.getLogger(Admin_Dashboard.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_blockBtnActionPerformed

    private void clrUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clrUserActionPerformed
        // TODO add your handling code here:
        clearUser();
    }//GEN-LAST:event_clrUserActionPerformed

    private void sortUsersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sortUsersActionPerformed
        // TODO add your handling code here:
        String selected = sortUsers.getSelectedItem().toString();
        loadUsersSorted(selected);
    }//GEN-LAST:event_sortUsersActionPerformed

    private void searchUserTxtFKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchUserTxtFKeyPressed
        // TODO add your handling code here:
        searchUserTxtF.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                String text = searchUserTxtF.getText();
                search(text);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                String text = searchUserTxtF.getText();
                search(text);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                //  search();
            }
        });

    }//GEN-LAST:event_searchUserTxtFKeyPressed

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.com$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean validateAllFields() {

        if (isFieldInvalid(userIdTxtF, "UserID")) {
            showError("Please enter a valid UserID.");
            return false;
        }
        if (isFieldInvalid(lnameTxtF, "Last Name")) {
            showError("Please enter a valid Last Name.");
            return false;
        }
        if (isFieldInvalid(fnameTxtF, "First Name")) {
            showError("Please enter a valid First Name.");
            return false;
        }
        if (isFieldInvalid(miTxtF, "M.I")) {
            showError("Please enter valid initials.");
            return false;
        }
        if (isFieldInvalid(addTxtF, "Address")) {
            showError("Please enter a valid Address.");
            return false;
        }
        if (isFieldInvalid(eaddTxtF, "Email Address")) {
            showError("Please enter a valid Email Address.");
            return false;
        }
        if (isFieldInvalid(contTxtF, "Contact Number")) {
            showError("Please enter a valid Contact Number.");
            return false;
        }
        if (dateChooser.getDate() == null) {
            showError("Please select a valid Date.");
            return false;
        }
        return true;
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Validation Error", JOptionPane.ERROR_MESSAGE);
    }

    private boolean isFieldInvalid(JTextField field, String placeholder) {
        String text = field.getText().trim();
        return text.isEmpty() || text.equalsIgnoreCase(placeholder);
    }

    private void clearUser() {
        userIdTxtF.setText(null);
        lnameTxtF.setText(null);
        fnameTxtF.setText(null);
        miTxtF.setText(null);
        eaddTxtF.setText(null);
        addTxtF.setText(null);
        contTxtF.setText(null);
        dateChooser.setDate(null);
        statuTxtF.setText(null);

        userIdTxtF.setEnabled(true);
        lnameTxtF.setEnabled(true);
        fnameTxtF.setEnabled(true);
        miTxtF.setEnabled(true);
        dateChooser.setEnabled(true);
    }

    //sorting method
    public void loadUsersSorted(String criteria) {
        String query = "SELECT * FROM userInfo";

        switch (criteria) {
            case "Employee Id":
                query += " ORDER BY UserId";
                break;
            case "Name":
                query += " ORDER BY lName, fName";
                break;
            case "Hired Date":
                query += " ORDER BY HiredDate";
                break;
            case "Status":
                query += " ORDER BY Status";
                break;
            default:
                // No sorting
                break;
        }

        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            // Clear table model first
            modelUser.setRowCount(0);
            
            while (rs.next()) {
                modelUser.addRow(new Object[]{
                    rs.getInt("UserId"),
                    rs.getString("lName") + ", " + rs.getString("fName") + " " + rs.getString("mInitials"),
                    rs.getString("address"),
                    rs.getString("emailAdd"),
                    rs.getString("contactNum"),
                    rs.getDate("HiredDate"),
                    rs.getString("Status")
                });
                

            }

        } catch (SQLException ex) {
            Logger.getLogger(Admin_Dashboard.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void loadProductsSorted(String criteria) {
    String query = "SELECT * FROM Product";

    switch (criteria) {
        case "Product ID":
            query += " ORDER BY ProductId";
            break;
        case "Product Name":
            query += " ORDER BY ProductName";
            break;
        case "Price":
            query += " ORDER BY Price";
            break;
        default:
            // No sorting
            break;
    }

    try {
        PreparedStatement ps = conn.prepareStatement(query);
        ResultSet rs = ps.executeQuery();

        // Clear existing rows
        modelProduct.setRowCount(0);

        while (rs.next()) {
            modelProduct.addRow(new Object[]{
                rs.getInt("ProductId"),
                rs.getString("ProductName"),
                rs.getDouble("Price"),
                rs.getInt("CategoryId"),
                rs.getBytes("ProductImage")
            });
        }

    } catch (SQLException ex) {
        ex.printStackTrace();
    }
}


    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;

                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Admin_Dashboard.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Admin_Dashboard.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Admin_Dashboard.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Admin_Dashboard.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Admin_Dashboard().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel accBody;
    private javax.swing.JLabel accLbl;
    private javax.swing.JPanel accPnl;
    private javax.swing.JButton addBtn;
    private javax.swing.JTextField addTxtF;
    private javax.swing.JButton addUser;
    private javax.swing.JPanel averageTPnl;
    private javax.swing.JPanel bannerPnl;
    private javax.swing.JButton blockBtn;
    private javax.swing.JPanel bodyPnl;
    private javax.swing.JComboBox<String> categoryCB;
    private javax.swing.JPanel categoryPnl;
    private javax.swing.JScrollPane categoryScrll;
    private javax.swing.JButton clearOrdersBtn;
    private javax.swing.JButton clrBtn;
    private javax.swing.JButton clrUser;
    private javax.swing.JTextField contTxtF;
    private com.toedter.calendar.JDateChooser dateChooser;
    private javax.swing.JButton deleteBtn;
    private javax.swing.JButton deleteUser;
    private javax.swing.JTextField eaddTxtF;
    private javax.swing.JComboBox<String> filterCol;
    private javax.swing.JTextField fnameTxtF;
    private javax.swing.JLabel headerLbl;
    private javax.swing.JLabel headerTitle;
    private javax.swing.JPanel homeBody;
    private javax.swing.JLabel homeLbl;
    private javax.swing.JPanel homePnl;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLayeredPane layeredPane;
    private javax.swing.JTextField lnameTxtF;
    private javax.swing.JLabel logoutLbl;
    private javax.swing.JPanel logoutPnl;
    private javax.swing.JPanel menuBody;
    private javax.swing.JLabel menuLbl;
    private javax.swing.JPanel menuPnl;
    private javax.swing.JTextField miTxtF;
    private javax.swing.JTextField nameTxtF;
    private javax.swing.JPanel navigationPnl1;
    private javax.swing.JScrollPane orderScroll;
    private javax.swing.JPanel ordersBdy;
    private javax.swing.JPanel ordersTPnl;
    public static javax.swing.JPanel panelMenu;
    private javax.swing.JPanel posBody;
    private javax.swing.JLabel posLbl;
    private javax.swing.JPanel posPnl;
    private javax.swing.JTextField priceTxtF;
    private javax.swing.JTextField priceTxtF1;
    private javax.swing.JLabel prodIdLbl;
    private javax.swing.JLabel productImg;
    private javax.swing.JTable productsTbl;
    private javax.swing.JPanel salesBody;
    private javax.swing.JLabel salesLbl;
    private javax.swing.JPanel salesPnl;
    private javax.swing.JPanel salesTPnl;
    private javax.swing.JScrollPane scrollPane1;
    private javax.swing.JComboBox<String> searchCatCB;
    private javax.swing.JTextField searchF;
    private javax.swing.JTextField searchTxtF;
    private javax.swing.JTextField searchUserTxtF;
    private javax.swing.JPanel slideshowPnl;
    private javax.swing.JComboBox<String> sortProd;
    private javax.swing.JComboBox<String> sortUsers;
    private javax.swing.JLabel statuTxtF;
    private javax.swing.JLabel subTValue;
    private javax.swing.JLabel taxValue;
    private javax.swing.JLabel totalValue;
    private javax.swing.JLabel transactionLbl;
    private javax.swing.JPanel transactionPnl;
    private javax.swing.JButton updateBtn;
    private javax.swing.JButton updateUser;
    private javax.swing.JTextField userIdTxtF;
    private javax.swing.JTable userTable;
    // End of variables declaration//GEN-END:variables
}
