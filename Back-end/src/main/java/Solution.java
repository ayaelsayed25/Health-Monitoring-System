import java.util.HashMap;

class Solution {
    public int romanToInt(String s) {
        HashMap<String, Integer> map = new HashMap<>();
        map.put("I", 1);
        map.put("V", 5);
        map.put("X", 10);
        map.put("L", 50);
        map.put("C", 100);
        map.put("D", 500);
        map.put("M", 1000);
        map.put("IV", 4);
        map.put("IX", 9);
        map.put("XL", 40);
        map.put("XC", 90);
        map.put("CD", 400);
        map.put("CM", 900);

        int number = 0;

        for(int i = 0 ;i < s.length(); i++) {
            String[] s = new String[3];
            Integer.MAX_VALUE
            if(i+1 < s.length() && map.containsKey(String.valueOf(s.charAt(i) + s.charAt(i+1)))) {
                number += map.get(String.valueOf(s.charAt(i) + s.charAt(++i)));
                //System.out.println(s.charAt(i) + s.charAt(++i));
            }
            else{
               // System.out.println(s.charAt(i));
                number += map.get(String.valueOf(s.charAt(i)));
            }
        }
        return number;

    }

    public static void main(String[] args) {
        Solution solution = new Solution();
        System.out.println(solution.romanToInt("MCMXCIV"));

    }
}