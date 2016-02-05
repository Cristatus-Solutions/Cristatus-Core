package core;

/**
 * @author Subhomoy Haldar
 * @version 1.0
 */
public class Complex {
    private final Rational real;
    private final Rational imag;

    public Complex(final Rational real,
                   final Rational imaginary) {
        this.real = real;
        imag = imaginary;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        boolean realPresent = !real.equals(Rational.ZERO);
        boolean imagPresent = !imag.equals(Rational.ZERO);

        if (realPresent) {
            builder.append(real);
            if (imagPresent)
                builder.append(" + i").append(imag);
        } else if (imagPresent) {
            builder.append("i").append(imag);
        } else {
            return "0";
        }
        return builder.toString();
    }
}
