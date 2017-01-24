package pl.edu.agh.ki.frazeusz.model.monitor.metrics;

import pl.edu.agh.ki.frazeusz.model.monitor.TimeFrame;

import java.util.List;


public class CrawlerSummary {

    private final List<CrawlerMetrics> metrics;
    private final TimeFrame timeFrame;

    public CrawlerSummary(List<CrawlerMetrics> metrics, TimeFrame timeFrame) {
        this.metrics = metrics;
        this.timeFrame = timeFrame;
    }

    public List<CrawlerMetrics> getMetrics() {
        return metrics;
    }

    public TimeFrame getTimeFrame() {
        return timeFrame;
    }

    public int getPagesQueueSize() {
        final CrawlerMetrics last = metrics.get(metrics.size() - 1);
        return last.getPagesQueueSize();
    }
}
