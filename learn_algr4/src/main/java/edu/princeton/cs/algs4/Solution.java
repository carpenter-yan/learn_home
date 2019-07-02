package edu.princeton.cs.algs4;

import sun.reflect.generics.tree.Tree;

import javax.swing.tree.TreeNode;
import java.util.*;
import java.util.Stack;

class Solution {
    public int[] nextGreaterElements(int[] nums) {
        int len = nums.length;
        int[] next = new int[len];
        Arrays.fill(next, -1);
        Stack<Integer> stack = new Stack<>();
        for(int i = 0; i < 2 * len; i++){
            int num = nums[i % len];
            while(!stack.isEmpty() && num > nums[stack.peek()]){
                next[stack.pop()] = num;
            }
            if(i < len) {
                stack.push(i);
            }
        }
        return next;
    }
}