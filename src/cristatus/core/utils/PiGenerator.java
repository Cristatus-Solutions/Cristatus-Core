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
import java.math.MathContext;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;

/**
 * This class acts as the access point for the &pi; approximation system.
 * This class validates the argument and invokes parallel computation threads.
 *
 * @author Subhomoy Haldar
 * @version 1.0
 */
public class PiGenerator {

    // The cache to store the computed value of pi, for reuse.
    private static Map<MathContext, Rational> PI_CACHE = new HashMap<>(10);

    /**
     * This method returns &pi; approximated to the desired precision. It
     * implements the Ramanujan formula in parallel to generate more digits
     * of the constant.
     *
     * @param context The {@link MathContext} to specify the precision of
     *                calculation.
     * @return &pi; with the desired precision.
     */
    public static Rational obtainRational(MathContext context) {
        // No nulls please
        if (context == null) {
            throw new ArithmeticException("A non-null context is needed.");
        }
        // Generation is expensive... use a cache
        if (PI_CACHE.containsKey(context)) {
            return PI_CACHE.get(context);
        }
        // Escape calculation altogether for under 16 digits of precision
        if (context.getPrecision() <= MathContext.DECIMAL64.getPrecision()) {
            BigDecimal pi = BigDecimal.valueOf(Math.PI).round(context);
            return Rational.valueOf(pi);
        }
        final int iterations
                = (context.getPrecision() >>> 3)    // precision / 8
                + (context.getPrecision() >>> 5);   // precision / 32
        BigDecimal root2times2 = BigMath.sqrt(8, context);
        Rational frontConstant = Rational.valueOf(root2times2, 9801);

        ForkJoinPool pool = new ForkJoinPool();
        Rational sum = pool.invoke(new RamanujanAdder(0, iterations));

        // Ramanujan's formula generates 1/pi
        Rational pi = (frontConstant.multiply(sum)).reciprocate();
        // Add the generated value to cache, for reuse later
        PI_CACHE.put(context, pi);
        return pi;
    }
}
