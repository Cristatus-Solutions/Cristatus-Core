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

import cristatus.core.utils.PiGenerator;
import org.testng.annotations.Test;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;

import static org.testng.Assert.assertEquals;

/**
 * @author Subhomoy Haldar
 * @version 1.0
 */
public class PiGeneratorTest {

    private static final MathContext CONTEXT = new MathContext(10_000);

    @Test
    public void testApproximation() throws Exception {
        int digits = CONTEXT.getPrecision() + 1;
        BigDecimal actualPi = new BigDecimal(readPiUpto(digits)).round(CONTEXT)
                .stripTrailingZeros();
        BigDecimal expectedPi = PiGenerator.of(CONTEXT).toBigDecimal(CONTEXT);
        assertEquals(actualPi, expectedPi);
        expectedPi = PiGenerator.of(CONTEXT).toBigDecimal(CONTEXT);
        assertEquals(actualPi, expectedPi);
    }

    private static String readPiUpto(int digits) {
        try (BufferedInputStream stream = new BufferedInputStream(
                PiGeneratorTest.class.getResourceAsStream("pi1000000.txt")
        )) {
            StringBuilder builder = new StringBuilder(digits + 1);
            for (int i = 0; i <= digits; i++) {
                builder.append((char) stream.read());
            }
            return builder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}