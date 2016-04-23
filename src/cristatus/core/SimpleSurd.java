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


import cristatus.core.utils.BigMath;
import cristatus.core.utils.Helper;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;

/**
 * @author Subhomoy Haldar
 * @version 1.0
 */
public class SimpleSurd extends Real {

    private final Rational value;
    private final int root;

    public SimpleSurd(final Rational value, final int root) {
        Rational copy = value;
        int n = root;
        if (n < 0) {
            n = -n;
            copy = copy.reciprocate();
        }
        this.value = copy;
        this.root = n;
    }

    // Methods from Number.java and Real.java

    @Override
    public int intValue() {
        return toBigInteger().intValue();
    }

    @Override
    public long longValue() {
        return toBigInteger().longValue();
    }

    @Override
    public float floatValue() {
        return toBigDecimal(MathContext.DECIMAL64).floatValue();
    }

    @Override
    public double doubleValue() {
        return toBigDecimal(MathContext.DECIMAL128).doubleValue();
    }

    @Override
    public BigInteger toBigInteger() {
        int precision = (int) (value.toBigInteger().bitLength() * 0.4 / root);
        return toBigDecimal(new MathContext(precision)).toBigInteger();
    }

    @Override
    public BigDecimal toBigDecimal(MathContext context) {
        BigDecimal num = BigMath.nthRoot(value.getNumerator(), root, context);
        BigDecimal den = BigMath.nthRoot(value.getDenominator(), root, context);
        return num.divide(den, Helper.expandContext(context, root));
    }

    @Override
    public String toString() {
        return "(" + value + ")^(1/" + root + ")";
    }
}
