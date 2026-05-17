import java.util.Scanner;

public class UserInputOutput{

    public static void main(String[] args) {
        // Create a Scanner object to read input from the user
        Scanner sc = new Scanner(System.in);

        // Read a character input from the user
        System.out.print("Enter any Character :-");
        char ch = sc.next().charAt(0);

        // Check if the character is an uppercase letter, then Print 1
        if(ch >= 'A' && ch <= 'Z')
            System.out.println(1);
            // Check if the character is a lowercase letter, then Print 0
        else if(ch >= 'a' && ch <= 'z')
            System.out.println(0);
            // If neither, it's not a letter, then Print -1
        else
            System.out.println(-1);

        // Close the scanner to prevent resource leak
        sc.close();
    }
}