/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package splashScreen;

import Objects.Category;
import Objects.Product;
import Objects.Sizes;
import database.DatabaseManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import openingScreen.opening;
import static openingScreen.opening.categories;
import static openingScreen.opening.dbManager;
import static openingScreen.opening.products;
import static openingScreen.opening.sizes;

/**
 *
 * @author Jealian Abby Sulit
 */
public class splashScreen extends javax.swing.JFrame {

    Connection con;
    PreparedStatement ps;
    ResultSet rs;
    String query;
    DatabaseManager m = new DatabaseManager();

    public splashScreen() {
        initComponents();
        //Image icon = Toolkit.getDefaultToolkit().getImage("C:\\Users\\Jealian Abby Sulit\\Documents\\NetBeansProjects\\JavaBeans_POS\\src\\Images\\logo.png");
        //this.setIconImage(icon);

        ImageIcon imgi = new ImageIcon("src/Images/logo.png");
        Image img = imgi.getImage();
        Image img2 = img.getScaledInstance(logoLbl.getWidth(), logoLbl.getHeight(), Image.SCALE_SMOOTH);
        imgi = new ImageIcon(img2);
        logoLbl.setIcon(imgi);

        con = dbManager.connectDatabase();
        dbManager.createCategoryTable();
        dbManager.createProductTable();
        dbManager.createSizeTable();
        dbManager.createUserInfoTable();
        initializeSizes();
        initializeCategory();
        initializeProducts();
    }

    public void initializeSizes() {
        dbManager.deleteAllSizes();
        String size[] = {"Tall", "Grande", "Venti"};
        String oz[] = {"12 oz", "16 oz", "24 oz"};
        double rate[] = {30.20, 40.30, 50.50};

        for (int i = 0; i < size.length; i++) {
            dbManager.addSizes(i + 1, size[i], oz[i], rate[i]);
            sizes.add(new Sizes(i + 1, size[i], oz[i], rate[i]));
        }

    }

    public void initializeCategory() {
        dbManager.deleteAllCategories();
        String[] category = {"Hot Coffee", "Iced Coffee", "Hot Tea", "Iced Tea", "Frappucino", "Refreshers",
            "Breakfast", "Bakery", "Pasta"};

        for (int i = 0; i < category.length; i++) {
            dbManager.addCategory(i + 1, category[i]);
            categories.add(new Category(i + 1, category[i]));
        }

    }

