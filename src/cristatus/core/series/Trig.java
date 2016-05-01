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

package cristatus.core.series;

import cristatus.core.Rational;

import java.math.MathContext;

/**
 * This class contains methods that help to approximate the various
 * trigonometric ratios using series expansions. These are accurate only for
 * angles upto 45 degrees (&pi;/4 radians).
 *
 * @author Subhomoy Haldar
 * @version 1.0
 */
public class Trig {

    private static Rational sinSeries(Rational angle, MathContext context) {
        Rational sum = Rational.ZERO;
        Rational partial = Rational.ONE;
        boolean negative = false;

        int limit = (int) (context.getPrecision() * 1.5);

        for (int i = 1; i <= limit; i++) {
            if ((i & 1) == 0) {
                sum = sum.add(negative ? partial.negate() : partial);
                negative ^= true;
            }
            partial = partial.multiply(Rational.valueOf(angle, i));
        }
        return sum;
    }

    private static Rational cosSeries(Rational angle, MathContext context) {
        Rational sum = Rational.ZERO;
        Rational partial = Rational.ONE;
        boolean negative = false;

        int limit = (int) (context.getPrecision() * 1.5);
        for (int i = 1; i <= limit; i++) {
            if ((i & 1) == 1) {
                sum = sum.add(negative ? partial.negate() : partial);
                negative ^= true;
            }
            partial = partial.multiply(Rational.valueOf(angle, i));
        }
        return sum;
    }
}
