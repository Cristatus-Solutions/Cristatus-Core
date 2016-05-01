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
import cristatus.core.utils.Helper;

import java.math.MathContext;

/**
 * @author Subhomoy Haldar
 * @version 1.0
 */
public class Exp {

    public static Rational expSeries(Rational term, MathContext context) {
        Rational sum = Rational.ZERO;
        Rational partial = Rational.ONE;

        int limit = (int) (context.getPrecision() * 1.5);
        MathContext workContext = Helper.expandContext(context, limit);

        for (int i = 1; i <= limit; i++) {
            sum = sum.add(partial).dropTo(workContext);

            partial = partial.multiply(term);
            partial = partial.divide(Rational.valueOf(i));
        }
        return sum.dropTo(context);
    }

    public static Rational logSeries(Rational term, MathContext context) {
        int limit = context.getPrecision() << 2;
        MathContext workContext = Helper.expandContext(context, limit);

        Rational part = Rational.ONE.subtract(Rational.valueOf(
                2, term.add(Rational.ONE)
        ));

        Rational sum = Rational.ZERO;
        Rational partial = part;
        part = part.pow(2);


        for (int i = 0; i < limit; i++) {
            sum = sum.add(partial.divide(Rational.valueOf((i << 1) + 1)));
            sum = sum.dropTo(workContext);

            partial = partial.multiply(part);
        }

        return sum.multiply(Rational.valueOf(2)).dropTo(workContext);
    }
}
