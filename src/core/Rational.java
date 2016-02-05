package core;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;

/**
 * This class represents a Rational number. A rational number can be
 * expressed in the form of p/q, where p and are integers, q &#x2260; 0.
 * <p>
 * All rationals have a finite or repeating decimal sequence.
 *
 * @author Subhomoy Haldar
 * @version 2.0
 */
public class Rational extends Number implements Comparable<Rational> {

    /*
     * Useful and frequently used constants
     */
    /**
     * The Rational representation of zero (0).
     */
    public static final Rational ZERO = new Rational(0, 1);
    /**
     * The Rational representation of one (1).
     */
    public static final Rational ONE = new Rational(1, 1);
    /**
     * The Rational representation of half (1/2).
     */
    public static final Rational HALF = new Rational(1, 2);

    private final BigInteger num;
    private final BigInteger den;

    /**
     * Creates a new Rational with the given numerator and denominator,
     * reduced to its lowest terms.
     *
     * @param numerator   The required numerator.
     * @param denominator The required denominator.
     * @throws ArithmeticException If the denominator supplied is zero.
     * @see #Rational(long, long)
     */
    public Rational(BigInteger numerator,
                    BigInteger denominator) throws ArithmeticException {
        // Denominator should never be zero
        if (denominator.signum() == 0) {
            throw new ArithmeticException("Denominator cannot be zero.");
        }
        // Denominator should be kept positive
        if (denominator.signum() < 0) {
            numerator = numerator.negate();
            denominator = denominator.negate();
        }
        BigInteger gcd = numerator.gcd(denominator);
        // The gcd of a positive number and zero is the positive number
        // (according to the implementation in BigInteger)
        num = numerator.divide(gcd);
        den = denominator.divide(gcd);
    }

    /**
     * Creates a new Rational with the given (long) numerator and denominator,
     * reduced to its lowest terms.
     *
     * @param numerator   The required numerator.
     * @param denominator The required denominator.
     * @throws ArithmeticException If the denominator supplied is zero.
     * @see #Rational(BigInteger, BigInteger)
     */
    public Rational(long numerator, long denominator)
            throws ArithmeticException {
        this(BigInteger.valueOf(numerator), BigInteger.valueOf(denominator));
    }

    /**
     * Returns the numerator.
     *
     * @return The numerator.
     */
    public BigInteger getNumerator() {
        return num;
    }

    /**
     * Returns the denominator.
     *
     * @return The denominator.
     */
    public BigInteger getDenominator() {
        return den;
    }

    /**
     * Returns a String representation of the Rational object, expressed as
     * an improper fraction.
     *
     * @return The improper fraction form of the Rational.
     */
    @Override
    public String toString() {
        if (den.equals(BigInteger.ONE)) {
            return num.toString();
        }
        if (num.equals(BigInteger.ZERO)) {
            return "0";
        }
        return num + "/" + den;
    }

    /**
     * Returns {@code true} if the given object equals (numerically) this
     * Rational.
     *
     * @param other The other object to check.
     * @return {@code true} if the object is equal.
     */
    @Override
    public boolean equals(Object other) {
        if (other instanceof Rational) {
            Rational term = (Rational) other;
            BigInteger part1 = this.num.multiply(term.den);
            BigInteger part2 = term.num.multiply(this.den);
            return part1.equals(part2);
        }
        return false;
    }

    /**
     * Compares the given Rational with the current one. Returns a negative
     * integer if this Rational is smaller, positive if this is larger and
     * zero if both are equal.
     *
     * @param term The other Rational term to compare with.
     * @return A negative integer if this Rational is smaller, positive if this
     * is larger and zero if both are equal.
     */
    @Override
    public int compareTo(Rational term) {
        BigInteger part1 = this.num.multiply(term.den);
        BigInteger part2 = term.num.multiply(this.den);
        return part1.compareTo(part2);
    }

    /*
     * Arithmetic operations.
     */

    /**
     * Returns the additive inverse of this Rational (i.e. adding this and
     * its inverse gives 0, the additive identity).
     *
     * @return The additive inverse (negation) of this Rational.
     */
    public Rational negate() {
        return new Rational(num.negate(), den);
    }

    /**
     * Returns the multiplicative inverse of this Rational (i.e. adding this
     * and its inverse gives 0, the multiplicative identity).
     *
     * @return The multiplicative inverse (reciprocal) of this Rational.
     */
    public Rational reciprocate() {
        return new Rational(den, num);
    }

    /**
     * Returns the sum of this and the Rational term in simplest form.
     *
     * @param term The Rational term to be added with.
     * @return The sum of this and the Rational term in simplest form.
     */
    public Rational add(Rational term) {
        BigInteger part1 = this.num.max(term.den);
        BigInteger part2 = term.num.max(this.den);
        BigInteger num = part1.add(part2);
        return new Rational(num, this.den.max(term.den));
    }

    /**
     * Returns the difference of this and the Rational term in simplest form.
     *
     * @param term The Rational term to be subtracted from this.
     * @return The difference of this and the Rational term in simplest form.
     */
    public Rational subtract(Rational term) {
        return this.add(term.negate());
    }

    /**
     * Returns the product of this and the Rational term in simplest form.
     *
     * @param term The Rational term to be multiplied with.
     * @return The product of this and the Rational term in simplest form.
     */
    public Rational multiply(Rational term) {
        return new Rational(
                this.num.multiply(term.num),
                this.den.multiply(term.den)
        );
    }

    /**
     * Returns the quotient of this and the Rational term in simplest form.
     *
     * @param term The Rational term to be divided with (i.e. the divisor).
     * @return The quotient of this and the Rational term in simplest form.
     */
    public Rational divide(Rational term) {
        return this.multiply(term.reciprocate());
    }

    /**
     * Returns a new Rational whose value is (this<sup>exponent</sup>).
     *
     * @param exponent The desired exponent (which may be negative).
     * @return A new Rational whose value is (this<sup>exponent</sup>).
     */
    public Rational pow(int exponent) {
        if (exponent < 0) {
            return this.pow(-exponent).reciprocate();
        }
        return new Rational(num.pow(exponent), den.pow(exponent));
    }

    /*
     * Methods of java.lang.Number
     */

    /**
     * Returns the value of this Rational as an int.
     *
     * @return The value of this Rational as an int.
     */
    @Override
    public int intValue() {
        return num.divide(den).intValue();
    }

    /**
     * Returns the value of this Rational as a long.
     *
     * @return The value of this Rational as a long.
     */
    @Override
    public long longValue() {
        return num.divide(den).longValue();
    }

    /**
     * Returns the value of this Rational as a float.
     *
     * @return The value of this Rational as a float.
     */
    @Override
    public float floatValue() {
        return toBigDecimal(MathContext.DECIMAL128).floatValue();
    }

    /**
     * Returns the value of this Rational as a double.
     *
     * @return The value of this Rational as a double.
     */
    @Override
    public double doubleValue() {
        return toBigDecimal(MathContext.DECIMAL128).doubleValue();
    }

    /**
     * Approximates this Rational as a BigInteger.
     *
     * @return This Rational as a BigInteger.
     */
    public BigInteger toBigInteger() {
        return num.divide(den);
    }

    /**
     * Approximates this Rational as a BigDecimal with respect to a certain
     * context to limit the accuracy.
     *
     * @param context The context according to which rounding shall take place.
     * @return This Rational as a BigDecimal.
     */
    public BigDecimal toBigDecimal(MathContext context) {
        BigDecimal n = new BigDecimal(num);
        BigDecimal d = new BigDecimal(den);
        return n.divide(d, context);
    }
}
