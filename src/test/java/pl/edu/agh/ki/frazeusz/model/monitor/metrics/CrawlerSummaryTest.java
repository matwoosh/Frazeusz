package pl.edu.agh.ki.frazeusz.model.monitor.metrics;

import org.junit.Test;
import pl.edu.agh.ki.frazeusz.model.crawler.Crawler;
import pl.edu.agh.ki.frazeusz.model.monitor.TimeFrame;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;

/**
 * Created by arkadiuszgil on 25/01/2017.
 */
public class CrawlerSummaryTest {
    @Test(expected = IndexOutOfBoundsException.class)
    public void getPagesQueueSizeOnEmptySummary() {
        List<CrawlerMetrics> metricsList = new ArrayList<>();
        CrawlerSummary summary = new CrawlerSummary(metricsList, TimeFrame.ALL_TIME);

        summary.getPagesQueueSize();
    }

    @Test
    public void getPagesQueueSizeOnNonEmptySummary() {
        Random r = new Random();
        List<CrawlerMetrics> metricsList = initMetrics(r);
        CrawlerMetrics last = createRandomMetric(r);
        metricsList.add(last);
        CrawlerSummary summary = new CrawlerSummary(metricsList, TimeFrame.ALL_TIME);

        int pageQueueSize = summary.getPagesQueueSize();

        assertEquals(pageQueueSize, last.getPagesQueueSize());
    }

    // Helpers

    private List<CrawlerMetrics> initMetrics(Random gen) {
        List<CrawlerMetrics> metricsList = new ArrayList<>();
        for(int i = 0; i < 10; i ++) {

            metricsList.add(createRandomMetric(gen));
        }
        return metricsList;
    }

    private CrawlerMetrics createRandomMetric(Random gen) {
        return new CrawlerMetrics(gen.nextInt(1000), gen.nextInt(1000), gen.nextInt(1000));
    }

}