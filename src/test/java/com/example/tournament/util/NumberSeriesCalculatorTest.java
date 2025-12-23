package com.example.tournament.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

/**
 * Test class for NumberSeriesCalculator.
 */
public class NumberSeriesCalculatorTest {
    
    @Test
    public void testEvaluateAdditionExpression_BasicCase() {
        String expression = "+36+3+366+336+366+36+36";
        long result = NumberSeriesCalculator.evaluateAdditionExpression(expression);
        assertEquals(1179, result, "The sum of 36+3+366+336+366+36+36 should be 1179");
    }
    
    @Test
    public void testEvaluateAdditionExpression_WithoutLeadingPlus() {
        String expression = "36+3+366+336+366+36+36";
        long result = NumberSeriesCalculator.evaluateAdditionExpression(expression);
        assertEquals(1179, result, "The sum should be 1179 regardless of leading plus");
    }
    
    @Test
    public void testEvaluateAdditionExpression_SingleNumber() {
        String expression = "42";
        long result = NumberSeriesCalculator.evaluateAdditionExpression(expression);
        assertEquals(42, result, "Single number should return itself");
    }
    
    @Test
    public void testEvaluateAdditionExpression_TwoNumbers() {
        String expression = "10+20";
        long result = NumberSeriesCalculator.evaluateAdditionExpression(expression);
        assertEquals(30, result, "10+20 should equal 30");
    }
    
    @Test
    public void testEvaluateAdditionExpression_WithWhitespace() {
        String expression = " 10 + 20 + 30 ";
        long result = NumberSeriesCalculator.evaluateAdditionExpression(expression);
        assertEquals(60, result, "Whitespace should be ignored");
    }
    
    @Test
    public void testEvaluateAdditionExpression_NullInput() {
        assertThrows(IllegalArgumentException.class, () -> {
            NumberSeriesCalculator.evaluateAdditionExpression(null);
        }, "Null input should throw IllegalArgumentException");
    }
    
    @Test
    public void testEvaluateAdditionExpression_EmptyInput() {
        assertThrows(IllegalArgumentException.class, () -> {
            NumberSeriesCalculator.evaluateAdditionExpression("");
        }, "Empty input should throw IllegalArgumentException");
    }
    
    @Test
    public void testEvaluateAdditionExpression_InvalidCharacter() {
        assertThrows(IllegalArgumentException.class, () -> {
            NumberSeriesCalculator.evaluateAdditionExpression("10*20");
        }, "Invalid character should throw IllegalArgumentException");
    }
    
    @Test
    public void testCalculateSumList_BasicCase() {
        List<Long> numbers = Arrays.asList(36L, 3L, 366L, 336L, 366L, 36L, 36L);
        long result = NumberSeriesCalculator.calculateSum(numbers);
        assertEquals(1179, result, "Sum of list should be 1179");
    }
    
    @Test
    public void testCalculateSumList_EmptyList() {
        List<Long> numbers = Arrays.asList();
        long result = NumberSeriesCalculator.calculateSum(numbers);
        assertEquals(0, result, "Sum of empty list should be 0");
    }
    
    @Test
    public void testCalculateSumList_NullList() {
        long result = NumberSeriesCalculator.calculateSum((List<Long>) null);
        assertEquals(0, result, "Sum of null list should be 0");
    }
    
    @Test
    public void testCalculateSumVarargs_BasicCase() {
        long result = NumberSeriesCalculator.calculateSum(36, 3, 366, 336, 366, 36, 36);
        assertEquals(1179, result, "Sum using varargs should be 1179");
    }
    
    @Test
    public void testCalculateSumVarargs_NoArguments() {
        long result = NumberSeriesCalculator.calculateSum();
        assertEquals(0, result, "Sum of no arguments should be 0");
    }
    
    @Test
    public void testCalculateSumVarargs_SingleArgument() {
        long result = NumberSeriesCalculator.calculateSum(100);
        assertEquals(100, result, "Sum of single argument should be that argument");
    }
    
    @Test
    public void testLargeNumbers() {
        String expression = "1000000+2000000+3000000";
        long result = NumberSeriesCalculator.evaluateAdditionExpression(expression);
        assertEquals(6000000, result, "Should handle large numbers correctly");
    }
    
    @Test
    public void testCalculateSumList_WithNullElement() {
        List<Long> numbers = Arrays.asList(10L, null, 20L);
        assertThrows(IllegalArgumentException.class, () -> {
            NumberSeriesCalculator.calculateSum(numbers);
        }, "List with null element should throw IllegalArgumentException");
    }
    
    @Test
    public void testEvaluateAdditionExpression_MalformedNumber() {
        assertThrows(IllegalArgumentException.class, () -> {
            // This would cause a NumberFormatException internally
            NumberSeriesCalculator.evaluateAdditionExpression("999999999999999999999999999999");
        }, "Malformed number should throw IllegalArgumentException");
    }
}
