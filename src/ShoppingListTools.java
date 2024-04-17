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

}
