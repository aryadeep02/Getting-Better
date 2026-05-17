package Stage1_Basics.Things_to_know_In_Java;

public class IF_Else_03 {

    // Method to compare two numbers
    public static String compareNM(int n, int m) {

        // Using Ternary Operator
        return (n > m) ? "greater" : (n < m) ? "lesser" : "equal";
    }

    public static void main(String[] args) {

        System.out.println(compareNM(10, 20)); // lesser
        System.out.println(compareNM(30, 10)); // greater
        System.out.println(compareNM(15, 15)); // equal
    }
}