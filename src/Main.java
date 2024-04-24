import java.io.File;

public class Main {
    public static void main(String[] args) {
        File categories = new File("categories.txt");
        if (!categories.exists()) {
            System.out.println("No file with the list of products. Prepare a file named categories.txt and fill it in the following way:\nCategory:Product\nCategory:Product\n");
            return;
        }

        ShoppingListGUI.runGUI();
    }
}


