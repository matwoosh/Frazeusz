package pl.edu.agh.ki.frazeusz;

import pl.edu.agh.ki.frazeusz.gui.crawler.CrawlerConfiguration;
import pl.edu.agh.ki.frazeusz.model.crawler.Crawler;
import pl.edu.agh.ki.frazeusz.model.monitor.CrawlerStatus;
import pl.edu.agh.ki.frazeusz.model.monitor.Monitor;
import pl.edu.agh.ki.frazeusz.model.nlp.NLProcessor;
import pl.edu.agh.ki.frazeusz.model.parser.Parser;
import pl.edu.agh.ki.frazeusz.model.ploter.Ploter;
import pl.edu.agh.ki.frazeusz.model.pm.PatternMatcher;
import pl.edu.agh.ki.frazeusz.utilities.Word;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by matwoosh on 14/01/2017.
 */
public class Main {
    private static ArrayList<String> urlList = new ArrayList<>();
    private static List<Word> wordList = new ArrayList<>();

    public static void main(String[] args) {
        PatternMatcher patternMatcher = new PatternMatcher(new NLProcessor(), new Ploter(), wordList);
        ArrayList<PatternMatcher> patternMatchers = new ArrayList<>();
        patternMatchers.add(patternMatcher);
        Parser parser = new Parser(patternMatchers);
        CrawlerStatus monitor = new Monitor();

        urlList.add("http://pl.bab.la");
        urlList.add("http://sfi.org.pl");

        CrawlerConfiguration crawlerConf = CrawlerConfiguration.builder()
                .setNestingDepth(2)
                .setThreadsNumber(10)
                .setUrlsToCrawl(urlList)
                .build();

        Crawler crawler = new Crawler(parser, monitor);
        crawler.start(crawlerConf);
    }

}
