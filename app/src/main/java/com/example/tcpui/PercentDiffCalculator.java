package com.example.tcpui;

import java.util.HashMap;
import java.util.Map;

public class PercentDiffCalculator {
    private Map<String, Double> percDiffCalc;

    public Map<String, Double> getPercDiffCalc() {
        return percDiffCalc;
    }

    public static double calculatePercentDiff(double value, double average) {
        return ((value - average) / average) * 100.0;
    }

    public void calculatePercentDiffs(Results result, Results averages) {
        this.percDiffCalc = new HashMap<>();
        this.percDiffCalc.put("% distance", calculatePercentDiff(result.getTotalDistance(), averages.getTotalDistance()));
        this.percDiffCalc.put("% elevation", calculatePercentDiff(result.getTotalElevationGain(), averages.getTotalElevationGain()));
        this.percDiffCalc.put("% time", calculatePercentDiff(result.getTotalTime(), averages.getTotalTime()));
        this.percDiffCalc.put("% speed", calculatePercentDiff(result.getAverageSpeed(), averages.getAverageSpeed()));
    }
}
