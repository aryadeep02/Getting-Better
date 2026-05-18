package Stage1_Basics.Logic_thinking_Using_Patterns;

public class Pattern_06 {
    static void main() {
        int n=5;
        for(int i=1;i<=n;i++){
            for(int j=1;j<=n-i+1;j++){
                System.out.print(j);
            }
            System.out.println();
        }
    }
}
