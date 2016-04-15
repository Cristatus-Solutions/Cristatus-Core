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

import cristatus.core.utils.BigMath;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.Random;

import static org.testng.Assert.assertEquals;
import static test.cristatus.core.TestUtils.getContextFor;
import static test.cristatus.core.TestUtils.getRandomBigDecimal;
import static test.cristatus.core.TestUtils.getRandomBigInteger;

/**
 * @author Subhomoy Haldar
 * @version 1.0
 */
public class BigMathTest {

    private static final int TRIES = 20;
    private static final int ROOT_BOUND = 100;
    private static final MathContext CONTEXT = MathContext.DECIMAL128;
    private static final int SQRT_FACTOR = 50;
    private static final int CBRT_FACTOR = 50;
    private static final double DBL_TOLERANCE = 1E-15;

    @Test(timeOut = TRIES * SQRT_FACTOR)
    public void testSquareRoot() throws Exception {
        Random random = new Random();
        for (int i = 0; i < TRIES; i++) {
            // BigInteger version
            BigInteger integer = getRandomBigInteger(random);
            MathContext context = getContextFor(integer, 2);
            BigDecimal nthRoot = BigMath.sqrt(integer, context);
            assertEquals(
                    nthRoot.pow(2, context).toBigInteger(),
                    integer
            );
            // BigDecimal version
            BigDecimal decimal = getRandomBigDecimal(random, CONTEXT);
            BigDecimal sqrRoot = BigMath.sqrt(decimal, CONTEXT);
            assertEquals(
                    sqrRoot.pow(2).round(CONTEXT).stripTrailingZeros(),
                    decimal.stripTrailingZeros()
            );
        }
    }

    @Test(timeOut = TRIES * CBRT_FACTOR)
    public void testCubeRoot() throws Exception {
        Random random = new Random();
        for (int i = 0; i < TRIES; i++) {
            // BigInteger version
            BigInteger integer = getRandomBigInteger(random);
            MathContext context = getContextFor(integer, 3);
            BigDecimal nthRoot = BigMath.cbrt(integer, context);
            assertEquals(
                    nthRoot.pow(3, context).toBigInteger(),
                    integer
            );
            // BigDecimal version
            BigDecimal decimal = getRandomBigDecimal(random, CONTEXT);
            BigDecimal sqrRoot = BigMath.cbrt(decimal, CONTEXT);
            assertEquals(
                    sqrRoot.pow(3).round(CONTEXT).stripTrailingZeros(),
                    decimal.stripTrailingZeros()
            );
        }
    }

    @Test
    public void testNthRoot() throws Exception {
        Random random = new Random();
        for (int i = 0; i < TRIES; i++) {
            int n = 2 + random.nextInt(ROOT_BOUND - 2);
            // BigInteger version
            BigInteger integer = getRandomBigInteger(random);
            MathContext context = getContextFor(integer, n);
            BigDecimal nthRoot = BigMath.nthRoot(integer, n, context);
            assertEquals(
                    nthRoot.pow(n, context).toBigInteger(),
                    integer
            );
            // BigDecimal version
            BigDecimal decimal = getRandomBigDecimal(random, CONTEXT);
            nthRoot = BigMath.nthRoot(decimal, n, CONTEXT);
            assertEquals(
                    nthRoot.pow(n, CONTEXT).stripTrailingZeros(),
                    decimal.stripTrailingZeros()
            );
        }
    }

    @Test
    public void testHypotenuse() throws Exception {
        Random random = new Random();
        for (int i = 0; i < TRIES; i++) {
            double x = random.nextDouble();
            double y = random.nextDouble();
            double h = Math.hypot(x, y);
            BigDecimal decimalX = BigDecimal.valueOf(x);
            BigDecimal decimalY = BigDecimal.valueOf(y);
            assertEquals(
                    BigMath.hypot(decimalX, decimalY, CONTEXT).doubleValue()
                            / h,
                    1,
                    DBL_TOLERANCE
            );
        }
    }
}