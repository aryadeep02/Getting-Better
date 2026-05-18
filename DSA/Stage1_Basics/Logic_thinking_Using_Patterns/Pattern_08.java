package Stage1_Basics.Logic_thinking_Using_Patterns;

public class Pattern_08 {

    static void main() {

        int n = 4;

        for(int i = 1; i <= n; i++) {

            // Spaces
            for(int j = 1; j <= n - i; j++) {
                System.out.print(" ");
            }

            // Numbers
            for(int k = 1; k <= i; k++) {
                System.out.print(i + " ");
            }

            System.out.println();
        }
    }
}