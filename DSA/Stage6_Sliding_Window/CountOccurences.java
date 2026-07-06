package Stage6_Sliding_Window;

import java.util.Arrays;

// https://www.geeksforgeeks.org/problems/count-occurences-of-anagrams5839/1

public class CountOccurences {
    public static void main(String[] args) {

        Solution1 obj = new Solution1();

        String txt = "forxxorfxdofr";
        String pat = "for";

        System.out.println(obj.search(pat, txt));   // Output: 3
    }
}

class Solution1 {

    private boolean allZero(int[] count) {
        for (int num : count) {
            if (num != 0) {
                return false;
            }
        }
        return true;
    }

    int search(String pat, String txt) {

        int k = pat.length();
        int[] count = new int[26];
        Arrays.fill(count, 0);

        // Store frequency of pattern
        for (char ch : pat.toCharArray()) {
            count[ch - 'a']++;
        }

        int i = 0, j = 0;
        int n = txt.length();
        int result = 0;

        while (j < n) {

            // Include current character in window
            count[txt.charAt(j) - 'a']--;

            // Window size becomes k
            if (j - i + 1 == k) {

                // If all frequencies become zero, it's an anagram
                if (allZero(count)) {
                    result++;
                }

                // Remove leftmost character from window
                count[txt.charAt(i) - 'a']++;
                i++;
            }

            j++;
        }

        return result;
    }
}