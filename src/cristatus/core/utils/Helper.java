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

import cristatus.core.Rational;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;

/**
 * This class contains several utility methods that help to determine the
 * category of a {@link Number}, such as to test if it is fractional or
 * integral, or to convert from one numeric type to another with least amount
 * of code. The main intent of this class is to make the code readable.
 *
 * @author Subhomoy Haldar
 * @version 1.0
 */
public class Helper {
    /**
     * Helper method to check if a number is of a fractional type. Helps to
     * make the code readable.
     *
     * @param number The number whose type to check.
     * @return {@code true} if the number is a float, double or
     * {@link BigDecimal}.
     */
    public static boolean isFractional(Number number) {
        if (number instanceof Rational) {
            Rational rational = (Rational) number;
            return !rational.isInteger();
        }
        return number instanceof Float || number instanceof Double
                || number instanceof BigDecimal;
    }

    /**
     * This method takes any type of number and converts it to a
     * {@link BigDecimal} with the desired accuracy (in case of a Rational).
     *
     * @param number  The number to be converted.
     * @param context The required precision (for Rationals).
     * @return The number as a {@link BigDecimal}.
     */
    static BigDecimal decimalFrom(Number number, MathContext context) {
        if (number instanceof BigDecimal) {
            return (BigDecimal) number;
        }
        return (number instanceof Rational)
                ? ((Rational) number).toBigDecimal(context)
                : new BigDecimal(number.toString());
    }

    /**
     * This method takes any type of number and converts it into a
     * {@link BigInteger}. In case of fractional numbers, the fractional part
     * (after the decimal) is truncated. In other words, it performs integral
     * division and returns the quotient, discarding the remainder.
     *
     * @param number The number to be converted.
     * @return The number as {@link BigInteger}.
     */
    static BigInteger integerFrom(Number number) {
        if (number instanceof BigInteger) {
            return (BigInteger) number;
        }
        return (number instanceof Rational)
                ? ((Rational) number).toBigInteger()
                : new BigInteger(number.toString());
    }

    /**
     * This method is useful when required to extend the precision of a
     * calculation to have some guard digits to help preserve accuracy.
     *
     * @param context The {@link MathContext} to expand from.
     * @param delta   The number of guard digits to add.
     * @return The expanded {@link MathContext}.
     */
    public static MathContext expandContext(MathContext context, int delta) {
        int newPrecision = context.getPrecision() + delta;
        return new MathContext(newPrecision, context.getRoundingMode());
    }
}
