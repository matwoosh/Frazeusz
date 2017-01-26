package pl.edu.agh.ki.frazeusz.model.crawler;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import pl.edu.agh.ki.frazeusz.model.parser.IParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by matwoosh on 14/01/2017.
 */
public class Downloader implements Runnable {

    private Crawler crawler;
    private IParser parser;

    private String content;
    private String httpHeader;

    Downloader(Crawler crawler, IParser parser) {
        this.crawler = crawler;
        this.parser = parser;
    }

    @Override
    public void run() {
        while (true) {
            Url newUrl = crawler.getUrlToCrawl();
            if (newUrl != null) {
                fetchUrl(newUrl);
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void fetchUrl(Url baseUrl) {
        try {
            extractUrlData(baseUrl);
            System.out.println("> Fetched ! (" + baseUrl + ")");

            parseUrlContent(baseUrl);
            System.out.printf("> Parsed !");
        } catch (Exception e) {
            crawler.addUnprocessedUrl(baseUrl);
        }
    }

    private void parseUrlContent(Url baseUrl) throws Exception {
        List<String> urlsFromParser;

        try {
            System.out.println("> Parsing...");

            urlsFromParser = parser.parseContent(httpHeader, content, baseUrl.getAbsoluteUrl());

            if (baseUrl.getNestingDepth() < crawler.getNestingDepth()) {

                List<Url> urlsToProcess = new ArrayList<>();
                if (urlsFromParser != null) {
                    if (!urlsFromParser.isEmpty()) {

                        for (String url : urlsFromParser) {
                            urlsToProcess.add(new Url(url, baseUrl.getNestingDepth() + 1));
                            System.out.println("  +" + url);
                        }
                        crawler.addUrlsToProcess(urlsToProcess);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("  (!) WARNING: Parser couldn't parse baseUrl: " + baseUrl.getAbsoluteUrl());
            throw new Exception();
        }

    }

    private void extractUrlData(Url url) throws Exception {
        System.out.println("> Started fetching: " + url);

        try {
            Connection.Response response = Jsoup.connect(url.getAbsoluteUrl()).execute();
            Document document = response.parse();

            if (document != null) {
                this.content = document.toString();
            } else
                this.content = "No data !";

            String resType = response.contentType();
            if (resType.contains(";"))
                resType = resType.substring(0, resType.indexOf(";"));
            this.httpHeader = resType;

            System.out.println("  + Type: " + resType);

            assert document != null;
            int pageSizeInBytes = 36 + document.toString().length() * 2;

            crawler.incrementStats(pageSizeInBytes);
        } catch (Exception e) {
            System.out.println("  (!) WARNING: Could not fetch url: " + url.getAbsoluteUrl());
            throw new Exception();
        }
    }

}
