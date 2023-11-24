/**
 * author     : geekplus
 * email      : geekcjj@gmail.com
 * date       : 11/24/23 03:51
 * description: 做什么的？
 */
package com.geekplus.common.util.text;

import com.geekplus.common.util.html.ArticleUtil;

public class TextComparer {

    public static void main(String[] args) {
    }

    //基础直接比对
    public static boolean isEqual(String text1, String text2) {
        if (text1.length() != text2.length()) {
            return false;
        }
        for (int i = 0; i < text1.length(); i++) {
            if (text1.charAt(i) != text2.charAt(i)) {
                return false;
            }
        }
        return true;
    }

    /**
     * KMP算法是一种改进的字符串匹配算法，它通过预处理模式串，构建next数组，从而实现在比对过程中避免不必要的回退。
     * KMP算法在处理长字符串时具有较高的效率。
     */
    public static boolean isEqualByKMP(String text1, String text2) {
        if (text1.length() != text2.length()) {
            return false;
        }
        int[] next = getNext(text2);
        int j = 0;
        for (int i = 0; i < text1.length(); i++) {
            while (j > 0 && text1.charAt(i) != text2.charAt(j)) {
                j = next[j - 1];
            }
            if (text1.charAt(i) == text2.charAt(j)) {
                j++;
            }
            if (j == text2.length()) {
                return true;
            }
        }
        return false;
    }

    private static int[] getNext(String pattern) {
        int[] next = new int[pattern.length()];
        int j = 0;
        for (int i = 1; i < pattern.length(); i++) {
            while (j > 0 && pattern.charAt(i) != pattern.charAt(j)) {
                j = next[j - 1];
            }
            if (pattern.charAt(i) == pattern.charAt(j)) {
                j++;
            }
            next[i] = j;
        }
        return next;
    }

    /**
     * Boyer-Moore算法是一种高效的字符串搜索算法，它在比对过程中利用模式串中的信息快速移动比对位置。
     * Boyer-Moore算法适合处理大型文本和较长的模式串。
     */
//    public static boolean isEqualByBoyerMoore(String text1, String text2) {
//        if (text1.length() != text2.length()) {
//            return false;
//        }
//        int[] badCharacter = buildBadCharacter(text2);
//        int[] goodSuffix = buildGoodSuffix(text2);
//        int i = text2.length() - 1;
//        while (i < text1.length()) {
//            int j = text2.length() - 1;
//            while (j >= 0 && text1.charAt(i) == text2.charAt(j)) {
//                i--;
//                j--;
//            }
//            if (j < 0) {
//                return true;
//            }
//            int x = badCharacter[text1.charAt(i)];
//            int y = goodSuffix[j];
//            i += Math.max(x, y);
//        }
//        return false;
//    }
//
//    private static int[] buildBadCharacter(String pattern) {
//        int[] badCharacter = new int[256];
//        for (int i = 0; i < 256; i++) {
//            badCharacter[i] = pattern.length();
//        }
//        for (int i = 0; i < pattern.length() - 1; i++) {
//            badCharacter[pattern.charAt(i)] = pattern.length() - i - 1;
//        }
//        return badCharacter;
//    }

}
