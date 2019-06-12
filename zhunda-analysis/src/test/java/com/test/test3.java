package com.test;

import java.util.Arrays;

public class test3 {
     public static void main(String args[]) {
         String s = "";
         String[] split = s.split(",");
        for (int i = 0; i < split.length; i++) {
             System.out.println("数组--"+split[i]);
         }
         String s1 = Arrays.toString(split);
         System.out.println(s1);

     }
}
