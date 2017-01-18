package pl.edu.agh.ki.frazeusz.gui.crawler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by matwoosh on 18/01/2017.
 */
public class CrawlerConfiguration {

    private final List<String> urlsToCrawl;
    private final int threadsNumber;
    private final int nestingDepth;

    public static CrawlerConfiguration.Builder builder(){
        return new CrawlerConfiguration.Builder();
    }

    private CrawlerConfiguration(CrawlerConfiguration.Builder builder) {
        this.urlsToCrawl = builder.urlsToCrawl;
        this.threadsNumber = builder.threadsNumber;
        this.nestingDepth = builder.nestingDepth;
    }

    public List<String> getUrlsToCrawl() {
        return urlsToCrawl;
    }

    public int getThreadsNumber() {
        return threadsNumber;
    }

    public int getNestingDepth() {
        return nestingDepth;
    }

    public static class Builder {

        private List<String> urlsToCrawl = new ArrayList<String>();
        private int threadsNumber;
        private int nestingDepth;

        public Builder setUrlsToCrawl(List<String> urlsToCrawl) {
            this.urlsToCrawl = urlsToCrawl;
            return this;
        }

        public Builder setThreadsNumber(int threadsNumber) {
            this.threadsNumber = threadsNumber;
            return this;
        }

        public Builder setNestingDepth(int nestingDepth) {
            this.nestingDepth = nestingDepth;
            return this;
        }

        public CrawlerConfiguration build(){
            return new CrawlerConfiguration(this);
        }

    }

}
