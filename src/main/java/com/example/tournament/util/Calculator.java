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
        
        // First pass: handle multiplication and division (higher precedence)
        String[] addSubTerms = expression.split("(?=[+\\-])|(?<=[+\\-])");
        double result = 0;
        String currentOp = "+";
        
        for (int i = 0; i < addSubTerms.length; i++) {
            String term = addSubTerms[i].trim();
            
            if (term.isEmpty()) continue;
            
            if (term.equals("+") || term.equals("-")) {
                currentOp = term;
            } else {
                double value = evaluateMultDiv(term);
                
                if (currentOp.equals("+")) {
                    result += value;
                } else if (currentOp.equals("-")) {
                    result -= value;
                }
            }
        }
        
        return result;
    }
    
    /**
     * Evaluates multiplication and division in a term.
     */
    private static double evaluateMultDiv(String term) {
        // Handle multiplication and division
        String[] factors = term.split("(?=[*/])|(?<=[*/])");
        double result = 0;
        String currentOp = null;
        boolean initialized = false;
        
        for (int i = 0; i < factors.length; i++) {
            String factor = factors[i].trim();
            
            if (factor.isEmpty()) continue;
            
            if (factor.equals("*") || factor.equals("/")) {
                currentOp = factor;
            } else {
                double value = parseNumber(factor);
                
                if (!initialized) {
                    result = value;
                    initialized = true;
                } else if (currentOp != null) {
                    if (currentOp.equals("*")) {
                        result *= value;
                    } else if (currentOp.equals("/")) {
                        if (value == 0) {
                            throw new ArithmeticException("Division by zero");
                        }
                        result /= value;
                    }
                    currentOp = null;
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
