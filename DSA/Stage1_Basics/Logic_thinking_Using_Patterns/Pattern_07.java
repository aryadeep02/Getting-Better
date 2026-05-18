package Stage1_Basics.Logic_thinking_Using_Patterns;

public class Pattern_07 {

    public static void main(String[] args) {
        int n=5;

        for(int i=0;i<n;i++){
            //Space
            for(int j=0;j<n-i-1;j++){
                System.out.print(" ");
            }
            //Star
            for(int k=0;k<2*i+1;k++){
                System.out.print("*");
            }

            //Space
            for(int l=0;l<n-i-1;l++){
                System.out.print(" ");
            }
            System.out.println();
        }


    }
}