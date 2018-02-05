package com.xwjr.utilcode.utils;

import java.util.List;

/**
 * 1.比较两个list<String>是否完全相同
 * 2.比较两个list<Integer>是否完全相同
 */

public class CompareUtils {

    public static boolean compare2ListString(List<String> string1, List<String> string2) {
        if (string1.size() != string2.size()) {
            return false;
        }
        for (int i = 0; i < string1.size(); i++) {
            if (!string1.get(i).equals(string2.get(i))) {
                return false;
            }
        }
        return true;
    }


    public static boolean compare2ListInteger(List<Integer> string1, List<Integer> string2) {
        if (string1.size() != string2.size()) {
            return false;
        }
        for (int i = 0; i < string1.size(); i++) {
            if (!string1.get(i).equals(string2.get(i))) {
                return false;
            }
        }
        return true;
    }
}
