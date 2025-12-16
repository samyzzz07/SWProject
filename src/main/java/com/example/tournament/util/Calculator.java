package com.example.tournament.util;

/**
 * Simple calculator utility for evaluating basic arithmetic expressions.
 * Supports addition (+), subtraction (-), multiplication (*), and division (/).
 */
public class Calculator {
    
    /**
     * Evaluates a simple arithmetic expression.
     * Handles expressions like "+36+", "5+3", "10-2", etc.
     * 
     * @param expression the arithmetic expression to evaluate
     * @return the result of the evaluation
     * @throws IllegalArgumentException if the expression is invalid
     */
    public static double evaluate(String expression) {
        if (expression == null || expression.trim().isEmpty()) {
            throw new IllegalArgumentException("Expression cannot be null or empty");
        }
        
        expression = expression.trim();
        
        // Handle expressions that start with an operator (e.g., "+36+")
        // Treat leading + as 0+36+, leading - as 0-36-
        if (expression.startsWith("+") || expression.startsWith("-")) {
            expression = "0" + expression;
        }
        
        // Handle expressions that end with an operator (e.g., "+36+")
        // Treat trailing + as +36+0, trailing - as -36-0
        if (expression.endsWith("+") || expression.endsWith("-") || 
            expression.endsWith("*") || expression.endsWith("/")) {
            expression = expression + "0";
        }
        
        return evaluateExpression(expression);
    }
    
    /**
     * Internal method to evaluate the expression after preprocessing.
     */
    private static double evaluateExpression(String expression) {
        // Remove all whitespace
        expression = expression.replaceAll("\\s+", "");
        
        // Parse and evaluate with proper operator precedence
        double result = 0;
        char currentOp = '+';
        int i = 0;
        
        while (i < expression.length()) {
            // Parse the next term (number with possible mult/div operations)
            StringBuilder term = new StringBuilder();
            
            // Collect characters for this term
            while (i < expression.length()) {
                char c = expression.charAt(i);
                
                // Check if this is an operator at the current level (+ or -)
                if ((c == '+' || c == '-') && term.length() > 0) {
                    // Make sure it's not part of a number (e.g., after * or /)
                    char lastChar = term.charAt(term.length() - 1);
                    if (lastChar != '*' && lastChar != '/') {
                        break;
                    }
                }
                
                term.append(c);
                i++;
            }
            
            // Evaluate the term (handles mult/div)
            double value = evaluateMultDiv(term.toString());
            
            // Apply the current operation
            if (currentOp == '+') {
                result += value;
            } else if (currentOp == '-') {
                result -= value;
            }
            
            // Get the next operator if available
            if (i < expression.length()) {
                currentOp = expression.charAt(i);
                i++;
            }
        }
        
        return result;
    }
    
    /**
     * Evaluates multiplication and division in a term.
     */
    private static double evaluateMultDiv(String term) {
        if (term == null || term.isEmpty()) {
            return 0;
        }
        
        double result = 0;
        char currentOp = 0;
        int i = 0;
        boolean firstNumber = true;
        
        while (i < term.length()) {
            // Skip leading + sign
            if (i == 0 && term.charAt(i) == '+') {
                i++;
                continue;
            }
            
            // Parse the next number (including potential negative sign)
            StringBuilder numStr = new StringBuilder();
            
            // Handle negative sign at start or after operator
            if (term.charAt(i) == '-' && (i == 0 || currentOp != 0)) {
                numStr.append('-');
                i++;
            }
            
            // Collect digits and decimal point
            while (i < term.length()) {
                char c = term.charAt(i);
                if (Character.isDigit(c) || c == '.') {
                    numStr.append(c);
                    i++;
                } else {
                    break;
                }
            }
            
            if (numStr.length() == 0 || (numStr.length() == 1 && numStr.charAt(0) == '-')) {
                throw new IllegalArgumentException("Invalid expression: missing number");
            }
            
            double value = parseNumber(numStr.toString());
            
            // Apply operation
            if (firstNumber) {
                result = value;
                firstNumber = false;
            } else if (currentOp == '*') {
                result *= value;
            } else if (currentOp == '/') {
                if (value == 0) {
                    throw new ArithmeticException("Division by zero");
                }
                result /= value;
            }
            
            // Get next operator if available
            currentOp = 0;
            if (i < term.length()) {
                char c = term.charAt(i);
                if (c == '*' || c == '/') {
                    currentOp = c;
                    i++;
                }
            }
        }
        
        return result;
    }
    
    /**
     * Parses a string to a number.
     */
    private static double parseNumber(String str) {
        try {
            return Double.parseDouble(str);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number format: " + str);
        }
    }
    
    /**
     * Main method for testing.
     */
    public static void main(String[] args) {
        // Test cases
        System.out.println("Testing Calculator:");
        System.out.println("+36+ = " + evaluate("+36+"));  // Should be 36
        System.out.println("5+3 = " + evaluate("5+3"));    // Should be 8
        System.out.println("10-2 = " + evaluate("10-2"));  // Should be 8
        System.out.println("4*5 = " + evaluate("4*5"));    // Should be 20
        System.out.println("20/4 = " + evaluate("20/4"));  // Should be 5
        System.out.println("2+3*4 = " + evaluate("2+3*4"));// Should be 14
    }
}
