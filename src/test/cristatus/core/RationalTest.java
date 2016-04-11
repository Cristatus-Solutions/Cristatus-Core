/*
 * The MIT License (MIT)
 * ---------------------
 *
 * Copyright (c) 2015-2016 Cristatus Solutions
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to
 * deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
 * OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN
 * NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR
 * THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package test.cristatus.core;


import cristatus.core.Rational;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.Random;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;
import static test.cristatus.core.TestUtils.*;
import static test.cristatus.core.TestUtils.getRandomBigDecimal;

/**
 * @author Subhomoy Haldar
 * @version 1.0
 */
public class RationalTest {

    private static final int TRIES = 200;
    private static final int POW_BOUND = 100;
    private static final MathContext CONTEXT = MathContext.DECIMAL128;
    private static final float FLT_TOLERANCE = 1E-6f;
    private static final double DBL_TOLERANCE = 1E-14;

    // Timing factors... Will make sure tests take a finite time
    private static final int SINGLE_ARGUMENT_FACTOR = 10;
    private static final int DOUBLE_ARGUMENT_FACTOR = 10;
    private static final int TO_BD_FACTOR = 10;
    private static final int TO_BI_FACTOR = 10;
    private static final int INTEGER_VALUE_FACTOR = 5;
    private static final int FRACTION_VALUE_FACTOR = 5;
    private static final int EQUALS_HASHCODE_FACTOR = 30;
    private static final int TO_STRING_FACTOR = 20;
    private static final int COMPARE_TO_FACTOR = 10;
    private static final int DBL_ADDITION_FACTOR = 10;
    private static final int BD_ADDITION_FACTOR = 250;
    private static final int CONSTANTS_CHECK_FACTOR = 1;
    private static final int GETTER_FACTOR = 1;
    private static final int MULTIPLICATION_FACTOR = 20;
    private static final int POWER_FACTOR = 1000;
    private static final int ABSOLUTE_FACTOR = 10;
    private static final int INTEGER_POWER_FACTOR = 1000;

    @Test(timeOut = TRIES * SINGLE_ARGUMENT_FACTOR)
    public void testValueOfSingleArgument() throws Exception {
        Random random = new Random();
        for (int i = 0; i < TRIES; i++) {
            testVOSAWithInteger(random);
            testVOSAWithLong(random);
            testVOSAWithFloat(random);
            testVOSAWithDouble(random);
            testVOSAWithBigInteger(random);
            testVOSAWithBigDecimal(random);
        }
    }

    private static void testVOSAWithInteger(Random random) {
        int randomInt = random.nextInt();
        Rational rational1 = Rational.valueOf(randomInt);
        assertEquals(randomInt, rational1.intValue());
    }

    private static void testVOSAWithLong(Random random) {
        long randomLong = random.nextLong();
        Rational rational2 = Rational.valueOf(randomLong);
        assertEquals(randomLong, rational2.longValue());
    }

    private static void testVOSAWithFloat(Random random) {
        float randomFloat = random.nextFloat();
        Rational rational3 = Rational.valueOf(randomFloat);
        assertEquals(randomFloat, rational3.floatValue());
    }

    private static void testVOSAWithDouble(Random random) {
        double randomDouble = random.nextDouble();
        Rational rational4 = Rational.valueOf(randomDouble);
        assertEquals(randomDouble, rational4.doubleValue());
    }

    private static void testVOSAWithBigInteger(Random random) {
        BigInteger randomBigInteger = getRandomBigInteger(random);
        Rational rational5 = Rational.valueOf(randomBigInteger);
        assertEquals(randomBigInteger, rational5.toBigInteger());
    }

    private static void testVOSAWithBigDecimal(Random random) {
        BigDecimal randomBigDecimal = getRandomBigDecimal(random, CONTEXT);
        Rational rational6 = Rational.valueOf(randomBigDecimal);
        assertEquals(randomBigDecimal, rational6.toBigDecimal(CONTEXT));
    }

