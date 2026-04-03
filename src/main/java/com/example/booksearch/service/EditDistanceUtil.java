package com.example.booksearch.service;

public final class EditDistanceUtil {

    private EditDistanceUtil() {}

    public static int levenshtein(String a, String b) {
        a = a.toLowerCase();
        b = b.toLowerCase();

        int[] costs = new int[b.length() + 1];
        for (int j = 0; j < costs.length; j++) {
            costs[j] = j;
        }

        for (int i = 1; i <= a.length(); i++) {
            costs[0] = i;
            int prev = i - 1;

            for (int j = 1; j <= b.length(); j++) {
                int current = costs[j];
                if (a.charAt(i - 1) == b.charAt(j - 1)) {
                    costs[j] = prev;
                } else {
                    costs[j] = 1 + Math.min(prev, Math.min(costs[j - 1], costs[j]));
                }
                prev = current;
            }
        }
        return costs[b.length()];
    }
}