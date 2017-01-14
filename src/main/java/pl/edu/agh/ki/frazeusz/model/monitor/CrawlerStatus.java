package pl.edu.agh.ki.frazeusz.model.monitor;

/**
 * Created by matwoosh on 14/01/2017.
 */
public interface CrawlerStatus {

    void addProcessedPages(int pagesCount, long pagesSizeInBytes);

    void setPagesQueueSize(int queueSize);

}
