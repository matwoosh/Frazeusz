package pl.edu.agh.ki.frazeusz.model.monitor.metrics;

import pl.edu.agh.ki.frazeusz.model.monitor.CrawlerStatus;


public class MetricsBucket implements CrawlerStatus {
    private int processedPages, processedSize, pagesQueueSize;

    public synchronized void addProcessedPages(int pagesCount, long pagesSizeInBytes) {
        processedPages += pagesCount;
        processedSize += pagesSizeInBytes;
    }

    public synchronized void setPagesQueueSize(int queueSize) {
        pagesQueueSize = queueSize;
    }

    public CrawlerMetrics flushData() {
        CrawlerMetrics metrics;
        synchronized (this) {
            metrics = new CrawlerMetrics(processedPages, processedSize, pagesQueueSize);
            processedPages = processedSize = 0;
        }
        return metrics;
    }
}
