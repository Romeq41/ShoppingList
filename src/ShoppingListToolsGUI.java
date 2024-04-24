import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.List;
import java.util.*;

public class ShoppingListToolsGUI {
    /**
     * Saves the shopping list to a file.
     *
     * @param shoppingList the current shopping list to be saved
     * @param fileName     the name of the file to save the shopping list to
     */
    public static void saveListGUI(Map<String, List<Product>> shoppingList, String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (Map.Entry<String, List<Product>> entry : shoppingList.entrySet()) {
                String key = entry.getKey();
                List<Product> values = entry.getValue();
                for (Product value : values) {
                    writer.write(key + ":" + value.productName + ":" + value.Quantity + ":" + value.units + "\n");
                }
            }
            JOptionPane.showMessageDialog(null, "File saved successfully at: " + fileName, "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error saving file", "File save error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Deletes a specific quantity of a product from the shopping list.
     * If the quantity from quantityTextArea is greater than or equal to the current quantity of the product in the shopping list, the product is removed from the list.
     *
     * @param shoppingList         the current shopping list
     * @param categoryNameTextArea the text area containing the category name
     * @param productNameTextArea  the text area containing the product name
     * @param quantityTextArea     the text area containing the quantity
     */
    public static void deleteProductGUI(Map<String, List<Product>> shoppingList, JTextArea categoryNameTextArea, JTextArea productNameTextArea, JTextArea quantityTextArea) {
        if (shoppingList.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Shopping list is empty", "Shopping list error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String categoryInput = categoryNameTextArea.getText().trim();
        String category = getCategoryIfExists(shoppingList, categoryInput);

        if (category == null) {
            JOptionPane.showMessageDialog(null, "Category \"" + categoryInput + "\" does not exist", "Category error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String productNameInput = productNameTextArea.getText().trim();
        List<Product> productList = shoppingList.get(category);

        Product productToBeRemoved = getProductIfExistsFromSpecificCategory(productList, productNameInput);

        if (productToBeRemoved == null) {
            JOptionPane.showMessageDialog(null, "Product \"" + productNameInput + "\" does not exist", "Product error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            double quantity = Double.parseDouble(quantityTextArea.getText().trim());

            if (quantity < 0) {
                JOptionPane.showMessageDialog(null, "Invalid quantity value", "Quantity error", JOptionPane.ERROR_MESSAGE);
            } else if (quantity >= productToBeRemoved.Quantity) {
                productList.remove(productToBeRemoved);
                if (productList.isEmpty()) {
                    shoppingList.remove(category);
                }
                JOptionPane.showMessageDialog(null, "Product \"" + productNameInput + "\" removed from category \"" + category + "\"", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                productToBeRemoved.Quantity -= quantity;
                JOptionPane.showMessageDialog(null, "Removed " + quantity + " units of \"" + productNameInput + "\" from category \"" + category + "\"", "Success", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid quantity value", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Retrieves a category from the shopping list if it exists.
     *
     * @param shoppingList      the current shopping list
     * @param inputCategoryName the name of the category being searched for
     * @return the category name if found, otherwise returns null
     */
    public static String getCategoryIfExists(Map<String, List<Product>> shoppingList, String inputCategoryName) {
        for (String category : shoppingList.keySet()) {
            if (category.equalsIgnoreCase(inputCategoryName)) {
                return category;
            }
        }
        return null;
    }


    /**
     * Searches for a product with the specified name in a list of products from a specific category.
     *
     * @param productsFromSpecificCategory the list of products from a specific category
     * @param newProduct                   the name of the product being searched for
     * @return the product if found, or null if not found
     */
    public static Product getProductIfExistsFromSpecificCategory(List<Product> productsFromSpecificCategory, String newProduct) {
        for (Product product : productsFromSpecificCategory) {
            String newProductName = newProduct.toLowerCase();
            String productName = product.productName.toLowerCase();
            if (Objects.equals(productName, newProductName)) {
                return product;
            }
        }
        return null;
    }


    /**
     * Removes all products in the specified category from the shopping list.
     *
     * @param shoppingList         the current shopping list
     * @param categoryNameTextArea the text area containing the name of the category to be removed
     */
    public static void removeAllProductsByCategory(Map<String, List<Product>> shoppingList, JTextArea categoryNameTextArea) {
        if (shoppingList.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Shopping list is empty", "Shopping list empty error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String input = categoryNameTextArea.getText().trim();
        String category = getCategoryIfExists(shoppingList, input);

        if (category == null) {
            JOptionPane.showMessageDialog(null, "Category does not exist", "Category error", JOptionPane.ERROR_MESSAGE);
        } else {
            List<Product> productList = shoppingList.get(category);
            productList.clear();
            shoppingList.remove(category);
            JOptionPane.showMessageDialog(null, "Category " + category + " has been deleted", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }


    /**
     * Removes all products from the shopping list.
     *
     * @param shoppingList the current shopping list to be cleared
     */
    public static void removeAllProductsGUI(Map<String, List<Product>> shoppingList) {
        if (shoppingList.isEmpty()) {
            JOptionPane.showMessageDialog(null, "The list is empty", "List empty error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        shoppingList.clear();
        JOptionPane.showMessageDialog(null, "Contents of the shopping list cleared", "Success", JOptionPane.INFORMATION_MESSAGE);
    }


    /**
     * Adds a product to the shopping list. If the selected product category does not exist, adds a new category to shoppingList.
     *
     * @param shoppingList  The current shopping list.
     * @param categoryInput The text area containing the input for the category.
     * @param productInput  The text area containing the input for the product.
     * @param quantityInput The text area containing the input for the quantity.
     */
    public static void addProductGUI(Map<String, List<Product>> shoppingList, JTextArea categoryInput, JTextArea productInput, JTextArea quantityInput) {
        Map<String, List<Product>> fileData;

        try {
            fileData = readCategoryFile("categories.txt");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (fileData == null) {
            JOptionPane.showMessageDialog(null, "Category file corrupted", "IO error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String input = categoryInput.getText().trim();
        String categoryIfExists = getCategoryIfExists(fileData, input);

        if (categoryIfExists == null) {
            JOptionPane.showMessageDialog(null, "Invalid category name", "Category Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String productName = productInput.getText().trim();
        List<Product> fileDataList = fileData.get(categoryIfExists);

        Product productToBeAdded = getProductIfExistsFromSpecificCategory(fileDataList, productName);

        if (productToBeAdded != null) {
            try {
                double quantity = Double.parseDouble(quantityInput.getText());

                if (quantity <= 0) {
                    JOptionPane.showMessageDialog(null, "Invalid quantity value", "Quantity Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                List<Product> tempProductList = shoppingList.get(categoryIfExists);
                if (tempProductList != null) {
                    Product tempProductToBeAdded = getProductIfExistsFromSpecificCategory(tempProductList, productToBeAdded.productName);
                    if (tempProductToBeAdded != null) {
                        tempProductToBeAdded.Quantity += quantity;
                    } else {
                        productToBeAdded.Quantity = quantity;
                        shoppingList.get(categoryIfExists).add(productToBeAdded);
                    }
                } else {
                    List<Product> newProductList = new ArrayList<>();
                    productToBeAdded.Quantity = quantity;
                    newProductList.add(productToBeAdded);
                    shoppingList.put(categoryIfExists, newProductList);
                }

            } catch (NumberFormatException | HeadlessException e) {
                JOptionPane.showMessageDialog(null, "Invalid quantity value: " + quantityInput.getText().trim(), "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            JOptionPane.showMessageDialog(null, "Added product: " + productToBeAdded.productName, "Success", JOptionPane.INFORMATION_MESSAGE);
        }

        if (productToBeAdded == null) {
            JOptionPane.showMessageDialog(null, "Invalid product name", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    /**
     * Prints either categories, products, or both from the shopping list to the specified output field based on the print option.
     *
     * @param shoppingList the shopping list containing categories and products
     * @param outputField  the text area where the output will be displayed
     * @param printOption  the print option specifying what to print (categories, products, or all)
     */
    public static void printCategoryFile(Map<String, List<Product>> shoppingList, JTextArea outputField, PrintOption printOption) {
        outputField.setText("");
        if (shoppingList.isEmpty()) {
            outputField.setText("category list is empty!");
            return;
        }
        switch (printOption) {
            case CATEGORIES:
                for (String category : shoppingList.keySet()) {
                    outputField.append(category + "\n");
                }
                break;
            case PRODUCTS:
                for (List<Product> productList : shoppingList.values()) {
                    for (Product product : productList) {
                        outputField.append(product.productName + "\n");
                    }
                }
                break;
            case All:
                for (Map.Entry<String, List<Product>> entry : shoppingList.entrySet()) {
                    String category = entry.getKey();
                    List<Product> productList = entry.getValue();
                    outputField.append(category + ":\n");
                    for (Product product : productList) {
                        outputField.append("     " + product.productName + "\n");
                    }
                }
                break;
            default:
                JOptionPane.showMessageDialog(null, "Invalid print option", "Print option error", JOptionPane.ERROR_MESSAGE);
        }
    }


    /**
     * Prints all products in the shopping list to the specified output field.
     *
     * @param shoppingList the shopping list containing categories and products
     * @param outputField  the text area where the output will be displayed
     */
    public static void printAllProductsGUI(Map<String, List<Product>> shoppingList, JTextArea outputField) {
        outputField.setText("");
        if (shoppingList.isEmpty()) {
            JOptionPane.showMessageDialog(null, "The list is empty", "Empty list error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        for (Map.Entry<String, List<Product>> entry : shoppingList.entrySet()) {
            String category = entry.getKey();
            List<Product> productList = entry.getValue();
            outputField.append(category + ":\n");
            for (Product product : productList) {
                outputField.append("     " + product.productName + ", Quantity: " + product.Quantity + " " + product.units + "\n");
            }
        }
    }


    /**
     * Prints products of a specified category to the output text field.
     *
     * @param shoppingList    the current shopping list
     * @param categoryName    the name of the category to be printed
     * @param outputTextField the text field where the output will be displayed
     */
    public static void printCategoryGUI(Map<String, List<Product>> shoppingList, JTextArea categoryName, JTextArea outputTextField) {
        if (shoppingList.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Shopping list is empty", "Shopping list empty error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String input = categoryName.getText().trim();
        String category = getCategoryIfExists(shoppingList, input);

        if (category == null) {
            JOptionPane.showMessageDialog(null, "Category named: " + input + " does not exist", "Category error", JOptionPane.ERROR_MESSAGE);
        } else {
            List<Product> productList = shoppingList.get(category);
            outputTextField.setText(category + ":\n");
            for (Product product : productList) {
                outputTextField.append("     " + product.productName + ", Quantity: " + product.Quantity + product.units + "\n");
            }
        }
    }


    /**
     * Initializes the category editing GUI by reading data from the specified file.
     * If the file does not exist, it creates a new file.
     *
     * @param fileName the name of the file to be read
     * @return the map containing category and product data if the file exists, otherwise null
     */
    private static Map<String, List<Product>> categoryEditInitGUI(String fileName) {
        try {
            return readCategoryFile(fileName);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "File not found\nCreating new File...", "File Error", JOptionPane.ERROR_MESSAGE);
            try {
                File newFile = new File(fileName);
                boolean isCreated = newFile.createNewFile();

                if (isCreated) {
                    JOptionPane.showMessageDialog(null, "Created File: " + fileName, "File Creation", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (IOException er) {
                JOptionPane.showMessageDialog(null, "Failed to create file", "File Error", JOptionPane.ERROR_MESSAGE);
            }
            return null;
        }

    }

    /**
     * Reads category and product data from a file.
     *
     * @param fileName the name of the file to be read
     * @return a map containing category and product data
     * @throws IOException if an I/O error occurs
     */
    public static Map<String, List<Product>> readCategoryFile(String fileName) throws IOException {
        Map<String, List<Product>> categoryTemplateList = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 4) {
                    String category = parts[0].trim();
                    String productName = parts[1].trim();
                    String tempQuantity = parts[2].trim();
                    double quantity;
                    try {
                        quantity = Double.parseDouble(tempQuantity);
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        return null;
                    }
                    String units = parts[3].trim();
                    Product product = new Product(productName, quantity, units);
                    if (categoryTemplateList.containsKey(category)) {
                        categoryTemplateList.get(category).add(product);
                    } else {
                        List<Product> list = new ArrayList<>();
                        list.add(product);
                        categoryTemplateList.put(category, list);
                    }
                }
            }
        }

        return categoryTemplateList;
    }

    /**
     * Adds a product to the category template file.
     *
     * @param fileName              the name of the file to be read
     * @param categoryInputTextArea the text area containing the category name
     * @param productInputTextArea  the text area containing the product name
     * @param unitInputTextArea     the text area containing the unit
     */
    public static void addProductToTemplateFileGUI(String fileName, JTextArea categoryInputTextArea, JTextArea productInputTextArea, JTextArea unitInputTextArea) {
        Map<String, List<Product>> categoryFile = categoryEditInitGUI(fileName);

        if (categoryFile == null) {
            return;
        }

        String newCategoryName = categoryInputTextArea.getText().trim();
        String tempCategories = getCategoryIfExists(categoryFile, newCategoryName);

        if (tempCategories != null) {
            newCategoryName = tempCategories;
        }

        String newProductName = productInputTextArea.getText().trim();
        List<Product> list = categoryFile.get(newCategoryName);

        if (list == null) {
            String units = unitInputTextArea.getText().trim();

            List<Product> newProductList = new ArrayList<>();
            Product product = new Product(newProductName, 0, units);

            newProductList.add(product);
            categoryFile.put(newCategoryName, newProductList);
        } else {
            Product newProduct = getProductIfExistsFromSpecificCategory(list, newProductName);
            if (newProduct != null) {
                JOptionPane.showMessageDialog(null, "This product already exists", "Product Error", JOptionPane.ERROR_MESSAGE);
            } else {
                String units = unitInputTextArea.getText().trim();

                Product product = new Product(newProductName, 0, units);
                list.add(product);
            }
        }
        ShoppingListToolsGUI.saveListGUI(categoryFile, fileName);
    }

    /**
     * Removes a specific product from the category template file.
     *
     * @param fileName      the name of the file to be read
     * @param categoryInput the text area containing the category name
     * @param productInput  the text area containing the product name
     */
    public static void removeProductFromTemplateFileGUI(String fileName, JTextArea categoryInput, JTextArea productInput) {
        Map<String, List<Product>> categoryFile;

        try {
            categoryFile = readCategoryFile(fileName);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "IO error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (categoryFile == null) {
            JOptionPane.showMessageDialog(null, "Category file corrupted", "IO error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String deleteCategoryName = categoryInput.getText().trim();

        if ((deleteCategoryName = getCategoryIfExists(categoryFile, deleteCategoryName)) == null) {
            JOptionPane.showMessageDialog(null, "Category named: " + categoryInput.getText().trim() + " does not exist", "Category error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String deleteProductName = productInput.getText().trim();
        List<Product> list = categoryFile.get(deleteCategoryName);

        Product deleteProduct = getProductIfExistsFromSpecificCategory(list, deleteProductName);

        if (deleteProduct == null) {
            JOptionPane.showMessageDialog(null, "Product named: " + productInput.getText().trim() + " does not exist", "Product error", JOptionPane.ERROR_MESSAGE);
            return;
        } else {
            list.remove(deleteProduct);
            if (list.isEmpty()) {
                categoryFile.remove(deleteCategoryName);
            }
            JOptionPane.showMessageDialog(null, "Deleted product: " + deleteProduct.productName, "Product error", JOptionPane.INFORMATION_MESSAGE);

        }

        ShoppingListToolsGUI.saveListGUI(categoryFile, fileName);

    }

}
