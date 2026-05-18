//https://takeuforward.org/pattern/pattern-3-right-angled-number-pyramid
package Stage1_Basics.Logic_thinking_Using_Patterns;
public class Pattern_02 {
    static void main() {
        int start=5;
        for(int i=1;i<=start;i++){
            for(int j=1;j<=i;j++){
                System.out.print(j);
            }
            System.out.println();
        }
    }
}
