package pl.edu.agh.ki.frazeusz.model.crawler;

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

    private Queue<Url> urlsToProcess;
    private Set<Url> allUrls;
    private int threadsNumber;
    private int nestingDepth;

    private ExecutorService downloadersExecutor;
    private boolean isCrawling;
    private boolean isSendingStats;

    private int processedPages;
    private long pageSizeInBytes;

    public Crawler(IParser parser, CrawlerStatus monitor) {
        this.parser = parser;
        this.monitor = monitor;

        urlsToProcess = new LinkedList<>();
        allUrls = new HashSet<>();

        this.processedPages = 0;
        this.pageSizeInBytes = 0;

        this.isSendingStats = true;
    }

    void addUrlsToProcess(List<Url> urlsToProcess) {
        if (urlsToProcess != null) {
            if (!urlsToProcess.isEmpty()) {
                if (urlsToProcess.get(0).getNestingDepth() <= nestingDepth) {
                    this.urlsToProcess.addAll(urlsToProcess);
                }
            }
        }
    }

    public void start(CrawlerConfiguration crawlerConfiguration) {
        prepareUrlsToProcess(crawlerConfiguration);
        threadsNumber = crawlerConfiguration.getThreadsNumber();
        nestingDepth = crawlerConfiguration.getNestingDepth();
        downloadersExecutor = Executors.newFixedThreadPool(threadsNumber);

        final ExecutorService executorStatsDownloaders = Executors.newFixedThreadPool(2);
        StatsSender statsSender = new StatsSender();
        Downloaders downloaders = new Downloaders();

        Future futureDownloaders = executorStatsDownloaders.submit(downloaders);
        Future futureStatsSender = executorStatsDownloaders.submit(statsSender);
    }

    private void prepareUrlsToProcess(CrawlerConfiguration crawlerConfiguration) {
        final List<String> urlsFromUser = crawlerConfiguration.getUrlsToCrawl();
        final List<Url> wrappedUrls = new ArrayList<>();

        for (String url : urlsFromUser) {
            wrappedUrls.add(new Url(url, 0));
        }

        addUrlsToProcess(wrappedUrls);
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

    synchronized void incrementStats(long pageSizeInBytes) {
        this.processedPages += 1;
        this.pageSizeInBytes += pageSizeInBytes;
    }

    private synchronized void decrementStats(int actualProcessedPages, double actualPageSizeInBytes) {
        this.processedPages -= actualProcessedPages;
        this.pageSizeInBytes -= actualPageSizeInBytes;
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
