package Stage1_Basics.Basic_Math;

public class ReverseNumber_02 {

    // Time Complexity = O(log n)
    // Space Complexity = O(1)

    public static int reverse(int x) {

        // Variable to store reversed number
        int rev = 0;

        // Loop until number becomes 0
        while (x != 0) {

            // Extract last digit
            int rem = x % 10;

            // Check for overflow
            if (rev > Integer.MAX_VALUE / 10 || rev < Integer.MIN_VALUE / 10) {
                return 0;
            }

            // Build reversed number
            rev = rev * 10 + rem;

            // Remove last digit
            x /= 10;
        }

        return rev;
    }

    public static void main(String[] args) {

        int number = 12345;

        System.out.println("Original Number: " + number);
        System.out.println("Reversed Number: " + reverse(number));
    }
}