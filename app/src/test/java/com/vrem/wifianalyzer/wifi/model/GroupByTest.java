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

package com.vrem.wifianalyzer.wifi.model;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GroupByTest {
    @Mock private WiFiDetails lhsWiFiDetails;
    @Mock private WiFiDetails rhsWiFiDetails;
    @Mock private WiFiFrequency lhsWiFiFrequency;
    @Mock private WiFiFrequency rhsWiFiFrequency;

    @Test
    public void testGroupByNumber() throws Exception {
        assertEquals(3, GroupBy.values().length);
    }

    @Test
    public void testFind() throws Exception {
        Assert.assertEquals(GroupBy.NONE, GroupBy.find(null));
        assertEquals(GroupBy.NONE, GroupBy.find(""));
        assertEquals(GroupBy.NONE, GroupBy.find("xYz"));

        assertEquals(GroupBy.NONE, GroupBy.find(GroupBy.NONE.name()));
        assertEquals(GroupBy.NONE, GroupBy.find(GroupBy.NONE.name().toLowerCase()));

        assertEquals(GroupBy.SSID, GroupBy.find(GroupBy.SSID.name()));
        assertEquals(GroupBy.SSID, GroupBy.find(GroupBy.SSID.name().toLowerCase()));

        assertEquals(GroupBy.CHANNEL, GroupBy.find(GroupBy.CHANNEL.name()));
        assertEquals(GroupBy.CHANNEL, GroupBy.find(GroupBy.CHANNEL.name().toLowerCase()));
    }

    @Test
    public void testGroupBy() throws Exception {
        assertTrue(GroupBy.NONE.groupBy() instanceof GroupBy.None);
        assertTrue(GroupBy.SSID.groupBy() instanceof GroupBy.SSIDGroupBy);
        assertTrue(GroupBy.CHANNEL.groupBy() instanceof GroupBy.ChannelGroupBy);
    }

    @Test
    public void testSortOrder() throws Exception {
        assertTrue(GroupBy.NONE.sortOrder() instanceof GroupBy.None);
        assertTrue(GroupBy.SSID.sortOrder() instanceof GroupBy.SSIDSortOrder);
        assertTrue(GroupBy.CHANNEL.sortOrder() instanceof GroupBy.ChannelSortOrder);
    }

    @Test
    public void testNoneComparator() throws Exception {
        GroupBy.None comparator = new GroupBy.None();

        assertEquals(0, comparator.compare(lhsWiFiDetails, lhsWiFiDetails));
        assertEquals(0, comparator.compare(rhsWiFiDetails, rhsWiFiDetails));
        assertEquals(1, comparator.compare(lhsWiFiDetails, rhsWiFiDetails));
        assertEquals(1, comparator.compare(rhsWiFiDetails, lhsWiFiDetails));
    }

    @Test
    public void testChannelGroupByComparator() throws Exception {
        validateChannelGroupBy(0, 0, 0);
        validateChannelGroupBy(-1, 0, 1);
        validateChannelGroupBy(1, 1, 0);

        verify(lhsWiFiDetails, times(3)).getWiFiFrequency();
        verify(rhsWiFiDetails, times(3)).getWiFiFrequency();
        verify(lhsWiFiFrequency, times(3)).getChannel();
        verify(rhsWiFiFrequency, times(3)).getChannel();
    }

    private void validateChannelGroupBy(int expected, int lhsValue, int rhsValue) {
        // setup
        GroupBy.ChannelGroupBy comparator = new GroupBy.ChannelGroupBy();
        when(lhsWiFiDetails.getWiFiFrequency()).thenReturn(lhsWiFiFrequency);
        when(rhsWiFiDetails.getWiFiFrequency()).thenReturn(rhsWiFiFrequency);
        when(lhsWiFiFrequency.getChannel()).thenReturn(lhsValue);
        when(rhsWiFiFrequency.getChannel()).thenReturn(rhsValue);
        // execute & validate
        assertEquals(expected, comparator.compare(lhsWiFiDetails, rhsWiFiDetails));
    }

    @Test
    public void testChannelSortOrderComparatorEquals() throws Exception {
        GroupBy.ChannelSortOrder comparator = new GroupBy.ChannelSortOrder();
        withDetailsInfo(lhsWiFiDetails, rhsWiFiFrequency, "BSSID");
        withDetailsInfo(rhsWiFiDetails, rhsWiFiFrequency, "BSSID");
        // execute & validate
        assertEquals(0, comparator.compare(lhsWiFiDetails, rhsWiFiDetails));
        verifyDetailsInfo(lhsWiFiDetails);
        verifyDetailsInfo(rhsWiFiDetails);
        verifyWiFiDetails();
    }

    private void verifyWiFiDetails() {
        verify(lhsWiFiDetails).getWiFiFrequency();
        verify(rhsWiFiDetails).getWiFiFrequency();
    }

    @Test
    public void testChannelSortOrderComparatorLess() throws Exception {
        GroupBy.ChannelSortOrder comparator = new GroupBy.ChannelSortOrder();
        withDetailsInfo(lhsWiFiDetails, rhsWiFiFrequency, "BSSID");
        withDetailsInfo(rhsWiFiDetails, rhsWiFiFrequency, "CSSID");
        // execute & validate
        assertEquals(-1, comparator.compare(lhsWiFiDetails, rhsWiFiDetails));
        verifyDetailsInfo(lhsWiFiDetails);
        verifyDetailsInfo(rhsWiFiDetails);
        verifyWiFiDetails();
    }

    @Test
    public void testChannelSortOrderComparatorMore() throws Exception {
        GroupBy.ChannelSortOrder comparator = new GroupBy.ChannelSortOrder();
        withDetailsInfo(lhsWiFiDetails, lhsWiFiFrequency, "BSSID");
        withDetailsInfo(rhsWiFiDetails, rhsWiFiFrequency, "ASSID");
        // execute & validate
        assertEquals(1, comparator.compare(lhsWiFiDetails, rhsWiFiDetails));
        verifyDetailsInfo(lhsWiFiDetails);
        verifyDetailsInfo(rhsWiFiDetails);
        verifyWiFiDetails();
    }

    private void withDetailsInfo(WiFiDetails wiFiDetails, WiFiFrequency wiFiFrequency, String BSSID) {
        when(wiFiDetails.getWiFiFrequency()).thenReturn(wiFiFrequency);
        when(wiFiFrequency.getChannel()).thenReturn(0);
        when(wiFiDetails.getLevel()).thenReturn(0);
        when(wiFiDetails.getSSID()).thenReturn("SSID");
        when(wiFiDetails.getBSSID()).thenReturn(BSSID);
    }

    private void verifyDetailsInfo(WiFiDetails wiFiDetails) {
        verify(wiFiDetails).getLevel();
        verify(wiFiDetails).getSSID();
        verify(wiFiDetails).getBSSID();
    }

    @Test
    public void testSSIDGroupByComparator() throws Exception {
        validateSSIDGroupBy(0, "B-SSID", "B-SSID");
        validateSSIDGroupBy(-1, "B-SSID", "C-SSID");
        validateSSIDGroupBy(1, "B-SSID", "A-SSID");

        verify(lhsWiFiDetails, times(3)).getSSID();
        verify(rhsWiFiDetails, times(3)).getSSID();
    }

    private void validateSSIDGroupBy(int expected, String lhsValue, String rhsValue) {
        // setup
        GroupBy.SSIDGroupBy comparator = new GroupBy.SSIDGroupBy();
        when(lhsWiFiDetails.getSSID()).thenReturn(lhsValue);
        when(rhsWiFiDetails.getSSID()).thenReturn(rhsValue);
        // execute & validate
        assertEquals(expected, comparator.compare(lhsWiFiDetails, rhsWiFiDetails));
    }

    @Test
    public void testSSIDSortOrderComparatorEquals() throws Exception {
        GroupBy.SSIDSortOrder comparator = new GroupBy.SSIDSortOrder();
        withDetailsInfo(lhsWiFiDetails, rhsWiFiFrequency, "BSSID");
        withDetailsInfo(rhsWiFiDetails, rhsWiFiFrequency, "BSSID");
        // execute & validate
        assertEquals(0, comparator.compare(lhsWiFiDetails, rhsWiFiDetails));
        verifyDetailsInfo(lhsWiFiDetails);
        verifyDetailsInfo(rhsWiFiDetails);
    }

    @Test
    public void testSSIDSortOrderComparatorLess() throws Exception {
        GroupBy.SSIDSortOrder comparator = new GroupBy.SSIDSortOrder();
        withDetailsInfo(lhsWiFiDetails, rhsWiFiFrequency, "BSSID");
        withDetailsInfo(rhsWiFiDetails, rhsWiFiFrequency, "CSSID");
        // execute & validate
        assertEquals(-1, comparator.compare(lhsWiFiDetails, rhsWiFiDetails));
        verifyDetailsInfo(lhsWiFiDetails);
        verifyDetailsInfo(rhsWiFiDetails);
    }

    @Test
    public void testSSIDSortOrderComparatorMore() throws Exception {
        GroupBy.SSIDSortOrder comparator = new GroupBy.SSIDSortOrder();
        withDetailsInfo(lhsWiFiDetails, rhsWiFiFrequency, "BSSID");
        withDetailsInfo(rhsWiFiDetails, rhsWiFiFrequency, "ASSID");
        // execute & validate
        assertEquals(1, comparator.compare(lhsWiFiDetails, rhsWiFiDetails));
        verifyDetailsInfo(lhsWiFiDetails);
        verifyDetailsInfo(rhsWiFiDetails);
    }
}