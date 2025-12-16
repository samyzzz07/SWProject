package com.example.tournament.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for LargeNumberCalculator.
 * Tests various operations on very large numbers.
 */
public class LargeNumberCalculatorTest {
    
    @Test
    public void testAddLargeNumbers() {
        // Test the problem statement case: +33333333333333333333333333333+
        String num = "33333333333333333333333333333";
        String expected = "66666666666666666666666666666";
        String result = LargeNumberCalculator.add(num, num);
        assertEquals(expected, result, "Adding large number to itself should double it");
    }
    
    @Test
    public void testAddSmallNumbers() {
        String result = LargeNumberCalculator.add("5", "3");
        assertEquals("8", result, "5 + 3 should equal 8");
    }
    
    @Test
    public void testAddNegativeNumbers() {
        String result = LargeNumberCalculator.add("-100", "-200");
        assertEquals("-300", result, "-100 + (-200) should equal -300");
    }
    
    @Test
    public void testAddVeryLargeNumbers() {
        String num1 = "999999999999999999999999999999";
        String num2 = "1";
        String expected = "1000000000000000000000000000000";
        String result = LargeNumberCalculator.add(num1, num2);
        assertEquals(expected, result, "Should handle overflow beyond long max");
    }
    
    @Test
    public void testSubtractLargeNumbers() {
        String result = LargeNumberCalculator.subtract("100000000000000000000", "1");
        assertEquals("99999999999999999999", result);
    }
    
    @Test
    public void testMultiplyLargeNumbers() {
        String num = "33333333333333333333333333333";
        String expected = "66666666666666666666666666666";
        String result = LargeNumberCalculator.multiply(num, "2");
        assertEquals(expected, result, "Multiplying by 2 should double the number");
    }
    
    @Test
    public void testDivideLargeNumbers() {
        String result = LargeNumberCalculator.divide("100000000000000000000", "10");
        assertEquals("10000000000000000000", result);
    }
    
    @Test
    public void testDivideByZero() {
        assertThrows(ArithmeticException.class, () -> {
            LargeNumberCalculator.divide("100", "0");
        }, "Division by zero should throw ArithmeticException");
    }
    
    @Test
    public void testNullInput() {
        assertThrows(IllegalArgumentException.class, () -> {
            LargeNumberCalculator.add(null, "5");
        }, "Null input should throw IllegalArgumentException");
    }
    
    @Test
    public void testEmptyInput() {
        assertThrows(IllegalArgumentException.class, () -> {
            LargeNumberCalculator.add("", "5");
        }, "Empty input should throw IllegalArgumentException");
    }
    
    @Test
    public void testWhitespaceOnlyInput() {
        assertThrows(IllegalArgumentException.class, () -> {
            LargeNumberCalculator.add("   ", "5");
        }, "Whitespace-only input should throw IllegalArgumentException");
    }
    
    @Test
    public void testInvalidNumberFormat() {
        assertThrows(IllegalArgumentException.class, () -> {
            LargeNumberCalculator.add("abc", "5");
        }, "Invalid number format should throw IllegalArgumentException");
    }
    
    @Test
    public void testAddZero() {
        String num = "12345678901234567890";
        String result = LargeNumberCalculator.add(num, "0");
        assertEquals(num, result, "Adding zero should return the same number");
    }
    
    @Test
    public void testMultiplicationByZero() {
        String result = LargeNumberCalculator.multiply("999999999999999999999", "0");
        assertEquals("0", result, "Any number multiplied by zero should be zero");
    }
    
    @Test
    public void testSubtractionResultingInZero() {
        String num = "12345678901234567890";
        String result = LargeNumberCalculator.subtract(num, num);
        assertEquals("0", result, "Subtracting a number from itself should equal zero");
    }
    
    @Test
    public void testNegativeResult() {
        String result = LargeNumberCalculator.subtract("5", "10");
        assertEquals("-5", result, "Subtracting larger from smaller should give negative");
    }
}
