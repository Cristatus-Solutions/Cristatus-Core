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
import java.util.HashMap;
import java.util.Map;

/**
 * @author Subhomoy Haldar
 * @version 1.0
 */
public class PiGenerator {

    private static Map<MathContext, Rational> PI_CACHE = new HashMap<>(10);

    private static final BigInteger _1103 = BigInteger.valueOf(1103);
    private static final BigInteger _26390 = BigInteger.valueOf(26390);
    private static final BigInteger _24591257856 = BigInteger.valueOf(396).pow(4);

    public static Rational of(MathContext context) {
        // Generation is expensive... use a cache
        if (PI_CACHE.containsKey(context)) {
            return PI_CACHE.get(context);
        }
        final int iterations = context.getPrecision() / 7;
        BigDecimal root2times2 = BigMath.sqrt(8, context);
        Rational frontConstant = Rational.valueOf(root2times2, 9801);

        BigInteger _26390k = BigInteger.ZERO;
        BigInteger _24591257856pk = BigInteger.ONE;

        Rational sum = Rational.ZERO;
        for (int k = 0; k < iterations; k++) {
            BigInteger num = Factorial.verified(BigInteger.valueOf(k << 2));
            num = num.multiply(_1103.add(_26390k));
            BigInteger den = Factorial.verified(BigInteger.valueOf(k)).pow(4);
            den = den.multiply(_24591257856pk);

            _26390k = _26390k.add(_26390);
            _24591257856pk = _24591257856pk.multiply(_24591257856);

            sum = sum.add(Rational.valueOf(num, den));
        }

        // Add the generated value to cache, for reuse later
        Rational pi = (frontConstant.multiply(sum)).reciprocate();
        PI_CACHE.put(context, pi);
        return pi;
    }
}
