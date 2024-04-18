import java.io.*;
import java.util.*;

public class ShoppingListTools {
    /**
     * print menu
     */
    public static void menuPrint() {
        System.out.println("1. Add a product to the list");
        System.out.println("2. Display all products from the list");
        System.out.println("3. Display all products from a specific category");
        System.out.println("4. Delete all products from the list");
        System.out.println("5. Delete all products from a specific category");
        System.out.println("6. Delete a product");
        System.out.println("7. Save the list to a file");
        System.out.println("8. Modify source file categories");
        System.out.println("0. Exit the program");
    }

    /**
     * Saves shoppingList to file
     *
     * @param shoppingList our current shoppingList
     * @param fileName     name of the file being read.
     */
    public static void saveList(Map<String, List<String>> shoppingList, String fileName) {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            try {
                for (Map.Entry<String, List<String>> entry : shoppingList.entrySet()) {
                    String key = entry.getKey();
                    List<String> values = entry.getValue();
                    for (String value : values) {
                        writer.write(key + ":" + value + "\n");
                    }
                }
            } catch (Exception NullPointerException) {
                return;
            }
            System.out.println("File save path: " + fileName);
        } catch (IOException e) {
            System.err.println("File save error: " + e.getMessage());
        }
    }


    /**
     * Delete specific product. If category empty delete category
     *
     * @param shoppingList our current shoppingList
     */
    public static void deleteProduct(Map<String, List<String>> shoppingList) {
        if (shoppingList.isEmpty()) {
            System.out.println("The list is empty!");
            return;
        }

        printAllProducts(shoppingList);

        String category;
        while (true) {
            System.out.println("Enter the category: ");
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            category = getCategoryIfExists(shoppingList, input);

            if (category == null) {
                System.out.println("There is no such category");
            } else {
                break;
            }
        }

        while (true) {
            System.out.println("Choose a product:");
            Scanner scanner = new Scanner(System.in);
            String productName = scanner.nextLine();

            List<String> list = shoppingList.get(category);
            String itemName = getProductIfExistsFromSpecificCategory(list, productName);

            if (itemName == null) {
                System.out.println("Choose the correct option!");
            } else {
                list.remove(itemName);
                if (list.isEmpty()) {
                    shoppingList.remove(category);
                }
                break;
            }
        }

    }

    /**
     * @param shoppingList our current shoppingList
     * @param categoryName categoryName being searched for.
     * @return return categoryName. If not found return null.
     */
    public static String getCategoryIfExists(Map<String, List<String>> shoppingList, String categoryName) {
        for (Map.Entry<String, List<String>> entry : shoppingList.entrySet()) {
            String category = entry.getKey();
            String category1 = category.toLowerCase();
            String input1 = categoryName.toLowerCase();
            if (Objects.equals(category1, input1)) {
                return category;
            }
        }
        return null;
    }

    /**
     * @param CategoriesInShoppingList categories in our shoppingList
     * @param productName              name of the product being searched for
     * @return productName if found. null if not found.
     */
    public static String getProductIfExistsFromSpecificCategory(List<String> CategoriesInShoppingList, String productName) {
        for (String itemName : CategoriesInShoppingList) {
            String productName1 = productName.toLowerCase();
            String itemName1 = itemName.toLowerCase();
            if (Objects.equals(productName1, itemName1)) {
                return itemName;
            }
        }
        return null;
    }


    /**
     * deletes products from specific category
     *
     * @param shoppingList our current shopping list
     */
    public static void deleteAllProductsByCategory(Map<String, List<String>> shoppingList) {
        if (shoppingList.isEmpty()) {
            System.out.println("The list is empty!");
            return;
        }

        for (Map.Entry<String, List<String>> entry : shoppingList.entrySet()) {
            System.out.println("\t" + entry.getKey());
        }

        String category;
        while (true) {
            System.out.println("Enter the category: ");
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();

            category = getCategoryIfExists(shoppingList, input);

            if (category == null) {
                System.out.println("There is no such category");
            } else {
                List<String> itemList = shoppingList.get(category);
                itemList.clear();
                shoppingList.remove(category);
                System.out.println("Category deleted: " + category);
                break;
            }

        }

    }

    /**
     * @param shoppingList our current shopping list
     */
    public static void deleteAllProducts(Map<String, List<String>> shoppingList) {
        if (shoppingList.isEmpty()) {
            System.out.println("The list is empty!!!");
            return;
        }
        shoppingList.clear();
        System.out.println("Content of the list deleted");

    }

    /**
     * Reads through file of categories and products and saves them to hashMap.
     *
     * @param fileName file name to read.
     * @return return hashMap of categories and products
     * @throws IOException if file not found.
     */
    public static Map<String, List<String>> readFile(String fileName) throws IOException {
        Map<String, List<String>> items = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    String category = parts[0].trim();
                    String itemName = parts[1].trim();
                    if (items.containsKey(category)) {
                        items.get(category).add(itemName);
                    } else {
                        List<String> list = new ArrayList<>();
                        list.add(itemName);
                        items.put(category, list);
                    }
                }
            }
        }

        return items;
    }


    /**
     * Adds product to a list. If selected product category doest not exist add new category to shoppingList
     *
     * @param shoppingList Our current shopping list.
     */
    public static void addProduct(Map<String, List<String>> shoppingList) throws IOException {
        Map<String, List<String>> FileData;

        try {
            FileData = readFile("Categories.txt");
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
            return;
        }

        for (Map.Entry<String, List<String>> entry : FileData.entrySet()) {
            String category = entry.getKey();
            System.out.println("\t" + category);
        }


        String fileCategory;
        while (true) {
            System.out.println("Choose a category (Enter a name from the above list):");
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();

            fileCategory = getCategoryIfExists(FileData, input);

            if (fileCategory == null) {
                System.out.println("Choose a correct option!");
            } else {
                break;
            }
        }

        List<String> tempList = FileData.get(fileCategory);
        for (String itemName : tempList) {
            System.out.println("\t" + itemName);
        }


        String productNameToBeAdded;
        while (true) {
            System.out.println("Choose a Product (Enter a name from the above list):");
            Scanner scanner = new Scanner(System.in);
            String productName = scanner.nextLine();

            List<String> list = FileData.get(fileCategory);


            productNameToBeAdded = getProductIfExistsFromSpecificCategory(list, productName);

            if (productNameToBeAdded != null) {
                if (!shoppingList.isEmpty() && shoppingList.get(fileCategory) != null) {
                    if (shoppingList.get(fileCategory).contains(productNameToBeAdded)) {
                        System.out.println("The product is already in the list. Returning to menu.");
                        fileCategory = null;
                        break;
                    }
                }
            }

            if (productNameToBeAdded == null) {
                System.out.println("Choose a correct option!");
            } else {
                System.out.println("Added product: " + productName);
                break;
            }
        }

        String category = getCategoryIfExists(shoppingList, fileCategory);

        if (category == null) {
            List<String> newProductList = new ArrayList<>();
            newProductList.add(productNameToBeAdded);
            shoppingList.put(fileCategory, newProductList);
        } else {
            shoppingList.get(fileCategory).add(productNameToBeAdded);
        }
    }

    /**
     * @param shoppingList // our current shopping list
     */
    public static void printAllProducts(Map<String, List<String>> shoppingList) {
        if (shoppingList.isEmpty()) {
            System.out.println("The list is empty!");
            return;
        }

        for (Map.Entry<String, List<String>> entry : shoppingList.entrySet()) {
            String category = entry.getKey();
            List<String> itemList = entry.getValue();
            System.out.println(category);
            for (String itemName : itemList) {
                System.out.println("\t" + itemName);
            }
        }
    }

    /**
     * @param shoppingList // our current shopping list
     */
    public static void printProductsByCategories(Map<String, List<String>> shoppingList) {
        if (shoppingList.isEmpty()) {
            System.out.println("The list is empty!");
            return;
        }

        for (Map.Entry<String, List<String>> entry : shoppingList.entrySet()) {
            String category = entry.getKey();
            System.out.println(category);
        }

        String category;
        while (true) {
            System.out.println("Enter a category: ");
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();

            category = getCategoryIfExists(shoppingList, input);

            if (category == null) {
                System.out.println("No such category exists");
            } else {
                List<String> itemList = shoppingList.get(category);
                System.out.println(category);

                for (String itemName : itemList) {
                    System.out.println("\t" + itemName);
                }


                break;
            }

        }
    }

    /**
     * @param fileName Name of the file that is going to be read through
     * @return null if fileName file does not exist. Filename if exists.
     */
    public static Map<String, List<String>> categoryEditInit(String fileName) {
        try {
            return readFile(fileName);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.out.println("Creating " + fileName + " file...");
            try {
                File newFile = new File(fileName);
                boolean isCreated = newFile.createNewFile();

                if(isCreated) System.out.println("Created file: " + fileName);
            }catch (IOException x){
                System.out.println(x.getMessage());
            }


            return null;
        }

    }

    /**
     * Adds product to category template file.
     * @param fileName filename of the file to be read.
     */
    public static void addProductToCategoryFile(String fileName){

        Map<String, List<String>> categoryFile = categoryEditInit(fileName);

        if (categoryFile == null) {
            return;
        }

        printAllProducts(categoryFile);

        String newCategoryName;
        while (true) {
            if ((newCategoryName = getCategoryInputAndValidateIsFileEmpty()) != null) {
                String tempCategories = getCategoryIfExists(categoryFile, newCategoryName);
                if (tempCategories != null) {
                    newCategoryName = tempCategories;
                } else {
                    System.out.println("No category found");
                }

                break;
            }

        }


        String newProductName;
        while (true) {
            Scanner scanner1 = new Scanner(System.in);
            System.out.println("Enter product name:");
            newProductName = scanner1.nextLine();
            List<String> list = categoryFile.get(newCategoryName);
            if (list == null) {
                List<String> newProductList = new ArrayList<>();
                newProductList.add(newProductName);
                categoryFile.put(newCategoryName, newProductList);
                break;
            } else {
                String newProduct = getProductIfExistsFromSpecificCategory(list, newProductName);
                if (newProduct != null) {
                    System.out.println("This product already exists");
                } else {
                    list.add(newProductName);
                    break;
                }
            }

        }

        saveList(categoryFile, fileName);

    }

    /**
     * Gets category input and checks if it is empty.
     * @return null if empty else return category name
     */
    public static String getCategoryInputAndValidateIsFileEmpty() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter category name:");
        String deleteCategoryName = scanner.nextLine();
        if (deleteCategoryName.isEmpty()) {
            System.out.println("Invalid category name");
        } else {
            return deleteCategoryName;
        }
        return null;
    }

    /**
     * Removes specific product from category template file.
     * @param fileName File with categories and products
     */
    public static void DeleteProductFromCategoryFile(String fileName) {
        Map<String, List<String>> categoryFile;

        try {
            categoryFile = readFile(fileName);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return;
        }

        printAllProducts(categoryFile);

        String deleteCategoryName;
        while (true) {

            deleteCategoryName = getCategoryInputAndValidateIsFileEmpty();

            if ((deleteCategoryName = getCategoryIfExists(categoryFile, deleteCategoryName)) == null) {
                System.out.println("No category found");
            } else {
                break;
            }
        }

        String deleteProductName;
        while (true) {
            Scanner scanner1 = new Scanner(System.in);
            System.out.println("Enter product name:");
            deleteProductName = scanner1.nextLine();

            List<String> list = categoryFile.get(deleteCategoryName);

            deleteProductName = getProductIfExistsFromSpecificCategory(list, deleteProductName);

            if (deleteProductName == null) {
                System.out.println("No product found");
            } else {
                list.remove(deleteProductName);
                if (list.isEmpty()) {
                    categoryFile.remove(deleteProductName);
                }
                System.out.println("Deleted product: " + deleteProductName);
                break;
            }
        }

        saveList(categoryFile, fileName);

    }
}
