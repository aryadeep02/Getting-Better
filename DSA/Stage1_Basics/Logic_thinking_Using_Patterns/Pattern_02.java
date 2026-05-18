package Stage1_Basics.Logic_thinking_Using_Patterns;
//https://takeuforward.org/pattern/pattern-3-right-angled-number-pyramid
public class Pattern_02 {
    static void main() {
        int start=5;
        for(int i=1;i<=start;i++){//initialised for i
            for(int j=1;j<=i;j++){//initialised for j
                System.out.print(j);//prints in range of j
            }
            System.out.println();
        }
    }
}
