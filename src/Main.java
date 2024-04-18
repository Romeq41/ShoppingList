import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {


        File categories = new File("categories.txt");
        if (!categories.exists()) {
            System.out.println("No file with the list of products. Prepare a file named categories.txt and fill it in the following way:\nCategory:Product\nCategory:Product\n");
            return;
        }

        Map<String, List<String>> categoryMap = new HashMap<>();
        try {
            categoryMap = ShoppingListTools.readFile("list.txt");
        } catch (Exception IOException) {
            System.out.println("Error");
        }

        ShoppingListTools.menuPrint();
        while (true) {


            System.out.println("Choose an action:");
            Scanner scanner = new Scanner(System.in);

            try {
                int input = scanner.nextInt();
                if (input < 0 || input > 9) {
                    System.out.println("Choose a correct number!");
                } else if (input == 1) {
                    ShoppingListTools.addProduct(categoryMap);
                } else if (input == 2) {
                    ShoppingListTools.printAllProducts(categoryMap);
                } else if (input == 3) {
                    ShoppingListTools.printProductsByCategories(categoryMap);
                } else if (input == 4) {
                    ShoppingListTools.deleteAllProducts(categoryMap);
                    ShoppingListTools.menuPrint();
                } else if (input == 5) {
                    ShoppingListTools.deleteAllProductsByCategory(categoryMap);
                    ShoppingListTools.menuPrint();
                } else if (input == 6) {
                    ShoppingListTools.deleteProduct(categoryMap);
                    ShoppingListTools.menuPrint();
                } else if (input == 7) {
                    ShoppingListTools.saveList(categoryMap, "list.txt");
                    ShoppingListTools.menuPrint();
                } else if (input == 8) {
                    System.out.println("Pick Action:");
                    System.out.println("1. Add product");
                    System.out.println("2. Delete product");
                    Scanner scanner1 = new Scanner(System.in);
                    int editInput = scanner1.nextInt();
                    if (editInput == 1) {
                        ShoppingListTools.addProductToCategoryFile("categories.txt");
                    } else if (editInput == 2) {
                        ShoppingListTools.DeleteProductFromCategoryFile("categories.txt");
                    } else {
                        System.out.println("Choose a correct number!");
                    }

                } else {
                    return;
                }

            } catch (Exception InputMismatchException) {
                System.out.println("Choose the correct option!");
            }
        }
    }
}


