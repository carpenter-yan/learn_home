package edu.princeton.cs.algs4;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

class Solution {
    public char nextGreatestLetter(char[] letters, char target) {
        int len = letters.length;
        int lo = 0, hi = len - 1;
        while(lo <= hi){
            int mi = lo + (hi - lo)/2;
            if(letters[lo] <= letters[hi]){
                lo = mi + 1;
            }else{
                lo = mi - 1;
            }
        }
        return lo < len ? letters[lo] : letters[0];
    }
}