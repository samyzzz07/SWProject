package com.example.tournament.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for calculating sums of number series and evaluating arithmetic expressions.
 */
public class NumberSeriesCalculator {
    
    /**
     * Evaluates an arithmetic expression containing addition operations.
     * Supports expressions like "+36+3+366+336+366+36+36"
     * 
     * @param expression The arithmetic expression to evaluate
     * @return The sum of all numbers in the expression
     * @throws IllegalArgumentException if the expression is invalid
     */
    public static long evaluateAdditionExpression(String expression) {
        if (expression == null || expression.trim().isEmpty()) {
            throw new IllegalArgumentException("Expression cannot be null or empty");
        }
        
        // Remove all whitespace
        expression = expression.replaceAll("\\s+", "");
        
        // Parse numbers from the expression
        List<Long> numbers = parseNumbers(expression);
        
        // Calculate sum
        return calculateSum(numbers);
    }
    
    /**
     * Parses numbers from an addition expression.
     * 
     * @param expression The expression to parse
     * @return List of numbers found in the expression
     * @throws IllegalArgumentException if the expression contains invalid characters or malformed numbers
     */
    private static List<Long> parseNumbers(String expression) {
        List<Long> numbers = new ArrayList<>();
        StringBuilder currentNumber = new StringBuilder();
        
        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);
            
            if (c == '+') {
                // Skip leading + signs
                if (currentNumber.length() > 0) {
                    try {
                        numbers.add(Long.parseLong(currentNumber.toString()));
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException("Invalid number format: " + currentNumber.toString(), e);
                    }
                    currentNumber = new StringBuilder();
                }
            } else if (Character.isDigit(c)) {
                currentNumber.append(c);
            } else {
                throw new IllegalArgumentException("Invalid character in expression: " + c);
            }
        }
        
        // Add the last number if present
        if (currentNumber.length() > 0) {
            try {
                numbers.add(Long.parseLong(currentNumber.toString()));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid number format: " + currentNumber.toString(), e);
            }
        }
        
        return numbers;
    }
    
    /**
     * Calculates the sum of a list of numbers.
     * 
     * @param numbers List of numbers to sum
     * @return The sum of all numbers
     * @throws IllegalArgumentException if the list contains null elements
     */
    public static long calculateSum(List<Long> numbers) {
        if (numbers == null || numbers.isEmpty()) {
            return 0;
        }
        
        long sum = 0;
        for (Long number : numbers) {
            if (number == null) {
                throw new IllegalArgumentException("List contains null element");
            }
            sum += number;
        }
        return sum;
    }
    
    /**
     * Calculates the sum of a series of numbers provided as varargs.
     * 
     * @param numbers The numbers to sum
     * @return The sum of all numbers
     */
    public static long calculateSum(long... numbers) {
        long sum = 0;
        for (long number : numbers) {
            sum += number;
        }
        return sum;
    }
}
