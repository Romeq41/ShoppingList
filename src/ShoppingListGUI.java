import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ShoppingListGUI {
    private final JFrame frame;
    private Map<String, List<Product>> shoppingList;

    public ShoppingListGUI() {
        frame = new JFrame("Shopping List");
        frame.setSize(800, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setResizable(false);

        File categoriesFile = new File("categories.txt");
        if (!categoriesFile.exists()) {
            JOptionPane.showMessageDialog(frame, "No file with the list of products. Prepare a file named categories.txt and fill it in the following way:\nCategory:Product\nCategory:Product\n");
            return;
        }

        try {
            shoppingList = ShoppingListToolsGUI.readCategoryFile("list.txt");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error: " + e.getMessage());
        }

        JLabel titleLabel = new JLabel("Shopping List");
        titleLabel.setBounds(150, 10, 100, 20);
        frame.add(titleLabel);


        JLabel titleLabel2 = new JLabel("Output");
        titleLabel2.setBounds(530, 10, 100, 20);
        frame.add(titleLabel2);

        JTextArea outputField = new JTextArea();
        outputField.setEditable(false);
        JScrollPane outputScrollPane = new JScrollPane(outputField);
        outputScrollPane.setBounds(400, 50, 300, 150);
        frame.add(outputScrollPane);

        JButton addButton = new JButton("Add Product");
        addButton.setBounds(50, 50, 120, 30);
        addButton.addActionListener(e -> addProductDialog("categories.txt"));
        frame.add(addButton);

        JButton printAllButton = new JButton("Print List");
        printAllButton.setBounds(200, 50, 120, 30);
        printAllButton.addActionListener(e -> ShoppingListToolsGUI.printAllProductsGUI(shoppingList, outputField));
        frame.add(printAllButton);

        JButton printSpecifiedCategoryButton = new JButton("Print Category");
        printSpecifiedCategoryButton.setBounds(50, 100, 120, 30);
        printSpecifiedCategoryButton.addActionListener(e -> printCategoryDialog(shoppingList, outputField));
        frame.add(printSpecifiedCategoryButton);

        JButton deleteAllProductsFromShoppingListButton = new JButton("Delete all");
        deleteAllProductsFromShoppingListButton.setBounds(200, 100, 120, 30);
        deleteAllProductsFromShoppingListButton.addActionListener(e -> ShoppingListToolsGUI.removeAllProductsGUI(shoppingList));
        frame.add(deleteAllProductsFromShoppingListButton);

        JButton deleteSpecifiedProductButton = new JButton("Delete product");
        deleteSpecifiedProductButton.setBounds(50, 150, 120, 30);
        deleteSpecifiedProductButton.addActionListener(e -> deleteProductDialog(shoppingList));
        frame.add(deleteSpecifiedProductButton);

        JButton saveListButton = new JButton("Save list");
        saveListButton.setBounds(200, 150, 120, 30);
        saveListButton.addActionListener(e -> ShoppingListToolsGUI.saveListGUI(shoppingList, "list.txt"));
        frame.add(saveListButton);

        JButton deleteAllProductsFromSpecifiedCategoryButton = new JButton("Delete category");
        deleteAllProductsFromSpecifiedCategoryButton.setBounds(190, 200, 150, 30);
        deleteAllProductsFromSpecifiedCategoryButton.addActionListener(e -> deleteCategoryDialog(shoppingList));
        frame.add(deleteAllProductsFromSpecifiedCategoryButton);

        JButton modifyListButton = new JButton("Modify template");
        modifyListButton.setBounds(30, 200, 150, 30);
        modifyListButton.addActionListener(e -> showModifyCategoryDialog("categories.txt"));
        frame.add(modifyListButton);


        frame.setVisible(true);
    }

    public static void runGUI() {
        SwingUtilities.invokeLater(ShoppingListGUI::new);
    }

    private void addProductToTemplateFileDialog(String fileName) {
        JDialog dialog = new JDialog(frame, "Add Category Template List", true);
        dialog.setLayout(null);
        dialog.setResizable(false);

        JLabel titleLabel = new JLabel("Enter Product details: ");
        titleLabel.setBounds(125, 25, 150, 20);
        dialog.add(titleLabel);

        JLabel categoryLabel = new JLabel("Category:");
        categoryLabel.setBounds(40, 50, 75, 20);
        dialog.add(categoryLabel);

        JTextArea categoryNameTextArea = new JTextArea();
        categoryNameTextArea.setBounds(100, 50, 175, 20);
        dialog.add(categoryNameTextArea);

        JLabel productLabel = new JLabel("Product:");
        productLabel.setBounds(40, 75, 75, 20);
        dialog.add(productLabel);

        JTextArea productNameTextArea = new JTextArea();
        productNameTextArea.setBounds(100, 75, 175, 20);
        dialog.add(productNameTextArea);

        JLabel unitLabel = new JLabel("Units:");
        unitLabel.setBounds(40, 100, 75, 20);
        dialog.add(unitLabel);

        JTextArea unitsTextArea = new JTextArea();
        unitsTextArea.setBounds(100, 100, 175, 20);
        dialog.add(unitsTextArea);

        JLabel fileContentsLabel = new JLabel("Contents of " + fileName + ":");
        fileContentsLabel.setBounds(320, 25, 200, 10);
        dialog.add(fileContentsLabel);

        JTextArea categoriesAndProductsOutputTextField = new JTextArea();
        categoriesAndProductsOutputTextField.setEditable(false);
        JScrollPane categoriesAndProductsOutputScrollPane = new JScrollPane(categoriesAndProductsOutputTextField);
        categoriesAndProductsOutputScrollPane.setBounds(320, 40, 250, 125);
        dialog.add(categoriesAndProductsOutputScrollPane);

        fillOutputCategoryTextArea(fileName,categoriesAndProductsOutputTextField);

        JButton addButton = new JButton("Add");
        addButton.setBounds(175, 125, 100, 30);
        addButton.addActionListener(e -> {
            ShoppingListToolsGUI.addProductToTemplateFileGUI(fileName, categoryNameTextArea, productNameTextArea, unitsTextArea);
            dialog.dispose();
        });
        dialog.add(addButton);

        dialog.setSize(600, 225);
        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }

    private void showModifyCategoryDialog(String fileName) {
        JDialog dialog = new JDialog(frame, "Modify Category Template List", true);
        dialog.setLayout(null);
        dialog.setResizable(false);

        JLabel titleLabel = new JLabel("Choose Action");
        titleLabel.setBounds(150, 25, 100, 20);
        dialog.add(titleLabel);

        JButton addButton = new JButton("Add");
        addButton.setBounds(75, 50, 100, 30);
        addButton.addActionListener(e -> {
            addProductToTemplateFileDialog(fileName);
            dialog.dispose();
        });
        dialog.add(addButton);

        JButton deleteButton = new JButton("Delete");
        deleteButton.setBounds(200, 50, 100, 30);
        deleteButton.addActionListener(e -> {
            deleteProductFromTemplateFileDialog();
            dialog.dispose();
        });
        dialog.add(deleteButton);

        dialog.setSize(400, 150);
        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }

    private void addProductDialog(String fileName) {
        JDialog dialog = new JDialog(frame, "Add product to shopping list", true);
        dialog.setLayout(null);
        dialog.setResizable(false);

        JLabel titleLabel = new JLabel("Enter product details: ");
        titleLabel.setBounds(125, 25, 150, 20);
        dialog.add(titleLabel);

        JLabel categoryLabel = new JLabel("Category:");
        categoryLabel.setBounds(40, 50, 75, 20);
        dialog.add(categoryLabel);

        JTextArea categoryNameTextArea = new JTextArea();
        categoryNameTextArea.setBounds(100, 50, 175, 20);
        dialog.add(categoryNameTextArea);

        JLabel productLabel = new JLabel("Product:");
        productLabel.setBounds(40, 75, 75, 20);
        dialog.add(productLabel);

        JTextArea productNameTextArea = new JTextArea();
        productNameTextArea.setBounds(100, 75, 175, 20);
        dialog.add(productNameTextArea);

        JLabel quantityLabel = new JLabel("Quantity:");
        quantityLabel.setBounds(40, 100, 75, 20);
        dialog.add(quantityLabel);

        JLabel templateTextAreaLabel = new JLabel("Contents of " + fileName + ":");
        templateTextAreaLabel.setBounds(320, 25, 200, 10);
        dialog.add(templateTextAreaLabel);

        JTextArea categoriesAndProductsOutputTextField = new JTextArea();
        categoriesAndProductsOutputTextField.setEditable(false);
        JScrollPane categoriesAndProductsOutputScrollPane = new JScrollPane(categoriesAndProductsOutputTextField);
        categoriesAndProductsOutputScrollPane.setBounds(320, 40, 250, 125);
        dialog.add(categoriesAndProductsOutputScrollPane);

        fillOutputCategoryTextArea(fileName,categoriesAndProductsOutputTextField);

        JTextArea quantityTextArea = new JTextArea();
        quantityTextArea.setBounds(100, 100, 175, 20);
        dialog.add(quantityTextArea);

        JButton addButton = new JButton("Add");
        addButton.setBounds(175, 125, 100, 30);
        addButton.addActionListener(e -> {

            ShoppingListToolsGUI.addProductGUI(shoppingList, categoryNameTextArea, productNameTextArea, quantityTextArea);
            dialog.dispose();
        });
        dialog.add(addButton);

        dialog.setSize(600, 250);
        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }

    private void fillOutputCategoryTextArea(String fileName, JTextArea categoriesAndProductsOutputTextField){
        Map<String, List<Product>> fileData;

        try {
            fileData = ShoppingListToolsGUI.readCategoryFile(fileName);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (fileData != null){
            ShoppingListToolsGUI.printCategoryFile(fileData, categoriesAndProductsOutputTextField, PrintOption.All);
        }
    }

    private void printCategoryDialog(Map<String, List<Product>> shoppingList, JTextArea outputTextArea) {
        JDialog dialog = new JDialog(frame, "Print Category", true);
        dialog.setLayout(null);
        dialog.setResizable(false);

        JLabel titleLabel = new JLabel("Enter category");
        titleLabel.setBounds(115, 25, 175, 20);
        dialog.add(titleLabel);

        JLabel categoryLabel = new JLabel("Category:");
        categoryLabel.setBounds(40, 50, 75, 20);
        dialog.add(categoryLabel);

        JTextArea categoryNameTextArea = new JTextArea();
        categoryNameTextArea.setBounds(100, 50, 175, 20);
        dialog.add(categoryNameTextArea);

        JTextArea categoriesAndProductsOutputTextField = new JTextArea();
        categoriesAndProductsOutputTextField.setEditable(false);

        JScrollPane categoriesAndProductsOutputScrollPane = new JScrollPane(categoriesAndProductsOutputTextField);
        categoriesAndProductsOutputScrollPane.setBounds(320, 25, 250, 100);

        dialog.add(categoriesAndProductsOutputScrollPane);

        ShoppingListToolsGUI.printCategoryFile(shoppingList, categoriesAndProductsOutputTextField, PrintOption.CATEGORIES);

        JButton addButton = new JButton("Print");
        addButton.setBounds(225, 150, 100, 30);
        addButton.addActionListener(e -> {
            ShoppingListToolsGUI.printCategoryGUI(shoppingList, categoryNameTextArea, outputTextArea);
            dialog.dispose();
        });
        dialog.add(addButton);

        dialog.setSize(600, 250);
        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }

    private void deleteCategoryDialog(Map<String, List<Product>> shoppingList) {
        JDialog dialog = new JDialog(frame, "Delete Category", true);
        dialog.setLayout(null);
        dialog.setResizable(false);

        JLabel titleLabel = new JLabel("Enter category");
        titleLabel.setBounds(115, 25, 175, 20);
        dialog.add(titleLabel);

        JLabel categoryLabel = new JLabel("Category:");
        categoryLabel.setBounds(40, 50, 75, 20);
        dialog.add(categoryLabel);

        JTextArea categoryNameTextArea = new JTextArea();
        categoryNameTextArea.setBounds(100, 50, 175, 20);
        dialog.add(categoryNameTextArea);


        JTextArea categoriesAndProductsOutputTextField = new JTextArea();
        categoriesAndProductsOutputTextField.setEditable(false);

        JScrollPane categoriesAndProductsOutputScrollPane = new JScrollPane(categoriesAndProductsOutputTextField);
        categoriesAndProductsOutputScrollPane.setBounds(320, 25, 250, 100);
        dialog.add(categoriesAndProductsOutputScrollPane);

        ShoppingListToolsGUI.printCategoryFile(shoppingList, categoriesAndProductsOutputTextField, PrintOption.CATEGORIES);

        JButton addButton = new JButton("Delete");
        addButton.setBounds(225, 150, 100, 30);
        addButton.addActionListener(e -> {
            ShoppingListToolsGUI.removeAllProductsByCategory(shoppingList, categoryNameTextArea);
            dialog.dispose();
        });
        dialog.add(addButton);

        dialog.setSize(600, 250);
        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }

    private void deleteProductDialog(Map<String, List<Product>> shoppingList) {
        JDialog dialog = new JDialog(frame, "Remove product from shopping list", true);
        dialog.setLayout(null);
        dialog.setResizable(false);

        JLabel titleLabel = new JLabel("Enter info about the product");
        titleLabel.setBounds(115, 25, 175, 20);
        dialog.add(titleLabel);

        JLabel categoryLabel = new JLabel("Category:");
        categoryLabel.setBounds(40, 50, 75, 20);
        dialog.add(categoryLabel);

        JTextArea categoryNameTextArea = new JTextArea();
        categoryNameTextArea.setBounds(100, 50, 175, 20);
        dialog.add(categoryNameTextArea);

        JLabel productLabel = new JLabel("Product:");
        productLabel.setBounds(40, 75, 75, 20);
        dialog.add(productLabel);

        JTextArea productNameTextArea = new JTextArea();
        productNameTextArea.setBounds(100, 75, 175, 20);
        dialog.add(productNameTextArea);

        JLabel quantityLabel = new JLabel("Quantity:");
        quantityLabel.setBounds(40, 100, 75, 20);
        dialog.add(quantityLabel);

        JTextArea quantityTextArea = new JTextArea();
        quantityTextArea.setBounds(100, 100, 175, 20);
        dialog.add(quantityTextArea);

        JTextArea categoriesAndProductsOutputTextField = new JTextArea();
        categoriesAndProductsOutputTextField.setEditable(false);
        JScrollPane categoriesAndProductsOutputScrollPane = new JScrollPane(categoriesAndProductsOutputTextField);
        categoriesAndProductsOutputScrollPane.setBounds(320, 25, 250, 100);
        dialog.add(categoriesAndProductsOutputScrollPane);


        ShoppingListToolsGUI.printAllProductsGUI(shoppingList, categoriesAndProductsOutputTextField);

        JButton addButton = new JButton("Delete");
        addButton.setBounds(225, 150, 100, 30);
        addButton.addActionListener(e -> {

            ShoppingListToolsGUI.deleteProductGUI(shoppingList, categoryNameTextArea, productNameTextArea, quantityTextArea);
            dialog.dispose();
        });
        dialog.add(addButton);

        dialog.setSize(600, 250);
        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }


    private void deleteProductFromTemplateFileDialog() {
        JDialog dialog = new JDialog(frame, "Remove product from template file", true);

        dialog.setLayout(null);
        dialog.setResizable(false);

        JLabel titleLabel = new JLabel("Enter info about the product");
        titleLabel.setBounds(115, 25, 175, 20);
        dialog.add(titleLabel);

        JLabel categoryLabel = new JLabel("Category:");
        categoryLabel.setBounds(40, 50, 75, 20);
        dialog.add(categoryLabel);

        JTextArea categoryNameTextArea = new JTextArea();
        categoryNameTextArea.setBounds(100, 50, 175, 20);
        dialog.add(categoryNameTextArea);

        JLabel productLabel = new JLabel("Product:");
        productLabel.setBounds(40, 75, 75, 20);
        dialog.add(productLabel);

        JTextArea productNameTextArea = new JTextArea();
        productNameTextArea.setBounds(100, 75, 175, 20);
        dialog.add(productNameTextArea);

        JLabel templateTextAreaLabel = new JLabel("Contents of " + "categories.txt" + ":");
        templateTextAreaLabel.setBounds(320, 25, 250, 10);
        dialog.add(templateTextAreaLabel);

        JTextArea categoriesAndProductsOutputTextField = new JTextArea();
        categoriesAndProductsOutputTextField.setEditable(false);
        JScrollPane categoriesAndProductsOutputScrollPane = new JScrollPane(categoriesAndProductsOutputTextField);
        categoriesAndProductsOutputScrollPane.setBounds(320, 50, 250, 90);
        dialog.add(categoriesAndProductsOutputScrollPane);

        fillOutputCategoryTextArea("categories.txt",categoriesAndProductsOutputTextField);

        JButton addButton = new JButton("Delete");
        addButton.setBounds(225, 150, 100, 30);
        addButton.addActionListener(e -> {
            ShoppingListToolsGUI.removeProductFromTemplateFileGUI("categories.txt", categoryNameTextArea, productNameTextArea);
            dialog.dispose();
        });
        dialog.add(addButton);

        dialog.setSize(600, 250);
        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }
}
