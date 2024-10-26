/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2017-2024 Ta4j Organization & respective
 * authors (see AUTHORS)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package org.ta4j.core.indicators;

import static org.ta4j.core.TestUtils.assertNumEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.ta4j.core.BarSeries;
import org.ta4j.core.Indicator;
import org.ta4j.core.mocks.MockBarSeriesBuilder;
import org.ta4j.core.num.Num;
import org.ta4j.core.num.NumFactory;

public class AccelerationDecelerationIndicatorTest extends AbstractIndicatorTest<Indicator<Num>, Num> {

    private BarSeries series;

    public AccelerationDecelerationIndicatorTest(NumFactory numFunction) {
        super(numFunction);
    }

    @BeforeEach
    public void setUp() {
        series = new MockBarSeriesBuilder().build();

        series.barBuilder().openPrice(0).closePrice(0).highPrice(16).lowPrice(8).add();
        series.barBuilder().openPrice(0).closePrice(0).highPrice(12).lowPrice(6).add();
        series.barBuilder().openPrice(0).closePrice(0).highPrice(18).lowPrice(14).add();
        series.barBuilder().openPrice(0).closePrice(0).highPrice(10).lowPrice(6).add();
        series.barBuilder().openPrice(0).closePrice(0).highPrice(8).lowPrice(4).add();
    }

    @Test
    public void calculateWithSma2AndSma3() {
        var acceleration = new AccelerationDecelerationIndicator(series, 2, 3);

        assertNumEquals(0, acceleration.getValue(0));
        assertNumEquals(0, acceleration.getValue(1));
        assertNumEquals(0.08333333333, acceleration.getValue(2));
        assertNumEquals(0.41666666666, acceleration.getValue(3));
        assertNumEquals(-2, acceleration.getValue(4));
    }

    @Test
    public void withSma1AndSma2() {
        var acceleration = new AccelerationDecelerationIndicator(series, 1, 2);

        assertNumEquals(0, acceleration.getValue(0));
        assertNumEquals(0, acceleration.getValue(1));
        assertNumEquals(0, acceleration.getValue(2));
        assertNumEquals(0, acceleration.getValue(3));
        assertNumEquals(0, acceleration.getValue(4));
    }

    @Test
    public void withSmaDefault() {
        var acceleration = new AccelerationDecelerationIndicator(series);

        assertNumEquals(0, acceleration.getValue(0));
        assertNumEquals(0, acceleration.getValue(1));
        assertNumEquals(0, acceleration.getValue(2));
        assertNumEquals(0, acceleration.getValue(3));
        assertNumEquals(0, acceleration.getValue(4));
    }
}
