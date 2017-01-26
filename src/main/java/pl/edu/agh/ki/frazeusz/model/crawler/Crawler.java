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
    private List<Url> unprocessedUrls;

    private int threadsNumber;
    private int nestingDepth;

    private int processedPages;
    private long pageSizeInBytes;

    private ExecutorService downloadersExecutor;

    private boolean isCrawling;
    private boolean isSendingStats;

    public Crawler(IParser parser, CrawlerStatus monitor) {
        this.parser = parser;
        this.monitor = monitor;
        this.urlsToProcess = new LinkedList<>();
        this.unprocessedUrls = new LinkedList<>();

        this.processedPages = 0;
        this.pageSizeInBytes = 0;

        this.isSendingStats = true;
    }

    synchronized void addUrlsToProcess(List<Url> urlsToProcess) {
        this.urlsToProcess.addAll(urlsToProcess);
    }

    public void start(CrawlerConfiguration crawlerConfiguration) {
        prepareUrlsToProcess(crawlerConfiguration);
        threadsNumber = crawlerConfiguration.getThreadsNumber();
        nestingDepth = crawlerConfiguration.getNestingDepth();
        downloadersExecutor = Executors.newFixedThreadPool(threadsNumber);

        final ExecutorService executorStatsDownloaders = Executors.newFixedThreadPool(2);
        StatsSender statsSender = new StatsSender(this, monitor);
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

    public boolean isSendingStats() {
        return isSendingStats;
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
            Downloader downloader = new Downloader(this, parser);
            downloadersExecutor.submit(downloader);
        }
        downloadersExecutor.shutdown();

        try {
            downloadersExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        isCrawling = false;
    }

    synchronized void addUnprocessedUrl(Url rejectedUrl) {
        this.unprocessedUrls.add(rejectedUrl);
    }

    synchronized void incrementStats(long pageSizeInBytes) {
        this.processedPages += 1;
        this.pageSizeInBytes += pageSizeInBytes;
    }

    synchronized void decrementStats(int actualProcessedPages, double actualPageSizeInBytes) {
        this.processedPages -= actualProcessedPages;
        this.pageSizeInBytes -= actualPageSizeInBytes;
    }

    synchronized Url getUrlToCrawl() {
        return urlsToProcess.poll();
    }

    public Queue<Url> getUrlsToProcess() {
        return urlsToProcess;
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

    public int getProcessedPages() {
        return processedPages;
    }

    public long getPageSizeInBytes() {
        return pageSizeInBytes;
    }

    public int getQueueSize() {
        return urlsToProcess.size();
    }

    public int getNestingDepth() {
        return nestingDepth;
    }

    public List<Url> getUnprocessedUrls() {
        return unprocessedUrls;
    }

}
