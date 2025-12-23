package com.example.tournament.util;

import java.math.BigInteger;

/**
 * Utility class for performing arithmetic operations on very large numbers
 * that exceed the capacity of standard int or long types.
 * 
 * This class uses BigInteger to handle numbers of arbitrary size.
 */
public class LargeNumberCalculator {
    
    /**
     * Adds two large numbers represented as strings.
     * 
     * @param num1 the first number as a string
     * @param num2 the second number as a string
     * @return the sum of the two numbers as a string
     * @throws IllegalArgumentException if either input is null, empty, or not a valid number
     */
    public static String add(String num1, String num2) {
        validateInput(num1, "First number");
        validateInput(num2, "Second number");
        
        try {
            BigInteger bigNum1 = new BigInteger(num1);
            BigInteger bigNum2 = new BigInteger(num2);
            BigInteger result = bigNum1.add(bigNum2);
            return result.toString();
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number format: " + e.getMessage(), e);
        }
    }
    
    /**
     * Subtracts the second number from the first number.
     * 
     * @param num1 the first number as a string
     * @param num2 the second number as a string
     * @return the difference (num1 - num2) as a string
     * @throws IllegalArgumentException if either input is null, empty, or not a valid number
     */
    public static String subtract(String num1, String num2) {
        validateInput(num1, "First number");
        validateInput(num2, "Second number");
        
        try {
            BigInteger bigNum1 = new BigInteger(num1);
            BigInteger bigNum2 = new BigInteger(num2);
            BigInteger result = bigNum1.subtract(bigNum2);
            return result.toString();
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number format: " + e.getMessage(), e);
        }
    }
    
    /**
     * Multiplies two large numbers.
     * 
     * @param num1 the first number as a string
     * @param num2 the second number as a string
     * @return the product of the two numbers as a string
     * @throws IllegalArgumentException if either input is null, empty, or not a valid number
     */
    public static String multiply(String num1, String num2) {
        validateInput(num1, "First number");
        validateInput(num2, "Second number");
        
        try {
            BigInteger bigNum1 = new BigInteger(num1);
            BigInteger bigNum2 = new BigInteger(num2);
            BigInteger result = bigNum1.multiply(bigNum2);
            return result.toString();
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number format: " + e.getMessage(), e);
        }
    }
    
    /**
     * Divides the first number by the second number.
     * 
     * @param num1 the dividend as a string
     * @param num2 the divisor as a string
     * @return the quotient as a string
     * @throws IllegalArgumentException if either input is null, empty, or not a valid number
     * @throws ArithmeticException if division by zero is attempted
     */
    public static String divide(String num1, String num2) {
        validateInput(num1, "First number");
        validateInput(num2, "Second number");
        
        try {
            BigInteger bigNum1 = new BigInteger(num1);
            BigInteger bigNum2 = new BigInteger(num2);
            
            if (bigNum2.equals(BigInteger.ZERO)) {
                throw new ArithmeticException("Division by zero is not allowed");
            }
            
            BigInteger result = bigNum1.divide(bigNum2);
            return result.toString();
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number format: " + e.getMessage(), e);
        }
    }
    
    /**
     * Validates that the input string is not null, not empty, and can be parsed as a number.
     * 
     * @param input the input string to validate
     * @param fieldName the name of the field for error messages
     * @throws IllegalArgumentException if the input is invalid
     */
    private static void validateInput(String input, String fieldName) {
        if (input == null) {
            throw new IllegalArgumentException(fieldName + " cannot be null");
        }
        String trimmedInput = input.trim();
        if (trimmedInput.isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be empty");
        }
    }
    
    /**
     * Main method to demonstrate the calculator functionality.
     * Example: handles the problem statement "+33333333333333333333333333333+"
     * 
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        // Example from problem statement: +33333333333333333333333333333+
        // This represents adding the large number to itself
        String largeNumber = "33333333333333333333333333333";
        
        System.out.println("Large Number Calculator Demo");
        System.out.println("============================");
        System.out.println();
        
        // Addition example
        String sum = add(largeNumber, largeNumber);
        System.out.println("Addition: " + largeNumber + " + " + largeNumber);
        System.out.println("Result: " + sum);
        System.out.println();
        
        // More examples
        String num1 = "999999999999999999999999999999";
        String num2 = "1";
        System.out.println("Addition: " + num1 + " + " + num2);
        System.out.println("Result: " + add(num1, num2));
        System.out.println();
        
        // Multiplication example
        String product = multiply(largeNumber, "2");
        System.out.println("Multiplication: " + largeNumber + " * 2");
        System.out.println("Result: " + product);
        System.out.println();
        
        // Verify the addition and multiplication give the same result
        if (sum.equals(product)) {
            System.out.println("✓ Verification: " + largeNumber + " + " + largeNumber + " = " + largeNumber + " * 2");
        }
    }
}
