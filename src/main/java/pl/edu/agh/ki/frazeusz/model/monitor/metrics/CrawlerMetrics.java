package pl.edu.agh.ki.frazeusz.model.monitor.metrics;

import java.util.Date;


public class CrawlerMetrics {
    private final int processedPages, processedSize, pagesQueueSize;
    private final Date time;

    public CrawlerMetrics(int pagesCount, int pagesSize, int queueSize) {
        processedPages = pagesCount;
        processedSize = pagesSize;
        pagesQueueSize = queueSize;
        time = new Date();
    }

    public int getProcessedPages() {
        return processedPages;
    }

    public int getProcessedSize() {
        return processedSize;
    }

    public int getPagesQueueSize() {
        return pagesQueueSize;
    }

    public Date getTime() {
        return time;
    }
}