    @Test(timeOut = TRIES * DOUBLE_ARGUMENT_FACTOR)
    public void testValueOfDoubleArgument() throws Exception {
        Random random = new Random();
        for (int i = 0; i < TRIES; i++) {
            // #1. int, fraction
            long long1 = random.nextLong();
            double double1 = random.nextDouble();
            Rational rational1 = Rational.valueOf(long1, double1);

            double quotient1 = long1 / double1;
            double quotient2 = rational1.doubleValue();

            assertEquals(quotient1 / quotient2, 1, DBL_TOLERANCE);

            // #2. fraction, BigInt
            double double2 = random.nextDouble();
            BigInteger integer1 = getRandomBigInteger(random);
            integer1 = getOneIfZero(integer1);
            Rational rational2 = Rational.valueOf(double2, integer1);

            BigDecimal quotient3 = BigDecimal.valueOf(double2)
                    .divide(new BigDecimal(integer1), CONTEXT)
                    .stripTrailingZeros();

            assertEquals(quotient3, rational2.toBigDecimal(CONTEXT));

            // #3. int, int
            BigInteger integer2 = getRandomBigInteger(random);
            long long2 = getOneIfZero(random.nextLong());
            Rational rational3 = Rational.valueOf(integer2, long2);

            BigDecimal quotient4 = new BigDecimal(integer2)
                    .divide(BigDecimal.valueOf(long2), CONTEXT)
                    .stripTrailingZeros();

            assertEquals(quotient4, rational3.toBigDecimal(CONTEXT));
        }
    }

    @Test(timeOut = TRIES * TO_BD_FACTOR)
    public void testToBigDecimal() throws Exception {
        Random random = new Random();
        for (int i = 0; i < TRIES; i++) {
            BigDecimal decimal = getRandomBigDecimal(random, CONTEXT);
            Rational rational = Rational.valueOf(decimal);
            assertEquals(decimal, rational.toBigDecimal(CONTEXT));
        }
    }

    @Test(timeOut = TRIES * TO_BI_FACTOR)
    public void testToBigInteger() throws Exception {
        Random random = new Random();
        for (int i = 0; i < TRIES; i++) {
            BigInteger integer1 = getRandomBigInteger(random);
            BigInteger integer2 = getRandomBigInteger(random);
            integer2 = getOneIfZero(integer2);
            Rational rational = Rational.valueOf(integer1, integer2);
            assertEquals(integer1.divide(integer2), rational.toBigInteger());
        }
    }

    @Test(timeOut = TRIES * INTEGER_VALUE_FACTOR)
    public void testIntValueAndLongValue() throws Exception {
        Random random = new Random();
        for (int i = 0; i < TRIES; i++) {
            // #1. Int
            int i1 = random.nextInt();
            int i2 = getOneIfZero(random.nextInt());
            Rational rational1 = Rational.valueOf(i1, i2);
            assertEquals(i1 / i2, rational1.intValue());
            // #2. Long
            long l1 = random.nextLong();
            long l2 = getOneIfZero(random.nextLong());
            Rational rational2 = Rational.valueOf(l1, l2);
            assertEquals(l1 / l2, rational2.longValue());
        }
    }

    @Test(timeOut = TRIES * FRACTION_VALUE_FACTOR)
    public void testFloatAndDoubleValue() throws Exception {
        Random random = new Random();
        for (int i = 0; i < TRIES; i++) {
            // #1. Float
            float f1 = random.nextFloat();
            float f2 = getOneIfZero(random.nextFloat());
            Rational rational1 = Rational.valueOf(f1, f2);
            float quotient1 = f1 / f2;
            float quotient2 = rational1.floatValue();
            assertEquals(quotient1 / quotient2, 1, FLT_TOLERANCE);
            // #2. Double
            double d1 = random.nextDouble();
            double d2 = getOneIfZero(random.nextDouble());
            Rational rational2 = Rational.valueOf(d1, d2);
            double quotient3 = d1 / d2;
            double quotient4 = rational2.doubleValue();
            assertEquals(quotient3 / quotient4, 1, DBL_TOLERANCE);
        }
    }

