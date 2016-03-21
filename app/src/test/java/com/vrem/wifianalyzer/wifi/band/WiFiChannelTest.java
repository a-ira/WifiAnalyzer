/*
 *    Copyright (C) 2015 - 2016 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.vrem.wifianalyzer.wifi.band;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

public class WiFiChannelTest {
    public static final int CHANNEL = 1;
    public static final int FREQUENCY = 200;

    private WiFiChannel fixture;
    private WiFiChannel other;

    @Before
    public void setUp() throws Exception {
        fixture = new WiFiChannel(CHANNEL, FREQUENCY);
        other = new WiFiChannel(CHANNEL, FREQUENCY);
    }

    @Test
    public void testFrequencyStart() throws Exception {
        assertEquals(198, fixture.getFrequencyStart());
    }

    @Test
    public void testFrequencyEnd() throws Exception {
        assertEquals(202, fixture.getFrequencyEnd());
    }

    @Test
    public void testEquals() throws Exception {
        assertEquals(fixture, other);
        assertNotSame(fixture, other);
    }

    @Test
    public void testHashCode() throws Exception {
        assertEquals(fixture.hashCode(), other.hashCode());
    }

    @Test
    public void testCompareTo() throws Exception {
        assertEquals(0, fixture.compareTo(other));
    }
}