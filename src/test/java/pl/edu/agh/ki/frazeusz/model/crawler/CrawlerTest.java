package pl.edu.agh.ki.frazeusz.model.crawler;

import org.junit.Test;
import pl.edu.agh.ki.frazeusz.gui.crawler.CrawlerConfiguration;
import pl.edu.agh.ki.frazeusz.gui.pm.PatternMatcherGui;
import pl.edu.agh.ki.frazeusz.model.monitor.Monitor;
import pl.edu.agh.ki.frazeusz.model.nlp.NLProcessor;
import pl.edu.agh.ki.frazeusz.model.parser.Parser;
import pl.edu.agh.ki.frazeusz.model.ploter.Ploter;
import pl.edu.agh.ki.frazeusz.model.pm.PatternMatcher;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by matwoosh on 26/01/2017.
 */
public class CrawlerTest {

    private ArrayList<String> getLinks() {
        ArrayList<String> urls = new ArrayList<>();
        for (int i = 2; i < 102; i++) {
            urls.add("https://forum.xda-developers.com/galaxy-s3/orig-development/rom-cyanogenmod-13-nightlies-i9300-t3272811/page" + i);
        }
        return urls;
    }


    /**
     * Testing execution time of crawling with given 100 links with 10 posts per link.
     * Time should not exceed 15 minutes. Crawling should not be working after this time.
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws InstantiationException
     */
    @Test(timeout = 15 * 60 * 1000)     // 15 min
    public void testCrawlingTime() throws IllegalAccessException, InvocationTargetException, InstantiationException {
        // given
        List<PatternMatcher> pmList = new LinkedList<>();
        pmList.add(new PatternMatcher(new NLProcessor(), new Ploter(), new PatternMatcherGui().getConfiguration()));

        Parser parser = new Parser(pmList);
        Crawler crawler = new Crawler(parser, new Monitor());

        CrawlerConfiguration.Builder builder = new CrawlerConfiguration.Builder();
        builder.setNestingDepth(1)
                .setThreadsNumber(100)
                .setUrlsToCrawl(getLinks())
                .build();

        Constructor<CrawlerConfiguration> constructor = (Constructor<CrawlerConfiguration>) CrawlerConfiguration.class.getDeclaredConstructors()[1];
        constructor.setAccessible(true);
        CrawlerConfiguration conf = constructor.newInstance(builder);

        // when
        crawler.start(conf);

        // then
        assertEquals(crawler.isCrawling(), false);
    }

}