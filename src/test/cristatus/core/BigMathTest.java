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

import cristatus.core.BigMath;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.Random;

import static org.testng.Assert.assertEquals;
import static test.cristatus.core.TestUtils.getRandomBigDecimal;
import static test.cristatus.core.TestUtils.getRandomBigInteger;

/**
 * @author Subhomoy Haldar
 * @version 1.0
 */
public class BigMathTest {

    private static final int TRIES = 50;
    private static final int ROOT_BOUND = 100;
    private static final MathContext CONTEXT = MathContext.DECIMAL128;

    @Test
    public void testSquareRoot() throws Exception {
        Random random = new Random();
        for (int i = 0; i < TRIES; i++) {
            // BigInteger version
            BigInteger integer = getRandomBigInteger(random);
            BigDecimal nthRoot = BigMath.sqrt(integer, CONTEXT);
            assertEquals(
                    nthRoot.pow(2).round(CONTEXT).stripTrailingZeros(),
                    new BigDecimal(integer).round(CONTEXT).stripTrailingZeros()
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

    @Test
    public void testCubeRoot() throws Exception {
        Random random = new Random();
        for (int i = 0; i < TRIES; i++) {
            // BigInteger version
            BigInteger integer = getRandomBigInteger(random);
            BigDecimal nthRoot = BigMath.cbrt(integer, CONTEXT);
            assertEquals(
                    nthRoot.pow(3).round(CONTEXT).stripTrailingZeros(),
                    new BigDecimal(integer).round(CONTEXT).stripTrailingZeros()
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
            BigDecimal nthRoot = BigMath.nthRoot(integer, n, CONTEXT);
            assertEquals(
                    nthRoot.pow(n, CONTEXT).stripTrailingZeros(),
                    new BigDecimal(integer).round(CONTEXT).stripTrailingZeros()
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
}