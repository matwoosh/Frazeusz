package pl.edu.agh.ki.frazeusz.model.monitor.metrics;

import org.junit.Test;
import pl.edu.agh.ki.frazeusz.model.monitor.TimeFrame;

import static org.junit.Assert.*;


public class MetricsProviderTest {
    @Test
    public void testSummaryWithEmptyPagesQueue() {
        final int expectedPagesQueueSize = 0;
        final int expectedMetricsListSize = 1;
        final TimeFrame expectedTimeFrame = TimeFrame.ALL_TIME;

        final MetricsProvider provider = new MetricsProvider(new MetricsBucket());
        final CrawlerSummary summary = provider.getSummary(expectedTimeFrame);

        assertEquals(expectedPagesQueueSize, summary.getPagesQueueSize());
        assertEquals(expectedMetricsListSize, summary.getMetrics().size());
        assertEquals(expectedTimeFrame, summary.getTimeFrame());
    }

    @Test
    public void testSummaryWithNonEmptyPagesQueue() {
        final int expectedPagesQueueSize = 512;
        final int expectedMetricsListSize = 1;
        final TimeFrame expectedTimeFrame = TimeFrame.LAST_TEN_SECONDS;

        final MetricsBucket bucket = new MetricsBucket();
        bucket.addProcessedPages(56, 56 * 1024);
        bucket.setPagesQueueSize(expectedPagesQueueSize);

        final MetricsProvider provider = new MetricsProvider(bucket);
        final CrawlerSummary summary = provider.getSummary(expectedTimeFrame);

        assertEquals(expectedPagesQueueSize, summary.getPagesQueueSize());
        assertEquals(expectedMetricsListSize, summary.getMetrics().size());
        assertEquals(expectedTimeFrame, summary.getTimeFrame());
    }
}