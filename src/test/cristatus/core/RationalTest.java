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

/**
 * @author Subhomoy Haldar
 * @version 1.0
 */
public class RationalTest {

    private static final int TRIES = 500;
    private static final int BIT_BOUND = 4_000;
    private static final int SCALE_BOUND = 10_000;
    private static final MathContext CONTEXT = MathContext.DECIMAL128;
    private static final float FLT_TOLERANCE = 1E-6f;
    private static final double DBL_TOLERANCE = 1E-15;

    @Test
    public void testValueOfSingleArgument() throws Exception {
        Random random = new Random();
        for (int i = 0; i < TRIES; i++) {
            // #1. Integer
            int randomInt = random.nextInt();
            Rational rational1 = Rational.valueOf(randomInt);
            assertEquals(randomInt, rational1.intValue());
            // #2. Long
            long randomLong = random.nextLong();
            Rational rational2 = Rational.valueOf(randomLong);
            assertEquals(randomLong, rational2.longValue());
            // #3. Float
            float randomFloat = random.nextFloat();
            Rational rational3 = Rational.valueOf(randomFloat);
            assertEquals(randomFloat, rational3.floatValue());
            // #4. Double
            double randomDouble = random.nextDouble();
            Rational rational4 = Rational.valueOf(randomDouble);
            assertEquals(randomDouble, rational4.doubleValue());
            // #5. BigInteger
            BigInteger randomBigInteger
                    = new BigInteger(BIT_BOUND, random);
            Rational rational5 = Rational.valueOf(randomBigInteger);
            assertEquals(randomBigInteger, rational5.toBigInteger());
            // #6. BigDecimal
            BigDecimal randomBigDecimal = new BigDecimal(
                    new BigInteger(BIT_BOUND, random), CONTEXT
            ).stripTrailingZeros();
            Rational rational6 = Rational.valueOf(randomBigDecimal);
            assertEquals(randomBigDecimal, rational6.toBigDecimal(CONTEXT));
        }
    }

    @Test
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
            BigInteger integer1 = new BigInteger(BIT_BOUND, random);
            // Zero divisor check
            if (integer1.signum() == 0)
                integer1 = BigInteger.ONE;
            Rational rational2 = Rational.valueOf(double2, integer1);
            BigDecimal quotient3 = BigDecimal.valueOf(double2)
                    .divide(new BigDecimal(integer1), CONTEXT)
                    .stripTrailingZeros();
            assertEquals(quotient3, rational2.toBigDecimal(CONTEXT));
            // #3. int, int
            BigInteger integer2 = new BigInteger(BIT_BOUND, random);
            long long2 = random.nextLong();
            // Zero divisor check
            if (long2 == 0)
                long2 = 1;
            Rational rational3 = Rational.valueOf(integer2, long2);
            BigDecimal quotient4 = new BigDecimal(integer2)
                    .divide(BigDecimal.valueOf(long2), CONTEXT)
                    .stripTrailingZeros();
            assertEquals(quotient4, rational3.toBigDecimal(CONTEXT));
        }
    }

    @Test
    public void testToBigDecimal() throws Exception {
        Random random = new Random();
        for (int i = 0; i < TRIES; i++) {
            BigDecimal decimal = new BigDecimal(
                    new BigInteger(BIT_BOUND, random),
                    random.nextInt(SCALE_BOUND) *
                            (random.nextBoolean() ? -1 : 1),
                    CONTEXT
            ).stripTrailingZeros();
            Rational rational = Rational.valueOf(decimal);
            assertEquals(decimal, rational.toBigDecimal(CONTEXT));
        }
    }

    @Test
    public void testToBigInteger() throws Exception {
        Random random = new Random();
        for (int i = 0; i < TRIES; i++) {
            BigInteger integer1 = new BigInteger(BIT_BOUND, random);
            BigInteger integer2 = new BigInteger(BIT_BOUND, random);
            // Zero divisors are nasty...
            if (integer2.signum() == 0)
                integer2 = BigInteger.ONE;
            Rational rational = Rational.valueOf(integer1, integer2);
            assertEquals(integer1.divide(integer2), rational.toBigInteger());
        }
    }

    @Test
    public void testIntValueAndLongValue() throws Exception {
        Random random = new Random();
        for (int i = 0; i < TRIES; i++) {
            // #1. Int
            int i1 = random.nextInt();
            int i2 = random.nextInt();
            if (i2 == 0)
                i2 = 1;
            Rational rational1 = Rational.valueOf(i1, i2);
            assertEquals(i1 / i2, rational1.intValue());
            // #2. Long
            long l1 = random.nextLong();
            long l2 = random.nextLong();
            if (l2 == 0)
                l2 = 1;
            Rational rational2 = Rational.valueOf(l1, l2);
            assertEquals(l1 / l2, rational2.longValue());
        }
    }

    @Test
    public void testFloatAndDoubleValue() throws Exception {
        Random random = new Random();
        for (int i = 0; i < TRIES; i++) {
            // #1. Float
            float f1 = random.nextFloat();
            float f2 = random.nextFloat();
            if (f2 == 0)
                f2 = 1;
            Rational rational1 = Rational.valueOf(f1, f2);
            float quotient1 = f1 / f2;
            float quotient2 = rational1.floatValue();
            assertEquals(quotient1 / quotient2, 1, FLT_TOLERANCE);
            // #2. Double
            double d1 = random.nextDouble();
            double d2 = random.nextDouble();
            if (d2 == 0)
                d2 = 1;
            Rational rational2 = Rational.valueOf(d1, d2);
            double quotient3 = d1 / d2;
            double quotient4 = rational2.doubleValue();
            assertEquals(quotient3 / quotient4, 1, DBL_TOLERANCE);
        }
    }

    @Test
    public void testEqualsAndHashCode() throws Exception {
        Random random = new Random();
        for (int i = 0; i < TRIES; i++) {
            BigInteger num = new BigInteger(BIT_BOUND, random);
            BigInteger den = new BigInteger(BIT_BOUND, random);
            if (den.signum() == 0)
                den = BigInteger.ONE;
            BigInteger gcd = new BigInteger(BIT_BOUND, random);
            Rational rational1 = Rational.valueOf(num, den);
            Rational rational2 = Rational.valueOf(
                    num.multiply(gcd), den.multiply(gcd)
            );
            assertEquals(rational1, rational2);
            assertEquals(rational1.hashCode(), rational2.hashCode());
            int div = random.nextInt();
            if (div == 0)
                div = 1;
            assertEquals(
                    Rational.valueOf(0, 1),
                    Rational.valueOf(0, div)
            );
        }
    }

    @Test
    public void testToString() throws Exception {
        Random random = new Random();
        for (int i = 0; i < TRIES; i++) {
            BigInteger num = new BigInteger(BIT_BOUND, random);
            BigInteger den = new BigInteger(BIT_BOUND, random);
            if (den.signum() == 0)
                den = BigInteger.ONE;
            BigInteger gcd = num.gcd(den);
            num = num.divide(gcd);
            den = den.divide(gcd);
            assertEquals(num + "/" + den, Rational.valueOf(num, den).toString());
        }
    }

    @Test
    public void testCompareTo() throws Exception {
        Random random = new Random();
        for (int i = 0; i < TRIES; i++) {
            BigInteger num = new BigInteger(BIT_BOUND, random);
            BigInteger den = new BigInteger(BIT_BOUND, random);
            assertEquals(
                    num.compareTo(den),
                    Rational.valueOf(num).compareTo(Rational.valueOf(den))
            );
        }
    }
}