package cristatus.core;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.Objects;

/**
 * @author Subhomoy Haldar
 * @version 4.0
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
        return toBigDecimal(MathContext.DECIMAL64).floatValue();
    }

    public double doubleValue() {
        return toBigDecimal(MathContext.DECIMAL128).doubleValue();
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
}
