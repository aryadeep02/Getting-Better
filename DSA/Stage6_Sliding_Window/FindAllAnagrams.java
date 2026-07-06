package Stage6_Sliding_Window;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// https://leetcode.com/problems/find-all-anagrams-in-a-string/description/

public class FindAllAnagrams {

    public static void main(String[] args) {

        Solution2 obj = new Solution2();

        String s = "cbaebabacd";
        String p = "abc";

        List<Integer> ans = obj.findAnagrams(s, p);

        System.out.println(ans);
    }
}

class Solution2 {

    private boolean allZero(int[] count) {
        for (int num : count) {
            if (num != 0) {
                return false;
            }
        }
        return true;
    }

    public List<Integer> findAnagrams(String s, String p) {

        List<Integer> result = new ArrayList<>();

        if (p.length() > s.length()) {
            return result;
        }

        int k = p.length();
        int[] count = new int[26];
        Arrays.fill(count, 0);

        // Frequency of pattern
        for (char ch : p.toCharArray()) {
            count[ch - 'a']++;
        }

        int i = 0, j = 0;
        int n = s.length();

        while (j < n) {

            // Include current character
            count[s.charAt(j) - 'a']--;

            // Window size becomes k
            if (j - i + 1 == k) {

                // If all frequencies are zero, an anagram is found
                if (allZero(count)) {
                    result.add(i);
                }

                // Remove leftmost character
                count[s.charAt(i) - 'a']++;
                i++;
            }

            j++;
        }

        return result;
    }
}