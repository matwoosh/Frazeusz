package pl.edu.agh.ki.frazeusz.model.crawler;

import pl.edu.agh.ki.frazeusz.model.monitor.CrawlerStatus;

import java.util.concurrent.Callable;

/**
 * Created by matwoosh on 14/01/2017.
 */
public class StatsSender implements Callable<Void> {

    private final Crawler crawler;
    private final CrawlerStatus monitor;

    public StatsSender(Crawler crawler, CrawlerStatus monitor) {
        this.crawler = crawler;
        this.monitor = monitor;
    }

    @Override
    public Void call() throws Exception {
        while (crawler.isSendingStats()) {
            int actualProcessedPages = crawler.getProcessedPages();
            long actualPageSizeInBytes = crawler.getPageSizeInBytes();

            monitor.addProcessedPages(actualProcessedPages, actualPageSizeInBytes);
            monitor.setPagesQueueSize(crawler.getQueueSize());
            crawler.decrementStats(actualProcessedPages, actualPageSizeInBytes);

            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