    @Test(timeOut = TRIES * EQUALS_HASHCODE_FACTOR)
    public void testEqualsAndHashCode() throws Exception {
        Random random = new Random();
        for (int i = 0; i < TRIES; i++) {
            BigInteger num = getRandomBigInteger(random);
            BigInteger den = getRandomBigInteger(random);
            den = getOneIfZero(den);
            BigInteger gcd = getRandomBigInteger(random);
            Rational rational1 = Rational.valueOf(num, den);
            Rational rational2 = Rational.valueOf(
                    num.multiply(gcd), den.multiply(gcd)
            );
            Rational rational3 = Rational.valueOf(
                    num.multiply(gcd), den
            );
            assertEquals(rational1, rational2);
            assertNotEquals(rational1, rational3);
            assertEquals(rational1.hashCode(), rational2.hashCode());
            int div = getOneIfZero(random.nextInt());
            assertEquals(
                    Rational.valueOf(0, 1),
                    Rational.valueOf(0, div)
            );
        }
    }

    @Test(timeOut = TRIES * TO_STRING_FACTOR)
    public void testToString() throws Exception {
        Random random = new Random();
        for (int i = 0; i < TRIES; i++) {
            BigInteger num = getRandomBigInteger(random);
            BigInteger den = getRandomBigInteger(random);
            den = getOneIfZero(den);
            BigInteger gcd = num.gcd(den);
            num = num.divide(gcd);
            den = den.divide(gcd);
            assertEquals(num + "/" + den, Rational.valueOf(num, den).toString());
        }
    }

    @Test(timeOut = TRIES * COMPARE_TO_FACTOR)
    public void testCompareTo() throws Exception {
        Random random = new Random();
        for (int i = 0; i < TRIES; i++) {
            BigDecimal a = getRandomBigDecimal(random, CONTEXT);
            BigDecimal b = getRandomBigDecimal(random, CONTEXT);
            assertEquals(
                    a.compareTo(b),
                    Rational.valueOf(a).compareTo(Rational.valueOf(b))
            );
        }
    }

    @Test(timeOut = TRIES * DBL_ADDITION_FACTOR)
    public void testDoubleAddition() throws Exception {
        Random random = new Random();
        for (int i = 0; i < TRIES; i++) {
            double d1 = random.nextDouble();
            double d2 = random.nextDouble();
            double sum = d1 + d2;
            double dif = d1 - d2;
            Rational r1 = Rational.valueOf(d1);
            Rational r2 = Rational.valueOf(d2);
            assertEquals(sum, r1.add(r2).doubleValue(), DBL_TOLERANCE);
            assertEquals(dif, r1.subtract(r2).doubleValue(), DBL_TOLERANCE);
            assertEquals(dif, r1.add(r2.negate()).doubleValue(), DBL_TOLERANCE);
        }
    }

    @Test(timeOut = TRIES * BD_ADDITION_FACTOR)
    public void testBigDecimalAddition() throws Exception {
        Random random = new Random();
        for (int i = 0; i < TRIES; i++) {
            BigDecimal d1 = getRandomBigDecimal(random, CONTEXT);
            BigDecimal d2 = getRandomBigDecimal(random, CONTEXT);
            BigDecimal sum = d1.add(d2, CONTEXT).stripTrailingZeros();
            BigDecimal dif = d1.subtract(d2, CONTEXT).stripTrailingZeros();
            Rational r1 = Rational.valueOf(d1);
            Rational r2 = Rational.valueOf(d2);
            assertEquals(sum, r1.add(r2).toBigDecimal(CONTEXT));
            assertEquals(dif, r1.subtract(r2).toBigDecimal(CONTEXT));
            assertEquals(dif, r1.add(r2.negate()).toBigDecimal(CONTEXT));
        }
    }

