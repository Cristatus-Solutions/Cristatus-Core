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

package test.cristatus.core;

import cristatus.core.Rational;
import cristatus.core.series.Exp;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Random;

import static org.testng.Assert.assertEquals;

/**
 * @author Subhomoy Haldar
 * @version 1.0
 */
public class ExpTest {
    private static final int TRIES = 2;
    private static final MathContext CONTEXT = MathContext.DECIMAL128;

    @Test
    public void testSeries() throws Exception {
        Random random = new Random();
        for (int i = 0; i < TRIES; i++) {

            StringBuilder builder = new StringBuilder(CONTEXT.getPrecision());
            for (int j = 0; j < CONTEXT.getPrecision(); j++) {
                builder.append(random.nextInt(10));
            }
            String data = builder.toString();
            BigDecimal decimal = new BigDecimal("." + data)
                    .round(CONTEXT).stripTrailingZeros();

            Rational val = Rational.valueOf(decimal);

            Rational exp1 = Exp.expSeries(val, CONTEXT);
            Rational log1 = Exp.logSeries(exp1, CONTEXT);
            assertEquals(log1.toBigDecimal(CONTEXT), decimal);

            Rational log2 = Exp.logSeries(val, CONTEXT);
            Rational exp2 = Exp.expSeries(log2, CONTEXT);
            assertEquals(exp2.toBigDecimal(CONTEXT), decimal);
        }
    }

}