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
package org.ta4j.core.rules;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.ta4j.core.Bar;
import org.ta4j.core.indicators.AbstractIndicatorTest;
import org.ta4j.core.indicators.helpers.DateTimeIndicator;
import org.ta4j.core.mocks.MockBarSeriesBuilder;
import org.ta4j.core.num.NumFactory;

public class TimeRangeRuleTest extends AbstractIndicatorTest<Object, Object> {

    public TimeRangeRuleTest(NumFactory numFunction) {
        super(numFunction);
    }

    @Test
    public void isSatisfiedForBuy() {
        final DateTimeFormatter dtf = DateTimeFormatter.ISO_ZONED_DATE_TIME;
        final var series = new MockBarSeriesBuilder().withNumFactory(numFactory).build();

        series.barBuilder().endTime(ZonedDateTime.parse("2019-09-17T00:00:00-00:00", dtf)).closePrice(100).add();
        series.barBuilder().endTime(ZonedDateTime.parse("2019-09-17T05:00:00-00:00", dtf)).closePrice(100).add();
        series.barBuilder().endTime(ZonedDateTime.parse("2019-09-17T07:00:00-00:00", dtf)).closePrice(100).add();
        series.barBuilder().endTime(ZonedDateTime.parse("2019-09-17T08:00:00-00:00", dtf)).closePrice(100).add();
        series.barBuilder().endTime(ZonedDateTime.parse("2019-09-17T15:00:00-00:00", dtf)).closePrice(100).add();
        series.barBuilder().endTime(ZonedDateTime.parse("2019-09-17T15:05:00-00:00", dtf)).closePrice(100).add();
        series.barBuilder().endTime(ZonedDateTime.parse("2019-09-17T16:59:00-00:00", dtf)).closePrice(100).add();
        series.barBuilder().endTime(ZonedDateTime.parse("2019-09-17T17:05:00-00:00", dtf)).closePrice(100).add();
        series.barBuilder().endTime(ZonedDateTime.parse("2019-09-17T23:00:00-00:00", dtf)).closePrice(100).add();
        series.barBuilder().endTime(ZonedDateTime.parse("2019-09-17T23:30:00-00:00", dtf)).closePrice(100).add();
        series.barBuilder().endTime(ZonedDateTime.parse("2019-09-17T23:35:00-00:00", dtf)).closePrice(100).add();

        var dateTimeIndicator = new DateTimeIndicator(series, Bar::getBeginTime);
        TimeRangeRule rule = new TimeRangeRule(
                Arrays.asList(new TimeRangeRule.TimeRange(LocalTime.of(0, 0), LocalTime.of(4, 0)),
                        new TimeRangeRule.TimeRange(LocalTime.of(6, 0), LocalTime.of(7, 0)),
                        new TimeRangeRule.TimeRange(LocalTime.of(12, 0), LocalTime.of(15, 0)),
                        new TimeRangeRule.TimeRange(LocalTime.of(17, 0), LocalTime.of(21, 0)),
                        new TimeRangeRule.TimeRange(LocalTime.of(22, 0), LocalTime.of(23, 30))),
                dateTimeIndicator);

        assertTrue(rule.isSatisfied(0, null));
        assertFalse(rule.isSatisfied(1, null));
        assertTrue(rule.isSatisfied(2, null));
        assertFalse(rule.isSatisfied(3, null));
        assertTrue(rule.isSatisfied(4, null));
        assertFalse(rule.isSatisfied(5, null));
        assertFalse(rule.isSatisfied(6, null));
        assertTrue(rule.isSatisfied(7, null));
        assertTrue(rule.isSatisfied(8, null));
        assertTrue(rule.isSatisfied(9, null));
        assertFalse(rule.isSatisfied(10, null));
    }
}
