package pl.edu.agh.ki.frazeusz.model.crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import pl.edu.agh.ki.frazeusz.model.parser.IParser;

import java.io.IOException;

/**
 * Created by matwoosh on 14/01/2017.
 */
public class Downloader implements Runnable {
    private Crawler crawler;
    private IParser parser;
    private String content;
    private String httpHeader;
    private String url;

    Downloader(Crawler crawler, IParser parser) {
        this.crawler = crawler;
        this.parser = parser;
    }

    @Override
    public void run() {
        // TODO

        Url<String> urlNode = fetchUrl(url);

        try {
            if (urlNode != null) {
                parser.parseContent(httpHeader, content, urlNode.getAbsoluteUrl());
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(" >> WARNING: Parser couldn't parse this url: " + urlNode.getAbsoluteUrl());
        }

        // after some execution - sendStats();
    }

    private Url<String> fetchUrl(String url) {
        try {
            Document document = Jsoup.connect(url).get();
            document.data();
        } catch (IOException e) {
            e.printStackTrace();
        }
        int pageSizeInBytes = 123;
        int processedPages = 1;

        System.out.println("Crawling: " + url);

        this.httpHeader = "Some header...";
        this.content = "Some content...";

        crawler.addPageSizeInBytes(pageSizeInBytes);
        crawler.addProcessedPages(processedPages);

        return null;
    }

}
