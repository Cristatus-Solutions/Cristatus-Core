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
import cristatus.core.series.Trig;
import org.testng.annotations.Test;

import java.math.MathContext;
import java.util.Random;

import static org.testng.Assert.assertEquals;

/**
 * @author Subhomoy Haldar
 * @version 1.0
 */
public class TrigTest {

    private static final int TRIES = 1_000;
    private static final MathContext CONTEXT = MathContext.DECIMAL64;

    private static final double DBL_TOLERANCE = 1e-15;

    @Test
    public void testSeries() throws Exception {
        Random random = new Random();
        for (int i = 0; i < TRIES; i++) {
            double d = random.nextDouble() * Math.PI / 4;
            Rational angle = Rational.valueOf(d);
            Rational sin = Trig.sinSeries(angle, CONTEXT);
            Rational cos = Trig.cosSeries(angle, CONTEXT);
            Rational tan = sin.divide(cos).dropTo(CONTEXT);
            assertEquals(Math.sin(d) / sin.doubleValue(), 1, DBL_TOLERANCE);
            assertEquals(Math.cos(d) / cos.doubleValue(), 1, DBL_TOLERANCE);
            assertEquals(Math.tan(d) / tan.doubleValue(), 1, DBL_TOLERANCE);
            assertEquals(
                    sin.pow(2).add(cos.pow(2)).dropTo(CONTEXT),
                    Rational.ONE
            );
        }
    }
}