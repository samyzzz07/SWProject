package com.example.tournament.util;

/**
 * Simple calculator utility for performing arithmetic operations.
 */
public class Calculator {
    
    /**
     * Calculates the sum of multiple numbers.
     * 
     * @param numbers variable number of integers to sum
     * @return the sum of all provided numbers
     */
    public static int sum(int... numbers) {
        int result = 0;
        for (int number : numbers) {
            result += number;
        }
        return result;
    }
    
    /**
     * Main method for testing the calculator.
     * Demonstrates the calculation: 3 + 3 + 6 = 12
     */
    public static void main(String[] args) {
        int result = sum(3, 3, 6);
        System.out.println("3 + 3 + 6 = " + result);
    }
}
