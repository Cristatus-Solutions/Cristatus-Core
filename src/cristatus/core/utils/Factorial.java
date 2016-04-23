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

package cristatus.core.utils;

import java.math.BigInteger;
import java.util.concurrent.ForkJoinPool;

/**
 * This class provides the functionality of calculating the factorial of
 * non-negative integers in parallel, thereby boosting performance.
 *
 * @author Subhomoy Haldar
 * @version 1.0
 */
public class Factorial {

    private static final String FAIL_MESSAGE =
            "The factorial function is only defined for non-negative integers.";

    /**
     * This method accepts any type of integer - int, long, and
     * even Rationals encapsulating integers and returns the factorial of
     * thai non-negative integer.
     *
     * @param integer The non-negative integer whose factorial to calculate.
     * @return The factorial of the verified integer.
     * @throws ArithmeticException If the number is not an integer or if it
     *                             is negative.
     */
    public static BigInteger of(Number integer) throws ArithmeticException {
        if (Helper.isFractional(integer)) {
            throw new ArithmeticException(FAIL_MESSAGE);
        }
        return of(Helper.integerFrom(integer));
    }

    /**
     * This method returns the factorial of the the given BigInteger.
     *
     * @param number The non-negative integer whose factorial to calculate.
     * @return The factorial of the verified integer.
     * @throws ArithmeticException If the number is not an integer or if it
     *                             is negative.
     */
    public static BigInteger of(BigInteger number) throws ArithmeticException {
        if (number.signum() < 0) {
            throw new ArithmeticException(FAIL_MESSAGE);
        }
        return verified(number);
    }

    /**
     * Control is delegated to this method after the verification has been
     * done. This method invokes the {@link SequentialMultiplier} via a
     * {@link ForkJoinPool}.
     *
     * @param number The  verified non-negative integer whose factorial to
     *               calculate.
     * @return The factorial of the verified integer.
     */
    static BigInteger verified(BigInteger number) {
        ForkJoinPool pool = new ForkJoinPool();
        return pool.invoke(new SequentialMultiplier(BigInteger.ONE, number));
    }
}