    @Test(timeOut = TRIES * CONSTANTS_CHECK_FACTOR)
    public void testConstants() throws Exception {
        assertEquals(Rational.ZERO, Rational.valueOf(0, 100));
        assertEquals(Rational.ONE, Rational.valueOf(Math.PI, Math.PI));
        assertEquals(Rational.TEN, Rational.valueOf(50, 5));
        assertEquals(Rational.HALF, Rational.valueOf(12, 24.0));
        assertEquals(Rational.QUARTER, Rational.valueOf(15.0, 60));
        assertEquals(Rational.THIRD, Rational.valueOf(5, 15));
        assertEquals(Rational.TENTH, Rational.valueOf(5, 50));
    }

    @Test(timeOut = TRIES * GETTER_FACTOR)
    public void testGetters() throws Exception {
        Random random = new Random();
        BigInteger i1 = getRandomBigInteger(random);
        BigInteger i2 = getRandomBigInteger(random);
        BigInteger gcd = i1.gcd(i2);
        i1 = i1.divide(gcd);
        i2 = i2.divide(gcd);
        Rational rational = Rational.valueOf(i1, i2);
        assertEquals(rational.getNumerator(), i1);
        assertEquals(rational.getDenominator(), i2);
    }

    @Test(timeOut = TRIES * MULTIPLICATION_FACTOR)
    public void testMultiplicationAndDivision() throws Exception {
        Random random = new Random();
        for (int i = 0; i < TRIES; i++) {
            BigDecimal decimal1 = getRandomBigDecimal(random, CONTEXT);
            BigDecimal decimal2 = getRandomBigDecimal(random, CONTEXT);
            BigDecimal product = decimal1.multiply(decimal2, CONTEXT)
                    .stripTrailingZeros();
            BigDecimal quotient = decimal1.divide(decimal2, CONTEXT)
                    .stripTrailingZeros();
            Rational rational1 = Rational.valueOf(decimal1);
            Rational rational2 = Rational.valueOf(decimal2);
            Rational rational3 = rational1.multiply(rational2);
            Rational rational4 = rational1.divide(rational2);
            assertEquals(rational3.toBigDecimal(CONTEXT), product);
            assertEquals(rational4.toBigDecimal(CONTEXT), quotient);
        }
    }

    @Test(timeOut = TRIES * POWER_FACTOR)
    public void testPow() throws Exception {
        Random random = new Random();
        for (int i = 0; i < TRIES; i++) {
            double n = random.nextInt(POW_BOUND) *
                    (random.nextBoolean() ? -1 : 1);
            double d = getOneIfZero(random.nextInt(POW_BOUND));
            double value = Math.abs(random.nextDouble());
            Rational rational = Rational.valueOf(value);
            Rational power = Rational.valueOf(n, d);
            assertEquals(
                    rational.pow(power, CONTEXT).doubleValue()
                            / Math.pow(value, n / d),
                    1,
                    DBL_TOLERANCE
            );
        }
    }

    @Test(timeOut = TRIES * ABSOLUTE_FACTOR)
    public void testAbsolute() throws Exception {
        Random random = new Random();
        for (int i = 0; i < TRIES; i++) {
            BigDecimal decimal = getRandomBigDecimal(random, CONTEXT);
            Rational rational = Rational.valueOf(decimal);
            assertEquals(
                    rational.abs().toBigDecimal(CONTEXT),
                    decimal.abs().round(CONTEXT).stripTrailingZeros()
            );
        }
    }

    @Test(timeOut = TRIES * INTEGER_POWER_FACTOR)
    public void testIntegerPowers() throws Exception {
        Random random = new Random();
        for (int i = 0; i < TRIES; i++) {
            int power = random.nextInt(POW_BOUND);
            BigDecimal decimal = getRandomBigDecimal(random, CONTEXT);
            Rational rational = Rational.valueOf(decimal);
            rational = rational.pow(Rational.valueOf(power), null);
            assertEquals(
                    rational.toBigDecimal(CONTEXT),
                    decimal.pow(power).round(CONTEXT).stripTrailingZeros()
            );
        }
    }
}