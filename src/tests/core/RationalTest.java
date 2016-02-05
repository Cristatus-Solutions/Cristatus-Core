package tests.core;

import core.Rational;
import org.junit.Test;

import java.math.BigInteger;

import static org.junit.Assert.*;

/**
 * This class contains the tests for core.Rational.
 *
 * @author Subhomoy Haldar
 * @version 1.0
 */
public class RationalTest {

    @Test
    public void toStringShouldReturnSimplestForm() throws Exception {
        // #1
        BigInteger integer1 = BigInteger.valueOf(24);
        BigInteger integer2 = BigInteger.valueOf(543);
        Rational rational1 = new Rational(integer1, integer2);
        assertEquals("8/181", rational1.toString());
        // #2
        BigInteger integer3 = BigInteger.valueOf(6453);
        BigInteger integer4 = BigInteger.valueOf(463);
        Rational rational2 = new Rational(integer3, integer4);
        assertEquals("6453/463", rational2.toString());
        // #3
        BigInteger integer5 = BigInteger.valueOf(356);
        BigInteger integer6 = BigInteger.valueOf(426);
        Rational rational3 = new Rational(integer5, integer6);
        assertEquals("178/213", rational3.toString());
    }

    @Test
    public void theNumeratorShouldEndUpNegative() throws Exception {
        BigInteger integer1 = BigInteger.valueOf(-1);
        BigInteger integer2 = BigInteger.valueOf(2);
        Rational rational1 = new Rational(integer1, integer2);
        BigInteger integer3 = BigInteger.valueOf(2);
        BigInteger integer4 = BigInteger.valueOf(-4);
        Rational rational2 = new Rational(integer3, integer4);
        BigInteger integer5 = BigInteger.valueOf(-16);
        BigInteger integer6 = BigInteger.valueOf(32);
        Rational rational3 = new Rational(integer5, integer6);
        assertEquals(rational1, rational2);
        assertEquals(rational1, rational3);
        assertEquals(rational2, rational3);
    }

    @Test(expected = ArithmeticException.class)
    public void zeroDenominatorShouldRaiseException() throws Exception {
        Rational rational = new Rational(42, 0);
        fail("Shouldn't reach here");
    }

    @Test
    public void compareToShouldWork() throws Exception {
        Rational rational1 = new Rational(1, 3);
        Rational rational2 = new Rational(1, 4);
        Rational rational3 = new Rational(1, 2);
        Rational rational4 = new Rational(2, 4);
        assertTrue(rational1.compareTo(rational2) > 0);
        assertTrue(rational3.compareTo(rational1) > 0);
        assertTrue(rational3.compareTo(rational4) == 0);
    }

    @Test
    public void testNegate() throws Exception {

    }

    @Test
    public void testReciprocate() throws Exception {

    }

    @Test
    public void testAdd() throws Exception {

    }

    @Test
    public void testMultiply() throws Exception {

    }

    @Test
    public void testPow() throws Exception {

    }

    @Test
    public void testIntValue() throws Exception {

    }

    @Test
    public void testDoubleValue() throws Exception {

    }

    @Test
    public void testToBigInteger() throws Exception {

    }

    @Test
    public void testToBigDecimal() throws Exception {

    }
}