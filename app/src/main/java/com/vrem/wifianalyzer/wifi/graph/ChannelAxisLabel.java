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

package com.vrem.wifianalyzer.wifi.graph;

import android.support.annotation.NonNull;

import com.jjoe64.graphview.LabelFormatter;
import com.jjoe64.graphview.Viewport;
import com.vrem.wifianalyzer.wifi.band.WiFiBand;
import com.vrem.wifianalyzer.wifi.band.WiFiChannel;

import org.apache.commons.lang3.StringUtils;

class ChannelAxisLabel implements LabelFormatter {
    private final WiFiBand wiFiBand;

    ChannelAxisLabel(@NonNull WiFiBand wiFiBand) {
        this.wiFiBand = wiFiBand;
    }

    @Override
    public String formatLabel(double value, boolean isValueX) {
        String result = StringUtils.EMPTY;

        int valueAsInt = (int) (value + (value < 0 ? -0.5 : 0.5));
        if (isValueX) {
            WiFiChannel wiFiChannel = wiFiBand.findChannel(valueAsInt);
            if (wiFiChannel != WiFiChannel.UNKNOWN) {
                result += wiFiChannel.getChannel();
            }
        } else {
            if (valueAsInt <= GraphViewBuilder.MAX_Y && valueAsInt > GraphViewBuilder.MIN_Y) {
                result += valueAsInt;
            }
        }
        return result;
    }

    @Override
    public void setViewport(Viewport viewport) {
        // ignore
    }
}