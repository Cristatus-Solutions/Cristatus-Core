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

package cristatus.core;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * @author Subhomoy Haldar
 * @version 1.0
 */
@SuppressWarnings("WeakerAccess")
public class Rational extends Number implements Comparable<Rational> {

    // "psf" constants

    public static final Rational ZERO = new Rational(0, 1);
    public static final Rational ONE = new Rational(1, 1);
    public static final Rational TEN = new Rational(10, 1);

    public static final Rational HALF = new Rational(1, 2);
    public static final Rational QUARTER = new Rational(1, 4);
    public static final Rational THIRD = new Rational(1, 3);
    public static final Rational TENTH = new Rational(1, 10);

    private static final MathContext DOUBLE_CONTEXT
            = new MathContext(64, RoundingMode.HALF_UP);

    // Class data

    private final BigInteger num;
    private final BigInteger den;

    // Constructors - use verified arguments. Never make public

    private Rational(final long num, final long den) {
        this(BigInteger.valueOf(num), BigInteger.valueOf(den));
    }

    private Rational(final BigInteger num, final BigInteger den) {
        BigInteger gcd = num.gcd(den);
        this.num = num.divide(gcd);
        this.den = den.divide(gcd);
    }

    // public getters

    public BigInteger getNumerator() {
        return num;
    }

    public BigInteger getDenominator() {
        return den;
    }

    public int signum() {
        return num.signum();
    }

    // Factory methods - always validate arguments

    public static Rational valueOf(Number number)
            throws IllegalArgumentException {
        // Quick exits
        if (number == null)
            throw new IllegalArgumentException("Null argument");
        if (number instanceof Rational)
            return (Rational) number;
        // Approximation isn't any faster than using String...
        if (number instanceof Double || number instanceof Float)
            return rationalFromBigDecimal(BigDecimal.valueOf(number.doubleValue()));
        // A simple BigInteger surely won't cause any trouble
        if (number instanceof BigInteger)
            return new Rational((BigInteger) number, BigInteger.ONE);
        if (number instanceof BigDecimal)
            return rationalFromBigDecimal((BigDecimal) number);
        // Unrecognised Number type... fallback to BigDecimal
        return rationalFromBigDecimal(new BigDecimal(number.toString()));
    }

    private static Rational rationalFromBigDecimal(BigDecimal decimal) {
        BigInteger num = decimal.unscaledValue();
        BigInteger den = BigInteger.ONE;
        int scale = decimal.scale();
        if (scale < 0)
            num = num.multiply(BigInteger.TEN.pow(-scale));
        else
            den = den.multiply(BigInteger.TEN.pow(scale));
        return getCorrectedRational(num, den);
    }

    public static Rational valueOf(Number num, Number den)
            throws IllegalArgumentException {
        if (num == null)
            throw new IllegalArgumentException("Null numerator.");
        if (den == null)
            throw new IllegalArgumentException("Null denominator.");
        if (isFractional(num) || isFractional(den)) {
            return getRationalFromFractions(num, den);
        } else {
            return getRationalFromIntegers(num, den);
        }
    }

    private static boolean isFractional(Number number) {
        return number instanceof Float || number instanceof Double
                || number instanceof BigDecimal;
    }

    private static Rational getRationalFromFractions(Number num,
                                                     Number den) {
        // try to prevent expensive String parsing
        BigDecimal n0 = num instanceof BigDecimal
                ? (BigDecimal) num
                : new BigDecimal(num.toString());
        BigDecimal d0 = den instanceof BigDecimal
                ? (BigDecimal) den
                : new BigDecimal(den.toString());
        BigInteger n = n0.unscaledValue();
        BigInteger d = d0.unscaledValue();
        int scale = n0.scale() - d0.scale();
        if (scale < 0)
            n = n.multiply(BigInteger.TEN.pow(-scale));
        else
            d = d.multiply(BigInteger.TEN.pow(scale));
        return getCorrectedRational(n, d);
    }

