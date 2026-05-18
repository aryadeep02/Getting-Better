package Stage1_Basics.Things_to_know_In_Java;
import java.util.*;
public class SwitchCase_04 {
    static void main() {
        Scanner scan =  new Scanner(System.in);
        System.out.print("Enter any number betweeen 1-7 :- ");
        int day=scan.nextInt();
        switch(day) {
            case 1:
                System.out.println("Monday");
                break;
            case 2:
                System.out.println("Tuesday");
                break;
            case 3:
                System.out.println("Wednesday");
                break;
            case 4:
                System.out.println("Thursday");
                break;
            case 5:
                System.out.println("Friday");
                break;
            case 6:
                System.out.println("Saturday");
                break;
            case 7:
                System.out.println("Sunday");
                break;
            default:
                System.out.println("Invalid");
        }


    }



}
