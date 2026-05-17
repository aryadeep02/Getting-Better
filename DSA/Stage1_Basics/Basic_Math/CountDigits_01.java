package Stage1_Basics.Basic_Math;

public class CountDigits_01 {

    // Time Complexity = O(log n)
    // Space Complexity = O(1)

    static int evenlyDivides(int N) {

        // Store original number
        int temp = N;

        // Counter for evenly dividing digits
        int count = 0;

        // Traverse through each digit
        while (temp != 0) {

            // Extract last digit
            int rem = temp % 10;

            // Check if digit is not 0
            // and divides N evenly
            if (rem != 0 && N % rem == 0) {
                count++;
            }

            // Remove last digit
            temp /= 10;
        }

        return count;
    }

    public static void main(String[] args) {

        int N = 1012;

        System.out.println("Count of evenly dividing digits: " + evenlyDivides(N));
    }
}