    private static Rational getRationalFromIntegers(Number num,
                                                    Number den) {
        // Try to skip toString altogether
        BigInteger n = num instanceof BigInteger
                ? (BigInteger) num
                : BigInteger.valueOf(num.longValue());
        BigInteger d = den instanceof BigInteger
                ? (BigInteger) den
                : BigInteger.valueOf(den.longValue());
        return getCorrectedRational(n, d);
    }

    private static Rational getCorrectedRational(BigInteger num,
                                                 BigInteger den)
            throws IllegalArgumentException {
        // Don't allow zero denominators
        if (den.signum() == 0)
            throw new IllegalArgumentException("Zero denominator.");

        // There is no concept of (+)ve or (-)ve 0 here...
        if (num.signum() == 0) return ZERO;

        // Make sure the numerator carries the sign
        if (den.signum() < 0) {
            num = num.negate();
            den = den.negate();
        }
        // The constructor will reduce the terms
        return new Rational(num, den);
    }

    // Methods from Number.java and extras

    public BigDecimal toBigDecimal(MathContext context) {
        return new BigDecimal(num)
                .divide(new BigDecimal(den), context)
                .stripTrailingZeros();
    }

    public BigInteger toBigInteger() {
        return num.divide(den);
    }

    public int intValue() {
        return toBigInteger().intValue();
    }

    public long longValue() {
        return toBigInteger().longValue();
    }

    public float floatValue() {
        return toBigDecimal(MathContext.DECIMAL128).floatValue();
    }

    public double doubleValue() {
        return toBigDecimal(DOUBLE_CONTEXT).doubleValue();
    }

    // Methods from Object.java

    public boolean equals(Object other) {
        if (!(other instanceof Rational)) return false;
        Rational r = (Rational) other;
        return signum() == r.signum()   // fast reject
                && num.multiply(r.den).equals(den.multiply(r.num));
    }

    public int hashCode() {
        return Objects.hash(num, den);
    }

    public String toString() {
        return num + "/" + den;
    }

    // Comparable compareTo...

    public int compareTo(Rational r) {
        // A big help, this one...
        if (signum() != r.signum())
            return Integer.compare(signum(), r.signum());
        return num.multiply(r.den).compareTo(r.num.multiply(den));
    }

    // Arithmetic methods

    public Rational add(Rational term) {
        BigInteger n1 = this.num.multiply(term.den);
        BigInteger n2 = term.num.multiply(this.den);
        return new Rational(n1.add(n2), this.den.multiply(term.den));
    }

    public Rational subtract(Rational term) {
        BigInteger n1 = this.num.multiply(term.den);
        BigInteger n2 = term.num.multiply(this.den);
        return new Rational(n1.subtract(n2), this.den.multiply(term.den));
    }

    public Rational negate() {
        return new Rational(num.negate(), den);
    }

    public Rational reciprocate() {
        return new Rational(den, num);
    }

    public Rational multiply(Rational term) {
        return new Rational(num.multiply(term.num), den.multiply(term.den));
    }

    public Rational divide(Rational term) {
        return this.multiply(term.reciprocate());
    }

    public Rational pow(Rational power, MathContext context)
            throws ArithmeticException {
        if (num.signum() < 0 && (power.intValue() & 1) != 1) {
            throw new ArithmeticException("Real principal root doesn't exist.");
        }
        int pow = power.num.intValueExact();
        boolean neg = pow < 0;
        pow = neg ? -pow : pow;
        BigInteger n0 = num.pow(pow);
        BigInteger d0 = den.pow(pow);
        int root = power.den.intValueExact();
        if (root == 1)
            return neg
                    ? getCorrectedRational(d0, n0)
                    : getCorrectedRational(n0, d0);
        BigDecimal n = BigMath.nthRoot(n0, root, context);
        BigDecimal d = BigMath.nthRoot(d0, root, context);
        return neg
                ? getRationalFromFractions(d, n)
                : getRationalFromFractions(n, d);
    }
}
