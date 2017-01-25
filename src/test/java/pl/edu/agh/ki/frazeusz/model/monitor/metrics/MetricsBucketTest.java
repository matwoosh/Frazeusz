package pl.edu.agh.ki.frazeusz.model.monitor.metrics;

import org.junit.Test;

import static org.junit.Assert.*;


public class MetricsBucketTest {
    @Test
    public void testProducedMetrics() {
        final int expectedProcessedPages = 56;
        final int expectedProcessedSize = expectedProcessedPages * 1024;
        final int expectedPagesQueueSize = 512;

        final MetricsBucket bucket = new MetricsBucket();
        bucket.addProcessedPages(expectedProcessedPages, expectedProcessedSize);
        bucket.setPagesQueueSize(expectedPagesQueueSize);
        final CrawlerMetrics metrics = bucket.flushData();

        assertEquals(expectedProcessedPages, metrics.getProcessedPages());
        assertEquals(expectedProcessedSize, metrics.getProcessedSize());
        assertEquals(expectedPagesQueueSize, metrics.getPagesQueueSize());
    }
}