    public void initializeProducts() {
        dbManager.deleteAllProducts();
        String[] imgPaths = {
            //hot coffee
            // hot coffee
            "src/Images/products/hotcoffee/blonderoast.png",
            "src/Images/products/hotcoffee/cafeamericano.png",
            "src/Images/products/hotcoffee/cafemisto.png",
            "src/Images/products/hotcoffee/cafemocha.png",
            "src/Images/products/hotcoffee/cappuccino.png",
            "src/Images/products/hotcoffee/cinnamondolce.png",
            "src/Images/products/hotcoffee/espressomacchiato.png",
            "src/Images/products/hotcoffee/flatwhite.png",
            // cold coffee
            "src/Images/products/coldcoffee/chocolatecreambrew.png",
            "src/Images/products/coldcoffee/icedcaramel.png",
            "src/Images/products/coldcoffee/icedcinnamon.png",
            "src/Images/products/coldcoffee/icedcoffee.png",
            "src/Images/products/coldcoffee/icedflatwhite.png",
            "src/Images/products/coldcoffee/nitrocoldbrew.png",
            "src/Images/products/coldcoffee/nondairyvanilla.png",
            "src/Images/products/coldcoffee/saltedcaramelbrew.png",
            // hot tea
            "src/Images/products/hottea/chailatte.png",
            "src/Images/products/hottea/earlgrey.png",
            "src/Images/products/hottea/honeycitrusmint.png",
            "src/Images/products/hottea/matchalatte.png",
            "src/Images/products/hottea/mintmajesty.png",
            "src/Images/products/hottea/royalenglish.png",
            // cold tea
            "src/Images/products/coldtea/icedblack.png",
            "src/Images/products/coldtea/icedchai.png",
            "src/Images/products/coldtea/icedcherrychai.png",
            "src/Images/products/coldtea/icedmatcha.png",
            "src/Images/products/coldtea/icedpassion.png",
            "src/Images/products/coldtea/icedpeach.png",
            // frappuccino
            "src/Images/products/frappucino/caramelfrappe.png",
            "src/Images/products/frappucino/mochacookie.png",
            "src/Images/products/frappucino/lavendercreme.png",
            "src/Images/products/frappucino/matchacreme.png",
            "src/Images/products/frappucino/strawberrycreme.png",
            "src/Images/products/frappucino/pistachiofrappe.png",
            // refreshers
            "src/Images/products/refreshers/blackberrysage.png",
            "src/Images/products/refreshers/dragondrink.png",
            "src/Images/products/refreshers/mangodragonfruit.png",
            "src/Images/products/refreshers/midnight.png",
            "src/Images/products/refreshers/pinkdrink.png",
            "src/Images/products/refreshers/strawberryacai.png",
            // breakfast
            "src/Images/products/bfast/eggpesto.png",
            "src/Images/products/bfast/sausagecheddar.png",
            "src/Images/products/bfast/doublesmokedbacon.png",
            "src/Images/products/bfast/turkeybacon.png",
            "src/Images/products/bfast/baconwrap.png",
            "src/Images/products/bfast/spenachfeta.png",
            // bakery
            "src/Images/products/bakery/cheesedanish.png",
            "src/Images/products/bakery/turkeysage.png",
            "src/Images/products/bakery/bakedapplecroissant.png",
            "src/Images/products/bakery/chococroissant.png",
            "src/Images/products/bakery/cinnamoncoffee.png",
            "src/Images/products/bakery/icedlemon.png",
            "src/Images/products/bakery/petitevanilla.png",
            "src/Images/products/bakery/blueberrystreusel.png",
            "src/Images/products/bakery/plainbagel.png",
            "src/Images/products/bakery/everythingbagel.png",
            // pasta
            "src/Images/products/pasta/birdsnest.png",
            "src/Images/products/pasta/chickenalfredo.png",
            "src/Images/products/pasta/chickenpesto.png",
            "src/Images/products/pasta/pestocarbonara.png",
            "src/Images/products/pasta/spanishsardines.png",
            "src/Images/products/pasta/trufflecarbonara.png"
        };

        String[] names = {"Blonde Roast", "Cafe Americano", "Cafe Misto", "Cafe Mocha", "Cappuccino", "Cinnamon Dolce", "Espresso Macchiato", "Flat White",
            "Chocolate Cream Brew", "Iced Caramel Macchiato", "Iced Cinnamon Latte", "Iced Coffee", "Iced Flat White", "Nitro Cold Brew", "Non-Dairy Vanilla Brew", "Salted Caramel Brew",
            "Chai Latte", "Earl Grey", "Honey Citrus Mint", "Matcha Latte", "Mint Majesty", "Royal English",
            "Iced Black Tea", "Iced Chai Latte", "Iced Cherry Chai Latte", "Iced Matcha Latte", "Iced Passion Tango Tea", "Iced Peach Green Tea",
            "Salted Caramel Frappe", "Mocha Cookie Frappe", "Lavender Creme", "Matcha Creme", "Strawberry Creme", "Pistachio Frappe",
            "Blackberry Sage", "Dragon Drink", "Mango Dragon Fruit", "Midnight Drink", "Pink Drink", "Strawberry Acai",
            "Egg Pesto Sandwich", "Sausage Cheddar Sandwich", "Double Smoked Bacon", "Turkey Bacon Sandwich", "Bacon & Sausage Wrap", "Spinach & Feta Wrap",
            "Cheese Danish", "Turkey Sage Danish", "Baked Apple Croissant", "Chocolate Croissant", "Cinnamon Coffee Cake", "Iced Lemon Loaf", "Petite Vanilla", "Blueberry Streusel Muffin", "Plain Bagel", "Everything Bagel",
            "Bird's Nest Carbonara", "Chicken Alfredo", "Chicken Pesto", "Pesto Carbonara", "Spanish Sardines and Tomatoes", "Truffle Carbonara"};

        double[] prices = {130.20, 100.00, 135.20, 140.00, 145.20, 140.00, 130.20, 120.10,
            145.30, 145.30, 150.00, 120.20, 130.20, 140.10, 135.00, 135.00,
            130.20, 100.00, 135.20, 140.00, 130.20, 120.10,
            140.20, 110.00, 125.20, 130.00, 140.20, 145.10,
            150.10, 135.20, 140.20, 135.20, 140.00, 155.20,
            140.00, 130.00, 135.00, 145.00, 120.20, 135.00,
            150.50, 150.00, 160.20, 170.00, 145.10, 135.10,
            120.00, 130.00, 100.00, 90.00, 95.00, 70.00, 75.00, 80.20, 60.00, 70.00,
            225.50, 230.00, 250.30, 260.20, 270.30, 280.00};
        int[] catIds = {1, 1, 1, 1, 1, 1, 1, 1,
            2, 2, 2, 2, 2, 2, 2, 2,
            3, 3, 3, 3, 3, 3,
            4, 4, 4, 4, 4, 4,
            5, 5, 5, 5, 5, 5,
            6, 6, 6, 6, 6, 6,
            7, 7, 7, 7, 7, 7,
            8, 8, 8, 8, 8, 8, 8, 8, 8, 8,
            9, 9, 9, 9, 9, 9};

        for (int i = 0; i < imgPaths.length; i++) {
            byte[] imageBytes = fileToBytes(imgPaths[i]); // convert path to byte[]

            dbManager.addProduct(1000 + i, names[i], prices[i], catIds[i], imageBytes); // use byte[]
            products.add(new Product(1000 + i, names[i], prices[i], catIds[i], imageBytes)); // use byte[]
        }
    }

