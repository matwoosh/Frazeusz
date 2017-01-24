package pl.edu.agh.ki.frazeusz.model.monitor;


public interface CrawlerStatus {
    void addProcessedPages(int pagesCount, long pagesSizeInBytes);

    void setPagesQueueSize(int queueSize);
}
