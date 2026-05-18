//https://takeuforward.org/pattern/pattern-2-right-angled-triangle-pattern
package Stage1_Basics.Logic_thinking_Using_Patterns;
public class Pattern_01 {
    static void main() {
        int row=6;
        for(int i=1;i<=row;i++){//works for i
            for(int j=1;j<=i;j++){//works for j
                System.out.print(" * ");
            }
            System.out.println();//for changing to second line
        }
    }
}
