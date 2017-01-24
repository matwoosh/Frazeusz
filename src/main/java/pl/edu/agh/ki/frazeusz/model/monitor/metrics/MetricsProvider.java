package pl.edu.agh.ki.frazeusz.model.monitor.metrics;


import pl.edu.agh.ki.frazeusz.model.monitor.TimeFrame;

import java.util.ArrayList;
import java.util.List;


public class MetricsProvider {

    private final MetricsBucket bucket;
    private final List<CrawlerMetrics> metrics = new ArrayList<>();

    private int lastTenSecondsStartIndex, lastHourStartIndex;

    public MetricsProvider(MetricsBucket bucket) {
        this.bucket = bucket;
    }

    public CrawlerSummary getSummary(TimeFrame timeFrame) {
        updateMetrics();
        final List<CrawlerMetrics> subList = getElemsForTimeFrame(timeFrame);
        return new CrawlerSummary(subList, timeFrame);
    }

    private List<CrawlerMetrics> getElemsForTimeFrame(TimeFrame timeFrame) {
        int first;
        switch (timeFrame) {
            case LAST_TEN_SECONDS: first = lastTenSecondsStartIndex; break;
            case LAST_HOUR:        first = lastHourStartIndex; break;
            default:               first = 0; break;
        }
        int last = metrics.size();

        return metrics.subList(first, last);
    }

    private void updateMetrics() {
        final CrawlerMetrics flushed = bucket.flushData();
        metrics.add(flushed);

        lastTenSecondsStartIndex = setIndex(flushed, lastTenSecondsStartIndex, 10);
        lastHourStartIndex = setIndex(flushed, lastHourStartIndex, 3600);
    }

    private int setIndex(CrawlerMetrics last, int index, int secondsInterval) {
        while (last.getTime().getTime() - metrics.get(index).getTime().getTime() > secondsInterval * 1000) {
            index++;
        }
        return index;
    }
}
