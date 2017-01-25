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
        Url<String> urlNode = fetchUrl(url);
        System.out.println("> Fetched ! (" + url + ")");

        List<String> urlsGotFromParser = null;

        try {
            if (urlNode != null) {
                System.out.println("> Parsing...");
                urlsGotFromParser = parser.parseContent(httpHeader, content, urlNode.getAbsoluteUrl());
            }
        } catch (Exception e) {
            System.out.println("  (!) WARNING: Parser couldn't parse url: " + urlNode.getAbsoluteUrl());
            e.printStackTrace();
        }

        assert urlsGotFromParser != null;
        for (String e : urlsGotFromParser) {
            System.out.println("  + (Urls from parser) " + e);
        }
        crawler.addUrlsToProcess(urlsGotFromParser);

        System.out.printf("> Parsed !");
    }

    private Url<String> fetchUrl(String url) {
        System.out.println("> Started fetching: " + url);

        Connection.Response response = null;
        Document document = null;

        try {
            response = Jsoup.connect(url).execute();
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
            if (resType.contains(";"))
                resType = resType.substring(0,resType.indexOf(";"));
            this.httpHeader = resType;

            System.out.println("  + Type: " + resType);
        } else
            this.httpHeader = "No header...";

        assert document != null;
        int pageSizeInBytes = document.toString().length();

        crawler.incrementStats(1, pageSizeInBytes);

        return new Url<String>(url);
    }

}
