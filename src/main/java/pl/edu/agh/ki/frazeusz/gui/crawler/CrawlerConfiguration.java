package pl.edu.agh.ki.frazeusz.gui.crawler;

import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by matwoosh on 18/01/2017.
 */
public class CrawlerConfiguration {

    private final List<String> urlsToCrawl;
    private final int threadsNumber;
    private final int nestingDepth;

    public static CrawlerConfiguration.Builder builder() {
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

        private final List<String> urlsToCrawl = new ArrayList<>();
        private int threadsNumber;
        private int nestingDepth;

        public Builder setUrlsToCrawl(List<String> urlsToCrawl) {
            this.urlsToCrawl.addAll(urlsToCrawl);
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

        private void validate() {
            Preconditions.checkArgument(!urlsToCrawl.isEmpty(), "List of URLs to process cannot be empty");
            Preconditions.checkArgument(threadsNumber > 0, "Number of threads must be greater than 0.");
            Preconditions.checkArgument(nestingDepth > 0, "Nesting depth must be greater than 0.");
        }

        public CrawlerConfiguration build() {
            validate();
            return new CrawlerConfiguration(this);
        }

    }

}
