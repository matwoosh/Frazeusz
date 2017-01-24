package pl.edu.agh.ki.frazeusz.model.crawler;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import pl.edu.agh.ki.frazeusz.gui.crawler.CrawlerConfiguration;
import pl.edu.agh.ki.frazeusz.model.monitor.CrawlerStatus;
import pl.edu.agh.ki.frazeusz.model.parser.IParser;

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

    private ExecutorService executor;
    private boolean isCrawling;
    private int processedPages;
    private long pageSizeInBytes;

    public Crawler(IParser parser, CrawlerStatus monitor) {
        this.parser = parser;
        this.monitor = monitor;

        urlsToProcess = new LinkedList<>();
        allUrls = new HashSet<>();

        this.processedPages = 0;
        this.pageSizeInBytes = 0;
    }

    void addProcessedPages(int processedPages) {
        this.processedPages += processedPages;
    }

    void addPageSizeInBytes(long pageSizeInBytes) {
        this.pageSizeInBytes += pageSizeInBytes;
    }

    public void start(CrawlerConfiguration crawlerConfiguration) {
        this.urlsToProcess.addAll(crawlerConfiguration.getUrlsToCrawl());
        this.threadsNumber = crawlerConfiguration.getThreadsNumber();
        this.nestingDepth = crawlerConfiguration.getNestingDepth();
        executor = Executors.newFixedThreadPool(threadsNumber);

        System.out.println(" >>> Got depth: " + nestingDepth + " threads: " + threadsNumber + "\n >>> Got Urls:");
        for (String url : urlsToProcess) {
            System.out.println("    + " + url);
        }

        if (!isCrawling) {
            for (String url : urlsToProcess) {
                allUrls.add(new Url<String>(null, url));
            }

            initializeDownloaders();
        }
    }

    public void stop() {
        stopThreads();
        this.isCrawling = false;
    }

    public boolean isCrawling() {
        return isCrawling;
    }

    private void stopThreads() {
        executor.shutdown();
        while (!executor.isTerminated()) {
            // TODO
        }
    }

    private void initializeDownloaders() {
        // TODO

        // Crawling started
        isCrawling = true;

        // Concurrent tasks
        // or Threadpool or etc...
        for (int i = 0; i < threadsNumber; i++) {
            // TODO - simple example
            if (urlsToProcess.peek() != null) {
                Downloader downloader = new Downloader(this, parser);
                executor.execute(downloader);
            }
        }

        // Crawling finished
        isCrawling = false;
    }

    private void sendStatsToMonitor() {
        monitor.addProcessedPages(processedPages, pageSizeInBytes);
        monitor.setPagesQueueSize(allUrls.size());
        // TODO
    }

}
