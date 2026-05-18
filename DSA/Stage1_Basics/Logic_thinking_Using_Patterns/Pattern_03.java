package Stage1_Basics.Logic_thinking_Using_Patterns;
//https://takeuforward.org/pattern/pattern-4-right-angled-number-pyramid-ii
public class Pattern_03 {
    static void main() {
        int blocks=5;

        for(int i=1;i<=blocks;i++){
            for(int j=1;j<=i;j++){
                System.out.print(i);
            }
            System.out.println();
        }

    }
}
