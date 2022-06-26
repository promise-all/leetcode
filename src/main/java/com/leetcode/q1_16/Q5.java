package com.leetcode.q1_16;

/**
 * 最长回文子串
 */
public class Q5 {
    public static final String CASE = "scscsc";

    /**
     * 动态规划
     */
    public static String solution1(String input) {
        char[] array = input.toCharArray();
        switch (array.length) {
            case 2:
                if (array[0] == array[1]) {
                    return input;
                }
            case 1:
                return input.substring(0, 1);
            default:
                int[][] tmp = new int[array.length][array.length];
                int start = 0, end = 0;
                for (int i = 0; i < array.length - 1; i++) {
                    tmp[i][i] = 1;
                    if (array[i] == array[i + 1]) {
                        tmp[i][i + 1] = 2;
                        if (end - start < 1) {
                            start = i;
                            end = i + 1;
                        }
                    }
                }
                tmp[array.length - 1][array.length - 1] = 1;

                for (int i = 2; i < array.length; i++) {
                    for (int j = 0; j < array.length - i; j++) {
                        if (array[j] == array[j + i]) {
                            if (tmp[j + 1][j + i - 1] != 0) {
                                tmp[j][j + i] = tmp[j + 1][j + i - 1] + 2;
                                if (end - start < i) {
                                    start = j;
                                    end = j + i;
                                }
                                continue;
                            }
                        }
                        tmp[j][j + i] = 0;
                    }
                }
                return input.substring(start, end + 1);
        }
    }
    /**
     * 动态规划，优化版
     */
    public static String solution2(String input) {
        char[] array = input.toCharArray();
        int[][] tmp = new int[2][array.length];
        int start = 0, end = 0;

        for (int i = 0; i < array.length - 1; i++) {
            tmp[0][i] = 1;
            if (array[i] == array[i + 1]) {
                tmp[1][i] = 2;
                if (end - start < 1) {
                    start = i;
                    end = i + 1;
                }
            } else {
                tmp[1][i] = 0;
            }
        }

        for (int i = 2; i < array.length; i++) {
            for (int j = 0; j < array.length - i; j++) {
                if (array[j] == array[j + i] && tmp[i % 2][j + 1] != 0) {
                    tmp[i % 2][j] = tmp[i % 2][j + 1] + 2;
                    if (end - start < i) {
                        start = j;
                        end = j + i;
                    }
                    continue;
                }
                tmp[i % 2][j] = 0;
            }
        }

        return input.substring(start, end + 1);
    }
    /**
     * 中心扩散法
     */
    public static String solution3(String input) {
        char[] array = input.toCharArray();
        int start = 0, end = 0;

        for (int i = 0; i < array.length - 1; i++) {
            int left = i,right = i;
            for (; left >= 0 && right < array.length ; left --, right ++) {
                if (array[left] == array[right]) {
                    continue;
                }
                break;
            }
            if (end - start < (right - left - 2)) {
                start = left + 1;
                end = right - 1;
            }
            left = i;
            right = i + 1;
            for (; left >= 0 && right < array.length ; left --, right ++) {
                if (array[left] == array[right]) {
                    continue;
                }
                break;
            }
            if (end - start < (right - left - 2)) {
                start = left + 1;
                end = right - 1;
            }
        }

        return input.substring(start, end + 1);
    }
    /**
     * 马拉车算法
     */
    public static String solution4(String input) {
        // 先扩展字符串，^、$是不会在字符串中出现的字符
        String newInput = "!#";
        for (int i = 0; i < input.length(); i++) {
            newInput += input.charAt(i) + "#";
        }
        newInput += "$";

        // 先锚定一个最长回文串的右边界 right 和中心点 center
        // 由于回文串中心点两侧一定是一一对应
        // 故位置 i 处的最大回文串长度应等于 2 center - i 处的最大回文串长度
        // 但有例外情况 2 center - i < 0; i + p[2 center - i] >= right
        // 此时需要使用中心扩散
        int[] tmp = new int[newInput.length()];
        int right = 0, center = 0, maxIndex = 0;
        for (int i = 2; i < newInput.length() - 2; i++) {
            if (i >= right || 2 + center - i < 0 ||  i + tmp[2 * center - i] >= right) {
                int j = 1;
                for (; j < tmp.length - i && i - j >= 0; j++) {
                    if (newInput.charAt(i + j) == newInput.charAt(i - j)) {
                        continue;
                    }
                    break;
                }
                tmp[i] = j - 1;
                if (tmp[maxIndex] < tmp[i]) {
                    maxIndex = i;
                }
                if (right < i + j - 1) {
                    right = i + j - 1;
                    center = i;
                }
            } else {
                tmp[i] = tmp[2 * center - i];
            }
        }
        center = (maxIndex - tmp[maxIndex]) / 2;
        return input.substring(center, center + tmp[maxIndex]);
    }

    /**
     * 由于创建新对象会消耗更多资源
     * 尽量不创建新对象反而是最快的
     */
    public static String solution5(String input) {
        C1 demo = new C1();
        return demo.longestPalindrome(input);
    }

    static class C1 {
        int len;
        char[] chars;
        int start;
        int end;

        public String longestPalindrome(String s) {

            start = 0;
            end = 0;
            chars = s.toCharArray();
            len = chars.length;

            process(0);
            return s.substring(start, end + 1);
        }
        public void process(int i){

            if(i >= len || 2 * (len - i) < end - start) return;
            int startN = i, endN = i;
            // 偶数回文串
            while(endN + 1 < len && chars[endN] == chars[endN + 1]) endN++;
            i = endN;
            // 奇数回文串
            while(startN > 0 && endN + 1 < len && chars[startN - 1] == chars[endN + 1]){

                startN--;
                endN++;
            }
            if(endN - startN > end - start){

                end = endN;
                start = startN;
            }
            process(i + 1);
        }
    }
}