    private byte[] fileToBytes(String filePath) {
        try {
            return Files.readAllBytes(Path.of(filePath));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        progressBar1 = new javax.swing.JProgressBar();
        loadingTxt = new javax.swing.JLabel();
        loadingValue = new javax.swing.JLabel();
        logoLbl = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(204, 153, 0));
        jPanel1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 102, 0), 5, true));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel1.add(progressBar1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 340, 350, 10));

        loadingTxt.setFont(new java.awt.Font("Serif", 1, 13)); // NOI18N
        loadingTxt.setForeground(new java.awt.Color(51, 51, 0));
        loadingTxt.setText("Text");
        jPanel1.add(loadingTxt, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 320, 270, -1));

        loadingValue.setFont(new java.awt.Font("Serif", 1, 13)); // NOI18N
        loadingValue.setForeground(new java.awt.Color(51, 51, 0));
        loadingValue.setText("%");
        jPanel1.add(loadingValue, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 340, 40, -1));
        jPanel1.add(logoLbl, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 100, 230, 200));

        jLabel2.setFont(new java.awt.Font("Serif", 3, 36)); // NOI18N
        jLabel2.setText("Java Beans Cafe");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 40, 280, -1));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 420, 390));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

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
            java.util.logging.Logger.getLogger(splashScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(splashScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(splashScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(splashScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        splashScreen s = new splashScreen();
        s.setVisible(true);

        for (int i = 0; i <= 101; i++) {
            try {
                Thread.sleep(100);//humihinto para makita loop in 100 milliseconds
            } catch (InterruptedException ex) {
                Logger.getLogger(splashScreen.class.getName()).log(Level.SEVERE, null, ex);
            }
            s.loadingValue.setText(i + "%");

            if (i == 10) {
                s.loadingTxt.setText("Turning on...");
            }

            if (i == 40) {
                s.loadingTxt.setText("Preparing the POS...");
            }

            if (i == 80) {
                s.loadingTxt.setText("Please wait...");
            }

            if (i == 100) {
                s.loadingTxt.setText("WELCOME TO JAVA BEANS!");
            }

            s.progressBar1.setValue(i);
        }
        s.dispose();
        opening os;
        os = new opening();
        os.setVisible(true);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel loadingTxt;
    private javax.swing.JLabel loadingValue;
    private javax.swing.JLabel logoLbl;
    private javax.swing.JProgressBar progressBar1;
    // End of variables declaration//GEN-END:variables
}
