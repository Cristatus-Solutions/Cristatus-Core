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

/**
 * @author Subhomoy Haldar
 * @version 1.0
 */
public class BigMath {

    public static void main(String[] args) {
        BigDecimal decimal = new BigDecimal("41800179870979097890");
        int n = 2;
        MathContext context = new MathContext(11);
        BigDecimal answer = nthRoot(decimal, n, context);
        System.out.println(answer.toPlainString());
        System.out.println(decimal.toPlainString());
        System.out.println(answer.pow(n, context)
                .stripTrailingZeros().toPlainString());
    }

    public static BigDecimal nthRoot(BigInteger integer, int n,
                                     MathContext context) {
        return nthRoot(new BigDecimal(integer), n, context);
    }

    public static BigDecimal nthRoot(BigDecimal decimal, int n,
                                     MathContext context) {
        BigInteger value = decimal.unscaledValue();
        BigInteger padding = BigInteger.TEN.pow(context.getPrecision() * n);
        value = value.multiply(padding);
        int newScale = decimal.scale() / n;
        BigInteger eps = BigInteger.ONE;
        return new BigDecimal(newtonApproximate(value, eps, n), newScale
                + context.getPrecision());
    }

    private static BigInteger newtonApproximate(BigInteger raw,
                                                BigInteger eps,
                                                int n) {
        int nM1 = n - 1;
        BigInteger N = BigInteger.valueOf(n);
        BigInteger guess = BigInteger.ONE.shiftLeft(
                raw.bitLength() / n
        );
        BigInteger delta;
        do {
            BigInteger powered = guess.pow(nM1);
            delta = raw.divide(powered).subtract(guess).divide(N);
            guess = guess.add(delta);
        } while (delta.abs().compareTo(eps) > 0);
        return guess;
    }

}
