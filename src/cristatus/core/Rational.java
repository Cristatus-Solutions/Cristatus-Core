package cristatus.core;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;

/**
 * @author Subhomoy Haldar
 * @version 1.0
 */
public class Rational extends Number {

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

    // Factory methods - always validate arguments

    public static Rational valueOf(Number number) {
        // Quick exits
        if (number == null)
            throw new ArithmeticException("Null argument");
        if (number instanceof Rational)
            return (Rational) number;
        // Approximation isn't any faster than using String...
        if (number instanceof Double || number instanceof Float)
            return processBig(BigDecimal.valueOf(number.doubleValue()));
        // A simple BigInteger surely won't cause any trouble
        if (number instanceof BigInteger)
            return new Rational((BigInteger) number, BigInteger.ONE);
        if (number instanceof BigDecimal)
            return processBig((BigDecimal) number);
        // Unrecognised Number type... fallback to BigDecimal
        return processBig(new BigDecimal(number.toString()));
    }

    public static Rational valueOf(Number num, Number den) {
        BigInteger n;
        BigInteger d;
        BigDecimal n0;
        BigDecimal d0;
        if (isFractional(num) || isFractional(den)) {
            n0 = num instanceof BigDecimal
                    ? (BigDecimal) num
                    : new BigDecimal(num.toString());
            d0 = den instanceof BigDecimal
                    ? (BigDecimal) den
                    : new BigDecimal(den.toString());
            n = n0.unscaledValue();
            d = d0.unscaledValue();
            int scale = n0.scale() - d0.scale();
            if (scale < 0)
                n = n.multiply(BigInteger.TEN.pow(-scale));
            else
                d = d.multiply(BigInteger.TEN.pow(scale));
        } else {
            n = new BigInteger(num.toString());
            d = new BigInteger(den.toString());
        }
        return getCorrected(n, d);
    }

    private static boolean isFractional(Number number) {
        return number instanceof Float || number instanceof Double
                || number instanceof BigDecimal;
    }


    private static Rational processBig(BigDecimal decimal) {
        BigInteger num = decimal.unscaledValue();
        BigInteger den = BigInteger.TEN.pow(-decimal.scale());
        return getCorrected(num, den);
    }

    private static Rational getCorrected(BigInteger num, BigInteger den)
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

    public BigDecimal toBigDecimal(MathContext context) {
        return new BigDecimal(num).divide(new BigDecimal(den), context);
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

    public boolean equals(Object other) {
        if (!(other instanceof Rational)) return false;
        Rational r = (Rational) other;
        return num.signum() == r.num.signum()
                && num.multiply(r.den).equals(den.multiply(r.num));
    }

    public String toString() {
        return num + "/" + den;
    }
}
