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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;

import static cristatus.core.utils.Helper.decimalFrom;
import static cristatus.core.utils.Helper.expandContext;

/**
 * The arbitrary-precision counterpart to {@link Math}, BigMath aims to
 * provide all the commonly used functionality in java.lang.Math with
 * arbitrary precision support.
 * <p>
 * It is advised to learn the use of a {@link MathContext} to help specify
 * the precision of the calculations.
 * <p>
 * It is guaranteed that enough accuracy and relative precision is used such
 * that if the inverse operation is applied to the result of applying an
 * operation (for example, squaring the square-root of a number), the result
 * will be equal to the original Number within the given context.
 *
 * @author Subhomoy Haldar
 * @version 1.0
 */
@SuppressWarnings("WeakerAccess")
public class BigMath {

    /**
     * Returns the hypotenuse of the right-angled triangle constructed with
     * the perpendicular sides of the given length.
     * <p>
     * This method is provided for the sake of shortening of code and ease of
     * access.
     *
     * @param x       One side of the triangle.
     * @param y       Another side of the triangle.
     * @param context The precision for the calculations.
     * @return The hypotenuse of the right-angled triangle constructed with
     * the perpendicular sides of the given length.
     */
    public static BigDecimal hypot(final Number x,
                                   final Number y,
                                   final MathContext context) {
        return hypot(decimalFrom(x, context), decimalFrom(y, context), context);
    }

    /**
     * Returns the hypotenuse of the right-angled triangle constructed with
     * the perpendicular sides of the given length.
     *
     * @param x       One side of the triangle.
     * @param y       Another side of the triangle.
     * @param context The precision for the calculations.
     * @return The hypotenuse of the right-angled triangle constructed with
     * the perpendicular sides of the given length.
     */
    public static BigDecimal hypot(final BigDecimal x,
                                   final BigDecimal y,
                                   MathContext context) {
        return sqrt(x.pow(2).add(y.pow(2)), context);
    }

    /**
     * This method calculates the square-root of the given number according
     * to the given context.
     * <p>
     * This method is provided for the sake of shortening of code and ease of
     * access.
     *
     * @param number  The number whose square-root is required.
     * @param context The context for the calculations.
     * @return The square-root of the given number according to the given
     * context.
     * @throws ArithmeticException If the argument is negative.
     */
    public static BigDecimal sqrt(final Number number,
                                  final MathContext context)
            throws ArithmeticException {
        return nthRoot(decimalFrom(number, context), 2, context);
    }

    /**
     * This method calculates the square-root of the given number according
     * to the given context.
     *
     * @param decimal The number whose square-root is required.
     * @param context The context for the calculations.
     * @return The square-root of the given number according to the given
     * context.
     * @throws ArithmeticException If the argument is negative.
     */
    public static BigDecimal sqrt(final BigDecimal decimal,
                                  final MathContext context)
            throws ArithmeticException {
        return nthRoot(decimal, 2, context);
    }

    /**
     * This method calculates the cube-root of the given number according
     * to the given context.
     * <p>
     * This method is provided for the sake of shortening of code and ease of
     * access.
     *
     * @param number  The number whose square-root is required.
     * @param context The context for the calculations.
     * @return The square-root of the given number according to the given
     * context.
     */
    public static BigDecimal cbrt(final Number number,
                                  final MathContext context) {
        return nthRoot(decimalFrom(number, context), 3, context);
    }

    /**
     * This method calculates the cube-root of the given number according
     * to the given context.
     *
     * @param decimal The number whose square-root is required.
     * @param context The context for the calculations.
     * @return The square-root of the given number according to the given
     * context.
     */
    public static BigDecimal cbrt(final BigDecimal decimal,
                                  final MathContext context) {
        return nthRoot(decimal, 3, context);
    }

    /**
     * This method calculates the "nth-root" of the given number according
     * to the given context.
     * <p>
     * This method is provided for the sake of shortening of code and ease of
     * access.
     *
     * @param number  The number whose square-root is required.
     * @param n       The required base for the root.
     * @param context The context for the calculations.
     * @return The square-root of the given number according to the given
     * context.
     * @throws ArithmeticException If the value of n is even and the argument
     *                             is negative.
     */
    public static BigDecimal nthRoot(final Number number,
                                     final int n,
                                     final MathContext context)
            throws ArithmeticException {
        return nthRoot(decimalFrom(number, context), n, context);
    }

    /**
     * This method calculates the "nth-root" of the given number according
     * to the given context.
     *
     * @param decimal The number whose square-root is required.
     * @param n       The required base for the root.
     * @param context The context for the calculations.
     * @return The square-root of the given number according to the given
     * context.
     * @throws ArithmeticException If the value of n is even and the argument
     *                             is negative.
     */
    public static BigDecimal nthRoot(final BigDecimal decimal,
                                     final int n,
                                     final MathContext context)
            throws ArithmeticException {
        if (decimal.signum() < 0 && (n & 1) == 0) {
            throw new ArithmeticException("Even root of negative number is not defined.");
        }
        BigInteger value = decimal.unscaledValue();
        int newScale = decimal.scale();
        // Because the scale must be in the form of k*n, where k is an integer
        int adjustment = n - newScale % n;
        newScale += adjustment;
        newScale /= n;
        int precision = expandContext(context, 2).getPrecision() + adjustment;
        BigInteger padding = BigInteger.TEN.pow(precision * n + adjustment);
        value = value.multiply(padding);
        BigInteger eps = BigInteger.ONE;
        return new BigDecimal(newtonApproximate(value, eps, n), newScale + precision);
    }

    /**
     * Performs the Newton-Raphson approximation for calculating roots.
     *
     * @param raw The integer whose square root is to be approximated.
     * @param eps The allowed tolerance.
     * @param n   Which root to find out?
     * @return The approximation for the nth root of the integer.
     */
    private static BigInteger newtonApproximate(final BigInteger raw,
                                                final BigInteger eps,
                                                final int n) {
        int nM1 = n - 1;
        BigInteger N = BigInteger.valueOf(n);
        // Make an initial guess equal to 2^(bit length of "raw")
        BigInteger guess = BigInteger.ONE.shiftLeft(raw.bitLength() / n);
        // Delta is a measure of the deviation
        BigInteger delta;
        do {
            BigInteger powered = guess.pow(nM1);
            delta = raw.divide(powered).subtract(guess).divide(N);
            guess = guess.add(delta);
        } while (delta.abs().compareTo(eps) > 0);
        return guess;
    }

}
