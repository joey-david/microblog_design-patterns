package fr.univ_lyon1.info.m1.tp_test;

public class CharManipulator implements ICharManipulator {
    @Override
    public String invertOrder(String s) {
        if (s == null) {
            return null;
        }

        char[] chars = s.toCharArray();
        int left = 0;
        int right = chars.length - 1;

        while (left < right) {
            char tmp = chars[left];
            chars[left] = chars[right];
            chars[right] = tmp;
            left++;
            right--;
        }

        return new String(chars);
    }

    @Override
    public String invertCase(String s) {
        if (s == null) {
            return null;
        }

        char[] chars = s.toCharArray();
        for(int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if(Character.isUpperCase(c)) {
                chars[i] = Character.toLowerCase(c);
            } else if (Character.isLowerCase(c)) {
                chars[i] = Character.toUpperCase(c);
            }
        }

        return new String(chars);
    }

    @Override
    /** Remove all occurrences of string s1 from string s2 */
    public String removePattern(String input, String pattern) {
        if (input == null || pattern == null || pattern.isEmpty()) {
            return input;  // Return the input if pattern is null or empty
        }
        return input.replace(pattern, "");
    }

}

