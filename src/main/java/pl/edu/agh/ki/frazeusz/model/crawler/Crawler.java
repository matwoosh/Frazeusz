package pl.edu.agh.ki.frazeusz.model.crawler;

import pl.edu.agh.ki.frazeusz.gui.crawler.BinarySemaphore;
import pl.edu.agh.ki.frazeusz.gui.crawler.CrawlerConfiguration;
import pl.edu.agh.ki.frazeusz.model.monitor.CrawlerStatus;
import pl.edu.agh.ki.frazeusz.model.parser.IParser;

import java.util.*;
import java.util.concurrent.*;

/**
 * Created by matwoosh on 14/01/2017.
 */
public class Crawler {

    private IParser parser;
    private CrawlerStatus monitor;

    private Queue<String> urlsToProcess;
    private Set<Url<String>> allUrls;
    private int threadsNumber;
    private int nestingDepth;

    private ExecutorService downloadersExecutor;
    private boolean isCrawling;
    private boolean isSendingStats;

    private BinarySemaphore bs;
    private int processedPages;
    private long pageSizeInBytes;

    public Crawler(IParser parser, CrawlerStatus monitor) {
        this.parser = parser;
        this.monitor = monitor;

        urlsToProcess = new LinkedList<>();
        allUrls = new HashSet<>();

        bs = new BinarySemaphore();
        this.processedPages = 0;
        this.pageSizeInBytes = 0;

        this.isSendingStats = true;
    }

    void addUrlsToProcess(List<String> urlsToProcess) {
        this.urlsToProcess.addAll(urlsToProcess);

        // TODO - Url hierarchy
        for (String url : urlsToProcess) {
            allUrls.add(new Url<String>(url));
        }
    }

    public void start(CrawlerConfiguration crawlerConfiguration) {
        addUrlsToProcess(crawlerConfiguration.getUrlsToCrawl());
        threadsNumber = crawlerConfiguration.getThreadsNumber();
        nestingDepth = crawlerConfiguration.getNestingDepth();
        downloadersExecutor = Executors.newFixedThreadPool(threadsNumber);

        final ExecutorService executorStatsDownloaders = Executors.newFixedThreadPool(2);
        StatsSender statsSender = new StatsSender();
        Downloaders downloaders = new Downloaders();

        Future futureDownloaders = executorStatsDownloaders.submit(downloaders);
        Future futureStatsSender = executorStatsDownloaders.submit(statsSender);
    }

    private class Downloaders implements Callable<Void> {
        @Override
        public Void call() throws Exception {
            initializeDownloaders();
            return null;
        }
    }

    private void initializeDownloaders() {
        isCrawling = true;

        for (int i = 0; i < threadsNumber; i++) {
            if (urlsToProcess.peek() != null) {
                Downloader downloader = new Downloader(this, parser, urlsToProcess.poll());
                downloadersExecutor.submit(downloader);
            }
        }

        downloadersExecutor.shutdown();
        try {
            downloadersExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        isCrawling = false;
    }

    private class StatsSender implements Callable<Void> {
        @Override
        public Void call() throws Exception {
            while (isSendingStats) {
                int actualProcessedPages = processedPages;
                long actualPageSizeInBytes = pageSizeInBytes;

                monitor.addProcessedPages(actualProcessedPages, actualPageSizeInBytes);
                monitor.setPagesQueueSize(urlsToProcess.size());
                decrementStats(actualProcessedPages, actualPageSizeInBytes);

                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }

    void incrementStats(int processedPages, long pageSizeInBytes) {
        try {
            bs.acquire();
            try {
                this.processedPages += processedPages;
                this.pageSizeInBytes += pageSizeInBytes;
            } finally {
                bs.release();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void decrementStats(int actualProcessedPages, double actualPageSizeInBytes) {
        try {
            bs.acquire();
            try {
                this.processedPages -= actualProcessedPages;
                this.pageSizeInBytes -= actualPageSizeInBytes;
            } finally {
                bs.release();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void stopThreads() {
        downloadersExecutor.shutdown();
        while (!downloadersExecutor.isTerminated()) {
            // TODO
        }
    }

    public void stop() {
        stopThreads();
        this.isCrawling = false;
        this.isSendingStats = false;
    }

    public boolean isCrawling() {
        return isCrawling;
    }

}
