package pl.edu.agh.ki.frazeusz.model.crawler;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import pl.edu.agh.ki.frazeusz.model.parser.IParser;

import java.io.IOException;
import java.net.ResponseCache;
import java.util.List;

/**
 * Created by matwoosh on 14/01/2017.
 */
public class Downloader implements Runnable {
    private Crawler crawler;
    private IParser parser;
    private String content;
    private String httpHeader;
    private String url;

    Downloader(Crawler crawler, IParser parser, String url) {
        this.crawler = crawler;
        this.parser = parser;
        this.url = url;
    }

    @Override
    public void run() {
        // TODO

        Url<String> urlNode = fetchUrl(url);
        System.out.println("-- Fetched");

        List<String> urlsGotFromParser = null;

        try {
            if (urlNode != null) {
                System.out.println("Parsing...");

                urlsGotFromParser = parser.parseContent(httpHeader, content, urlNode.getAbsoluteUrl());
                for (String e : urlsGotFromParser) {
                    System.out.println(" > (from parser) " + e);
                }
                crawler.addUrlsToProcess(urlsGotFromParser);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(" >> WARNING: Parser couldn't parse this url: " + urlNode.getAbsoluteUrl());
        }

        // after some execution - sendStats();
    }

    private Url<String> fetchUrl(String url) {
        System.out.println("-- Started fetching: " + url);
        //System.out.println("Crawling: " + url);

        Connection.Response response = null;
        Document document = null;
        try {
            response = Jsoup.connect(url).timeout(10*1000).execute();
            document = response.parse();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (document != null) {
            this.content = document.data();
        } else
            this.content = "No data !";

        if (response != null) {
            String resType = response.contentType();
            resType = resType.substring(0,resType.indexOf(";"));
            System.out.println(" > (Type) " + resType);
            this.httpHeader = resType;
        } else
            this.httpHeader = "No header...";

        assert document != null;
        int pageSizeInBytes = document.toString().length();

        crawler.addPageSizeInBytes(pageSizeInBytes);
        crawler.addProcessedPages(1);

        return new Url<String>(url);
    }

}
