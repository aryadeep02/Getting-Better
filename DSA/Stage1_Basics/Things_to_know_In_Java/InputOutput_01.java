package Stage1_Basics.Things_to_know_In_Java;

import java.util.Scanner;

public class InputOutput_01 {
    static void main() {
        Scanner sc=new Scanner(System.in);
        System.out.print("Enter your name:- ");
        String A=sc.next();
        System.out.print("Enter ur Age:- ");
        int b=sc.nextInt();
        System.out.println("Entered Name is "+A+ " and age is "+b);

    }

}
