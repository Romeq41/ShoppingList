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
    public static void saveList(Map<String, List<Product>> shoppingList, String fileName) {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            try {
                for (Map.Entry<String, List<Product>> entry : shoppingList.entrySet()) {
                    String key = entry.getKey();
                    List<Product> values = entry.getValue();
                    for (Product value : values) {
                        writer.write(key + ":" + value.productName + ":" + value.Quantity + ":" + value.units+"\n");
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
    public static void deleteProduct(Map<String, List<Product>> shoppingList) {
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
        Product productToBeRemoved;
        List<Product> categoryList;
        while (true) {
            System.out.println("Choose a product:");
            Scanner scanner = new Scanner(System.in);
            String productName = scanner.nextLine();

            categoryList = shoppingList.get(category);
            productToBeRemoved = getProductIfExistsFromSpecificCategory(categoryList, productName);

            if (productToBeRemoved == null) {
                System.out.println("Choose the correct option!");
            } else {
                break;
            }
        }

        double quantity;
        while (true) {
            System.out.println("Enter quantity of " + productToBeRemoved.productName + " in " + productToBeRemoved.units + " to be removed:");
            Scanner scanner1 = new Scanner(System.in);

            try {
                quantity = scanner1.nextDouble();

                if (quantity < 1) {
                    System.out.println("Enter correct value!");
                    continue;
                } else if (quantity >= productToBeRemoved.Quantity) {
                    categoryList.remove(productToBeRemoved);
                    System.out.println("value exceeds quantity of product. Product removed.");
                    if (categoryList.isEmpty()) {
                        shoppingList.remove(category);
                    }
                    break;
                }

                productToBeRemoved.Quantity -= quantity;
                break;
            } catch (InputMismatchException e) {
                System.out.println("Incorrect value");
            }
        }

    }

    /**
     * @param shoppingList our current shoppingList
     * @param categoryName categoryName being searched for.
     * @return return categoryName. If not found return null.
     */
    public static String getCategoryIfExists(Map<String, List<Product>> shoppingList, String categoryName) {
        for (Map.Entry<String, List<Product>> entry : shoppingList.entrySet()) {
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
     * @param productsFromSpecificCategory categories in our shoppingList
     * @param newProduct                   name of the newProduct being searched for
     * @return newProduct if found. null if not found.
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
     * deletes products from specific category
     *
     * @param shoppingList our current shopping list
     */
    public static void deleteAllProductsByCategory(Map<String, List<Product>> shoppingList) {
        if (shoppingList.isEmpty()) {
            System.out.println("The list is empty!");
            return;
        }

        for (Map.Entry<String, List<Product>> entry : shoppingList.entrySet()) {
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
                List<Product> productList = shoppingList.get(category);
                productList.clear();
                shoppingList.remove(category);
                System.out.println("Category deleted: " + category);
                break;
            }

        }

    }

    /**
     * @param shoppingList our current shopping list
     */
    public static void deleteAllProducts(Map<String, List<Product>> shoppingList) {
        if (shoppingList.isEmpty()) {
            System.out.println("The list is empty!!!");
            return;
        }
        shoppingList.clear();
        System.out.println("Content of the list deleted");
    }


    /**
     * Adds product to a list. If selected product category doest not exist add new category to shoppingList
     *
     * @param shoppingList Our current shopping list.
     */
    public static void addProduct(Map<String, List<Product>> shoppingList) throws IOException {
        Map<String, List<Product>> FileData;

        try {
            FileData = readCategoryFile("Categories.txt");
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
            return;
        }

        for (Map.Entry<String, List<Product>> entry : FileData.entrySet()) {
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

        List<Product> tempProductListDisplay = FileData.get(fileCategory);
        for (Product product : tempProductListDisplay) {
            System.out.println("\t" + product.productName);
        }


        Product productToBeAdded;
        while (true) {
            System.out.println("Choose a Product (Enter a name from the above list):");
            Scanner scanner = new Scanner(System.in);
            String productName = scanner.nextLine();

            List<Product> fileDataList = FileData.get(fileCategory);
            productToBeAdded = getProductIfExistsFromSpecificCategory(fileDataList, productName);

            if (productToBeAdded != null) {
                double quantity;
                while (true) {
                    System.out.println("Enter quantity of " + productToBeAdded.productName + " in " + productToBeAdded.units + ":");
                    Scanner scanner1 = new Scanner(System.in);

                    try {
                        quantity = scanner1.nextDouble();

                        if (quantity < 1) {
                            System.out.println("Enter correct value!");
                            continue;
                        }
                        List<Product> tempProductList = shoppingList.get(fileCategory);
                        if (tempProductList != null) {
                            Product tempProductToBeAdded = getProductIfExistsFromSpecificCategory(tempProductList, productToBeAdded.productName);
                            if (tempProductToBeAdded != null) {
                                tempProductToBeAdded.Quantity += quantity;
                            } else {
                                productToBeAdded.Quantity = quantity;
                                shoppingList.get(fileCategory).add(productToBeAdded);
                            }
                        } else {
                            List<Product> newProductList = new ArrayList<>();
                            productToBeAdded.Quantity = quantity;
                            newProductList.add(productToBeAdded);
                            shoppingList.put(fileCategory, newProductList);
                        }
                        break;
                    } catch (InputMismatchException e) {
                        System.out.println("Incorrect value");
                    }


                }
            }

            if (productToBeAdded == null) {
                System.out.println("Choose a correct option!");
            } else {
                System.out.println("Added product: " + productToBeAdded.productName);
                break;
            }
        }


    }

    /**
     * @param shoppingList // our current shopping list
     */
    public static void printAllProducts(Map<String, List<Product>> shoppingList) {
        if (shoppingList.isEmpty()) {
            System.out.println("The list is empty!");
            return;
        }

        for (Map.Entry<String, List<Product>> entry : shoppingList.entrySet()) {
            String category = entry.getKey();
            List<Product> productListList = entry.getValue();
            System.out.println(category);
            for (Product product : productListList) {
                System.out.println("\t" + product.productName + ", Quantity: " + product.Quantity + " " + product.units);
            }
        }
    }

    /**
     * @param shoppingList // our current shopping list
     */
    public static void printProductsByCategories(Map<String, List<Product>> shoppingList) {
        if (shoppingList.isEmpty()) {
            System.out.println("The list is empty!");
            return;
        }

        for (Map.Entry<String, List<Product>> entry : shoppingList.entrySet()) {
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
                List<Product> productList = shoppingList.get(category);
                System.out.println(category);

                for (Product product : productList) {
                    System.out.println("\t" + product.productName + ", Quantity: " + product.Quantity + product.units);
                }


                break;
            }

        }
    }

    /**
     * @param fileName Name of the file that is going to be read through
     * @return null if fileName file does not exist. Filename if exists.
     */
    public static Map<String, List<Product>> categoryEditInit(String fileName) {
        try {
            return readCategoryFile(fileName);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.out.println("Creating " + fileName + " file...");
            try {
                File newFile = new File(fileName);
                boolean isCreated = newFile.createNewFile();

                if (isCreated) System.out.println("Created file: " + fileName);
            } catch (IOException x) {
                System.out.println(x.getMessage());
            }


            return null;
        }

    }


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
                        System.out.println(e.getMessage());
                        categoryTemplateList = null;
                        return categoryTemplateList;
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
     * Adds product to category template file.
     *
     * @param fileName filename of the file to be read.
     */
    public static void addProductToCategoryFile(String fileName) {

        Map<String, List<Product>> categoryFile = categoryEditInit(fileName);

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
            List<Product> list = categoryFile.get(newCategoryName);
            if (list == null) {
                Scanner scanner2 = new Scanner(System.in);
                System.out.println("Enter units of measurement:");
                String units = scanner2.nextLine();

                List<Product> newProductList = new ArrayList<>();
                Product product = new Product(newProductName, 0, units);
                newProductList.add(product);
                categoryFile.put(newCategoryName, newProductList);
                break;
            } else {
                Product newProduct = getProductIfExistsFromSpecificCategory(list, newProductName);
                if (newProduct != null) {
                    System.out.println("This product already exists");
                } else {
                    Scanner scanner2 = new Scanner(System.in);
                    System.out.println("Enter units of measurement:");
                    String units = scanner2.nextLine();

                    Product product = new Product(newProductName, 0, units);
                    list.add(product);
                    break;
                }
            }

        }

        saveList(categoryFile, fileName);

    }

    /**
     * Gets category input and checks if it is empty.
     *
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
     *
     * @param fileName File with categories and products
     */
    public static void DeleteProductFromCategoryFile(String fileName) {
        Map<String, List<Product>> categoryFile;

        try {
            categoryFile = readCategoryFile(fileName);
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

        Product deleteProduct;
        while (true) {
            Scanner scanner1 = new Scanner(System.in);
            System.out.println("Enter product name:");
            String newdeleteProductName = scanner1.nextLine();

            List<Product> list = categoryFile.get(deleteCategoryName);

            deleteProduct = getProductIfExistsFromSpecificCategory(list, newdeleteProductName);

            if (deleteProduct == null) {
                System.out.println("No product found");
            } else {
                list.remove(deleteProduct);
                if (list.isEmpty()) {
                    categoryFile.remove(deleteCategoryName);
                }
                System.out.println("Deleted product: " + deleteProduct.productName + ", Quantity: " + deleteProduct.Quantity + " " + deleteProduct.units);
                break;
            }
        }

        saveList(categoryFile, fileName);

    }

    public static final class Product {
        private final String productName;
        private final String units;
        private double Quantity;

        public Product(String productName, double Quantity, String units) {
            this.productName = productName;
            this.Quantity = Quantity;
            this.units = units;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (obj == null || obj.getClass() != this.getClass()) return false;
            var that = (Product) obj;
            return Objects.equals(this.productName, that.productName) && Double.doubleToLongBits(this.Quantity) == Double.doubleToLongBits(that.Quantity) && Objects.equals(this.units, that.units);
        }

        @Override
        public String toString() {
            return "Product[" + "productName=" + productName + ", " + "Quantity=" + Quantity + ", " + "units=" + units + ']';
        }

    }
}
