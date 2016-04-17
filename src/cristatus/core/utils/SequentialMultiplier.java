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

import java.math.BigInteger;
import java.util.concurrent.RecursiveTask;

/**
 * This is a subclass of {@link RecursiveTask} that performs sequential
 * multiplications in parallel and thus providing a boost to performance.
 * This package private class is to be used for calculating factorials.
 *
 * @author Subhomoy Haldar
 * @version 1.0
 */
class SequentialMultiplier extends RecursiveTask<BigInteger> {
    private BigInteger start;   // The inclusive lower limit
    private BigInteger end;     // The inclusive upper limit

    // If the difference between the limits is within the threshold, then the
    // computation is carried out directly, instead of dividing it into two.
    private static final BigInteger THRESHOLD = BigInteger.valueOf(10_000L);

    /**
     * Creates a new SequentialMultiplier ready to be forked or invoked.
     *
     * @param start The inclusive lower limit.
     * @param end   The inclusive upper limit.
     */
    SequentialMultiplier(final BigInteger start, final BigInteger end) {
        this.start = start;
        this.end = end;
    }

    /**
     * The difference between the limits is within the threshold; compute the
     * product directly.
     *
     * @return The product of all integers in the range: [start, end]
     */
    private BigInteger computeDirectly() {
        BigInteger product = BigInteger.ONE;
        while (start.compareTo(end) <= 0) {
            product = product.multiply(start);
            start = start.add(BigInteger.ONE);
        }
        return product;
    }

    /**
     * This is the method that delegates control to the direct computation
     * method if the threshold requirement is met, or subdivides the task
     * into two separate tasks and executes them in parallel.
     *
     * @return The product of all integers in the range: [start, end],
     * calculated in parallel.
     */
    @Override
    protected BigInteger compute() {
        // The difference is within the threshold... compute directly.
        if (end.subtract(start).compareTo(THRESHOLD) <= 0) {
            return computeDirectly();
        }
        BigInteger mid = start.add(end).shiftRight(1);  // (start + end) / 2
        SequentialMultiplier task1 = new SequentialMultiplier(start, mid.subtract(BigInteger.ONE));
        SequentialMultiplier task2 = new SequentialMultiplier(mid, end);
        task1.fork();
        return task2.compute().multiply(task1.join());
    }
}
